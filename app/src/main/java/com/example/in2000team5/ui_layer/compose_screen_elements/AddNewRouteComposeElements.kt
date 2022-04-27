package com.example.in2000team5.ui_layer.compose_screen_elements

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.in2000team5.ui_layer.viewmodels.BicycleRouteViewModel

@Composable
fun ShowNewRouteButton(bicycleRouteViewModel: BicycleRouteViewModel) {
    val showForm = remember { mutableStateOf(false) }
    Scaffold(
        content = { ShowAllRoutes(ruter = bicycleRouteViewModel.getRoutes())},
        floatingActionButton = {

            FloatingActionButton(
                modifier = Modifier.padding(24.dp, 64.dp),
                onClick = {
                    showForm.value = true
                }) {
                Icon(imageVector = Icons.Default.Add, "")
            }
        }
    )
    if (showForm.value) ShowNewRouteForm(showForm, bicycleRouteViewModel)
}

@Composable
fun ShowNewRouteForm(showForm: MutableState<Boolean>, bicycleRouteViewModel: BicycleRouteViewModel) {
    if (showForm.value) {
        val start = remember { mutableStateOf("") }
        val end = remember { mutableStateOf("") }

        CreateDialog(showForm, start, end, bicycleRouteViewModel) {
            UserInputView(start, end)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserInputView(start: MutableState<String>, end: MutableState<String>) {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Divider()
        StartAndEndInput(Modifier, start, end)
    }
}

@Composable
fun CreateDialog(showForm: MutableState<Boolean>,
                 start: MutableState<String>,
                 end: MutableState<String>,
                 bicycleRouteViewModel: BicycleRouteViewModel,
                 content: @Composable (() -> Unit)? = null
                  )
{
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = { showForm.value = false }, // sjekk ut om dette er riktig
        title = {
            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "Legg til ny rute")
            }
        },
        text = content,
        confirmButton = {
            TextButton(onClick = {
                Log.d("TEST LEGG TIL", start.value + " - " + end.value)
                if (bicycleRouteViewModel.addRouteFromUser(context, start.value, end.value))
                    showForm.value = false
            })
            { Text(text = "Legg til rute") }
        },
        dismissButton = {
            TextButton(onClick = { showForm.value = false })
            { Text(text = "Avbryt") }
        },
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@ExperimentalComposeUiApi
@Composable
fun StartAndEndInput(
    modifier: Modifier = Modifier,
    start: MutableState<String> = remember {
        mutableStateOf("")
    }, end: MutableState<String> = remember {
        mutableStateOf("")
    }
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = start.value,
            onValueChange = { start.value = it },
            label = { Text("Start") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {
                keyboardController?.hide()
                focusRequester.requestFocus()
            }),
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = end.value,
            onValueChange = { end.value = it },
            label = { Text("Slutt") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {
                keyboardController?.hide()
                focusRequester.requestFocus()
            })
        )
    }
}