package com.example.education.models

import android.os.Parcel
import android.os.Parcelable

data class Group(
    val id: Int = 0,
    var name: String?,
    val course_name: String?,
    val student_size: Int,
    var isOpen: Int,
    var time: String?,
    val day: String?,
    var mentor:String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(course_name)
        parcel.writeInt(student_size)
        parcel.writeInt(isOpen)
        parcel.writeString(time)
        parcel.writeString(day)
        parcel.writeString(mentor)
    }

    override fun describeContents(): Int {
        TODO("not implementation")
    }

    override fun toString(): String {
        return "Groups(id=$id, name=$name, course_name=$course_name, student_size=$student_size, isOpen=$isOpen, time=$time, day=$day)"
    }

    companion object CREATOR : Parcelable.Creator<Group> {
        override fun createFromParcel(parcel: Parcel): Group {
            return Group(parcel)
        }

        override fun newArray(size: Int): Array<Group?> {
            return arrayOfNulls(size)
        }
    }
}