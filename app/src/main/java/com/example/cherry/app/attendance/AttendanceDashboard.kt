package com.example.cherry.app.attendance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AttendanceDashboard(
    onMarkAttendance: () -> Unit,
    onAddSubject: () -> Unit
) {
    var filter by remember { mutableStateOf("All") }
    var subjects by remember { mutableStateOf(AttendanceRepository.subjects.toList()) }
    val filters = listOf("All", "Theory", "Lab")

    val filteredSubjects = when (filter) {
        "Theory" -> subjects.filter { it.type == "Theory" }
        "Lab" -> subjects.filter { it.type == "Lab" }
        else -> subjects
    }

    Scaffold(
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                FloatingActionButton(
                    onClick = onAddSubject,
                    containerColor = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Subject", tint = MaterialTheme.colorScheme.onPrimary)
                }
                FloatingActionButton(
                    onClick = onMarkAttendance,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Filled.DateRange, contentDescription = "Mark Attendance", tint = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Attendance Tracker",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            item { OverallAttendanceCard() }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    filters.forEach { f ->
                        FilterChip(selected = filter == f, onClick = { filter = f }, label = { Text(f) })
                    }
                }
            }
            items(filteredSubjects) { subject ->
                SubjectCard(subject = subject)
            }
        }
    }
}

@Composable
fun OverallAttendanceCard() {
    val overall = AttendanceRepository.overallPercentage()
    val color = when {
        overall >= 75f -> MaterialTheme.colorScheme.primary
        overall >= 60f -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.error
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Overall Attendance", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(8.dp))
            Text("%.1f%%".format(overall), style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold, color = color)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { overall / 100f },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color = color,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (overall >= 75f) "✅ You're on track!" else "⚠️ Attendance is low!",
                style = MaterialTheme.typography.bodySmall,
                color = color
            )
        }
    }
}

@Composable
fun SubjectCard(subject: Subject) {
    val needed = AttendanceRepository.classesNeededToReachTarget(subject)
    val canSkip = AttendanceRepository.classesCanSkip(subject)
    val color = when {
        subject.percentage >= 75f -> MaterialTheme.colorScheme.primary
        subject.percentage >= 60f -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.error
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(subject.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Text(subject.type, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text("%.1f%%".format(subject.percentage), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = color)
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { subject.percentage / 100f },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                color = color,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text("${subject.attendedClasses}/${subject.totalClasses} classes attended", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            if (needed > 0) {
                Text("⚠️ Attend $needed more classes to reach ${AttendanceRepository.targetPercentage.toInt()}%", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
            } else if (canSkip > 0) {
                Text("✅ You can safely skip $canSkip classes", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}