package com.example.education.database

import com.example.education.models.Course
import com.example.education.models.Group
import com.example.education.models.Mentor
import com.example.education.models.Student

interface EducationService {
    //todo : Student
    fun addStudents(students: Student)

    fun getStudents() : MutableList<Student>

    fun updateStudents(students: Student)

    fun deleteStudents(students: Student)

    //todo : Courses
    fun addCosrses(courses: Course)

    fun getCourses() : MutableList<Course>

    fun updateCourses(courses: Course)

    fun deleteCourses(courses: Course)

    //todo : Mentors
    fun addMentors(mentors: Mentor)

    fun getMentors() : MutableList<Mentor>

    fun updateMentros(mentors: Mentor)

    fun deleteMentros(mentors: Mentor)

    //todo : Groups
    fun addGroups(groups: Group)

    fun getGroups() : MutableList<Group>

    fun updateGroups(groups: Group)

    fun deleteGroups(groups: Group)

    fun getGroupId(group: String): Int

    fun getCourseId(course: String): Int

    fun getIsOpenGroup(isOpen: Int) : MutableList<Group>

    fun getMentorCourseId(course: String) : Int

    fun getGroupStudentsSize(name: String?) : Int

    fun setGroupSize(size: Int, name: String?)
}