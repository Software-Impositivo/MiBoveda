package com.example.miboveda.features.login.domain.model

/**
 * Usuario autenticado. Solo datos no sensibles: el ID token nunca se guarda aquí.
 */
data class AuthUser(
    val email: String,
    val displayName: String?,
    val photoUrl: String?,
)