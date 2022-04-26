package com.example.in2000team5.ui_layer

import android.util.Log
import android.widget.Toast
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
import com.example.in2000team5.domain_layer.BicycleViewModel
import com.example.in2000team5.ui_layer.cardViewActivity.VisAlleRuter

@Composable
fun VisNyRuteKnapp(bicycleViewModel: BicycleViewModel) {
    val showForm = remember { mutableStateOf(false) }
    Scaffold(
        content = { VisAlleRuter(ruter = bicycleViewModel.getRoutes())},
        floatingActionButton = {

            FloatingActionButton(
                modifier = Modifier.padding(24.dp, 64.dp),
                onClick = {
                    /*
                    val nyRute = mutableStateOf(BigBikeRoute(
                        200,
                        bicycleRouteList[2].value.fragmentList,
                        "Oslo",
                        "Tøyen",
                        200000.20,
                        mutableStateOf(2.0)
                    ))

                    bicycleViewModel.postRoutes(nyRute as SnapshotMutableState<BigBikeRoute>)
                     */
                    showForm.value = true
                }) {
                Icon(imageVector = Icons.Default.Add, "")
            }
        }
    )
    if (showForm.value) VisNyRuteSkjema(showForm, bicycleViewModel)
}

@Composable
fun VisNyRuteSkjema(showForm: MutableState<Boolean>, bicycleViewModel: BicycleViewModel) {
    if (showForm.value) {
        val start = remember { mutableStateOf("") }
        val slutt = remember { mutableStateOf("") }

        OpprettDialog(showForm, start, slutt, bicycleViewModel) {
            BrukerInputView(start, slutt)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BrukerInputView(start: MutableState<String>, slutt: MutableState<String>) {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Divider()
        StartOgSluttInput(Modifier, start, slutt)
    }
}

@Composable
fun OpprettDialog(showForm: MutableState<Boolean>,
                  start: MutableState<String>,
                  slutt: MutableState<String>,
                  bicycleViewModel: BicycleViewModel,
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
                Log.d("TEST LEGG TIL", start.value + " - " + slutt.value)
                if (bicycleViewModel.addRouteFromUser(context, start.value, slutt.value))
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
fun StartOgSluttInput(
    modifier: Modifier = Modifier,
    start: MutableState<String> = remember {
        mutableStateOf("")
    }, slutt: MutableState<String> = remember {
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
            value = slutt.value,
            onValueChange = { slutt.value = it },
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