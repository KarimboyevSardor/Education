package com.example.education.groups_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.education.R
import com.example.education.adapters.CoursesAdapter
import com.example.education.database.MyDb
import com.example.education.databinding.FragmentCoursesGuruhBinding
import com.example.education.models.Course
import com.example.education.object_class.AllData.coursesList
import com.example.education.viewmodel.MainViewModelFactory
import com.example.education.viewmodel.MyViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Courses : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var binding: FragmentCoursesGuruhBinding? = null
    lateinit var coursesAdapter: CoursesAdapter
    lateinit var viewModel: MyViewModel
    lateinit var myDb: MyDb
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCoursesGuruhBinding.inflate(inflater, container, false)

        binding?.apply {
            viewModel = ViewModelProvider(requireActivity(), MainViewModelFactory())[MyViewModel::class.java]
            myDb = MyDb(requireContext())
            coursesList = myDb.getCourses()
            viewModel.courseLiveData?.value = coursesList
            orqagaBtn.setOnClickListener {
                findNavController().navigateUp()
            }
            coursesAdapter = CoursesAdapter(coursesList, object : CoursesAdapter.OnItemPressed{
                override fun OnClick(course: Course) {
                    val fragment = AllGroups()
                    val bundle = Bundle()
                    bundle.putParcelable("name", course)
                    fragment.arguments = bundle
                    findNavController().navigate(R.id.action_courses_guruh_to_allGroups_guruh, bundle)
                }
            })
            coursesRec.adapter = coursesAdapter
            viewModel.getCourseMutableLiveData()?.observe(requireActivity()) {
                coursesAdapter.filterList(it)
            }
        }

        return binding?.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Courses().apply {
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