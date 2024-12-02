package com.supakavadeer.bookshelf.network

import kotlinx.serialization.Serializable


@Serializable
data class BooksList(
    val items: List<BookItem>
)

@Serializable
data class BookItem(
    val volumeInfo: VolumeInfo,
    val id: String,
    val selfLink: String
)

@Serializable
data class VolumeInfo(
    val title: String,
    val authors: List<String>? = null,
    val publisher: String? = null,
    val publishedDate: String? = null,
    val description: String? = null,
    val pageCount: Int? = null,
    val categories: List<String>? = null,
    val imageLinks: ImageLinks? = null,
    val language: String? = null,
    val previewLink: String? = null,
    val infoLink: String? = null
)

@Serializable
data class ImageLinks(
    var thumbnail: String?,
    var smallThumbnail: String?
)