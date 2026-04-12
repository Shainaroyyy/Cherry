package com.example.cherry.app.passwords

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAccountScreen(onSave: () -> Unit, onBack: () -> Unit, existingAccount: Account? = null) {
    var platform by remember { mutableStateOf(existingAccount?.platform ?: "") }
    var username by remember { mutableStateOf(existingAccount?.username ?: "") }
    var password by remember { mutableStateOf(existingAccount?.password ?: "") }
    var notes by remember { mutableStateOf(existingAccount?.notes ?: "") }
    var showPassword by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (existingAccount == null) "Add Account" else "Edit Account", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(value = platform, onValueChange = { platform = it; error = "" }, label = { Text("Platform Name") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), isError = error.isNotEmpty() && platform.isEmpty())
            OutlinedTextField(value = username, onValueChange = { username = it; error = "" }, label = { Text("Username / Email") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), isError = error.isNotEmpty() && username.isEmpty())
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; error = "" },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, contentDescription = if (showPassword) "Hide" else "Show")
                    }
                },
                isError = error.isNotEmpty() && password.isEmpty()
            )
            OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Notes (optional)") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), minLines = 3)
            if (error.isNotEmpty()) { Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    when {
                        platform.isEmpty() -> error = "Platform name is required"
                        username.isEmpty() -> error = "Username is required"
                        password.isEmpty() -> error = "Password is required"
                        else -> {
                            if (existingAccount == null) {
                                AccountRepository.addAccount(Account(id = (AccountRepository.accounts.maxOfOrNull { it.id } ?: 0) + 1, platform = platform, username = username, password = password, notes = notes))
                            } else {
                                AccountRepository.updateAccount(existingAccount.copy(platform = platform, username = username, password = password, notes = notes))
                            }
                            onSave()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(if (existingAccount == null) "Save Account" else "Update Account", fontWeight = FontWeight.Bold)
            }
        }
    }
}