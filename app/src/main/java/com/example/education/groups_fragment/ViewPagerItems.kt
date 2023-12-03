package com.example.education.groups_fragment

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.education.R
import com.example.education.adapters.GroupAdapter
import com.example.education.database.MyDb
import com.example.education.databinding.DeleteStudentDialogBinding
import com.example.education.databinding.EditGroupDialogBinding
import com.example.education.databinding.FragmentViewPagerItemsGuruhBinding
import com.example.education.models.Course
import com.example.education.models.Group
import com.example.education.object_class.AllData
import com.example.education.object_class.AllData.groupsList
import com.example.education.object_class.AllData.mentorsList
import com.example.education.viewmodel.MainViewModelFactory
import com.example.education.viewmodel.MyViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

class ViewPagerItems : Fragment() {
    private var param1: Group? = null
    private var param2: Int? = null
    private var param3: Course? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param2 = it.getInt(ARG_PARAM2)
            param3 = it.getParcelable(ARG_PARAM3)
        }
    }

    private var binding: FragmentViewPagerItemsGuruhBinding? = null
    lateinit var groupAdapter: GroupAdapter
    lateinit var myDb: MyDb
    private var TAG = "VIEWPAGER"
    lateinit var viewModel: MyViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentViewPagerItemsGuruhBinding.inflate(inflater, container, false)

        binding?.apply {
            viewModel = ViewModelProvider(requireActivity(), MainViewModelFactory())[MyViewModel::class.java]
            myDb = MyDb(requireContext())
            groupsList = myDb.getGroups()
            mentorsList = myDb.getMentors()
            viewModel.mentorLiveData?.value = mentorsList
            mentorsList = viewModel.mentorLiveData?.value!!
            viewModel.groupLiveData?.value = groupsList
            groupAdapter =
                GroupAdapter(viewModel.groupLiveData!!.value!!.filter { it.isOpen == param2 && it.course_name == param3?.name } as MutableList<Group>,
                    object : GroupAdapter.onItemClick {
                        override fun Click(group: Group) {
                            val fm = Groups()
                            val bundle = Bundle()
                            bundle.putParcelable("position", group)
                            fm.arguments = bundle
                            findNavController().navigate(
                                R.id.action_allGroups_guruh_to_group_guruh,
                                bundle
                            )
                        }
                        override fun deleteGroup(group: Group) {
                            deleteGroups(group)
                        }
                        override fun editGroup(group: Group) {
                            editGroups(group)
                        }
                    })
            groupsRec.adapter = groupAdapter
            viewModel.getGroupMutableLiveData()?.observe(requireActivity()) {
                it->groupAdapter.filterList(it.filter { it.isOpen == param2 && it.course_name == param3?.name } as MutableList<Group>)
            }
        }
        return binding?.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param2: Int, param3: Course) =
            ViewPagerItems().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM2, param2)
                    putParcelable(ARG_PARAM3, param3)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun deleteGroups(group: Group) {
        val dialog = Dialog(requireContext())
        val dialogBinding = DeleteStudentDialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialogBinding.apply {
            haBtn.setOnClickListener {
                myDb.deleteGroups(group)
                groupsList.remove(group)
                viewModel.groupLiveData?.value = groupsList
                groupsList = viewModel.groupLiveData?.value!!
                dialog.dismiss()
            }
            yoqBtn.setOnClickListener {
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    fun editGroups(group: Group) {
        val dialog = Dialog(requireContext())
        val dialogBinding = EditGroupDialogBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(dialogBinding.root)
        val vaqtList = mutableListOf<String>()
        vaqtList.add("08:00 - 10:00")
        vaqtList.add("10:30 - 12:30")
        vaqtList.add("14:00 - 16:00")
        vaqtList.add("16:30 - 18:30")
        vaqtList.add("18:30 - 20:30")
        val mentorList = mutableListOf<String>()
        val list = viewModel.mentorLiveData?.value!!.filter { it.coursesId == param3?.id }
        for (i in 0 until list.size) {
            val s = list[i].name.split("%")
            mentorList.add(s[0] + " " + s[1] + " " + s[2])
        }
        dialogBinding.apply {
            editGroupNameEt.setText(group.name)
            editGroupMentorSp.adapter = ArrayAdapter(requireContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, mentorList)
            editGroupTimeSp.adapter = ArrayAdapter(requireContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, vaqtList)
            val index = mentorList.indexOf(group.mentor)
            val index1 = vaqtList.indexOf(group.time)
            editGroupMentorSp.setSelection(index)
            editGroupTimeSp.setSelection(index1)
            yopishBtn.setOnClickListener {
                dialog.dismiss()
            }
            var mentorPos = -1
            var timePos = -1
            editGroupTimeSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long, ) {
                    timePos = position
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    timePos = index
                }
            }
            editGroupMentorSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long, ) {
                    mentorPos = position
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    mentorPos = index
                }
            }
            ozgartirishBtn.setOnClickListener {
                val name = editGroupNameEt.text.toString()
                if (name.isNotEmpty() && timePos != -1 && mentorPos != -1) {
                    group.name = name
                    group.mentor = mentorList[mentorPos]
                    group.time = vaqtList[timePos]
                    var in1 = groupsList.indexOfFirst { it.name == group.name }
                    groupsList[in1] = group
                    myDb.updateGroups(group)
                    viewModel.groupLiveData?.value = groupsList
                    groupsList = viewModel.groupLiveData?.value!!
                    Toast.makeText(requireContext(), "Guruh malumoti yangilandi", Toast.LENGTH_SHORT)
                        .show()
                    dialog.dismiss()
                } else {
                    Log.d(TAG, "$name ${mentorPos} + ${timePos}")
                    Toast.makeText(requireContext(), "Iltimos bo'sh maydon qoldirmang", Toast.LENGTH_SHORT).show()
                }
            }
            yopishBtn.setOnClickListener {
                dialog.dismiss()
            }
        }
        dialog.show()
    }
}