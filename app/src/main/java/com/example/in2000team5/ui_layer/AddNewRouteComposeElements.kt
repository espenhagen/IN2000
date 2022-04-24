package com.example.in2000team5.ui_layer

import android.util.Log
import android.widget.EditText
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotMutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.in2000team5.data_layer.BicycleRoute
import com.example.in2000team5.data_layer.BigBikeRoute
import com.example.in2000team5.domain_layer.BicycleViewModel
import com.example.in2000team5.ui_layer.bicycleRouteList

@Composable
fun VisNyRuteKnapp(bicycleViewModel: BicycleViewModel) {
    val showForm = remember { mutableStateOf(false) }
    Scaffold(
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
    ) {

    }
    if (showForm.value) VisNyRuteSkjema(showForm)
    // TODO: remove form showForm is true
}


@Composable
fun VisNyRuteSkjema(showForm: MutableState<Boolean>) {
    if (showForm.value) {
        OpprettDialog(showForm) {
            BrukerInputView()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BrukerInputView() {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Divider()
        StartOgSluttInput()
    }
}

@Composable
fun OpprettDialog(showForm: MutableState<Boolean>, content: @Composable (() -> Unit)? = null) {
    // TODO: Create the form and make user input values
    Log.d("test", "test klikk")
    var start = ""
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
            TextButton(onClick = {})
            { Text(text = "Legg til rute") }
        },
        dismissButton = {
            TextButton(onClick = {})
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