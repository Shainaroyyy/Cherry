package com.example.cherry.app.attendance

data class Subject(
    val id: Int,
    val name: String,
    val type: String,
    var totalClasses: Int = 0,
    var attendedClasses: Int = 0
) {
    val percentage: Float
        get() = if (totalClasses == 0) 0f else (attendedClasses.toFloat() / totalClasses) * 100
}

data class TimetableEntry(
    val id: Int,
    val day: String,
    val time: String,
    val subjectId: Int
)

data class AttendanceRecord(
    val id: Int,
    val date: String,
    val subjectId: Int,
    val isPresent: Boolean
)

object AttendanceRepository {
    var targetPercentage = 75f

    val subjects = mutableListOf(
        Subject(1, "Mathematics", "Theory", 20, 15),
        Subject(2, "Physics", "Theory", 18, 12),
        Subject(3, "Chemistry Lab", "Lab", 10, 8),
        Subject(4, "English", "Theory", 15, 10),
        Subject(5, "Computer Science", "Theory", 20, 18),
    )

    val timetable = mutableListOf(
        TimetableEntry(1, "Monday", "9:00 AM", 1),
        TimetableEntry(2, "Monday", "11:00 AM", 2),
        TimetableEntry(3, "Tuesday", "9:00 AM", 5),
        TimetableEntry(4, "Wednesday", "9:00 AM", 1),
        TimetableEntry(5, "Wednesday", "2:00 PM", 3),
        TimetableEntry(6, "Thursday", "11:00 AM", 4),
        TimetableEntry(7, "Friday", "9:00 AM", 5),
    )

    val records = mutableListOf<AttendanceRecord>()

    fun getSubject(id: Int) = subjects.find { it.id == id }
    fun addSubject(subject: Subject) = subjects.add(subject)
    fun removeSubject(id: Int) = subjects.removeAll { it.id == id }
    fun getTodayTimetable(day: String) = timetable.filter { it.day == day }

    fun markAttendance(subjectId: Int, isPresent: Boolean, date: String) {
        val subject = getSubject(subjectId) ?: return
        subject.totalClasses++
        if (isPresent) subject.attendedClasses++
        records.add(AttendanceRecord(records.size + 1, date, subjectId, isPresent))
    }

    fun classesNeededToReachTarget(subject: Subject): Int {
        if (subject.percentage >= targetPercentage) return 0
        var needed = 0
        var total = subject.totalClasses
        var attended = subject.attendedClasses
        while ((attended.toFloat() / (total + needed)) * 100 < targetPercentage) {
            needed++
            attended++
        }
        return needed
    }

    fun classesCanSkip(subject: Subject): Int {
        if (subject.percentage < targetPercentage) return 0
        var canSkip = 0
        val total = subject.totalClasses
        val attended = subject.attendedClasses
        while (true) {
            val newPercentage = (attended.toFloat() / (total + canSkip + 1)) * 100
            if (newPercentage < targetPercentage) break
            canSkip++
            if (canSkip > 20) break
        }
        return canSkip
    }

    fun overallPercentage(): Float {
        if (subjects.isEmpty()) return 0f
        return subjects.map { it.percentage }.average().toFloat()
    }
}