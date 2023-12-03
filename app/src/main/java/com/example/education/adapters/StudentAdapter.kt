package com.example.education.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.education.databinding.StudentsItemBinding
import com.example.education.models.Student

class StudentAdapter(var list: MutableList<Student>, val listener: StudentOptions) :
    RecyclerView.Adapter<StudentAdapter.MyVh>() {

    fun filterList(list: MutableList<Student>) {
        this.list = list
        notifyDataSetChanged()
    }

    class MyVh(val binding: StudentsItemBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVh {
        return MyVh(StudentsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyVh, position: Int) {
        val s = list[position].name?.split("%")!!
        holder.binding.mentorNameTv.text = "${s[0]} ${s[1]}"
        holder.binding.mentorOchestvaTv.text = "${s[2]}"
        holder.binding.sanaTv.text = list[position].joinTime
        holder.binding.deleteMentorBtn.setOnClickListener {
            listener.deleteStudent(list[position])
        }
        holder.binding.editMentorBtn.setOnClickListener {
            listener.editStudent(list[position])
        }
    }
    interface StudentOptions{
        fun deleteStudent(student: Student)
        fun editStudent(student: Student)
    }
}