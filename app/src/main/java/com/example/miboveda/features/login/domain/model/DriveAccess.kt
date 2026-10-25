package com.example.miboveda.features.login.domain.model

import android.app.PendingIntent

/** Resultado de pedir acceso a Google Drive (permiso drive.file). */
sealed interface DriveAccess {
    /** Acceso concedido. [accessToken] dura poco: úsalo al momento y no lo guardes. */
    data class Granted(val accessToken: String) : DriveAccess

    /** El usuario aún debe aceptar el permiso: la pantalla lanza [pendingIntent]. */
    data class ConsentRequired(val pendingIntent: PendingIntent) : DriveAccess

    /** El usuario rechazó o cerró el diálogo de permiso. */
    data object Denied : DriveAccess

    data class Failed(val reason: DriveFailure) : DriveAccess
}

enum class DriveFailure {
    /** Sin conexión o servicio no disponible por ahora. Se puede reintentar. */
    UNAVAILABLE,

    UNKNOWN,
}