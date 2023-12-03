package com.example.education.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.education.models.Course
import com.example.education.models.Group
import com.example.education.models.Mentor
import com.example.education.models.Student
import com.example.education.object_class.AllData.coursesList
import com.example.education.object_class.AllData.groupsList
import com.example.education.object_class.AllData.mentorsList
import com.example.education.object_class.AllData.studentsList

public class MyViewModel : ViewModel() {
    var groupLiveData: MutableLiveData<MutableList<Group>>? = null
    var courseLiveData: MutableLiveData<MutableList<Course>>? = null
    var studentLiveData: MutableLiveData<MutableList<Student>>? = null
    var mentorLiveData: MutableLiveData<MutableList<Mentor>>? = null


    init {
        groupLiveData = MutableLiveData()
        groupLiveData!!.value = groupsList
        courseLiveData = MutableLiveData()
        courseLiveData!!.value = coursesList
        studentLiveData = MutableLiveData()
        studentLiveData!!.value = studentsList
        mentorLiveData = MutableLiveData()
        mentorLiveData!!.value = mentorsList
    }

    fun getGroupMutableLiveData() : MutableLiveData<MutableList<Group>>? {
        return groupLiveData
    }

    fun getMentorMutableLiveData() : MutableLiveData<MutableList<Mentor>>? {
        return mentorLiveData
    }

    fun getStudentMutableLiveData() : MutableLiveData<MutableList<Student>>? {
        return studentLiveData
    }

    fun getCourseMutableLiveData() : MutableLiveData<MutableList<Course>>? {
        return courseLiveData
    }
}