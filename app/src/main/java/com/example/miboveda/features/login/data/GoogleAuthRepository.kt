package com.example.miboveda.features.login.data

import android.content.Context
import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.example.miboveda.BuildConfig
import com.example.miboveda.features.login.domain.model.AuthUser
import com.example.miboveda.features.login.domain.model.SignInFailure
import com.example.miboveda.features.login.domain.model.SignInResult
import com.example.miboveda.features.login.domain.repository.AuthRepository
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException

private const val TAG = "GoogleAuthRepository"

class GoogleAuthRepository(
    private val webClientId: String,
) : AuthRepository {

    override suspend fun signInWithGoogle(activityContext: Context): SignInResult {
        if (webClientId.isBlank()) return SignInResult.Failure(SignInFailure.NOT_CONFIGURED)

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(GetSignInWithGoogleOption.Builder(webClientId).build())
            .build()

        return try {
            val response = CredentialManager.create(activityContext)
                .getCredential(context = activityContext, request = request)
            response.credential.toAuthUser()
                ?.let { SignInResult.Success(it) }
                ?: SignInResult.Failure(SignInFailure.UNKNOWN)
        } catch (cancelled: GetCredentialCancellationException) {
            SignInResult.Cancelled
        } catch (error: GetCredentialException) {
            if (BuildConfig.DEBUG) Log.w(TAG, "Inicio de sesión fallido (${error.type})", error)
            SignInResult.Failure(error.toSignInFailure())
        }
    }
}

private fun Credential.toAuthUser(): AuthUser? {
    if (this !is CustomCredential ||
        type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
    ) return null

    return try {
        val google = GoogleIdTokenCredential.createFrom(data)
        AuthUser(
            email = google.id,
            displayName = google.displayName,
            photoUrl = google.profilePictureUri?.toString(),
        )
    } catch (parsingError: GoogleIdTokenParsingException) {
        null
    }
}