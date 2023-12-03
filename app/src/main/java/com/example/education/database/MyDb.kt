package com.example.education.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.education.models.Course
import com.example.education.models.Group
import com.example.education.models.Mentor
import com.example.education.models.Student

class MyDb(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION), EducationService {
    companion object {
        const val DB_NAME = "education"
        const val DB_VERSION = 1

        const val COURSES_TABLE = "courses"
        const val COURSES_ID = "id"
        const val COURSES_NAME = "name"
        const val COURSES_DESCRIPTION = "description"

        const val GROUP_TABLE = "groups"
        const val GROUP_ID = "id"
        const val GROUP_NAME = "name"
        const val GROUP_STUDENT_SIZE = "student_size"
        const val GROUP_COURSE_NAME = "course_name"
        const val GROUP_ISOPEN = "is_open"
        const val GROUP_OPENING_TIME = "opening_time"
        const val GROUP_DAY = "day"
        const val GROUP_MENTOR = "mentor"

        const val MENTORS_TABLE = "mentors"
        const val MENTORS_ID = "id"
        const val MENTORS_NAME = "name"
        const val MENTORS_COURSES_ID = "courses_id"
        const val MENTORS_STUDENTS_ID = "students_id"

        const val STUDENTS_TABLE = "students"
        const val STUDENTS_ID = "id"
        const val STUDENTS_NAME = "name"
        const val STUDENTS_GROUPS_ID = "groups_id"
        const val STUDENT_COURSES_ID = "courses_id"
        const val STUDENT_JOIN_TIME = "join_time"
        const val STUDENT_MENTOR_NAME = "mentor_name"
     }

    override fun onCreate(p0: SQLiteDatabase?) {
        val query1 = "create table $COURSES_TABLE($COURSES_ID integer not null primary key autoincrement, $COURSES_NAME text not null, $COURSES_DESCRIPTION text not null)"
        val query2 = "create table $GROUP_TABLE($GROUP_ID integer not null primary key autoincrement, $GROUP_NAME text not null, $GROUP_STUDENT_SIZE integer not null, $GROUP_COURSE_NAME text not null, $GROUP_ISOPEN integer not null, $GROUP_OPENING_TIME text not null, $GROUP_DAY text, $GROUP_MENTOR text not null)"
        val query3 = "create table $MENTORS_TABLE($MENTORS_ID integer not null primary key autoincrement, $MENTORS_NAME text not null, $MENTORS_STUDENTS_ID integer, $MENTORS_COURSES_ID integer)"
        val query4 = "create table $STUDENTS_TABLE($STUDENTS_ID integer not null primary key autoincrement, $STUDENTS_NAME text not null, $STUDENTS_GROUPS_ID integer not null, $STUDENT_COURSES_ID integer not null, $STUDENT_JOIN_TIME text not null, $STUDENT_MENTOR_NAME text)"
        p0?.execSQL(query4)
        p0?.execSQL(query3)
        p0?.execSQL(query2)
        p0?.execSQL(query1)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    override fun addStudents(students: Student) {
        val db = this.writableDatabase
        val value = ContentValues()
        value.put(STUDENTS_GROUPS_ID, students.groupId)
        value.put(STUDENT_COURSES_ID, students.coursesId)
        value.put(STUDENTS_NAME, students.name)
        value.put(STUDENT_JOIN_TIME, students.joinTime)
        value.put(STUDENT_MENTOR_NAME, students.mentor)
        db.insert(STUDENTS_TABLE, null, value)
    }

    override fun getStudents(): MutableList<Student> {
        val studentsMutableList = mutableListOf<Student>()
        val db = this.readableDatabase
        val s = db.rawQuery("SELECT * FROM $STUDENTS_TABLE", null)
        if (s != null) {
            if (s.moveToFirst()) {
                do {
                    val id = s.getInt(0)
                    val name = s.getString(1)
                    val group_id = s.getInt(2)
                    val course_id = s.getInt(3)
                    val joinTime = s.getString(4)
                    val m = s.getColumnIndex(STUDENT_MENTOR_NAME)
                    val mentor = s.getString(m)
                    val students = Student(
                        id = id,
                        name = name,
                        groupId = group_id,
                        coursesId = course_id,
                        joinTime = joinTime,
                        mentor = mentor
                    )
                    studentsMutableList.add(students)
                } while (s.moveToNext())
            }
        }
        return studentsMutableList
    }

    override fun updateStudents(students: Student) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(STUDENTS_NAME, students.name)
        contentValues.put(STUDENT_COURSES_ID, students.coursesId)
        contentValues.put(STUDENTS_GROUPS_ID, students.groupId)
        contentValues.put(STUDENT_JOIN_TIME, students.joinTime)
        db.update(STUDENTS_TABLE, contentValues, "$STUDENTS_ID = ?", arrayOf(students.id.toString())
        )
    }

