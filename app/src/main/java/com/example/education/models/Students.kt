package com.example.education.models

import android.os.Parcel
import android.os.Parcelable

data class Student(
    val id: Int = 0,
    val name: String?,
    val groupId: Int,
    val coursesId: Int,
    val joinTime: String?,
    val mentor: String? = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun toString(): String {
        return "Students(id=$id, name='$name', groupId=$groupId, coursesId=$coursesId, joinTime='$joinTime, mentor=$mentor')"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeInt(groupId)
        parcel.writeInt(coursesId)
        parcel.writeString(joinTime)
        parcel.writeString(mentor)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Student> {
        override fun createFromParcel(parcel: Parcel): Student {
            return Student(parcel)
        }

        override fun newArray(size: Int): Array<Student?> {
            return arrayOfNulls(size)
        }
    }
}
