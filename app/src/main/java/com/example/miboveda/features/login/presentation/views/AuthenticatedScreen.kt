package com.example.miboveda.features.login.presentation.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.miboveda.R
import com.example.miboveda.features.login.domain.model.AuthUser

/** Pantalla temporal: se reemplaza por el Home cuando exista. */
@Composable
fun AuthenticatedScreen(user: AuthUser, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = (user.displayName ?: user.email).take(1).uppercase(),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
            )
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.authenticated_title),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(Modifier.height(8.dp))
        user.displayName?.let {
            Text(text = it, color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.labelMedium)
        }
        Text(text = user.email, color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.authenticated_placeholder),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
    }
}