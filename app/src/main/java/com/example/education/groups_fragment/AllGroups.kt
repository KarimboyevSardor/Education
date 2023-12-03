package com.example.education.groups_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.education.R
import com.example.education.adapters.ViewPagerAdapter
import com.example.education.database.MyDb
import com.example.education.databinding.FragmentAllGroupsGuruhBinding
import com.example.education.models.Course
import com.example.education.models.Group
import com.example.education.object_class.AllData.groupsList
import com.example.education.viewmodel.MainViewModelFactory
import com.example.education.viewmodel.MyViewModel
import com.google.android.material.tabs.TabLayout

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AllGroups : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    private var binding: FragmentAllGroupsGuruhBinding? = null
    lateinit var viewPagerAdapter: ViewPagerAdapter
    lateinit var myDb: MyDb
    lateinit var viewModel: MyViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAllGroupsGuruhBinding.inflate(inflater, container, false)

        binding?.apply {
            orqagaBtn.setOnClickListener {
                findNavController().navigateUp()
            }
            myDb = MyDb(requireContext())
            groupsList = myDb.getGroups()
            viewModel = ViewModelProvider(requireActivity(), MainViewModelFactory())[MyViewModel::class.java]
            viewModel.groupLiveData?.value = groupsList
            nameKursTv.text = arguments?.getParcelable<Course>("name")?.name +  " Development"
            viewPagerAdapter = ViewPagerAdapter(this@AllGroups, arguments?.getParcelable("name")!!)
            groupVp.adapter = viewPagerAdapter
            tabLayout.addTab(tabLayout.newTab().setText("Ochilgan guruhlar"))
            tabLayout.addTab(tabLayout.newTab().setText("Ochilayotgan guruhlar"))
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) {
                        if (tab.position == 0) {
                            addBtn.visibility = View.INVISIBLE
                        } else if (tab.position == 1) {
                            addBtn.visibility = View.VISIBLE
                        }
                        groupVp.currentItem = tab.position
                    }
                }
                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })

            addBtn.setOnClickListener {
                val fm = AddGroup()
                val bundle = Bundle()
                bundle.putParcelable("name", arguments?.getParcelable<Course>("name"))
                fm.arguments = bundle
                findNavController().navigate(R.id.action_allGroups_guruh_to_addGroup_guruh, bundle)
            }

            groupVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    tabLayout.selectTab(tabLayout.getTabAt(position))
                }
            })
        }

        return binding?.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AllGroups().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}