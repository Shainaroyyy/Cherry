package com.example.cherry.app.attendance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSubjectScreen(onSave: () -> Unit, onBack: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("Theory") }
    var error by remember { mutableStateOf("") }
    val types = listOf("Theory", "Lab")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Subject", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(value = name, onValueChange = { name = it; error = "" }, label = { Text("Subject Name") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), isError = error.isNotEmpty())
            Text("Subject Type", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onBackground)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                types.forEach { t -> FilterChip(selected = type == t, onClick = { type = t }, label = { Text(t) }) }
            }
            if (error.isNotEmpty()) {
                Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    if (name.isEmpty()) {
                        error = "Subject name is required"
                    } else {
                        AttendanceRepository.addSubject(Subject(id = (AttendanceRepository.subjects.maxOfOrNull { it.id } ?: 0) + 1, name = name, type = type))
                        onSave()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Add Subject", fontWeight = FontWeight.Bold)
            }
        }
    }
}