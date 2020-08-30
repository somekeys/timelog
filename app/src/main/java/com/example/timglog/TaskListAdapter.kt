package com.example.timglog

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.timglog.databinding.ItemDateBinding
import com.example.timglog.databinding.ItemTaskBinding
import com.example.timglog.ui.home.StickyHeaders
import java.time.format.DateTimeFormatter

class TaskListAdapter (
    context: FragmentActivity?
) : ListAdapter<TaskItem,RecyclerView.ViewHolder>(DIFF_CALLBACK), StickyHeaders {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    inner class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        val titleView: TextView = binding.titleView
        val durationView : TextView=binding.durationView
        val categoryView : TextView=binding.categoryView

    }

    inner class DateViewHolder(val binding: ItemDateBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is TaskItem.Day ->  DAY_ITEM
            is TaskItem.TaskEntry -> TASK_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):RecyclerView.ViewHolder {
        return when(viewType){
            DAY_ITEM -> {
                val binding = ItemDateBinding.inflate(inflater,parent,false)
                DateViewHolder(binding)

            }
            else ->{
                val binding = ItemTaskBinding.inflate(inflater,parent,false)
                TaskViewHolder(binding)
            }

        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType) {
            TASK_ITEM -> {
                val holder = holder as TaskViewHolder
                val current = (getItem(position) as TaskItem.TaskEntry).task
                Log.d("TAsklistadapter debuging","onbind task item " + current.name )

                holder.titleView.text = current.name
                holder.durationView.text =
                    TaskViewModel.secondsToSpaning(((current.endTime - current.startTime)).toInt())
                holder.categoryView.text = current.category
            }
            else ->{
                val holder  = holder as DateViewHolder
                val current = (getItem(position) as TaskItem.Day).day
                val formatter: DateTimeFormatter =
                    DateTimeFormatter.ofPattern("E, MMM dd")
                holder.binding.itemDate.text = formatter.format(current)

            }
        }

    }



    override fun isStickyHeader(position: Int): Boolean {
        return when(getItemViewType(position)){
            DAY_ITEM -> true
            else -> false
        }
    }

    companion object{

        const val DAY_ITEM = 0
        const val TASK_ITEM = 1


        private val DIFF_CALLBACK: DiffUtil.ItemCallback<TaskItem> =
            object :  DiffUtil.ItemCallback<TaskItem> (){
                override fun areItemsTheSame(oldItem: TaskItem, newItem: TaskItem): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(oldItem: TaskItem, newItem: TaskItem): Boolean {
                    return if (oldItem is TaskItem.TaskEntry && newItem is TaskItem.TaskEntry) {

                        oldItem.task == newItem.task

                    }else if(oldItem is TaskItem.Day && newItem is TaskItem.Day ){
                        oldItem.day == newItem.day

                    }else{
                        false
                    }
                }
            }
    }
}