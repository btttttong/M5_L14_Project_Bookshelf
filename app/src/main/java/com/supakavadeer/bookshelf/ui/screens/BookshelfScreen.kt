package com.supakavadeer.bookshelf.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.supakavadeer.bookshelf.R
import com.supakavadeer.bookshelf.network.BookItem

@Composable
fun BookshelfScreen(
    retryAction: () -> Unit,
    bookshelfUiState: BookshelfUiState,
    onNextButtonClicked: () -> Unit,
    viewModel: BookshelfViewModel,
    navController: NavHostController,
) {

    when (bookshelfUiState) {
        is BookshelfUiState.Success ->
            PhotosGridScreen(
                books = bookshelfUiState.books,
                modifier = Modifier.fillMaxWidth(),
                navController = navController,
                viewModel = viewModel
            )

        BookshelfUiState.Loading ->
            LoadingScreen(
                modifier = Modifier.fillMaxSize(),
            )

        BookshelfUiState.Error ->
            ErrorScreen(
                modifier = Modifier.fillMaxWidth(),
                retryAction = retryAction
            )

    }
}

@Composable
fun BooksPhotoCard(
    book: BookItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(book.volumeInfo.imageLinks?.thumbnail ?: "")
                .crossfade(true)
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(R.string.books_photo),
            error = painterResource(id = R.drawable.ic_broken_image),
            placeholder = painterResource(id = R.drawable.loading_img),
            modifier = modifier.fillMaxWidth()
        )
    }

}

@Composable
fun PhotosGridScreen(
    books: List<BookItem>,
    viewModel: BookshelfViewModel,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    navController: NavController,
) {
    if (books.isEmpty()) {
        EmptyScreen()
        return
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            modifier = modifier.padding(horizontal = 8.dp),
            contentPadding = contentPadding,
        ) {
            items(books, key = { it.id }) { book ->
                BooksPhotoCard(
                    book = book,
                    modifier = Modifier
                        .padding(8.dp)
                        .aspectRatio(1f),
                    onClick = {
                        viewModel.getCurrentBook()
                        navController.navigate(com.supakavadeer.bookshelf.ui.BookshelfScreen.DETAIL.name)
                    },
                )
            }
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(id = R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading),
    )
}

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    retryAction: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error),
            contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed))
        Button(onClick = retryAction) {
            Text(text = stringResource(R.string.retry))
        }
    }
}

@Composable
fun EmptyScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("No books found", style = MaterialTheme.typography.displayLarge)
    }
}


//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun HomeScreenPreview() {
//    BookshelfTheme {
//        BookshelfScreen(
//            retryAction = {},
//            bookshelfUiState = BookshelfUiState.Success(listOf()),
//            onNextButtonClicked = {}
//        )
//    }
//}