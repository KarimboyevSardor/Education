package com.example.education.groups_fragment

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.education.R
import com.example.education.adapters.StudentAdapter
import com.example.education.database.MyDb
import com.example.education.databinding.DeleteStudentDialogBinding
import com.example.education.databinding.FragmentGroupGuruhBinding
import com.example.education.models.Group
import com.example.education.models.Student
import com.example.education.object_class.AllData.groupsList
import com.example.education.object_class.AllData.studentsList
import com.example.education.viewmodel.MainViewModelFactory
import com.example.education.viewmodel.MyViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Groups : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var binding: FragmentGroupGuruhBinding? = null
    lateinit var viewModel: MyViewModel
    lateinit var studentAdapter: StudentAdapter
    lateinit var myDb: MyDb
    private var TAG = "Groups"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGroupGuruhBinding.inflate(inflater, container, false)

        val groups = arguments?.getParcelable<Group>("position")!!
        binding?.apply {
            myDb = MyDb(requireContext())
            viewModel = ViewModelProvider(requireActivity(), MainViewModelFactory())[MyViewModel::class.java]
            studentsList = myDb.getStudents()
            viewModel.studentLiveData?.value = studentsList
            orqagaBtn.setOnClickListener {
                findNavController().navigateUp()
            }
            studentAdapter = StudentAdapter(viewModel.studentLiveData?.value!!.filter { it.id == groups.id } as MutableList<Student>, object: StudentAdapter.StudentOptions{
                override fun deleteStudent(student: Student) {
                    Log.d(TAG, "$groups%$student")
                    deleteStudentDialog(student)
                }
                override fun editStudent(student: Student) {
                    val fm = EditStudent()
                    val bundle = Bundle()
                    bundle.putParcelable("pos", groups)
                    bundle.putParcelable("student", student)
                    bundle.putString("nameGr", groupNameTv.text.toString())
                    fm.arguments = bundle
                    findNavController().navigate(R.id.action_group_guruh_to_editStudent_guruh, bundle)
                }
            })
            viewModel.getStudentMutableLiveData()?.observe(requireActivity()) {
                studentAdapter.filterList(it.filter { it.groupId == groups.id } as MutableList<Student>)
            }
            studentsRec.adapter = studentAdapter
            groupNameTv.text = "Guruh nomi: ${groups.name}"
            groupTimeTv.text = "Dars vaqti: ${groups.time}"
            groupStudentSizeTv.text = "O'quvchilar soni: ${groups.student_size.toString()}"
            nameGr.text = groups.name
            if (groups.isOpen == 1) {
                darsniBoshlashBtn.text = "Dars boshlangan!!"
            } else {
                darsniBoshlashBtn.text = "Darsni boshlash"
            }
            darsniBoshlashBtn.setOnClickListener {
                groups.isOpen = 1
                myDb.updateGroups(groups)
                groupsList = myDb.getGroups()
                viewModel.groupLiveData?.value = groupsList
                groupsList = viewModel.groupLiveData?.value!!
                darsniBoshlashBtn.text = "Dars boshlangan!!"
            }
            addBtn.setOnClickListener {
                val fm = AddStudent()
                val bundle = Bundle()
                bundle.putString("grName", groupNameTv.text.toString())
                bundle.putParcelable("gr", groups)
                fm.arguments = bundle
                findNavController().navigate(R.id.action_group_guruh_to_addStudent_guruh, bundle)
            }
        }

        return binding?.root
    }

    private fun deleteStudentDialog(student: Student) {
        val dialog = Dialog(requireContext())
        val dialogBinding = DeleteStudentDialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialogBinding.apply {
            haBtn.setOnClickListener {
                studentsList.remove(student)
                viewModel.studentLiveData?.value = studentsList
                studentsList = viewModel.studentLiveData?.value!!
                myDb.deleteStudents(student)
                var grName = groupsList.filter { it.id == student.groupId }[0].name
                myDb.setGroupSize(myDb.getGroupStudentsSize(grName) - 1, grName)
                groupsList = myDb.getGroups()
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

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Groups().apply {
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