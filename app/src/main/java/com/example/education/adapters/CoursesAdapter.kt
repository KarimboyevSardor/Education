package com.example.education.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.education.databinding.CoursesItemBinding
import com.example.education.models.Course

class CoursesAdapter(var list: MutableList<Course>, val listener: OnItemPressed) : Adapter<CoursesAdapter.Vh>() {

    class Vh(val binding: CoursesItemBinding) : ViewHolder(binding.root)

    fun filterList(list: MutableList<Course>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(CoursesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.binding.courseNameTv.text = list[position].name
        holder.binding.cardview.setOnClickListener {
            listener.OnClick(list[position])
        }
    }

    interface OnItemPressed {
        fun OnClick(course: Course)
    }
}