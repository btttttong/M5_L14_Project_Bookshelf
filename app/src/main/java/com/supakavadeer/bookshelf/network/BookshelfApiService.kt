package com.supakavadeer.bookshelf.network

import retrofit2.http.GET
import retrofit2.http.Path

interface BookshelfApiService {

    @GET("volumes?q=jazz+history")
    suspend fun getListOfBooks(): BooksList

    @GET("photos")
    suspend fun getBookDetails(@Path("id") bookId: String): BookItem
}