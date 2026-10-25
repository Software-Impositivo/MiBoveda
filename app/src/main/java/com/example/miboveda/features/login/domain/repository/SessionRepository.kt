package com.example.miboveda.features.login.domain.repository

import com.example.miboveda.features.login.domain.model.AuthUser
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    /** Emite el usuario con sesión guardada, o null si no hay. */
    val session: Flow<AuthUser?>

    suspend fun save(user: AuthUser)

    suspend fun clear()
}
