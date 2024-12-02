package com.supakavadeer.bookshelf.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.supakavadeer.bookshelf.R
import com.supakavadeer.bookshelf.ui.screens.BookDetailScreen
import com.supakavadeer.bookshelf.ui.screens.BookshelfScreen
import com.supakavadeer.bookshelf.ui.screens.BookshelfViewModel
import com.supakavadeer.bookshelf.ui.screens.QuerySelectionScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookshelfApp() {
    val viewModel: BookshelfViewModel = viewModel(
        factory = BookshelfViewModel.factory
    )
    val useFakeData = true
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val navController: NavHostController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = BookshelfScreen.valueOf(
        backStackEntry?.destination?.route ?: BookshelfScreen.START.name
    )


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            BookshelfTopAppBar(
                canNavigateBack = navController.previousBackStackEntry != null,
                currentScreen = currentScreen,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = BookshelfScreen.START.name,
            modifier = Modifier
                .padding(innerPadding)
                .padding(dimensionResource((R.dimen.padding_medium)))
        ) {
            composable(BookshelfScreen.START.name) {
                QuerySelectionScreen(
                    viewModel = viewModel,
                    onQuerySubmitted = { query -> viewModel.updateQuery(query) },
//                    onCancelButtonClicked = { cancelAndNavigateToStart(viewModel, navController) },
                    onNextButtonClicked = { navController.navigate(BookshelfScreen.SHELF.name) },
                )
            }
            composable(BookshelfScreen.SHELF.name) {
                BookshelfScreen(
                    retryAction = { viewModel.resetOrder() },
                    bookshelfUiState = viewModel.bookshelfUiState,
                    onNextButtonClicked= { navController.navigate(BookshelfScreen.DETAIL.name) },
                    viewModel = viewModel,
                    navController = navController
                )
            }
            composable(BookshelfScreen.DETAIL.name) {
                BookDetailScreen(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookshelfTopAppBar(
    currentScreen: BookshelfScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

enum class BookshelfScreen(@StringRes val title: Int) {
    START(title = R.string.choose_volume),
    SHELF(title = R.string.app_name),
    DETAIL(title = R.string.book_detail),
}
