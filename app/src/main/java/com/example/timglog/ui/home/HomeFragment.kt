package com.example.timglog.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timglog.TaskItems
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
        val layoutManager = StickyHeadersLinearLayoutManager<TaskListAdapter>(
            context, LinearLayoutManager.VERTICAL, false)

        val dividerItemDecoration = DividerItemDecoration(context, layoutManager.orientation)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(dividerItemDecoration)
        taskViewModel.alltasks.observe(viewLifecycleOwner, Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let { adapter.submitList(TaskItems.convertList(it)) }
        })

        val swipeHandler = object : SwipeHelper(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recyclerView.adapter as TaskListAdapter
                adapter.getTask(viewHolder.adapterPosition)?.let { taskViewModel.deleteTask(it) }
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        return binding.root
    }
}