    override fun deleteStudents(students: Student) {
        val db = this.writableDatabase
        db.delete(STUDENTS_TABLE, "$STUDENTS_ID = ?", arrayOf(students.id.toString()))
    }

    override fun addCosrses(courses: Course) {
        val db = this.writableDatabase
        val value = ContentValues()
        value.put(COURSES_NAME, courses.name)
        value.put(COURSES_DESCRIPTION, courses.description)
        db.insert(COURSES_TABLE, null, value)
    }

    override fun getCourses(): MutableList<Course> {
        val coursesMutableList = mutableListOf<Course>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $COURSES_TABLE", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val name = cursor.getString(1)
                val desc = cursor.getString(2)
                coursesMutableList.add(Course(id, name, desc))
            } while (cursor.moveToNext())
        }
        return coursesMutableList
    }

    override fun updateCourses(courses: Course) {
        val db = this.writableDatabase
        val content = ContentValues()
        content.put(COURSES_NAME, courses.name)
        content.put(COURSES_DESCRIPTION, courses.description)
        db.update(COURSES_TABLE, content, "$COURSES_TABLE = ?", arrayOf(courses.id.toString()))
    }

    override fun deleteCourses(courses: Course) {
        val db = this.writableDatabase
        db.delete(COURSES_TABLE, "$COURSES_ID = ?", arrayOf(courses.id.toString()))
    }

    override fun addMentors(mentors: Mentor) {
        val db = this.writableDatabase
        val content = ContentValues()
        content.put(MENTORS_NAME, mentors.name)
        content.put(MENTORS_COURSES_ID, mentors.coursesId)
        content.put(MENTORS_STUDENTS_ID, mentors.studentsId)
        db.insert(MENTORS_TABLE, null, content)
    }

    override fun getMentors(): MutableList<Mentor> {
        val mentorMutableList = mutableListOf<Mentor>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $MENTORS_TABLE", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val name = cursor.getString(1)
                val student_id = cursor.getInt(2)
                val course_id = cursor.getInt(3)
                mentorMutableList.add(Mentor(id, name, student_id, course_id))
            } while (cursor.moveToNext())
        }
        return mentorMutableList
    }

    override fun updateMentros(mentors: Mentor) {
        val db = this.writableDatabase
        val content = ContentValues()
        content.put(MENTORS_NAME, mentors.name)
        content.put(MENTORS_STUDENTS_ID, mentors.studentsId)
        content.put(MENTORS_COURSES_ID, mentors.coursesId)
        db.update(MENTORS_TABLE, content, "$MENTORS_ID = ?", arrayOf(mentors.id.toString()))
    }

    override fun deleteMentros(mentors: Mentor) {
        val db = this.readableDatabase
        db.delete(MENTORS_TABLE, "$MENTORS_ID = ?", arrayOf(mentors.id.toString()))
    }

    override fun addGroups(groups: Group) {
        val db = this.writableDatabase
        val content = ContentValues()
        content.put(GROUP_NAME, groups.name)
        content.put(GROUP_STUDENT_SIZE, groups.student_size)
        content.put(GROUP_COURSE_NAME, groups.course_name)
        content.put(GROUP_ISOPEN, groups.isOpen)
        content.put(GROUP_OPENING_TIME, groups.time)
        content.put(GROUP_DAY, groups.day)
        content.put(GROUP_MENTOR, groups.mentor)
        db.insert(GROUP_TABLE, null, content)
    }

    override fun getGroups(): MutableList<Group> {
        val groupsMutableList = mutableListOf<Group>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $GROUP_TABLE", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val name = cursor.getString(1)
                val son = cursor.getInt(2)
                val course_name = cursor.getString(3)
                val isOpen = cursor.getInt(4)
                val time = cursor.getString(5)
                val day = cursor.getString(6)
                val mentor = cursor.getString(7)
                groupsMutableList.add(Group(id, name, course_name, son, isOpen, time, day, mentor))
            } while (cursor.moveToNext())
        }
        return groupsMutableList
    }

    override fun updateGroups(groups: Group) {
        val db = this.writableDatabase
        val content = ContentValues()
        content.put(GROUP_COURSE_NAME, groups.course_name)
        content.put(GROUP_STUDENT_SIZE, groups.student_size)
        content.put(GROUP_NAME, groups.name)
        content.put(GROUP_ISOPEN, groups.isOpen)
        content.put(GROUP_OPENING_TIME, groups.time)
        content.put(GROUP_DAY, groups.day)
        content.put(GROUP_MENTOR, groups.mentor)
        db.update(GROUP_TABLE, content, "$GROUP_ID = ?", arrayOf(groups.id.toString()))
    }

    override fun deleteGroups(groups: Group) {
        val db = this.writableDatabase
        db.delete(STUDENTS_TABLE, "$STUDENTS_GROUPS_ID = ?", arrayOf(groups.id.toString()))
        db.delete(GROUP_TABLE, "$GROUP_ID = ?", arrayOf(groups.id.toString()))
    }

    override fun getGroupId(group: String) : Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $GROUP_ID FROM $GROUP_TABLE WHERE $GROUP_NAME = ?", arrayOf(group))
        var id = 0
        if (cursor.moveToFirst()) {
            do {
                val d = cursor.getColumnIndex(GROUP_ID)
                id = cursor.getInt(d)
            } while (cursor.moveToNext())
        }
        return id
    }

    override fun getCourseId(mentor: String) : Int{
        var id = 0
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $MENTORS_COURSES_ID FROM $MENTORS_TABLE WHERE $MENTORS_NAME = ?", arrayOf(mentor))
        if (cursor.moveToFirst()) {
            do {
                val d = cursor.getColumnIndex(MENTORS_COURSES_ID)
                id = cursor.getInt(d)
            } while (cursor.moveToNext())
        }
        return id
    }

    override fun getIsOpenGroup(isOpen: Int): MutableList<Group> {
        var list = mutableListOf<Group>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $GROUP_TABLE WHERE $GROUP_ISOPEN = ?", arrayOf(isOpen.toString()))
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val name = cursor.getString(1)
                val son = cursor.getInt(2)
                val course_name = cursor.getString(3)
                val isOpen = cursor.getInt(4)
                val time = cursor.getString(5)
                val day = cursor.getString(6)
                val mentor = cursor.getString(7)
                list.add(Group(id, name, course_name, son, isOpen, time, day, mentor))
            } while (cursor.moveToNext())
        }
        return list
    }

     override fun getMentorCourseId(course: String): Int {
        var id: Int
        var id1 = 0
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $COURSES_ID FROM $COURSES_TABLE WHERE $COURSES_NAME = ?", arrayOf(course))
        if (cursor.moveToFirst()) {
            do {
                val d = cursor.getColumnIndex(COURSES_ID)
                id1 = cursor.getInt(d)
            } while (cursor.moveToNext())
        }
        id = id1
        return id
    }

    override fun getGroupStudentsSize(name: String?) : Int{
        var size = 0
        val cursor = this.readableDatabase.rawQuery("SELECT $GROUP_STUDENT_SIZE FROM $GROUP_TABLE WHERE $GROUP_NAME = ?", arrayOf(name))
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getColumnIndex(GROUP_STUDENT_SIZE)
                size = cursor.getInt(id)
            } while (cursor.moveToNext())
        }
        Log.d("MYDB", "getGroupStudentsSize: $size")
        return size
    }

    override fun setGroupSize(size: Int, name: String?) {
        val content = ContentValues()
        content.put(GROUP_STUDENT_SIZE, size)
        val whereClause = "$GROUP_NAME = ?"
        this.writableDatabase.update(GROUP_TABLE, content, whereClause, arrayOf(name))
    }
}