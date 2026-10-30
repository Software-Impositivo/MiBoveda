package com.example.miboveda.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val MiBovedaLight = lightColorScheme(
    primary = titlesGreen,
    onPrimary = labelsGreen,
    primaryContainer = titlesGreen,       // tarjeta oscura, barras de gastos
    onPrimaryContainer = white,           // texto sobre la tarjeta oscura
    secondary = incomeBar,                // barras de ingresos
    tertiary = vaultAccent,               // "MI BÓVEDA"
    background = mainBglight,
    surface = white,                      // barra inferior, botón de campana
    surfaceContainer = bgField,           // tarjeta del gráfico
    onSurfaceVariant = smallText,         // "Saldo neto", leyenda, meses
    secondaryContainer = bgField,
    onSecondaryContainer = hintField,
    outline = borderField,
    onSurface = white,
    outlineVariant = borderContainers
)

@Composable
fun MiBovedaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        else -> MiBovedaLight
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MiBovedaTypography,
        content = content
    )
}