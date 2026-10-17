package com.example.miboveda.features.login.data

import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.GetCredentialInterruptedException
import androidx.credentials.exceptions.GetCredentialProviderConfigurationException
import androidx.credentials.exceptions.GetCredentialUnsupportedException
import androidx.credentials.exceptions.NoCredentialException
import com.example.miboveda.features.login.domain.model.SignInFailure

internal fun GetCredentialException.toSignInFailure(): SignInFailure = when (this) {
    is NoCredentialException -> SignInFailure.NO_GOOGLE_ACCOUNT
    is GetCredentialInterruptedException -> SignInFailure.INTERRUPTED
    is GetCredentialProviderConfigurationException,
    is GetCredentialUnsupportedException -> SignInFailure.UNSUPPORTED
    else -> SignInFailure.UNKNOWN
}