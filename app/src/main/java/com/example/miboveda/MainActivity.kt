package com.example.miboveda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.miboveda.features.login.data.DataStoreSessionRepository
import com.example.miboveda.features.login.data.GoogleAuthRepository
import com.example.miboveda.features.login.presentation.LoginRoute
import com.example.miboveda.features.login.presentation.LoginViewModel
import com.example.miboveda.ui.theme.MiBovedaTheme

class MainActivity : ComponentActivity() {

    private val loginViewModelFactory = viewModelFactory {
        initializer {
            LoginViewModel(
                authRepository = GoogleAuthRepository(BuildConfig.GOOGLE_WEB_CLIENT_ID),
                sessionRepository = DataStoreSessionRepository(applicationContext),
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiBovedaTheme {
                LoginRoute(viewModel = viewModel<LoginViewModel>(factory = loginViewModelFactory))
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MiBovedaTheme {
        Greeting("Android")
    }
}