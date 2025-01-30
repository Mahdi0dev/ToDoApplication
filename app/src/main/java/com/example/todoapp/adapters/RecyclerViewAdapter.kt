package com.example.todoapp.adapters

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.models.ToDo
import com.example.todoapp.viewModels.ToDoViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class RecyclerViewAdapter(private val context: Context, private val viewModel: ToDoViewModel): RecyclerView.Adapter<RecyclerViewAdapter.ToDoHolder>() {
    private lateinit var onToDoClickListener: OnToDoClickListener
    private var toDoesArrayList = ArrayList<ToDo>()
    inner class ToDoHolder(private val v: View): RecyclerView.ViewHolder(v), OnClickListener {
        var checkBox: CheckBox = v.findViewById(R.id.checkBox)
        var removeButton: ImageButton = v.findViewById(R.id.removeImageButton)

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            onToDoClickListener.onItemClick(toDoesArrayList[adapterPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoHolder {
        return ToDoHolder(LayoutInflater.from(context).inflate(R.layout.item_todo, parent, false))
    }

    override fun getItemCount(): Int {
        return toDoesArrayList.size
    }

    override fun onBindViewHolder(holder: ToDoHolder, position: Int) {
        val item = toDoesArrayList[position]
        holder.checkBox.text = item.task

        holder.checkBox.setOnCheckedChangeListener(null)

        holder.checkBox.isChecked = item.status

        if (item.status) {
            holder.checkBox.paintFlags = holder.checkBox.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.checkBox.paintFlags = holder.checkBox.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        holder.checkBox.setOnCheckedChangeListener { compoundButton, isChecked ->
            item.status = isChecked

            if (isChecked) {
                holder.checkBox.paintFlags = holder.checkBox.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                holder.checkBox.paintFlags = holder.checkBox.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            MainScope().launch { viewModel.editToDo(item) }
        }

        holder.removeButton.setOnClickListener {
            Toast.makeText(context, "Swipe the item to right for remove.", Toast.LENGTH_LONG).show()
        }
    }

    fun setToDoesList(list: List<ToDo>) {
        val diffUtilsResult = DiffUtil.calculateDiff(DiffUtils(toDoesArrayList, list))
        toDoesArrayList.clear()
        toDoesArrayList.addAll(list)
        diffUtilsResult.dispatchUpdatesTo(this)
    }

    fun getToDoItem(position: Int): ToDo {
        return toDoesArrayList[position]
    }

    interface OnToDoClickListener {
        fun onItemClick(toDo: ToDo)
    }

    fun setOnToDoClickListener(onToDoClickListener: OnToDoClickListener) {
        this.onToDoClickListener = onToDoClickListener
    }
}
