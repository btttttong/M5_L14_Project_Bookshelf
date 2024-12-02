package com.supakavadeer.bookshelf.data

import com.supakavadeer.bookshelf.network.BooksList
import com.supakavadeer.bookshelf.network.BookshelfApiService

class NetworkBookshelfRepository(private val bookshelfApiService: BookshelfApiService) : BookshelfRepository {

    override suspend fun getListOfBooks(query: String): BooksList {
        val booksList = bookshelfApiService.getListOfBooks(query)
        booksList.items.forEach { bookItem ->
            bookItem.volumeInfo.imageLinks?.thumbnail = bookItem.volumeInfo.imageLinks?.thumbnail?.replace("http://", "https://")
            bookItem.volumeInfo.imageLinks?.smallThumbnail = bookItem.volumeInfo.imageLinks?.smallThumbnail?.replace("http://", "https://")
        }
        return booksList
    }

}