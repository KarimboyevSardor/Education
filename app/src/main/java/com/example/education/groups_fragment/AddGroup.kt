package com.example.education.groups_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.education.database.MyDb
import com.example.education.databinding.FragmentAddGroupGuruhBinding
import com.example.education.models.Course
import com.example.education.models.Group
import com.example.education.object_class.AllData.coursesList
import com.example.education.object_class.AllData.groupsList
import com.example.education.object_class.AllData.mentorsList
import com.example.education.viewmodel.MainViewModelFactory
import com.example.education.viewmodel.MyViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddGroup : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var binding: FragmentAddGroupGuruhBinding? = null
    private lateinit var vaqtList: MutableList<String>
    private lateinit var mentorList: MutableList<String>
    private lateinit var kunList: MutableList<String>
    lateinit var myDb: MyDb
    lateinit var viewModel: MyViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddGroupGuruhBinding.inflate(inflater, container, false)

        binding?.apply {
            myDb = MyDb(requireContext())
            viewModel = ViewModelProvider(requireActivity(), MainViewModelFactory())[MyViewModel::class.java]
            vaqtList = mutableListOf()
            mentorList = mutableListOf()
            kunList = mutableListOf()
            mentorsList = myDb.getMentors()
            viewModel.mentorLiveData?.value = mentorsList
            mentorsList = viewModel.mentorLiveData?.value!!
            setVaqt()
            var mentorName: Int = -1
            var dayName: Int = -1
            var timeName: Int = -1
            guruhTimeSp.adapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, vaqtList)
            guruhDaySp.adapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, kunList)
            guruhMentorSp.adapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, mentorList)
            guruhMentorSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    mentorName = position
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
            guruhTimeSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    timeName = position
                }

                override fun onNothingSelected(parent: AdapterView<*>?){}
            }
            guruhDaySp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    dayName = position
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            addGroupBtn.setOnClickListener {
                val name = arguments?.getParcelable<Course>("name")
                val fam = familiyaEt.text.toString()
                var b = false
                b = groupsList.filter { it.name == fam }.isNotEmpty()
                if (!b) {
                    if (fam.isNotEmpty() && dayName != -1 && timeName != -1 && mentorName != -1) {
                        val group = Group(
                            name = fam,
                            course_name = name?.name,
                            student_size = 0,
                            isOpen = 0,
                            time = vaqtList[timeName],
                            day = kunList[dayName],
                            mentor = mentorList[mentorName],
                        )
                        myDb.addGroups(group)
                        groupsList.add(group)
                        viewModel.groupLiveData?.value = groupsList
                        groupsList = viewModel.groupLiveData?.value!!
                        Toast.makeText(requireContext(), "Guruh qo'shildi!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Qo'shilmadi!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Bunday Guruh bor!", Toast.LENGTH_SHORT).show()
                }
            }

            orqagaBtn.setOnClickListener {
                findNavController().navigateUp()
            }
        }
        return binding?.root
    }

    private fun setVaqt() {
        val name = arguments?.getParcelable<Course>("name")
        //vaqt
        vaqtList.add("08:00 - 10:00")
        vaqtList.add("10:30 - 12:30")
        vaqtList.add("14:00 - 16:00")
        vaqtList.add("16:30 - 18:30")
        vaqtList.add("18:30 - 20:30")

        //kun
        kunList.add("Dushanba, Chorshanba, Juma")
        kunList.add("Seshanba, Payshanba, Shanba")

        //mentor
        val viewModel = ViewModelProvider(requireActivity(), MainViewModelFactory())[MyViewModel::class.java]
        val list = viewModel.mentorLiveData?.value!!.filter { it.coursesId == name?.id }
        for (i in 0 until list.size) {
            val s = list[i].name.split("%")
            mentorList.add(s[0] + " " + s[1] + " " + s[2])
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddGroup().apply {
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