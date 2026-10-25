package com.example.miboveda.features.login.presentation

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miboveda.features.login.domain.model.DriveAccess
import com.example.miboveda.features.login.domain.model.DriveFailure
import com.example.miboveda.features.login.domain.model.SignInFailure
import com.example.miboveda.features.login.domain.model.SignInResult
import com.example.miboveda.features.login.domain.repository.AuthRepository
import com.example.miboveda.features.login.domain.repository.DriveAuthorizationRepository
import com.example.miboveda.features.login.domain.repository.SessionRepository
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository,
    private val driveRepository: DriveAuthorizationRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        // La sesión guardada es la única fuente de verdad del usuario.
        viewModelScope.launch {
            sessionRepository.session.collect { user ->
                _uiState.update { state ->
                    val sameAccount = state.user?.email == user?.email
                    state.copy(
                        user = user,
                        isRestoringSession = false,
                        // El estado de Drive pertenece a una cuenta: si cambia, se reinicia.
                        driveStatus = if (sameAccount) state.driveStatus else DriveStatus.UNKNOWN,
                        driveError = if (sameAccount) state.driveError else null,
                    )
                }
            }
        }
    }

    /**
     * Es suspend a propósito: la pantalla la lanza con su propio scope, así el Context de la
     * Activity no queda retenido por el ViewModel si el usuario rota o cierra la pantalla.
     */
    suspend fun signInWithGoogle(activityContext: Context) {
        if (_uiState.value.isLoading) return // evita doble toque

        _uiState.update { it.copy(isLoading = true, error = null) }
        try {
            when (val result = authRepository.signInWithGoogle(activityContext)) {
                is SignInResult.Success -> saveSession(result)
                SignInResult.Cancelled -> Unit
                is SignInResult.Failure -> _uiState.update { it.copy(error = result.reason) }
            }
        } finally {
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    suspend fun signOut(context: Context) {
        try {
            authRepository.signOut(context)
        } finally {
            sessionRepository.clear() // la sesión local se borra siempre
        }
    }

    /**
     * Pide el permiso de Drive. Devuelve el PendingIntent de la pantalla de consentimiento si el
     * usuario debe aceptar; la pantalla lo lanza. Devuelve null si ya se resolvió sin interacción.
     */
    suspend fun connectDrive(context: Context): PendingIntent? {
        val email = _uiState.value.user?.email ?: return null
        if (_uiState.value.isDriveBusy) return null

        _uiState.update { it.copy(isDriveBusy = true, driveError = null) }
        var consentIntent: PendingIntent? = null
        try {
            when (val access = driveRepository.authorize(context, email)) {
                is DriveAccess.ConsentRequired -> consentIntent = access.pendingIntent
                else -> _uiState.update { it.withDriveAccess(access) }
            }
        } finally {
            // Con consentimiento pendiente seguimos "ocupados" hasta que llegue el resultado.
            if (consentIntent == null) _uiState.update { it.copy(isDriveBusy = false) }
        }
        return consentIntent
    }

    fun onDriveConsentResult(context: Context, resultCode: Int, data: Intent?) {
        val access = if (resultCode == Activity.RESULT_OK) {
            driveRepository.accessFromConsentResult(context, data)
        } else {
            DriveAccess.Denied
        }
        _uiState.update { it.withDriveAccess(access).copy(isDriveBusy = false) }
    }

    suspend fun disconnectDrive(context: Context) {
        val email = _uiState.value.user?.email ?: return
        if (_uiState.value.isDriveBusy) return

        _uiState.update { it.copy(isDriveBusy = true, driveError = null) }
        try {
            val revoked = driveRepository.revoke(context, email)
            _uiState.update {
                if (revoked) {
                    it.copy(driveStatus = DriveStatus.NOT_CONNECTED)
                } else {
                    it.copy(driveError = DriveError.UNKNOWN)
                }
            }
        } finally {
            _uiState.update { it.copy(isDriveBusy = false) }
        }
    }

    fun onErrorShown() {
        _uiState.update { it.copy(error = null, driveError = null) }
    }

    private suspend fun saveSession(result: SignInResult.Success) {
        try {
            sessionRepository.save(result.user)
        } catch (error: IOException) {
            _uiState.update { it.copy(error = SignInFailure.UNKNOWN) }
        }
    }
}

/** Aquí el token se descarta a propósito: la UI solo necesita saber si hay acceso. */
private fun LoginUiState.withDriveAccess(access: DriveAccess): LoginUiState = when (access) {
    is DriveAccess.Granted -> copy(driveStatus = DriveStatus.CONNECTED)
    DriveAccess.Denied -> copy(driveStatus = DriveStatus.NOT_CONNECTED, driveError = DriveError.DENIED)
    is DriveAccess.Failed -> copy(driveError = access.reason.toDriveError())
    is DriveAccess.ConsentRequired -> this
}

private fun DriveFailure.toDriveError(): DriveError = when (this) {
    DriveFailure.UNAVAILABLE -> DriveError.UNAVAILABLE
    DriveFailure.UNKNOWN -> DriveError.UNKNOWN
}