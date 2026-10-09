package com.example.miboveda.features.login.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.R.attr.includeFontPadding


@Composable
fun Input(typeField: String) {

    Column {
        when (typeField) {
            "Normal" -> NormalTextField()
            "Password" -> PasswordTextField()
        }
    }
}

@Composable
fun NormalTextField() {
    var fieldValue by remember { mutableStateOf("") }
    val isValid = fieldValue.isNotEmpty() && !fieldValue.contains("@")
    
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            fontSize = 16.sp,
            platformStyle = PlatformTextStyle(
                includeFontPadding = false
            ),
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.Both
            )
        ),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        ),
        value = fieldValue,
        onValueChange = { fieldValue = it },
        leadingIcon = {
            Icon(
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                imageVector = Icons.Default.Email,
                contentDescription = "Icono de correo",
            )
        },
        placeholder = { Text("Correo electrónico", color = MaterialTheme.colorScheme.onSecondaryContainer) },
        //label = { Text("Correo electrónico", color = MaterialTheme.colorScheme
        //    .onSecondaryContainer) },
        isError = isValid,
        shape = RoundedCornerShape(8.dp)
    )
    if (isValid) {
        Text(
            text = "Correo electrónico no válido",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun PasswordTextField() {
    val state = remember { TextFieldState() }
    var showPassword by remember { mutableStateOf(false) }
    BasicSecureTextField(
        state = state,
        textObfuscationMode =
            if (showPassword) {
                TextObfuscationMode.Visible
            } else {
                TextObfuscationMode.RevealLastTyped
            },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)),
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            fontSize = 16.sp,
            platformStyle = PlatformTextStyle(
                includeFontPadding = false
            ),
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.Both
            )
        ),
        decorator = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(vertical = 18.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (state.text.isEmpty()) {
                        Text(
                            text = "Contraseña",
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }

                        innerTextField()
                }
                Icon(
                    if (showPassword) {
                        Icons.Filled.Visibility
                    } else {
                        Icons.Filled.VisibilityOff
                    },
                    contentDescription = "Toggle password visibility",
                    modifier = Modifier
                        //.requiredSize(48.dp).padding(16.dp)
                        .clickable { showPassword = !showPassword },
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    )
}