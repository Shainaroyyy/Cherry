package com.example.cherry.app.passwords

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LockScreen(onUnlocked: () -> Unit) {
    var pin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }
    var savedPin by remember { mutableStateOf("") }
    var isSettingUp by remember { mutableStateOf(true) }
    var isConfirming by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🔐", style = MaterialTheme.typography.displayMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = when { isSettingUp && !isConfirming -> "Set up your PIN"; isConfirming -> "Confirm your PIN"; else -> "Enter your PIN" },
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("Protect your password vault", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
        Spacer(modifier = Modifier.height(32.dp))
        OutlinedTextField(
            value = if (isConfirming) confirmPin else pin,
            onValueChange = { if (isConfirming) confirmPin = it else pin = it; error = "" },
            label = { Text("PIN") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            isError = error.isNotEmpty()
        )
        if (error.isNotEmpty()) {
            Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 4.dp))
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                when {
                    isSettingUp && !isConfirming -> { if (pin.length < 4) error = "PIN must be at least 4 digits" else { savedPin = pin; pin = ""; isConfirming = true } }
                    isConfirming -> { if (confirmPin == savedPin) onUnlocked() else { error = "PINs don't match, try again"; confirmPin = "" } }
                    else -> { if (pin == savedPin) onUnlocked() else { error = "Incorrect PIN"; pin = "" } }
                }
            },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(if (isSettingUp && !isConfirming) "Set PIN" else if (isConfirming) "Confirm" else "Unlock", fontWeight = FontWeight.Bold)
        }
    }
}