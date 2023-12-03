package com.example.education.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.education.R
import com.example.education.databinding.GroupsSpinnerBinding
import com.example.education.models.Group
import com.example.education.models.Student

class GroupSpinnerAdapter(var list: MutableList<Group>, context: Context) : ArrayAdapter<Group>(context, R.layout.groups_spinner, list) {

    fun filterList(list: MutableList<Group>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var binding: GroupsSpinnerBinding
        if (convertView == null) {
            binding = GroupsSpinnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else {
            binding = GroupsSpinnerBinding.bind(convertView)
        }
        binding.timeTv.text = list[position].time
        binding.soniTv.text = list[position].student_size.toString()
        binding.dayTv.text = list[position].day
        binding.nameGrTv.text = list[position].name

        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var binding: GroupsSpinnerBinding
        if (convertView == null) {
            binding = GroupsSpinnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else {
            binding = GroupsSpinnerBinding.bind(convertView)
        }
        binding.timeTv.text = list[position].time
        binding.soniTv.text = list[position].student_size.toString()
        binding.dayTv.text = list[position].day
        binding.nameGrTv.text = list[position].name

        return binding.root
    }
}