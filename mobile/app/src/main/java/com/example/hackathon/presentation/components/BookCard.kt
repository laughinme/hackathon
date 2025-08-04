package com.example.hackathon.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.example.hackathon.domain.model.*
import java.time.OffsetDateTime

@Composable
fun BookCard(book: Book, onItemClick : () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = book.photoUrls.firstOrNull(),
                contentDescription = "Book cover",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = com.example.hackathon.R.drawable.ic_launcher_background), // Замени на свою заглушку
                error = painterResource(id = R.drawable.ic_launcher_background) // Замени на свою заглушку
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Автор: ${book.author.name}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Жанр: ${book.genre.name}",
                    style = MaterialTheme.typography.bodySmall
                )
                if (!book.description.isNullOrBlank()) {
                    Text(
                        text = book.description,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Состояние: ${book.condition}",
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = "Город: ${book.exchangeLocation.city.name}",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}


@Preview(showBackground = true, name = "Light Theme")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Theme")
@Composable
fun BookCardPreview() {
    PreviewTheme {
        BookCard(
            book = Book(
                id = "1",
                ownerId = "2",
                title = "Приключения Шерлока Холмса",
                description = "Сборник рассказов о знаменитом сыщике и его верном спутнике докторе Ватсоне.",
                extraTerms = "Только обмен на детективы",
                author = Author(id = 1, name = "Артур Конан Дойл"),
                genre = Genre(id = 1, name = "Детектив"),
                language = "Русский",
                pages = 320,
                condition = BookCondition.NEW,
                photoUrls = listOf("https://pictures.abebooks.com/isbn/9780140620719-us.jpg"), // Example image
                exchangeLocation = ExchangeLocation(
                    id = 1,
                    title = "Миша шиша",
                    address = "ул. Тверская, д. 1",
                    latitude = 30.33,
                    longitude = 30.33,
                    city = City(id = 1, name = "Москва"),
                    isActive = true
                ),
                isAvailable = true,
                createdAt = OffsetDateTime.now(),
                updatedAt = OffsetDateTime.now()
            ),
            onItemClick = {}
        )
    }
}
