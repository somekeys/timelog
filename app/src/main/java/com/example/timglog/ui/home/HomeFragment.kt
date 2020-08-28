package com.example.timglog.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timglog.TaskListAdapter
import com.example.timglog.TaskViewModel
import com.example.timglog.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding : FragmentHomeBinding
    private lateinit var taskViewModel: TaskViewModel



    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        val recyclerView = binding.recyclerview
        val adapter =  TaskListAdapter(activity)
//        val layoutManager = StickyHeadersLinearLayoutManager<TaskListAdapter>(
//            context, LinearLayoutManager.VERTICAL, false)
        val layoutManager = LinearLayoutManager(activity)

        val dividerItemDecoration = DividerItemDecoration(context, layoutManager.orientation)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(dividerItemDecoration)
        taskViewModel.alltasks.observe(viewLifecycleOwner, Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let { adapter.setTasks(it) }
        })

        return binding.root
    }
}