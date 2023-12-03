package com.example.education.courses_fragment

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.education.R
import com.example.education.adapters.CoursesAdapter
import com.example.education.database.MyDb
import com.example.education.databinding.AddCourseDialogBinding
import com.example.education.databinding.FragmentCoursesKursBinding
import com.example.education.models.Course
import com.example.education.object_class.AllData.coursesList
import com.example.education.viewmodel.MainViewModelFactory
import com.example.education.viewmodel.MyViewModel

private const val ARG_PARAM1 = "name"

class Courses : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    private var binding: FragmentCoursesKursBinding? = null
    lateinit var list: MutableList<Course>
    lateinit var coursesAdapter: CoursesAdapter
    lateinit var myDb: MyDb
    lateinit var myViewModel: MyViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCoursesKursBinding.inflate(inflater, container, false)

        binding?.apply {
            orqagaBtn.setOnClickListener {
                findNavController().navigateUp()
            }
            myDb = MyDb(requireContext())
            myViewModel = ViewModelProvider(requireActivity(), MainViewModelFactory())[MyViewModel::class.java]
            coursesList = ArrayList(myDb.getCourses())
            myViewModel.courseLiveData?.value = coursesList
            coursesAdapter = CoursesAdapter(coursesList, object: CoursesAdapter.OnItemPressed{
                override fun OnClick(course: Course) {
                    val fragment = Info()
                    val bundle = Bundle()
                    bundle.putInt("name", course.id)
                    fragment.arguments = bundle
                    findNavController().navigate(R.id.action_courses_kurs_to_info_kurs, bundle)
                }
            })
            courseRec.adapter = coursesAdapter
            addBtn.setOnClickListener {
                showAddCourseDialog()
                myViewModel.getCourseMutableLiveData()?.observe(requireActivity()) {
                    coursesAdapter.filterList(it)
                }
            }
        }

        return binding?.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            Courses().apply {
                arguments = Bundle().apply {
                    putString("name", param1)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun showAddCourseDialog() {
        myDb = MyDb(requireContext())
        val dialog = Dialog(requireContext())
        val dialogBinding = AddCourseDialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.setCancelable(false)
        dialogBinding.apply {
            yopishBtn.setOnClickListener {
                dialog.dismiss()
            }
            qoshishBtn.setOnClickListener {
                val name = kursNameEt.text.toString()
                val bio = kursHaqidaEt.text.toString()
                if (name.isNotEmpty() && bio.isNotEmpty()) {
                    var b: Boolean = coursesList.any { it.name == name }
                    if (!b) {
                        myDb.addCosrses(Course(name = name, description = name))
                        coursesList.add(Course(name = name, description = bio))
                        myViewModel.courseLiveData?.value = coursesList
                        coursesList = myViewModel.courseLiveData!!.value!!
                        Toast.makeText(requireContext(), "Kurs malumotlari qo'shildi", Toast.LENGTH_SHORT)
                            .show()
                        dialog.dismiss()
                    } else {
                        Toast.makeText(requireContext(), "Bunday kurs bor iltimos boshqa kurs nomini kiriting!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
        dialog.show()
    }
}