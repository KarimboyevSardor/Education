package com.example.education.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.education.groups_fragment.ViewPagerItems
import com.example.education.models.Course
import com.example.education.object_class.AllData.groupsList

class ViewPagerAdapter(fm: Fragment, val course: Course) : FragmentStateAdapter(fm) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                ViewPagerItems.newInstance(1, course)
            }
            1 -> {
                ViewPagerItems.newInstance(0, course)
            }
            else -> {
                Fragment()
            }
        }
    }
}