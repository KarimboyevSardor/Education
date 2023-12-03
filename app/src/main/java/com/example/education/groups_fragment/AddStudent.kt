package com.example.education.groups_fragment

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.education.database.MyDb
import com.example.education.databinding.FragmentAddStudentGuruhBinding
import com.example.education.models.Group
import com.example.education.models.Student
import com.example.education.object_class.AllData.coursesList
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

    private var binding: FragmentAddStudentGuruhBinding? = null
    lateinit var myDb: MyDb
    lateinit var viewModel: MyViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddStudentGuruhBinding.inflate(inflater, container, false)

        binding?.apply {
            myDb = MyDb(requireContext())
            viewModel = ViewModelProvider(requireActivity(), MainViewModelFactory())[MyViewModel::class.java]
            val nameGr = arguments?.getString("grName")!!
            val groups = arguments?.getParcelable<Group>("gr")
            orqagaBtn.setOnClickListener {
                findNavController().navigateUp()
            }
            var sana = ""
            calendarIb.setOnClickListener {
                val dialog = DatePickerDialog(requireContext(), { view, year, month, dayOfMonth ->
                    sanaEt.text = "$year/$month/$dayOfMonth" }, 2022, 4, 5)
                dialog.show()
            }
            saqlashBtn.setOnClickListener {
                val fam = familiyaEt.text.toString()
                val ism = ismiEt.text.toString()
                val och = ochestvaEt.text.toString()
                val fio = "$fam%$ism%$och"
                var name = ""
                var courseId = -1
                var course_name = groups!!.course_name
                courseId = coursesList.filter { it.name == course_name }[0].id
                if (fam.isNotEmpty() && ism.isNotEmpty() && och.isNotEmpty() && courseId != -1 && sanaEt.text.toString() != "Sana") {
                    val student =
                        Student(name = fio, groupId = groups.id, joinTime = sanaEt.text.toString(), coursesId = courseId, mentor = groups.mentor)
                    myDb.addStudents(student)
                    studentsList.add(student)
                    viewModel.studentLiveData?.value = studentsList
                    studentsList = viewModel.studentLiveData?.value!!
                    myDb.setGroupSize(myDb.getGroupStudentsSize(groups.name) + 1, groups.name)
                    Toast.makeText(requireContext(), "Talaba qo'shildi!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(),
                        "Maydonlarni to'ldiring!!", Toast.LENGTH_SHORT).show()
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