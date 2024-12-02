package com.supakavadeer.bookshelf.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DetailScreen(
    retryAction: () -> Unit,
    bookshelfUiState: BookshelfUiState,
) {

}

@Composable
fun BookDetailScreen(viewModel: BookshelfViewModel) {
    val book = viewModel.getCurrentBook()

    if (book != null) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Title: ${book.volumeInfo.title}", style = MaterialTheme.typography.headlineMedium)
            Text("Authors: ${book.volumeInfo.authors?.joinToString(", ")}")
            Text("Description: ${book.volumeInfo.description}")
        }
    } else {
        Text("Book not found")
    }
}

