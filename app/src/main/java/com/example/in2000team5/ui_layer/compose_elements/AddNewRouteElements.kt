package com.example.in2000team5.ui_layer.compose_elements

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.in2000team5.R
import com.example.in2000team5.ui_layer.viewmodels.BicycleInformationViewModel

// Displays the button that allows user to add new routes
// TODO: Lag ny funksjon som faktisk viser kun knappen, og ikke hele skjermen
@Composable
fun ShowNewRouteButton(bicycleInformationViewModel: BicycleInformationViewModel) {
    val showForm = remember { mutableStateOf(false) }
    Scaffold(
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                ShowAllRoutes(ruter = bicycleInformationViewModel.getRoutes())
            }},
        floatingActionButton = {

            FloatingActionButton(
                onClick = {
                    showForm.value = true
                }) {
                Icon(imageVector = Icons.Default.Add, stringResource(R.string.add_new_route_button))
            }
        }
    )
    if (showForm.value) ShowNewRouteForm(showForm, bicycleInformationViewModel)
}

@Composable
fun ShowNewRouteForm(showForm: MutableState<Boolean>, bicycleInformationViewModel: BicycleInformationViewModel) {
    if (showForm.value) {
        val start = remember { mutableStateOf("") }
        val end = remember { mutableStateOf("") }

        CreateDialog(showForm, start, end, bicycleInformationViewModel) {
            UserInputView(start, end)
        }
    }
}

// Shows the input areas to the user
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
                 bicycleInformationViewModel: BicycleInformationViewModel,
                 content: @Composable (() -> Unit)? = null
                 )
{
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = { showForm.value = false },
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
                if (bicycleInformationViewModel.addRouteFromUser(context, start.value, end.value))
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

// Constructs the input areas, and shows descriptions with exampled of start and end routes.
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
            label = { Text("Start: (Eks. Forskningsparken) ") },
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
            label = { Text("Slutt: (Eks. Nydalen)") },
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