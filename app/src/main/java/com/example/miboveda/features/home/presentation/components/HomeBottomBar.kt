package com.example.miboveda.features.home.presentation.components

import android.annotation.SuppressLint
import android.content.res.Resources
import android.media.Image
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.miboveda.R
import com.example.miboveda.ui.theme.NavInactive
import com.example.miboveda.ui.theme.cardTitle
import kotlinx.serialization.Serializable

internal enum class HomeTab(
    @StringRes val label: Int,
    @DrawableRes val icon: Int,
) {
    Home(R.string.nav_home, R.drawable.ic_home),
    Receipts(R.string.nav_receipts, R.drawable.ic_receipt),
    Scan(R.string.nav_scan, R.drawable.ic_camera),
    Reports(R.string.nav_reports, R.drawable.ic_bar_chart),
    Profile(R.string.nav_profile, R.drawable.ic_person),
}


// @Composable
// internal fun ReceiptsBottomBar(
//     selectedTab: HomeTab,
//     modifier: Modifier = Modifier,
//     onTabSelected: (Home) -> Unit = {},
// ) {
//     val activeColor = cardTitle
//     val navController = rememberNavController()
//     val navBackStackEntry by navController.currentBackStackEntryAsState()
//     val currentDestination = navBackStackEntry?.destination

//     NavigationBar(
//         modifier = modifier,
//         containerColor = MaterialTheme.colorScheme.surface,
//         tonalElevation = 0.dp,
//     ) {
//         topLevelRoutes.forEach { item ->
//             val selected = item == Home
//             NavigationBarItem(
//                 // onClick = { onTabSelected(item) },
//                 icon = { Icon(imageVector = ImageVector.vectorResource(item.icon), contentDescription =
//                     stringResource
//                     (item.name)) },
//                 label = {
//                     Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                         Text(
//                             text = stringResource(item.name),
//                             style = MaterialTheme.typography.labelMedium,
//                             fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
//                         )
//                         Spacer(Modifier.height(4.dp))
//                         Spacer(
//                             Modifier
//                                 .width(48.dp)
//                                 .height(2.dp)
//                                 .background(if (selected) activeColor else Color.Transparent),
//                         )
//                     }
//                 },
//                 selected = currentDestination?.hierarchy?.any {
//                     it.hasRoute(item.route::class)
//                 } == true,
//                 onClick = {
//                     navController.navigate(item.route) {
//                         popUpTo(navController.graph.findStartDestination().id) {
//                             saveState = true
//                         }
//                         launchSingleTop = true
//                         restoreState = true
//                     }
//                 },
//                 colors = NavigationBarItemDefaults.colors(
//                     selectedIconColor = activeColor,
//                     selectedTextColor = activeColor,
//                     indicatorColor = Color.Transparent,
//                     unselectedIconColor = NavInactive,
//                     unselectedTextColor = NavInactive,
//                 ),
//             )
//         }
//     }
// }