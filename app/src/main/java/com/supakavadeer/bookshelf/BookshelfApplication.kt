package com.supakavadeer.bookshelf

import android.app.Application
import com.supakavadeer.bookshelf.data.AppContainer
import com.supakavadeer.bookshelf.data.DefaultAppContainer

class BookshelfApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}