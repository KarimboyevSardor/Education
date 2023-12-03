package com.example.education.courses_fragment

import android.app.DatePickerDialog
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
import com.example.education.adapters.GroupSpinnerAdapter
import com.example.education.database.MyDb
import com.example.education.databinding.FragmentAddStudentKursBinding
import com.example.education.models.Group
import com.example.education.models.Student
import com.example.education.object_class.AllData.coursesList
import com.example.education.object_class.AllData.groupsList
import com.example.education.object_class.AllData.mentorsList
import com.example.education.object_class.AllData.studentsList
import com.example.education.viewmodel.MainViewModelFactory
import com.example.education.viewmodel.MyViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddStudent : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var binding: FragmentAddStudentKursBinding? = null
    private var mentorList = mutableListOf<String>()
    private var groupList = mutableListOf<String>()
    lateinit var groupSpinnerAdapter: GroupSpinnerAdapter
    lateinit var myDb: MyDb
    lateinit var viewModel: MyViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddStudentKursBinding.inflate(inflater, container, false)

        binding?.apply {
            var id = arguments?.getInt("name")!!
            viewModel = ViewModelProvider(requireActivity(), MainViewModelFactory())[MyViewModel::class.java]
            myDb = MyDb(requireContext())
            groupsList = myDb.getGroups()
            mentorsList = myDb.getMentors()
            viewModel.groupLiveData?.value = groupsList
            viewModel.mentorLiveData?.value = mentorsList
            orqagaBtn.setOnClickListener {
                findNavController().navigateUp()
            }
            mentorList = mutableListOf()
            val s1 = mentorsList.filter { it.coursesId == id }
            for (element in s1) {
                val s = element.name.split("%")
                if (s.isNotEmpty()) {
                    mentorList.add(s[0] + " " + s[1] + " " + s[2])
                }
            }
            var groupIndex = -1
            var name: String = coursesList.filter { it.id == id }[0].name!!
            val groupList = groupsList.filter { it.course_name == name } as MutableList<Group>
            groupSpinnerAdapter = GroupSpinnerAdapter(groupList, requireContext())
            guruhSp.adapter = groupSpinnerAdapter
            guruhSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    groupIndex = position
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            calendarIb.setOnClickListener {
                val dialog = DatePickerDialog(requireContext(), { view, year, month, dayOfMonth ->
                    sanaEt.text = "$year/$month/$dayOfMonth" }, 2022, 4, 5)
                dialog.show()
            }
            viewModel.getGroupMutableLiveData()?.observe(requireActivity()) {
                groupSpinnerAdapter.filterList(it.filter { it.course_name == name } as MutableList<Group>)
            }
            saqlashBtn.setOnClickListener {
                Log.d("TAG", "onCreateView: $id $name % ${groupsList.filter { it.course_name == name } as MutableList<Group>}")
                val fam = familiyaEt.text.toString()
                val ism = ismiEt.text.toString()
                val ochestva = ochestvaEt.text.toString()
                val date = sanaEt.text.toString()
                var guruh = -1
                if (groupIndex != -1) {
                    guruh = groupList[groupIndex].id
                }
                if (fam.isNotEmpty() && ism.isNotEmpty() && ochestva.isNotEmpty() && date.isNotEmpty() && guruh != -1 && sanaEt.text.toString() != "Sana") {
                    val name = "$fam%$ism%$ochestva"
                    studentsList.add(Student(name = name, groupId = guruh, coursesId = id, joinTime = date, mentor = groupsList.filter { it.id == guruh }[0].mentor))
                    viewModel.studentLiveData?.value = studentsList
                    studentsList = viewModel.studentLiveData?.value!!
                    myDb.addStudents(Student(name = name, groupId = guruh, coursesId = id, joinTime = date, mentor = groupsList.filter { it.id == guruh }[0].mentor))
                    myDb.setGroupSize(myDb.getGroupStudentsSize(groupList[groupIndex].name) + 1, groupList[groupIndex].name)
                    Toast.makeText(requireContext(), "Student haqidagi malumot qo'shildi", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Iltimos maydonlarni to'ldiring", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding?.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddStudent().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}