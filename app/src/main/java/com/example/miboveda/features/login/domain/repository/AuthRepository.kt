package com.example.miboveda.features.login.domain.repository

import android.content.Context
import com.example.miboveda.features.login.domain.model.SignInResult

interface AuthRepository {
    /**
     * [activityContext] debe ser el Context de una Activity: Credential Manager lo usa
     * para mostrar su hoja de selección de cuenta.
     */
    suspend fun signInWithGoogle(activityContext: Context): SignInResult
}