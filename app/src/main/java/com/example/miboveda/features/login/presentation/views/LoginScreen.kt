package com.example.miboveda.features.login.presentation.views

import android.graphics.drawable.Icon
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.miboveda.R
import com.example.miboveda.features.login.presentation.components.Input
import com.example.miboveda.ui.theme.mainBglight

@Composable
fun LoginScreen() {
    Column(
        Modifier.background(color = MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            Modifier.fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
                text = "Accede a tu Bóveda",
            )
            Spacer(Modifier.height(8.dp))
            Text(
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium,
                text = "Ingresa tus credenciales para continuar",
            )
        }
        Column(
            Modifier.fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                Modifier.fillMaxWidth()
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.labelMedium,
                    text = "Correo electrónico",
                    textAlign = TextAlign.Left
                )
                Spacer(Modifier.height(8.dp))
                Input("Normal")
            }
            Spacer(Modifier.height(20.dp))
            Column(
                Modifier.fillMaxWidth()
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.labelMedium,
                    text = "Contraseña",
                    textAlign = TextAlign.Left
                )
                Spacer(Modifier.height(8.dp))
                Input("Password")
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(all = 12.dp)
                ) {
                    Text(
                        "Entra",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelMedium

                    )
                    Spacer(Modifier.height(10.dp))
                    Icon(
                        tint = MaterialTheme.colorScheme.onSurface,
                        imageVector = Icons.Default.ArrowOutward,
                        contentDescription = "Flecha"
                    )
                }
                Spacer(Modifier.height(20.dp))
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.fillMaxWidth()
                        .wrapContentHeight(),
                ) {
                    Text(
                        text = "¿Olvidaste tu contraseña?",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.labelSmall,
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f)
                    .height(1.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    ),
            ) { }
            Row(
                modifier = Modifier.weight(1f)
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "o continúa con",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.End
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth()
                    .height(1.dp)
                    .weight(1f)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    ),
            ) { }
        }
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                contentPadding = PaddingValues(12.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google_logo),
                    contentDescription = "Logo de Google",
                    modifier = Modifier.size(30.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "Google",
                    color = MaterialTheme.colorScheme.outlineVariant,
                    style = MaterialTheme.typography.displayMedium
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "¿No tienes cuenta?",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(Modifier.width(5.dp))
            Text(
                text = "Crea una aquí",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

