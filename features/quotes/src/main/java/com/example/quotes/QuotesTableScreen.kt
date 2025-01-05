package com.example.quotes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.grid.GridCells.Fixed
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList


@Preview(showBackground = true)
@Composable
fun QuotesTablePreview() {
    QuotesTableView(
        quotes = listOf(
            Quote(
                "ticker",
                2.0,
                "exchange",
                "name",
                10.0,
                1.0
            ),
            Quote(
                "ticker",
                2.0,
                "exchange",
                "name",
                10.0,
                1.0
            )
        ).toPersistentList(),
        isLoading = false
    )
}


@Composable
fun QuotesTableView(viewModel: QuoteViewModel = hiltViewModel()) {
    val state by viewModel.quotesTableState.collectAsStateWithLifecycle()

    QuotesTableView(
        quotes = state.quotes.toPersistentList(),
//  Todo      quotesLogo = listOf(QuotesLogo), // или на месте буду качать?
        isLoading = state.isLoading,
        // Todo Ошибка?
    )
}

//Todo посмотреть что с загрузкой
@Composable
fun LoaderComponents(modifier: Modifier = Modifier) {
    Surface(modifier) {
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
            Text(
                text = "loading",
                Modifier
                    .align(Alignment.BottomCenter)
                    .windowInsetsPadding(WindowInsets.systemBars)
            )
        }
    }
}

@Composable
private fun QuotesTableView(
    quotes: PersistentList<Quote>,
    isLoading: Boolean,
) {
    LazyVerticalGrid(
        columns = Fixed(1),
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        items(quotes) { quote ->
            QuoteView(
                quote = quote,
                modifier = Modifier.padding(4.dp),
            )
        }
    }
}

@Composable
fun QuoteView(
    quote: Quote,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Todo  Картинка тикера пока так для теста
            AsyncImage(
                model = "https://tradernet.com/logos/get-logo-by-ticker?ticker=${quote.ticker?.lowercase()}",
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp) // Todo в ресурсы
                    .clip(CircleShape)
                    .background(Color(0xFFE0E0E0)) //Todo Фон ошибки доработать
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Информация о тикере
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Тикер (пример: TGKA)
                Text(
                    text = quote.ticker ?: "",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                // Биржа и название бумаги (пример: MCX | TGK-1)
                Text(
                    text = "${quote.exchange} | ${quote.name}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Процент изменения и цена
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.weight(1.5f)
            ) {
                // Изменение в процентах
                Text(
                    text = "%.2f%%".format(quote.changePercent),
                    style = MaterialTheme.typography.titleMedium,
                    color = getChangeColor(quote.changePercent),
                    fontWeight = FontWeight.Bold
                )
                // Цена и изменение цены
                Text(
                    text = "${"%.6f".format(quote.lastPrice)} (${if ((quote.priceChange ?: 0.0) >= 0) "+" else ""}${
                        "%.6f".format(
                            quote.priceChange
                        )
                    })",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            thickness = 1.dp,
            color = Color(0xFFE0E0E0)
        )
    }
}


//Todo это просто заглуша. Потом написать логику, перенести во вью модель
fun getChangeColor(value: Double?): Color {
    return if (value != null) {
        if (value > 0) Color.Green else if (value < 0) Color.Red else Color.Gray
    } else Color.Red
}