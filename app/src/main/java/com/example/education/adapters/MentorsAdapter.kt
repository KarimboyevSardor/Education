package com.example.education.adapters

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.education.database.MyDb
import com.example.education.databinding.EditMentorDialogBinding
import com.example.education.databinding.MentorItemBinding
import com.example.education.mentor_fragment.Mentors
import com.example.education.models.Course
import com.example.education.models.Mentor
import com.example.education.viewmodel.MainViewModelFactory
import com.example.education.viewmodel.MyViewModel

class MentorsAdapter(var list: MutableList<Mentor>, val listener: Onclick) : Adapter<MentorsAdapter.MyVh>() {

    class MyVh(val binding: MentorItemBinding) : ViewHolder(binding.root)

    fun filterList(list: MutableList<Mentor>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVh {
        return MyVh(MentorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyVh, position: Int) {
        val s = list[position].name.split("%")
        holder.binding.mentorNameTv.text = s[0] + " " + s[1]
        holder.binding.mentorOchestvaTv.text = s[2]
        holder.binding.deleteMentorBtn.setOnClickListener {
            listener.deleteMentor(list[position])
        }
        holder.binding.editMentorBtn.setOnClickListener {
            listener.editMentor(list[position])
        }
    }

    interface Onclick{
        fun deleteMentor(mentors: Mentor)

        fun editMentor(mentors: Mentor)
    }

}