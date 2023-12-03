package com.example.education.mentor_fragment

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.education.R
import com.example.education.adapters.MentorsAdapter
import com.example.education.database.MyDb
import com.example.education.databinding.DeleteStudentDialogBinding
import com.example.education.databinding.EditMentorDialogBinding
import com.example.education.databinding.FragmentMentorsMentorBinding
import com.example.education.models.Mentor
import com.example.education.object_class.AllData.coursesList
import com.example.education.object_class.AllData.mentorsList
import com.example.education.viewmodel.MainViewModelFactory
import com.example.education.viewmodel.MyViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Mentors : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var binding: FragmentMentorsMentorBinding? = null
    lateinit var mentorsAdapter: MentorsAdapter
    lateinit var viewModel: MyViewModel
    lateinit var myDb: MyDb
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMentorsMentorBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding?.apply {
            myDb = MyDb(requireContext())
            courseRec.addItemDecoration(DividerItemDecoration(requireContext(), layoutManager.orientation))
            orqagaBtn.setOnClickListener {
                findNavController().navigateUp()
            }
            var courseId = coursesList.filter { it.name == arguments?.getString("name") }[0].id
            viewModel = ViewModelProvider(requireActivity(), MainViewModelFactory())[MyViewModel::class.java]
            mentorsList = myDb.getMentors()
            viewModel.mentorLiveData?.value = mentorsList
            mentorsAdapter = MentorsAdapter(viewModel.mentorLiveData!!.value!!.filter { it.coursesId ==  courseId} as MutableList<Mentor>, object :MentorsAdapter.Onclick{
                override fun deleteMentor(mentors: Mentor) {
                    deleteMentors(mentors)
                }
                override fun editMentor(mentors: Mentor) {
                    editMentors(mentors)
                }
            })
            textCourses.text = arguments?.getString("name") + " Development"
            addBtn.setOnClickListener {
                val fm = AddMentor()
                val bundle = Bundle()
                bundle.putString("name", arguments?.getString("name"))
                fm.arguments = bundle
                findNavController().navigate(R.id.action_all_mentors_mentor_to_addMentor_mentor, bundle)
            }
            courseRec.adapter = mentorsAdapter
            viewModel.getMentorMutableLiveData()?.observe(requireActivity()) { it1 ->
                mentorsAdapter.filterList(it1.filter { it.coursesId == courseId } as MutableList<Mentor>)
            }
        }

        return binding?.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Mentors().apply {
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

    private fun editMentors(mentor: Mentor) {
        val viewModel = ViewModelProvider(requireActivity(), MainViewModelFactory())[MyViewModel::class.java]
        val dialog = Dialog(requireContext())
        val dialogBinding: EditMentorDialogBinding = EditMentorDialogBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(dialogBinding.root)
        dialogBinding.apply {
            val s = mentor.name.split("%")
            editGroupOchestvaEt.setText(s[2])
            editMentorFamiliyaEt.setText(s[0])
            editMentorNameEt.setText(s[1])
            val ism = editMentorNameEt.text
            val familiya = editMentorFamiliyaEt.text
            val ochestva = editGroupOchestvaEt.text
            ozgartirishBtn.setOnClickListener {
                if (ism.isNotEmpty() && familiya.isNotEmpty() && ochestva.isNotEmpty()) {
                    val Fio = "$familiya%$ism%$ochestva"
                    mentor.name = Fio
                    val myDb = MyDb(requireContext())
                    myDb.updateMentros(mentor)
                    var index = -1
                    for (i in 0 until mentorsList.size) {
                        if (mentorsList[i].name == mentor.name) {
                            index = i
                            break
                        }
                    }
                    mentorsList[index] = mentor
                    viewModel.mentorLiveData?.value = mentorsList
                    mentorsList = viewModel.mentorLiveData?.value!!
                    Toast.makeText(requireContext(), "Mentor haqidagi malumotlar yangilandi.", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } else {
                    Toast.makeText(requireContext(), "Mentor haqidagi malumotlar yangilanmadi!", Toast.LENGTH_SHORT).show()
                }
            }
            yopishBtn.setOnClickListener {
                dialog.dismiss()
            }
        }
        dialog.show()
    }
    private fun deleteMentors(mentor: Mentor) {
        val dialog = Dialog(requireContext())
        val dialogBinding = DeleteStudentDialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialogBinding.apply {
            haBtn.setOnClickListener {
                val viewModel = ViewModelProvider(requireActivity(), MainViewModelFactory())[MyViewModel::class.java]
                val myDb = MyDb(requireContext())
                myDb.deleteMentros(mentor)
                mentorsList.remove(mentor)
                viewModel.mentorLiveData?.value = mentorsList
                mentorsList = viewModel.mentorLiveData?.value!!
                dialog.dismiss()
                Toast.makeText(requireContext(), "${mentor.name} haqidagi malumotlar o'chirildi", Toast.LENGTH_SHORT).show()
            }
            yoqBtn.setOnClickListener {
                dialog.dismiss()
            }
        }
        dialog.show()
    }
}