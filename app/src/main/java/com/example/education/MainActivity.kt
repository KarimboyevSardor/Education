package com.example.education

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.education.database.MyDb
import com.example.education.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var myDb: MyDb
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        myDb = MyDb(this)
//        coursesList = myDb.getCourses()
//        groupsList = myDb.getGroups()
//        studentsList = myDb.getStudents()
//        mentorsList = myDb.getMentors()
    }
}