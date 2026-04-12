package com.example.cherry.app.attendance

import androidx.compose.runtime.*

@Composable
fun AttendanceScreen() {
    var currentScreen by remember { mutableStateOf("dashboard") }

    when (currentScreen) {
        "dashboard" -> AttendanceDashboard(
            onMarkAttendance = { currentScreen = "mark" },
            onAddSubject = { currentScreen = "addSubject" }
        )
        "mark" -> MarkAttendanceScreen(onBack = { currentScreen = "dashboard" })
        "addSubject" -> AddSubjectScreen(onSave = { currentScreen = "dashboard" }, onBack = { currentScreen = "dashboard" })
    }
}