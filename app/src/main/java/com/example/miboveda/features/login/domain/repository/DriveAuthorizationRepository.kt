package com.example.miboveda.features.login.domain.repository

import android.content.Context
import android.content.Intent
import com.example.miboveda.features.login.domain.model.DriveAccess

interface DriveAuthorizationRepository {
    /** Pide acceso a Drive para [accountEmail]. Si ya fue concedido, no muestra ninguna pantalla. */
    suspend fun authorize(context: Context, accountEmail: String): DriveAccess

    /** Procesa el resultado de la pantalla de permiso que lanzó la Activity. */
    fun accessFromConsentResult(context: Context, data: Intent?): DriveAccess

    /** Quita el permiso de Drive de esta app para [accountEmail]. Devuelve si pudo hacerlo. */
    suspend fun revoke(context: Context, accountEmail: String): Boolean
}