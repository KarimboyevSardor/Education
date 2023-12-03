package com.example.education.groups_fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.education.database.MyDb
import com.example.education.databinding.FragmentEditStudentGuruhBinding
import com.example.education.models.Group
import com.example.education.models.Student
import com.example.education.object_class.AllData.studentsList
import com.example.education.viewmodel.MainViewModelFactory
import com.example.education.viewmodel.MyViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class EditStudent : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var binding: FragmentEditStudentGuruhBinding? = null
    lateinit var viewModel: MyViewModel
    lateinit var myDb: MyDb
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditStudentGuruhBinding.inflate(inflater, container, false)

        binding?.apply {
            myDb = MyDb(requireContext())
            viewModel = ViewModelProvider(requireActivity(), MainViewModelFactory())[MyViewModel::class.java]
            val pos = arguments?.getParcelable<Group>("pos")!!
            val nameGr = arguments?.getString("nameGr")!!
            var student = arguments?.getParcelable<Student>("student")
            val s = student?.name?.split("%")!!
            familiyaEt.setText(s[0])
            ismiEt.setText(s[1])
            ochestvaEt.setText(s[2])
            sanaEt.text = student.joinTime
            var date = ""
            orqagaBtn.setOnClickListener {
                findNavController().navigateUp()
            }
            calendarIb.setOnClickListener {
                val dialog = DatePickerDialog(requireContext(), { view, year, month, dayOfMonth ->
                    sanaEt.text = "$year/$month/$dayOfMonth" }, 2022, 4, 5)
                dialog.show()
                Log.d("TAG", s.toString())
            }
            saqlashBtn.setOnClickListener {
                val fam = familiyaEt.text.toString()
                val ism = ismiEt.text.toString()
                val os = ochestvaEt.text.toString()
                val sana = sanaEt.text.toString()

                if (fam.isNotEmpty() && ism.isNotEmpty() && os.isNotEmpty() && sana.isNotEmpty()) {
                    val name = "$fam%$ism%$os"
                    val student = Student(id = student!!.id, name = name, joinTime = sanaEt.text.toString(), groupId = student.groupId, coursesId = student.coursesId, mentor = student.mentor)
                    myDb.updateStudents(student)
                    studentsList = myDb.getStudents()
                    viewModel.studentLiveData?.value = studentsList
                    studentsList = viewModel.studentLiveData?.value!!
                    Toast.makeText(requireContext(), "Talaba malumoti yangilandi!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Malumotlar yangilanmadi!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding?.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditStudent().apply {
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