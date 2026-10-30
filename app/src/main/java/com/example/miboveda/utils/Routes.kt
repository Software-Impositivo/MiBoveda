package com.example.miboveda.utils


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.miboveda.R
import kotlinx.serialization.Serializable

// Cada pantalla tiene su ruta. Son objetos porque no reciben argumentos.
@Serializable object Home
@Serializable object Scan
@Serializable object Receipts
@Serializable object Reports
@Serializable object Profile

@Serializable data class ReceiptPreview(val path: String)

data class TopLevelRoute<T : Any>(
    val name: Int,
    val route: T,
    val icon: Int
)

val topLevelRoutes = listOf(
    TopLevelRoute(R.string.nav_home, Home, R.drawable.ic_home),
    TopLevelRoute(R.string.nav_scan, Scan, R.drawable.ic_camera),
    // TopLevelRoute(R.string.nav_receipts, Receipts, R.drawable.ic_receipt),
    // TopLevelRoute(R.string.nav_reports, Reports, R.drawable.ic_bar_chart),
    // TopLevelRoute(R.string.nav_profile, Profile, R.drawable.ic_person)
)
