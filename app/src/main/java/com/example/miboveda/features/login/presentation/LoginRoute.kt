package com.example.miboveda.features.login.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.miboveda.R
import com.example.miboveda.features.login.domain.model.SignInFailure
import com.example.miboveda.features.login.presentation.views.AuthenticatedScreen
import com.example.miboveda.features.login.presentation.views.LoginScreen
import kotlinx.coroutines.launch

@Composable
fun LoginRoute(viewModel: LoginViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Pantalla de consentimiento de Google para el permiso de Drive.
    val driveConsentLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        viewModel.onDriveConsentResult(context, result.resultCode, result.data)
    }

    val signInError = uiState.error
    val driveError = uiState.driveError
    val errorMessage = when {
        signInError != null -> stringResource(signInError.messageRes())
        driveError != null -> stringResource(driveError.messageRes())
        else -> null
    }
    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            snackbarHostState.showSnackbar(errorMessage)
            viewModel.onErrorShown()
        }
    }

    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        val user = uiState.user
        when {
            uiState.isRestoringSession -> Unit // solo fondo: evita el parpadeo del login
            user == null -> LoginScreen(
                isLoading = uiState.isLoading,
                onGoogleSignInClick = { scope.launch { viewModel.signInWithGoogle(context) } },
            )
            else -> AuthenticatedScreen(
                user = user,
                driveStatus = uiState.driveStatus,
                isDriveBusy = uiState.isDriveBusy,
                onConnectDriveClick = {
                    scope.launch {
                        viewModel.connectDrive(context)?.let { consent ->
                            driveConsentLauncher.launch(
                                IntentSenderRequest.Builder(consent.intentSender).build()
                            )
                        }
                    }
                },
                onDisconnectDriveClick = { scope.launch { viewModel.disconnectDrive(context) } },
                onSignOutClick = { scope.launch { viewModel.signOut(context) } },
            )
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(16.dp),
        ) { data ->
            Snackbar(
                snackbarData = data,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@StringRes
private fun SignInFailure.messageRes(): Int = when (this) {
    SignInFailure.NOT_CONFIGURED -> R.string.login_error_not_configured
    SignInFailure.NO_GOOGLE_ACCOUNT -> R.string.login_error_no_google_account
    SignInFailure.INTERRUPTED -> R.string.login_error_interrupted
    SignInFailure.UNSUPPORTED -> R.string.login_error_unsupported
    SignInFailure.UNKNOWN -> R.string.login_error_unknown
}

@StringRes
private fun DriveError.messageRes(): Int = when (this) {
    DriveError.DENIED -> R.string.drive_error_denied
    DriveError.UNAVAILABLE -> R.string.drive_error_unavailable
    DriveError.UNKNOWN -> R.string.drive_error_unknown
}