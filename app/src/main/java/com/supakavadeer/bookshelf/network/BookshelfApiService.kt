package com.supakavadeer.bookshelf.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookshelfApiService {

    @GET("volumes")
    suspend fun getListOfBooks(@Query("q") query: String): BooksList

    @GET("volumes/{id}")
    suspend fun getBookDetails(@Path("id") bookId: String): BookItem
}