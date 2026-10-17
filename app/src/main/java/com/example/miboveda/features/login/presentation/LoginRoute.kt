package com.example.miboveda.features.login.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.miboveda.R
import com.example.miboveda.features.login.domain.model.SignInFailure
import com.example.miboveda.features.login.presentation.views.AuthenticatedScreen
import com.example.miboveda.features.login.presentation.views.LoginScreen
import kotlinx.coroutines.launch

@Composable
fun LoginRoute(viewModel: LoginViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val errorMessage = uiState.error?.let { stringResource(it.messageRes()) }
    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            snackbarHostState.showSnackbar(errorMessage)
            viewModel.onErrorShown()
        }
    }

    Box(Modifier.fillMaxSize()) {
        val user = uiState.user
        if (user == null) {
            LoginScreen(
                isLoading = uiState.isLoading,
                onGoogleSignInClick = { scope.launch { viewModel.signInWithGoogle(context) } },
            )
        } else {
            AuthenticatedScreen(user = user)
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(16.dp),
        )
    }
}

@StringRes
private fun SignInFailure.messageRes(): Int = when (this) {
    SignInFailure.NOT_CONFIGURED -> R.string.login_error_not_configured
    SignInFailure.NO_GOOGLE_ACCOUNT -> R.string.login_error_no_google_account
    SignInFailure.INTERRUPTED -> R.string.login_error_interrupted
    SignInFailure.UNSUPPORTED -> R.string.login_error_unsupported
    SignInFailure.UNKNOWN -> R.string.login_error_unknown
}