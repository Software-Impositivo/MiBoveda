package com.example.miboveda.features.ocr

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import java.io.File
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage

@Composable
fun ReceiptPreviewScreen(
    imagePath: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReceiptViewModel = viewModel(),
) {
    val context = LocalContext.current
    val file = remember(imagePath) { File(imagePath) }
    val state by viewModel.ocrState.collectAsStateWithLifecycle()

    LaunchedEffect(file) {
        viewModel.recognize(context.applicationContext, file)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            model = file,
            contentDescription = "Foto del recibo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 400.dp)
                .clip(RoundedCornerShape(16.dp)),
        )

        Spacer(Modifier.height(16.dp))

        when (val s = state) {
            is OcrState.Loading -> CircularProgressIndicator()
            is OcrState.Error -> Text(s.message, color = MaterialTheme.colorScheme.error)
            is OcrState.Success -> {
                Text(
                    text = "Texto detectado",
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(Modifier.height(8.dp))
                SelectionContainer {
                    Text(
                        text = s.text.ifBlank { "No se detectó texto" },
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(onClick = onBack) { Text("Volver a escanear") }
    }
}