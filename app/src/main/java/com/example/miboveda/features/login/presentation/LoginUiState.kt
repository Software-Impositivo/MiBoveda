package com.example.miboveda.features.login.presentation

import com.example.miboveda.features.login.domain.model.AuthUser
import com.example.miboveda.features.login.domain.model.SignInFailure

data class LoginUiState(
    val isLoading: Boolean = false,
    val user: AuthUser? = null,
    val error: SignInFailure? = null,
)