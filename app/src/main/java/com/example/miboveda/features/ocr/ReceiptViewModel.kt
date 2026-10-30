package com.example.miboveda.features.ocr

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import java.io.IOException

sealed interface OcrState {
    data object Loading : OcrState
    data class Success(val text: String) : OcrState
    data class Error(val message: String) : OcrState
}

class ReceiptViewModel : ViewModel() {

    private val _ocrState = MutableStateFlow<OcrState>(OcrState.Loading)
    val ocrState: StateFlow<OcrState> = _ocrState

    private val recognizer =
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun recognize(context: Context, file: File) {
        _ocrState.value = OcrState.Loading
        try {
            val image = InputImage.fromFilePath(context, Uri.fromFile(file))
            recognizer.process(image)
                .addOnSuccessListener { result ->
                    _ocrState.value = OcrState.Success(result.text)
                }
                .addOnFailureListener { e ->
                    _ocrState.value = OcrState.Error(e.message ?: "Error en el OCR")
                }
        } catch (e: IOException) {
            _ocrState.value = OcrState.Error("No se pudo leer la imagen")
        }
    }

    override fun onCleared() {
        recognizer.close()
    }
}