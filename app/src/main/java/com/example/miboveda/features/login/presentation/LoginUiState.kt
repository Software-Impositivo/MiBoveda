package com.example.miboveda.features.login.presentation

import com.example.miboveda.features.login.domain.model.AuthUser
import com.example.miboveda.features.login.domain.model.SignInFailure

data class LoginUiState(
    /** true hasta que se lee la sesión guardada: evita mostrar el login un instante. */
    val isRestoringSession: Boolean = true,
    val isLoading: Boolean = false,
    val user: AuthUser? = null,
    val error: SignInFailure? = null,
)