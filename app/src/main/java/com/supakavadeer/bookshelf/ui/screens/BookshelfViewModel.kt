package com.supakavadeer.bookshelf.ui.screens

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.supakavadeer.bookshelf.BookshelfApplication
import com.supakavadeer.bookshelf.data.BookshelfRepository
import com.supakavadeer.bookshelf.network.BookItem
import com.supakavadeer.bookshelf.network.BooksList
import kotlinx.coroutines.launch
import java.io.IOException

class BookshelfViewModel(private val bookshelfRepository: BookshelfRepository) : ViewModel() {

    var bookshelfUiState: BookshelfUiState by mutableStateOf(BookshelfUiState.Loading)
        private set

    init {
        getListOfBooks()
    }

    fun getListOfBooks() {
        viewModelScope.launch {
            bookshelfUiState = try {
                val booksList = bookshelfRepository.getListOfBooks()
                BookshelfUiState.Success(booksList.items)
            } catch (e: Exception) {
                Log.e("my_tag", e.stackTraceToString())
                BookshelfUiState.Error
            } catch (e: IOException) {
                BookshelfUiState.Error
            }
        }
    }


    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as BookshelfApplication)
                val bookshelfRepository = application.container.bookshelfRepository
                BookshelfViewModel(bookshelfRepository = bookshelfRepository)
            }
        }
    }
}


sealed interface BookshelfUiState {
    data class Success(val books: List<BookItem>) : BookshelfUiState
    object Loading : BookshelfUiState
    object Error : BookshelfUiState
}
