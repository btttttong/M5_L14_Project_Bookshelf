package com.supakavadeer.bookshelf.data

import com.supakavadeer.bookshelf.network.BooksList

interface BookshelfRepository {
    suspend fun getListOfBooks(): BooksList
}