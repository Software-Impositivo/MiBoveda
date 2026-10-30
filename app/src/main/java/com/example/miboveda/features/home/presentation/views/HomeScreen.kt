package com.example.miboveda.features.home.presentation.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.miboveda.features.home.presentation.components.MonthlyChartCard
import com.example.miboveda.features.home.presentation.components.RecentReceiptsHeader
import com.example.miboveda.features.home.presentation.states.HomeUiState
import com.example.miboveda.features.home.presentation.components.ScanReceiptCard
import com.example.miboveda.features.home.presentation.components.VaultCard
import com.example.miboveda.features.home.presentation.states.SampleHomeState
import com.example.miboveda.ui.theme.MiBovedaTheme

private val ScreenHorizontalPadding = 24.dp
private val SectionSpacing = 40.dp

@Composable
fun HomeScreen(
    state: HomeUiState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
//        bottomBar = { ReceiptsBottomBar(selectedTab = HomeTab.Home) },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier,
            contentPadding = PaddingValues(
                start = ScreenHorizontalPadding,
                end = ScreenHorizontalPadding,
                top = innerPadding.calculateTopPadding() + 8.dp,
                bottom = innerPadding.calculateBottomPadding() + 16.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(SectionSpacing),
        ) {
            item(key = "scan", contentType = "card") {
                ScanReceiptCard()
            }
            item(key = "vault", contentType = "card") {
                VaultCard(state = state)
            }
            item(key = "chart", contentType = "card") {
                MonthlyChartCard(months = state.months, netBalance = state.netBalance)
            }
            item(key = "recent_header", contentType = "header") {
                RecentReceiptsHeader()
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=390dp,height=844dp")
@Composable
private fun HomeScreenPreview() {
    MiBovedaTheme { HomeScreen(state = SampleHomeState) }
}