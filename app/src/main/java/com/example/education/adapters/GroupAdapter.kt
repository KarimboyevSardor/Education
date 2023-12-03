package com.example.education.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.education.databinding.GroupsItemBinding
import com.example.education.models.Course
import com.example.education.models.Group

class GroupAdapter(var list: MutableList<Group>, val listener: onItemClick) : Adapter<GroupAdapter.Vh>() {

    class Vh(val binding: GroupsItemBinding) : ViewHolder(binding.root)

    fun filterList(list: MutableList<Group>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(GroupsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.binding.groupNameTv.text = list[position].name
        holder.binding.studentSizeTv.text = "O'quvchilar soni: ${list[position].student_size} ta"
        holder.binding.showGroupBtn.setOnClickListener {
            listener.Click(list[position])
        }
        holder.binding.editGroupBtn.setOnClickListener {
            listener.editGroup(list[position])
        }
        holder.binding.deleteGroupBtn.setOnClickListener {
            listener.deleteGroup(list[position])
        }
    }
    interface onItemClick {
        fun Click(group: Group)

        fun deleteGroup(group: Group)

        fun editGroup(group: Group)
    }
}