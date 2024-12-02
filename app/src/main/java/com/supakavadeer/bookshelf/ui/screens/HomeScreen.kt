package com.supakavadeer.bookshelf.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction

@Composable
fun QuerySelectionScreen(
    viewModel: BookshelfViewModel,
    onQuerySubmitted: (String) -> Unit,
    onNextButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var query by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = modifier) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Enter keyword") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                if (query.isNotBlank()) {
                    onQuerySubmitted(query)
                    keyboardController?.hide()
                } else {
                    Toast.makeText(context, "Please enter a valid keyword.", Toast.LENGTH_SHORT).show()
                }
            }),
            modifier = Modifier.fillMaxWidth()
        )

        MenuScreenButtonGroup(
            selectedItemName = query,
            onCancelButtonClicked = { query = "" },
            onNextButtonClicked = {
                if (query.isNotBlank()) {
                    viewModel.updateQuery(query)
                    onNextButtonClicked()
                } else {
                    Toast.makeText(context, "Please enter a valid keyword.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}