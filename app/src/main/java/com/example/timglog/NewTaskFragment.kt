package com.example.timglog

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.timglog.databinding.FragmentNewTaskBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewTask.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewTaskFragment : Fragment() {
    private lateinit var binding : FragmentNewTaskBinding
    private lateinit var taskViewModel: TaskViewModel


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

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(NewTaskFragment::class.java.name, "oncreate view")

        // Inflate the layout for this fragment
        taskViewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)
        binding =  FragmentNewTaskBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAutoComplete()

        binding.taskName.requestFocus()
//        binding.taskName.showDropDown()
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.taskName, InputMethodManager.SHOW_IMPLICIT)


    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        Log.d(NewTaskFragment::class.java.name, "oncreate option menu")

//        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.task_edit, menu)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.done -> {
                val name = binding.taskName.text.toString()
                val cate = binding.taskCategory.text.toString()
                if (name == null || name == "") {
                    Toast.makeText(context, "Name can not be empty", Toast.LENGTH_SHORT).show()
                } else if (cate == null || cate == "") {
                    Toast.makeText(context, "Category can not be empty", Toast.LENGTH_SHORT).show()
                } else {
                    taskViewModel.startTask(name, cate)
                    findNavController().navigate(R.id.action_nav_newtask_to_nav_task)
                }
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    /**
     * setup adaptors for auto complete for name and category
     */
    private fun setUpAutoComplete(){
        taskViewModel.alltasks.observe(viewLifecycleOwner, Observer { tasks ->
            taskViewModel.nameTaskMap = hashMapOf()
            for (task in tasks) {
                taskViewModel.nameTaskMap.getOrPut(task.name) { task }
            }

            val nameAdapter = context?.let {
                ArrayAdapter(
                    it,
                    R.layout.item_select,
                    taskViewModel.nameTaskMap.keys.toList()
                )
            }
            binding.taskName.setAdapter(nameAdapter)


        })

        taskViewModel.categories.observe(viewLifecycleOwner, Observer { tags ->

            val categoryAdapter = context?.let { ArrayAdapter(it, R.layout.item_select, tags) }
            binding.taskCategory.setAdapter(categoryAdapter)
        })

        binding.taskName.setOnItemClickListener { parent, view, position, id ->
            binding.taskName.adapter.getItem(position).toString().also {
                val task : Task? = taskViewModel.getLastByName(it)
                if (task != null) {
                    binding.taskCategory.setText(task.category)
                }

            }
        }
        //show dropdown when no input
        binding.taskCategory.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.taskCategory.showDropDown()
            }
        }
        binding.taskName.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.taskName.showDropDown()
            }
        }
        binding.taskCategory.setOnTouchListener { _, _ ->  binding.taskCategory.showDropDown();false;}
        binding.taskName.setOnTouchListener { _, _ -> binding.taskName.showDropDown();false; }


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewTask.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewTaskFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}