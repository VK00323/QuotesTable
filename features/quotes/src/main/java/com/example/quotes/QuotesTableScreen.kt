package com.example.quotes

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

@Preview(showBackground = true)
@Composable
fun QuotesTablePreview() {
    QuotesTableView(
        quotes = listOf(
            Quote(
                "SBER",
                2.0,
                "Sberbank",
                100.0,
                10.0,
                "MCX"
            ),
            Quote(
                "VTB",
                2.0,
                "VTB",
                122.2,
                10.0,
                "SPB"
            )
        ).toPersistentList(),
        isLoading = false,
        isError = true,
        onRetryClick = {
            Log.d("QuotesTablePreview", "Retry button clicked")
        }
    )
}


@Composable
fun QuotesTableView(viewModel: QuoteViewModel = hiltViewModel()) {
    val state by viewModel.quotesTableState.collectAsStateWithLifecycle()

    QuotesTableView(
        quotes = state.quotes.toPersistentList(),
        isLoading = state.isLoading,
        isError = state.isError,
        onRetryClick = viewModel::onRetryClick,
    )
}

@Composable
private fun QuotesTableView(
    quotes: PersistentList<Quote>,
    isLoading: Boolean,
    isError: Boolean,
    onRetryClick: () -> Unit = {},
) {
    when {
        isLoading -> LoaderComponent(isLoading = true)
        isError -> NetworkError(isError = true, onRetryClick = onRetryClick)
        else -> QuoteTable(quotes = quotes)
    }
}

@Composable
private fun QuoteTable(quotes: PersistentList<Quote>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(horizontal = 8.dp),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        items(quotes, key = { it.ticker.hashCode() }) { quote ->
            QuoteView(
                quote = quote,
                modifier = Modifier.padding(4.dp),
            )
        }
    }
}

@Composable
fun NetworkError(
    isError: Boolean,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isError) {
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    modifier = Modifier.padding(top = 120.dp),
                    onClick = onRetryClick
                ) {
                    Text(text = stringResource(R.string.retry))
                }
            }
        }
    }
}

@Composable
fun LoaderComponent(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    if (isLoading) {
        Surface(modifier) {
            Box(
                modifier = modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun QuoteView(
    quote: Quote,
    modifier: Modifier = Modifier,
) {
    val imageSize = 24.dp

    //TODO Вернуться
    var previousChangePercent by rememberSaveable { mutableDoubleStateOf(quote.changePercent) }
    var isHighlighted by rememberSaveable { mutableStateOf(false) }
    val highlightColor by remember(quote.changePercent) {
        mutableStateOf(if ((quote.changePercent) >= 0) Color.Green else Color.Red)
    }

    LaunchedEffect(quote.changePercent) {
        if (quote.changePercent != previousChangePercent) {
            isHighlighted = true
            delay(500)
            isHighlighted = false
            previousChangePercent = quote.changePercent
        }
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Информация о тикере
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row {
                    AsyncImage(
                        model = getImageRequest(quote),
                        contentDescription = null,
                        modifier = Modifier
                            .size(imageSize)
                            .then(
                                if (quote.ticker.isEmpty()) Modifier.background(Color.Transparent)
                                else Modifier
                            ),
                        placeholder = ColorPainter(Color.Gray),
                        onError = {
                            Modifier.size(0.dp)
                        },
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Тикер (пример: TGKA)
                    Text(
                        text = quote.ticker,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Clip
                    )
                }

                // Биржа и название бумаги (пример: MCX | TGK-1)
                Text(
                    text = "${quote.exchangeLatestTrade} | ${quote.name}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Процент изменения и цена
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.weight(1.1f)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = if (isHighlighted) highlightColor else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    // Изменение в процентах
                    Text(
                        text = stringResource(
                            R.string.percentage_change_pattern,
                            quote.changePercent,
                        ),
                        style = MaterialTheme.typography.titleLarge,
                        color = if (isHighlighted) Color.White else getChangeColor(quote.changePercent),
                    )
                }

                // Цена и изменение цены
                Text(
                    text = stringResource(
                        R.string.price_and_price_change__pattern,
                        quote.lastPrice?.toBigDecimal().toString(),
                        positiveOrNegativeTransformedString(quote.priceChange)
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(imageSize)
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 8.dp, end = 8.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
private fun getImageRequest(quote: Quote): ImageRequest {
    val url = stringResource(R.string.quote_logo_url_pattern, quote.ticker.lowercase())
    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(url)
        .dispatcher(Dispatchers.IO)
        .memoryCacheKey(url)
        .diskCacheKey(url)
        .error(null)
        .fallback(null)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()
    return imageRequest
}

//Todo по хорошему в Utils
fun getChangeColor(value: Double?): Color {
    return if (value != null) {
        if (value > 0) Color.Green else if (value < 0) Color.Red else Color.Gray
    } else Color.Red
}

fun positiveOrNegativeTransformedString(amount: Double): String {
    val string = amount.toBigDecimal().toString()
    return if (amount > 0) {
        "+$string"
    } else {
        string
    }
}
