package com.example.hackathon.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.compose.PreviewTheme
import com.example.hackathon.R
import com.example.hackathon.domain.model.Author
import com.example.hackathon.domain.model.Book
import com.example.hackathon.domain.model.BookCondition
import com.example.hackathon.domain.model.City
import com.example.hackathon.domain.model.ExchangeLocation
import com.example.hackathon.domain.model.Genre
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * Composable для отображения детальной информации о книге внутри ModalBottomSheet.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsBottomSheet(
    book: Book,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onLikeClick: () -> Unit,
    onReserveClick: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        contentWindowInsets = { WindowInsets(0) }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ... (существующий код для отображения информации о книге)
            item {
                AsyncImage(
                    model = book.photoUrls.firstOrNull(),
                    contentDescription = "Book cover",
                    modifier = Modifier
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Fit,
                    placeholder = painterResource(id = R.drawable.ic_launcher_background),
                    error = painterResource(id = R.drawable.ic_launcher_background)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = book.author.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            if (!book.description.isNullOrBlank()) {
                item { InfoRow(label = "Описание", value = book.description) }
            }
            item { InfoRow(label = "Жанр", value = book.genre.name) }
            item { InfoRow(label = "Язык", value = book.language) }
            if (book.pages != null) {
                item { InfoRow(label = "Количество страниц", value = book.pages.toString()) }
            }
            item { InfoRow(label = "Состояние", value = book.condition.name) }
            if (!book.extraTerms.isNullOrBlank()) {
                item { InfoRow(label = "Условия обмена", value = book.extraTerms) }
            }
            item {
                InfoRow(
                    label = "Место обмена",
                    value = "${book.exchangeLocation.city.name}, ${book.exchangeLocation.address}"
                )
            }
            val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
            item { InfoRow(label = "Дата добавления", value = book.createdAt.format(formatter)) }

            // Кнопки действий
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TextButton(
                        onClick = onLikeClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Favorite, contentDescription = "Like")
                        Spacer(modifier = Modifier.height(ButtonDefaults.IconSpacing))
                        Text("В избранное")
                    }
                    Button(
                        onClick = onReserveClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Reserve")
                        Spacer(modifier = Modifier.height(ButtonDefaults.IconSpacing))
                        Text("Обменять")
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun BookDetailsBottomSheetPreview() {
    val book = Book(
        id = "1",
        ownerId = "2",
        title = "Приключения Шерлока Холмса",
        description = "Сборник рассказов о знаменитом сыщике и его верном спутнике докторе Ватсоне. Книга в отличном состоянии, почти новая.",
        extraTerms = "Только обмен на детективы Артура Конан Дойла или Агаты Кристи.",
        author = Author(id = 1, name = "Артур Конан Дойл"),
        genre = Genre(id = 1, name = "Детектив"),
        language = "Русский",
        pages = 320,
        condition = BookCondition.NEW,
        photoUrls = listOf("https://pictures.abebooks.com/isbn/9780140620719-us.jpg"),
        exchangeLocation = ExchangeLocation(
            id = 1,
            title = "Книжный клуб",
            address = "ул. Тверская, д. 1",
            latitude = 30.33,
            longitude = 30.33,
            city = City(id = 1, name = "Москва"),
            isActive = true
        ),
        isAvailable = true,
        createdAt = OffsetDateTime.now().minusDays(5),
        updatedAt = OffsetDateTime.now()
    )
    PreviewTheme {
        BookDetailsBottomSheet(
            book = book,
            sheetState = rememberModalBottomSheetState(),
            onDismiss = {},
            onLikeClick = {},
            onReserveClick = {}
        )
    }
}
