package com.example.hackathon.data.remote.dto

import com.example.hackathon.domain.model.Author
import com.example.hackathon.domain.model.Book
import com.example.hackathon.domain.model.BookCondition
import com.example.hackathon.domain.model.City
import com.example.hackathon.domain.model.CreateBookParams
import com.example.hackathon.domain.model.Exchange
import com.example.hackathon.domain.model.ExchangeLocation
import com.example.hackathon.domain.model.ExchangeProgress
import com.example.hackathon.domain.model.Genre
import com.example.hackathon.domain.model.UserShare
import com.google.gson.annotations.SerializedName
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
    latitude = this.latitude,
    longitude = this.longitude,
    city = this.city.toDomain(),
    isActive = this.isActive
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

fun UserShareDto.toDomain(): UserShare {
    return UserShare(
        id = this.id,
        username = this.username,
        avatarUrl = this.avatarUrl,
        city = this.city.toDomain() // Предполагается, что CityMapper уже существует
    )
}

fun ExchangeModelDto.toDomain(): Exchange {
    return Exchange(
        id = this.id,
        owner = this.owner.toDomain(),
        requester = this.requester.toDomain(),
        book = this.book.toDomain(), // Предполагается, что BookMapper уже существует
        progress = try {
            ExchangeProgress.valueOf(this.progress.uppercase())
        } catch (e: IllegalArgumentException) {
            ExchangeProgress.UNKNOWN
        },
        cancelReason = this.cancelReason,
        comment = this.comment,
        meetingTime = this.meetingTime,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}


/**
 * DTO для публичной информации о пользователе, как она приходит с сервера.
 */
data class UserShareDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("username")
    val username: String?,

    @SerializedName("avatar_url")
    val avatarUrl: String?,

    @SerializedName("city")
    val city: CityModelDto // Предполагается, что CityModelDto уже существует
)

/**
 * DTO для представления объекта обмена, как он приходит с сервера.
 */
data class ExchangeModelDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("owner")
    val owner: UserShareDto,

    @SerializedName("requester")
    val requester: UserShareDto,

    @SerializedName("book")
    val book: BookModelDto, // Предполагается, что BookModelDto уже существует

    @SerializedName("progress")
    val progress: String, // "created", "accepted", etc.

    @SerializedName("cancel_reason")
    val cancelReason: String?,

    @SerializedName("comment")
    val comment: String?,

    @SerializedName("meeting_time")
    val meetingTime: OffsetDateTime?,

    @SerializedName("created_at")
    val createdAt: OffsetDateTime,

    @SerializedName("updated_at")
    val updatedAt: OffsetDateTime?
)

