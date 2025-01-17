package com.example.quotes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.quotes.enums.HighlightColorEnum
import com.example.quotes.model.Quote
import com.example.quotes.model.QuotesState
import com.example.quotes.utils.positiveOrNegativeTransformedString
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers

@Preview(showBackground = true)
@Composable
fun QuotesTablePreview() {
    QuoteTable(
        quotes = listOf(
            Quote(
                "SBER",
                2.0,
                "Sberbank",
                100.0,
                10.0,
                "MCX",
            ),
            Quote(
                "VTB",
                2.0,
                "VTB",
                122.2,
                10.0,
                "SPB",
            )
        ).toPersistentList(),
    )
}


@Composable
fun QuotesTableView(viewModel: QuoteViewModel = hiltViewModel()) {
    val state by viewModel.quotesTableState.collectAsStateWithLifecycle()

    when (state) {
        is QuotesState.Loading -> LoaderComponent()
        is QuotesState.ShowQuotes -> QuoteTable(quotes = (state as QuotesState.ShowQuotes).quotes)
        QuotesState.Error -> NetworkError(onRetryClick = viewModel::onRetryClick)
    }
}

@Composable
private fun QuoteTable(quotes: List<Quote>) {
    val persistentList = quotes.toPersistentList()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
    ) {
        items(persistentList, key = { it.ticker.hashCode() }) { quote ->
            QuoteView(
                quote = quote,
                modifier = Modifier.padding(4.dp),
            )
        }
    }
}

@Composable
fun NetworkError(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                modifier = Modifier.padding(top = 120.dp),
                onClick = onRetryClick,
            ) {
                Text(text = stringResource(R.string.retry))
            }
            Text(
                text = stringResource(R.string.internet_lost_description),
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun LoaderComponent(modifier: Modifier = Modifier) {
    Surface(modifier) {
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun QuoteView(
    quote: Quote,
    modifier: Modifier = Modifier,
) {
    val isHighlighted = quote.isHighlightNeeded
    val highlightColor = quote.highlightColor

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TickerInfo(
                quote,
                Modifier.weight(1f),
            )

            PriceInfo(
                isHighlighted,
                highlightColor,
                quote,
                Modifier.weight(1.1f),
            )

            Spacer(modifier = Modifier.width(4.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(24.dp),
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 8.dp, end = 8.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline,
        )
    }
}

@Composable
private fun PriceInfo(
    isHighlighted: Boolean,
    highlightColor: HighlightColorEnum,
    quote: Quote,
    modifier: Modifier,
) {
    Column(
        horizontalAlignment = Alignment.End,
        modifier = modifier,
    ) {
        PercentageChange(
            isHighlighted,
            highlightColor,
            quote,
        )

        Text(
            text = stringResource(
                R.string.price_and_price_change__pattern,
                quote.lastPrice?.toBigDecimal().toString(),
                positiveOrNegativeTransformedString(quote.changePrice)
            ),
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun PercentageChange(
    isHighlighted: Boolean,
    highlightColor: HighlightColorEnum,
    quote: Quote,
) {
    Box(
        modifier = Modifier
            .background(
                color = if (isHighlighted) getHighlightColor(highlightColor) else Color.Transparent,
                shape = RoundedCornerShape(8.dp),
            )
    ) {
        Text(
            text = stringResource(
                R.string.percentage_change_pattern,
                positiveOrNegativeTransformedString(quote.percentageChange)
            ),
            style = MaterialTheme.typography.titleLarge,
            color = if (isHighlighted) Color.White else getChangeColor(quote.percentageChange),
        )
    }
}

@Composable
private fun TickerInfo(quote: Quote, modifier: Modifier) {
    Column(
        modifier = modifier
    ) {
        Row {
            TickerImage(quote)
            Spacer(modifier = Modifier.width(8.dp))
            Ticker(quote)
        }
        ExchangeAndName(quote)
    }
}

@Composable
private fun TickerImage(quote: Quote) {
    AsyncImage(
        model = getImageRequest(quote),
        contentDescription = null,
        modifier = Modifier
            .sizeIn(
                maxWidth = 24.dp,
                maxHeight = 24.dp
            ),
    )
}

@Composable
private fun ExchangeAndName(quote: Quote) {
    Text(
        text = buildString {
            append(quote.exchangeLatestTrade)
            if (!quote.name.isNullOrEmpty()) {
                append(" | ${quote.name}")
            }
        },
        style = MaterialTheme.typography.bodyMedium,
        color = Color.Gray,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun Ticker(quote: Quote) {
    Text(
        text = quote.ticker,
        style = MaterialTheme.typography.titleLarge,
        maxLines = 1,
        overflow = TextOverflow.Clip
    )
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

@Composable
fun getHighlightColor(highlightColor: HighlightColorEnum): Color {
    return when (highlightColor) {
        HighlightColorEnum.TRANSPARENT -> Color.Transparent
        HighlightColorEnum.RED -> Color.Red
        HighlightColorEnum.GREEN -> colorResource(id = R.color.custom_green)
    }
}

@Composable
fun getChangeColor(value: Double?): Color {
    return if (value != null) {
        if (value > 0) colorResource(id = R.color.custom_green) else if (value < 0) Color.Red else Color.Gray
    } else Color.Red
}

