package com.example.miboveda.features.scan.presentation.views

import android.view.Surface
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.viewfinder.compose.MutableCoordinateTransformer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.FlashOff
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.geometry.takeOrElse
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.miboveda.ui.theme.bigScanButton
import com.example.miboveda.ui.theme.cardTitle
import com.example.miboveda.ui.theme.smallText
import com.example.miboveda.ui.theme.white
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.delay
import java.util.UUID
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScanPermission(
    viewModel: ScanViewModel,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onToggleFlash: () -> Unit,
    onCapture: () -> Unit,
    onOpenGallery: () -> Unit,
    onHelp: () -> Unit,
) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    if (cameraPermissionState.status.isGranted) {
        ScanContent(
            viewModel = viewModel,
            onBack = onBack,
            onToggleFlash = onToggleFlash,
            onCapture = onCapture,
            onOpenGallery = onOpenGallery,
            onHelp = onHelp
        )
    } else {
        Column(
            modifier = modifier.fillMaxSize().wrapContentSize().widthIn(max = 480.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val textToShow = if (cameraPermissionState.status.shouldShowRationale) {
                // If the user has denied the permission but the rationale can be shown,
                // then gently explain why the app requires this permission
                "Whoops! Looks like we need your camera to work our magic!" +
                        "Don't worry, we just wanna see your pretty face (and maybe some cats).  " +
                        "Grant us permission and let's get this party started!"
            } else {
                // If it's the first time the user lands on this feature, or the user
                // doesn't want to be asked again for this permission, explain that the
                // permission is required
                "Hi there! We need your camera to work our magic! ✨\n" +
                        "Grant us permission and let's get this party started! \uD83C\uDF89"
            }
            Text(textToShow, textAlign = TextAlign.Center, color = white)
            Spacer(Modifier.height(16.dp))
            Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                Text("Unleash the Camera!", color = white)
            }
        }
    }
}

@Composable
fun ScanContent(
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScanTopBar(
                flashOn = flashOn,
                onBack = onBack,
                onToggleFlash = onToggleFlash,
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Alinea el recibo dentro del marco",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
            )

            Spacer(Modifier.height(16.dp))

            CameraPreviewContent(
                viewModel = viewModel
            )

            Spacer(Modifier.height(16.dp))

            DetectionChip(text = statusText)

            Spacer(Modifier.height(20.dp))
        }

        ScanBottomBar(
            onCapture = onCapture,
            onOpenGallery = onOpenGallery,
            onHelp = onHelp,
        )
    }
}

@Composable
fun CameraPreviewContent(
    viewModel: ScanViewModel,
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    val surfaceRequest by viewModel.surfaceRequest.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val view = LocalView.current
    val configuration = LocalConfiguration.current

    LaunchedEffect(lifecycleOwner) {
        viewModel.bindToCamera(context.applicationContext, lifecycleOwner)
    }

    LaunchedEffect(configuration.orientation) {
        viewModel.updateRotation(view.display?.rotation ?: Surface.ROTATION_0)
    }

    var autofocusRequest by remember { mutableStateOf(UUID.randomUUID() to Offset.Unspecified) }
    val autofocusRequestId = autofocusRequest.first
    val showAutofocusIndicator = autofocusRequest.second.isSpecified
    val autofocusCoords = remember(autofocusRequestId) { autofocusRequest.second }

    if(showAutofocusIndicator) {
        LaunchedEffect(autofocusRequestId) {
            delay(1000.milliseconds)
            autofocusRequest = autofocusRequestId to Offset.Unspecified
        }
    }

    surfaceRequest?.let { request ->
        val coordinateTransformer = remember { MutableCoordinateTransformer() }
        CameraXViewfinder(
            surfaceRequest = request,
            coordinateTransformer = coordinateTransformer,
            modifier = modifier.pointerInput(viewModel, coordinateTransformer) {
                detectTapGestures { tapCoords ->
                    with(coordinateTransformer) {
                        viewModel.tapToFocus(tapCoords.transform())
                    }
                    autofocusRequest = UUID.randomUUID() to tapCoords
                }
            }
        )
        AnimatedVisibility(
            visible = showAutofocusIndicator,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .offset { autofocusCoords.takeOrElse { Offset.Zero } .round() }
                .offset((-24).dp, (-24).dp)
        ) {
            Spacer(Modifier.border(2.dp, white, CircleShape).size(48.dp))
        }
    }
}

