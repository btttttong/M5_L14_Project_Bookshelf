package com.supakavadeer.bookshelf.ui.screens

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
import kotlinx.coroutines.launch
import java.io.IOException

class BookshelfViewModel(private val bookshelfRepository: BookshelfRepository) : ViewModel() {

    var bookshelfUiState: BookshelfUiState by mutableStateOf(BookshelfUiState.Loading)
        private set

    var query: String by mutableStateOf("")
    var currentBookId: String? by mutableStateOf(null)

    init {
        bookshelfUiState = BookshelfUiState.Loading
        Log.d("BookshelfViewModel", "ViewModel is initialized")
    }


    fun getListOfBooks(query: String) {
        viewModelScope.launch {
            bookshelfUiState = try {
                val booksList = bookshelfRepository.getListOfBooks(query)
                BookshelfUiState.Success(booksList.items)
            } catch (e: Exception) {
                Log.e("my_tag", e.stackTraceToString())
                BookshelfUiState.Error
            } catch (e: IOException) {
                BookshelfUiState.Error
            }
        }
    }

    fun resetOrder() {
        bookshelfUiState = BookshelfUiState.Loading
        getListOfBooks(query)
    }

    fun updateQuery(newQuery: String) {
        if (newQuery != query) {
            query = newQuery
            getListOfBooks(query)
        }
    }

    fun clearQuery() {
        query = ""
    }

    fun getCurrentBook(): BookItem? {
        return bookshelfUiState.let {
            if (it is BookshelfUiState.Success) {
                it.books.find { book -> book.id == currentBookId }
            } else null
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
