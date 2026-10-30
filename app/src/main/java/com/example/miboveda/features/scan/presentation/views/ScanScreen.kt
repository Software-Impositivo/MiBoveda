package com.example.miboveda.features.scan.presentation.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.FlashOff
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.ui.graphics.Shape
import com.example.miboveda.ui.theme.MiBovedaTheme
import com.example.miboveda.ui.theme.bigScanButton
import com.example.miboveda.ui.theme.cardTitle
import com.example.miboveda.ui.theme.smallText

/**
 * Pantalla "Escanear recibo".
 *
 * [cameraContent] es el slot donde va la vista previa de CameraX
 * (por ejemplo un PreviewView dentro de AndroidView). Por defecto muestra
 * un recibo de ejemplo para previews.
 */
@Composable
fun ScanScreen(
    viewModel: ScanViewModel,
    onBack: () -> Unit,
    onToggleFlash: () -> Unit,
    onCapture: () -> Unit,
    onOpenGallery: () -> Unit,
    onHelp: () -> Unit,
    modifier: Modifier = Modifier,
    flashOn: Boolean = false,
    statusText: String = "Recibo detectado · Mantén el pulso",
) {
    ScanPermission(
        viewModel = viewModel,
        onBack = onBack,
        onToggleFlash = onToggleFlash,
        onCapture = onCapture,
        onOpenGallery = onOpenGallery,
        onHelp = onHelp
    )
}



// @Preview(showBackground = true, showSystemUi = true)
// @Composable
// private fun ScanReceiptScreenPreview() {
//     MiBovedaTheme {
//         ScanScreen(
//             onBack = {},
//             onToggleFlash = {},
//             onCapture = {},
//             onOpenGallery = {},
//             onHelp = {},
//         )
//     }
// }