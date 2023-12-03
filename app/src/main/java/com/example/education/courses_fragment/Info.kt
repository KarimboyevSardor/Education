package com.example.education.courses_fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.education.R
import com.example.education.database.MyDb
import com.example.education.databinding.FragmentInfoKursBinding
import com.example.education.models.Course

private const val ARG_PARAM1 = "name"

class Info : Fragment() {
    private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString("name")
        }
    }

    private var binding: FragmentInfoKursBinding? = null
    lateinit var list: MutableList<Course>
    lateinit var myDb: MyDb
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentInfoKursBinding.inflate(inflater, container, false)

        binding?.apply {
            list = mutableListOf()
            myDb = MyDb(requireContext())
            list = myDb.getCourses()
            var name1 = arguments?.getInt("name", -1)!!.toInt()
            if (name1 == 0 || list.filter { it.id == name1 }.isEmpty()) {
                findNavController().navigateUp()
            } else {
                nameTv.text = "${list.filter { it.id == name1 }[0].name} Developer"
                bioTv.text = list.filter { it.id == name1 }[0].description!!
            }
            orqagaBtn.setOnClickListener {
                findNavController().navigateUp()
            }
            addStudentBtn.setOnClickListener {
                val fm = AddStudent()
                val bundle = Bundle()
                bundle.putInt("name", name1)
                fm.arguments = bundle
                findNavController().navigate(R.id.action_info_kurs_to_add_student_kurs, bundle)
            }
        }

        return binding?.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            Info().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}