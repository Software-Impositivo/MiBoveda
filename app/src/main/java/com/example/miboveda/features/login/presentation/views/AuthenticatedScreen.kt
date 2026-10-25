package com.example.miboveda.features.login.presentation.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.miboveda.features.login.presentation.DriveStatus

/** Pantalla temporal: se reemplaza por el Home cuando exista. */
@Composable
fun AuthenticatedScreen(
    user: AuthUser,
    driveStatus: DriveStatus,
    isDriveBusy: Boolean,
    onConnectDriveClick: () -> Unit,
    onDisconnectDriveClick: () -> Unit,
    onSignOutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(driveStatus.labelRes()),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelMedium,
        )
        Spacer(Modifier.height(12.dp))

        if (driveStatus == DriveStatus.CONNECTED) {
            OutlinedButton(
                onClick = onDisconnectDriveClick,
                enabled = !isDriveBusy,
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                contentPadding = PaddingValues(12.dp),
            ) {
                DriveButtonContent(isBusy = isDriveBusy, textRes = R.string.drive_disconnect)
            }
        } else {
            Button(
                onClick = onConnectDriveClick,
                enabled = !isDriveBusy,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(12.dp),
            ) {
                DriveButtonContent(isBusy = isDriveBusy, textRes = R.string.drive_connect)
            }
        }

        Spacer(Modifier.height(16.dp))
        OutlinedButton(
            onClick = onSignOutClick,
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            contentPadding = PaddingValues(12.dp),
        ) {
            Text(
                text = stringResource(R.string.authenticated_sign_out),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

@Composable
private fun DriveButtonContent(isBusy: Boolean, textRes: Int) {
    if (isBusy) {
        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
    } else {
        Text(
            text = stringResource(textRes),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

private fun DriveStatus.labelRes(): Int = when (this) {
    DriveStatus.UNKNOWN -> R.string.drive_status_unknown
    DriveStatus.CONNECTED -> R.string.drive_status_connected
    DriveStatus.NOT_CONNECTED -> R.string.drive_status_not_connected
}