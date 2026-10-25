package com.example.miboveda.features.login.presentation

import com.example.miboveda.features.login.domain.model.AuthUser
import com.example.miboveda.features.login.domain.model.SignInFailure

enum class DriveStatus { UNKNOWN, CONNECTED, NOT_CONNECTED }

enum class DriveError { DENIED, UNAVAILABLE, UNKNOWN }

data class LoginUiState(
    /** true hasta que se lee la sesión guardada: evita mostrar el login un instante. */
    val isRestoringSession: Boolean = true,
    val isLoading: Boolean = false,
    val user: AuthUser? = null,
    val error: SignInFailure? = null,
    val driveStatus: DriveStatus = DriveStatus.UNKNOWN,
    val isDriveBusy: Boolean = false,
    val driveError: DriveError? = null,
)