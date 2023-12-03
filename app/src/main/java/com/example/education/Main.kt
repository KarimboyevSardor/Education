package com.example.education

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.example.education.databinding.FragmentMainBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Main : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var bindin: FragmentMainBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        bindin = FragmentMainBinding.inflate(inflater, container, false)

        bindin?.apply {
            mentorsCv.setOnClickListener {
                findNavController().navigate(R.id.action_main_to_courses_mentor)
            }
            guruhlarCv.setOnClickListener {
                findNavController().navigate(R.id.action_main_to_courses_guruh)
            }
            kurslarCv.setOnClickListener {
                findNavController().navigate(R.id.action_main_to_courses_kurs)
            }
        }

        return bindin?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bindin = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Main().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if (isEnabled) {
                    requireActivity().finish()
                }
            }
        })
    }
}