package com.example.education.object_class

import com.example.education.models.Course
import com.example.education.models.Group
import com.example.education.models.Mentor
import com.example.education.models.Student

object AllData {
    var coursesList: MutableList<Course> = mutableListOf()
    var groupsList: MutableList<Group> = mutableListOf()
    var mentorsList: MutableList<Mentor> = mutableListOf()
    var studentsList: MutableList<Student> = mutableListOf()
}