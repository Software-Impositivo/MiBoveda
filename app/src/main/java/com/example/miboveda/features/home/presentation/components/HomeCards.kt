package com.example.miboveda.features.home.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.miboveda.R
import com.example.miboveda.features.home.presentation.states.HomeUiState
import com.example.miboveda.features.home.presentation.states.MonthBar
import com.example.miboveda.ui.theme.bigScanButton
import com.example.miboveda.ui.theme.smallText
import com.example.miboveda.ui.theme.cardTitle
import com.example.miboveda.ui.theme.white

private val CardShape = RoundedCornerShape(24.dp)
private val VaultShape = RoundedCornerShape(20.dp)

// ───────────────────────── Digitaliza un recibo ─────────────────────────

@Composable
internal fun ScanReceiptCard(
    modifier: Modifier = Modifier,
    onScanClick: () -> Unit = {},
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = CardShape,
        color = MaterialTheme.colorScheme.secondaryContainer,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(MaterialTheme.colorScheme.onSurface, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_file_search),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = bigScanButton,
                    )
                }
                Column {
                    Text(
                        text = stringResource(R.string.scan_title),
                        style = MaterialTheme.typography.displayLarge,
                        color = cardTitle
                    )
                    Text(
                        text = stringResource(R.string.scan_subtitle),
                        style = MaterialTheme.typography.bodySmall,
                        color = smallText,
                    )
                }
            }
            Button(
                onClick = onScanClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = bigScanButton,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_camera),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                Text(
                    text = stringResource(R.string.scan_action),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

// ───────────────────────────── Mi bóveda ─────────────────────────────

@Composable
internal fun VaultCard(
    state: HomeUiState,
    modifier: Modifier = Modifier,
    onNotificationsClick: () -> Unit = {},
) {
    val colors = MaterialTheme.colorScheme
    val shadowColor = colors.primaryContainer.copy(alpha = 0.45f)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = VaultShape,
                ambientColor = shadowColor,
                spotColor = shadowColor,
            ),
        shape = VaultShape,
        color = colors.primaryContainer,
        contentColor = colors.onPrimaryContainer,
    ) {
        Column(modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.vault_label),
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.tertiary,
                    )
                    Text(
                        text = stringResource(R.string.vault_greeting, state.userName),
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }
                FilledIconButton(
                    onClick = onNotificationsClick,
                    modifier = Modifier.size(40.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = colors.surface,
                        contentColor = colors.primaryContainer,
                    ),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_bell),
                        contentDescription = stringResource(R.string.cd_notifications),
                        modifier = Modifier.size(20.dp),
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.vault_spent_title, state.year),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f),
                )
                GrowthChip(text = state.growth)
            }

            Text(
                text = state.totalSpent,
                style = MaterialTheme.typography.displaySmall,
            )

            Spacer(Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                val secondary = colors.onPrimaryContainer.copy(alpha = 0.8f)
                Text(
                    text = stringResource(R.string.vault_receipts_saved, state.receiptCount),
                    style = MaterialTheme.typography.bodySmall,
                    color = secondary,
                )
                Text(
                    text = stringResource(R.string.vault_vat, state.vat),
                    style = MaterialTheme.typography.bodySmall,
                    color = secondary,
                )
            }
        }
    }
}

@Composable
private fun GrowthChip(text: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = white.copy(alpha = 0.16f),
        contentColor = white,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_trending_up),
                contentDescription = null,
                modifier = Modifier.size(14.dp),
            )
            Text(text = text, style = MaterialTheme.typography.labelMedium)
        }
    }
}

// ───────────────────── Ingresos y gastos mensuales ─────────────────────

private val ChartHeight = 125.dp
private val BarWidth = 16.dp
private val BarGap = 4.dp
private val BarCorner = 4.dp

@Composable
internal fun MonthlyChartCard(
    months: List<MonthBar>,
    netBalance: String,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = CardShape,
        color = colors.surfaceContainer,
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.chart_title),
                        style = MaterialTheme.typography.titleSmall,
                        color = cardTitle,
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        LegendItem(colors.secondary, stringResource(R.string.legend_income))
                        LegendItem(colors.primaryContainer, stringResource(R.string.legend_expenses))
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(R.string.net_balance_label),
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.onSurfaceVariant,
                    )
                    Text(
                        text = netBalance,
                        style = MaterialTheme.typography.titleSmall,
                        color = bigScanButton,
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            val chartDescription = stringResource(R.string.cd_chart)
            MonthlyBars(
                months = months,
                incomeColor = colors.secondary,
                expenseColor = colors.primaryContainer,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ChartHeight)
                    .semantics { contentDescription = chartDescription },
            )

            Spacer(Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                months.forEach { month ->
                    Text(
                        text = month.label,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(Modifier.size(10.dp).background(color, CircleShape))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/** Un único nodo de dibujo: sin un composable por barra. */
@Composable
private fun MonthlyBars(
    months: List<MonthBar>,
    incomeColor: Color,
    expenseColor: Color,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val groupWidth = size.width / months.size
        val barWidth = BarWidth.toPx()
        val gap = BarGap.toPx()
        val groupContent = barWidth * 2 + gap

        months.forEachIndexed { index, month ->
            val startX = index * groupWidth + (groupWidth - groupContent) / 2f
            drawBar(startX, barWidth, month.income * size.height, incomeColor)
            drawBar(startX + barWidth + gap, barWidth, month.expense * size.height, expenseColor)
        }
    }
}

private fun DrawScope.drawBar(x: Float, width: Float, height: Float, color: Color) {
    val radius = CornerRadius(BarCorner.toPx())
    val path = Path().apply {
        addRoundRect(
            RoundRect(
                left = x,
                top = size.height - height,
                right = x + width,
                bottom = size.height,
                topLeftCornerRadius = radius,
                topRightCornerRadius = radius,
            ),
        )
    }
    drawPath(path, color)
}

// ──────────────────────────── Recibos recientes ────────────────────────────

@Composable
internal fun RecentReceiptsHeader(
    modifier: Modifier = Modifier,
    onSeeAllClick: () -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.recent_title),
            style = MaterialTheme.typography.titleMedium,
            color = cardTitle,
        )
        TextButton(
            onClick = onSeeAllClick,
            contentPadding = PaddingValues(0.dp),
        ) {
            Text(
                text = stringResource(R.string.see_all),
                style = MaterialTheme.typography.labelMedium,
                color = bigScanButton,
            )
        }
    }
}