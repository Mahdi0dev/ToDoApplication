package com.example.todoapp.handlers

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.adapters.RecyclerViewAdapter
import com.example.todoapp.viewModels.ToDoViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SwipeToDeleteCallback(private val adapter: RecyclerViewAdapter, private val viewModel: ToDoViewModel): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val toDoItem = adapter.getToDoItem(viewHolder.adapterPosition)
        MainScope().launch { viewModel.removeToDo(toDoItem) }
    }
}