@Composable
private fun ScanTopBar(
    flashOn: Boolean,
    onBack: () -> Unit,
    onToggleFlash: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RoundTopButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Volver",
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Escanear recibo",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = "OCR automático",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
                color = bigScanButton,
            )
        }

        RoundTopButton(onClick = onToggleFlash) {
            Icon(
                imageVector = if (flashOn) Icons.Outlined.FlashOn else Icons.Outlined.FlashOff,
                contentDescription = if (flashOn) "Apagar flash" else "Encender flash",
            )
        }
    }
}

@Composable
private fun RoundTopButton(
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(44.dp),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
            contentColor = MaterialTheme.colorScheme.primary,
        ),
        content = content,
    )
}

/** Marco con borde verde oscuro y el "papel" interior donde va la cámara. */
@Composable
private fun ScanFrame(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                border = BorderStroke(3.dp, MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(32.dp),
            )
            .padding(8.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface),
        content = content,
    )
}

@Composable
private fun DetectionChip(text: String) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                Modifier
                    .size(8.dp)
                    .background(bigScanButton, CircleShape)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                ),
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun ScanBottomBar(
    onCapture: () -> Unit,
    onOpenGallery: () -> Unit,
    onHelp: () -> Unit,
) {
    Surface(
        modifier = Modifier.wrapContentHeight(),
        color = MaterialTheme.colorScheme.primaryContainer,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 40.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BottomSideButton(
                shape = RoundedCornerShape(14.dp),
                onClick = onOpenGallery,
            ) {
                Icon(
                    imageVector = Icons.Outlined.PhotoLibrary,
                    contentDescription = "Elegir de la galería",
                )
            }

            CaptureButton(onClick = onCapture)

            BottomSideButton(
                shape = CircleShape,
                onClick = onHelp,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.HelpOutline,
                    contentDescription = "Ayuda",
                )
            }
        }
    }
}

@Composable
private fun BottomSideButton(
    shape: Shape,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(48.dp),
        shape = shape,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.18f),
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        content = content,
    )
}

@Composable
private fun CaptureButton(onClick: () -> Unit) {
    val ring = MaterialTheme.colorScheme.onPrimaryContainer
    Box(
        modifier = Modifier
            .size(76.dp)
            .border(4.dp, ring, CircleShape)
            .padding(6.dp)
            .clip(CircleShape)
            .background(bigScanButton)
            .clickable(onClick = onClick)
            .semantics {
                role = Role.Button
                contentDescription = "Capturar recibo"
            }
    )
}

/** Recibo de ejemplo (solo para preview / mientras no hay cámara). */
@Composable
private fun BoxScope.SampleReceipt() {
    Column(
        modifier = Modifier
            .align(Alignment.TopCenter)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "ÉXITO",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Black,
                fontSize = 22.sp,
            ),
            color = cardTitle,
        )
        Spacer(Modifier.height(6.dp))
        ReceiptSmallText("Almacenes Éxito S.A.")
        ReceiptSmallText("NIT 890.900.608-9")

        Spacer(Modifier.height(16.dp))
        ReceiptDivider()
        Spacer(Modifier.height(12.dp))

        ReceiptRow("FACTURA", "FE 482719", labelMuted = true, valueBold = true)
        Spacer(Modifier.height(8.dp))
        ReceiptRow("FECHA", "09/09/2026 · 11:42", labelMuted = true, valueBold = true)

        Spacer(Modifier.height(12.dp))
        ReceiptDivider()
        Spacer(Modifier.height(12.dp))

        ReceiptRow("Café molido 500 g", "$18.900")
        Spacer(Modifier.height(6.dp))
        ReceiptRow("Leche entera x 6", "$24.600")
        Spacer(Modifier.height(6.dp))
        ReceiptRow("Pan artesanal", "$9.500")

        Spacer(Modifier.height(12.dp))
        ReceiptDivider()
        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "TOTAL",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                ),
                color = cardTitle,
            )
            Text(
                text = "$53.000",
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp),
                color = cardTitle,
            )
        }

        Spacer(Modifier.height(16.dp))
        ReceiptSmallText("Documento válido para la DIAN")
    }
}

@Composable
private fun ReceiptSmallText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
        color = smallText,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun ReceiptDivider() {
    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f))
}

@Composable
private fun ReceiptRow(
    label: String,
    value: String,
    labelMuted: Boolean = false,
    valueBold: Boolean = false,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
            color = if (labelMuted) smallText else cardTitle,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 12.sp,
                fontWeight = if (valueBold) FontWeight.Bold else FontWeight.Normal,
            ),
            color = cardTitle,
        )
    }
}