package com.example.miboveda.features.login.data

import android.accounts.Account
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.miboveda.BuildConfig
import com.example.miboveda.features.login.domain.model.DriveAccess
import com.example.miboveda.features.login.domain.model.DriveFailure
import com.example.miboveda.features.login.domain.repository.DriveAuthorizationRepository
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.AuthorizationResult
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.RevokeAccessRequest
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

private const val TAG = "DriveAuthorization"
private const val GOOGLE_ACCOUNT_TYPE = "com.google"
private const val DRIVE_FILE_URL = "https://www.googleapis.com/auth/drive.file"
private val DRIVE_FILE_SCOPE = Scope(DRIVE_FILE_URL)

class GoogleDriveAuthorizationRepository : DriveAuthorizationRepository {

    override suspend fun authorize(context: Context, accountEmail: String): DriveAccess {
        val request = AuthorizationRequest.builder()
            .setRequestedScopes(listOf(DRIVE_FILE_SCOPE))
            .setAccount(Account(accountEmail, GOOGLE_ACCOUNT_TYPE))
            .build()

        return try {
            Identity.getAuthorizationClient(context).authorize(request).await().toDriveAccess()
        } catch (error: ApiException) {
            error.toDriveAccess()
        }
    }

    override fun accessFromConsentResult(context: Context, data: Intent?): DriveAccess = try {
        Identity.getAuthorizationClient(context)
            .getAuthorizationResultFromIntent(data)
            .toDriveAccess()
    } catch (error: ApiException) {
        error.toDriveAccess()
    }

    override suspend fun revoke(context: Context, accountEmail: String): Boolean {
        val request = RevokeAccessRequest.builder()
            .setAccount(Account(accountEmail, GOOGLE_ACCOUNT_TYPE))
            .setScopes(listOf(DRIVE_FILE_SCOPE))
            .build()

        return try {
            Identity.getAuthorizationClient(context).revokeAccess(request).await()
            true
        } catch (error: ApiException) {
            if (BuildConfig.DEBUG) Log.w(TAG, "No se pudo revocar (código ${error.statusCode})")
            false
        }
    }
}

private fun AuthorizationResult.toDriveAccess(): DriveAccess {
    if (hasResolution()) {
        val intent = pendingIntent ?: return DriveAccess.Failed(DriveFailure.UNKNOWN)
        return DriveAccess.ConsentRequired(intent)
    }
    val token = accessToken
    return if (token != null && DRIVE_FILE_URL in grantedScopes) {
        DriveAccess.Granted(token)
    } else {
        DriveAccess.Denied
    }
}

private fun ApiException.toDriveAccess(): DriveAccess {
    if (BuildConfig.DEBUG) Log.w(TAG, "Autorización de Drive fallida (código $statusCode)")
    return when (statusCode) {
        CommonStatusCodes.CANCELED -> DriveAccess.Denied
        CommonStatusCodes.NETWORK_ERROR,
        CommonStatusCodes.TIMEOUT -> DriveAccess.Failed(DriveFailure.UNAVAILABLE)
        else -> DriveAccess.Failed(DriveFailure.UNKNOWN)
    }
}

private suspend fun <T> Task<T>.await(): T = suspendCancellableCoroutine { continuation ->
    addOnSuccessListener { continuation.resume(it) }
    addOnFailureListener { continuation.resumeWithException(it) }
    addOnCanceledListener { continuation.cancel() }
}