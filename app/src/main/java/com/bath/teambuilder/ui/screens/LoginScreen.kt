package com.bath.teambuilder.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun LoginScreenBody(onLoginClick: (String) -> Unit) {

    var email by rememberSaveable { mutableStateOf<String>("") }
    var password by rememberSaveable { mutableStateOf<String>("") }

    Surface {
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LoginFields(
                email = email,
                password = password,
                onEmailChange = {new_email -> email = new_email},
                onPasswordChange = {new_password -> password = new_password},
                onLoginClick = onLoginClick
            )
        }
    }
}


@Composable
fun LoginFields(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        verticalArrangement = Arrangement.spacedBy(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Please login")

        OutlinedTextField(
            value = email,
            placeholder = { Text(text = "user@email.com") },
            label = { Text(text = "email") },
            onValueChange = onEmailChange,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )

        OutlinedTextField(
            value = password,
            placeholder = { Text(text = "password") },
            label = { Text(text = "password") },
            onValueChange = onPasswordChange,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )

        Button(onClick = {
            if (email.isBlank() == false && password.isBlank() == false) {
                onLoginClick(email)
                focusManager.clearFocus()
            } else {
                Toast.makeText(
                    context,
                    "Please enter an email and password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }) {
            Text("Login")
        }
    }
}