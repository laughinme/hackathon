package com.example.hackathon.data.remote.dto

import com.example.hackathon.domain.model.Author
import com.example.hackathon.domain.model.Book
import com.example.hackathon.domain.model.BookCondition
import com.example.hackathon.domain.model.City
import com.example.hackathon.domain.model.CreateBookParams
import com.example.hackathon.domain.model.ExchangeLocation
import com.example.hackathon.domain.model.Genre
import java.time.OffsetDateTime

fun BookModelDto.toDomain(): Book = Book(
    id = this.id,
    ownerId = this.ownerId,
    title = this.title,
    description = this.description,
    extraTerms = this.extraTerms,
    author = this.author.toDomain(),
    genre = this.genre.toDomain(),
    language = this.language,
    pages = this.pages,
    condition = BookCondition.valueOf(this.condition.uppercase()),
    photoUrls = this.photoUrls,
    exchangeLocation = this.exchangeLocation.toDomain(),
    isAvailable = this.isAvailable,
    createdAt = OffsetDateTime.parse(this.createdAt),
    updatedAt = this.updatedAt?.let { OffsetDateTime.parse(it) }
)

fun AuthorModelDto.toDomain(): Author = Author(id = this.id, name = this.name)
fun GenreModelDto.toDomain(): Genre = Genre(id = this.id, name = this.name)
fun CityModelDto.toDomain(): City = City(id = this.id, name = this.name)

fun ExchangeLocationDto.toDomain(): ExchangeLocation = ExchangeLocation(
    id = this.id,
    title = this.title,
    address = this.address,
    city = this.city.toDomain()
)


// --- Мапперы из Domain в Request DTO ---

fun CreateBookParams.toRequest(): BookCreateRequest = BookCreateRequest(
    title = this.title,
    description = this.description,
    extra_terms = this.extraTerms,
    authorId = this.authorId,
    genreId = this.genreId,
    language = this.language,
    pages = this.pages,
    condition = this.condition.name.lowercase(),
    exchangeLocationId = this.exchangeLocationId
)