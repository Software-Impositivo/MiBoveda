package com.example.miboveda.features.login.domain.model

sealed interface SignInResult {
    data class Success(val user: AuthUser) : SignInResult
    data object Cancelled : SignInResult
    data class Failure(val reason: SignInFailure) : SignInResult
}

enum class SignInFailure {
    /** Falta GOOGLE_WEB_CLIENT_ID en local.properties. */
    NOT_CONFIGURED,

    /** No hay cuenta de Google disponible en el dispositivo. */
    NO_GOOGLE_ACCOUNT,

    /** La solicitud se interrumpió (por ejemplo, por la red). Se puede reintentar. */
    INTERRUPTED,

    /** Dispositivo o Google Play Services no compatibles. */
    UNSUPPORTED,

    UNKNOWN,
}