package com.example.timglog

import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.timglog.databinding.FragmentTaskBinding
import java.util.*
import kotlin.math.abs
import kotlin.math.log10

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private lateinit var binding : FragmentTaskBinding
private lateinit var taskViewModel: TaskViewModel
private var seconds = 0
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TaskFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TaskFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        Log.d(TaskFragment::class.java.name, "oncreate option menu~〜")

//        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.task_ongoing, menu)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.tomato -> {
                Log.d(TaskFragment::class.java.name, "onclick tomato")
                return super.onOptionsItemSelected(item)
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        taskViewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)

        binding  = FragmentTaskBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskViewModel.title.observe(viewLifecycleOwner, androidx.lifecycle.Observer { binding.tasknameView.text = it })
        taskViewModel.category.observe(viewLifecycleOwner, androidx.lifecycle.Observer { binding.categoryName.text = it })
        taskViewModel.duration_text.observe(viewLifecycleOwner, androidx.lifecycle.Observer { binding.taskTime.text =it })
        binding.buttonStop.setOnClickListener{
            taskViewModel.stopTask()
            findNavController().navigate(R.id.action_nav_task_to_nav_home)

        }

    }





    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TaskFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TaskFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}