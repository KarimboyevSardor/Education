package com.example.education.models

data class Mentor(
    val id: Int = 0,
    var name: String,
    val studentsId: Int = 0,
    val coursesId: Int = 0
) {
    override fun toString(): String {
        return "Mentors(id=$id, name='$name', studentsId=$studentsId, coursesId=$coursesId)"
    }
}
