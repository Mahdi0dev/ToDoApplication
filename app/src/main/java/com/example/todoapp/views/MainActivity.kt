package com.example.todoapp.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.adapters.RecyclerViewAdapter
import com.example.todoapp.handlers.SwipeToDeleteCallback
import com.example.todoapp.models.ToDo
import com.example.todoapp.viewModels.ToDoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: ToDoViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var addButton: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        bindRequirements()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        viewModel.readAllToDoes().observe(this) { list ->
            adapter.setToDoesList(list)
        }


        val launcherAddActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val resultData = result.data ?: return@registerForActivityResult
                    val task = resultData.getStringExtra(ToDo.TASK_NAME)!!
                    val description = resultData.getStringExtra(ToDo.DESCRIPTION_NAME)!!

                    val toDoItem = ToDo(task = task, description = description)

                    MainScope().launch { viewModel.addNewToDo(toDoItem) }
                }
            }

        val launcherEditActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val resultData = result.data ?: return@registerForActivityResult
                    val id = resultData.getIntExtra(ToDo.ID_NAME, 0)
                    val status = resultData.getBooleanExtra(ToDo.STATUS_NAME, false)
                    val task = resultData.getStringExtra(ToDo.TASK_NAME)!!
                    val description = resultData.getStringExtra(ToDo.DESCRIPTION_NAME)!!

                    val toDoItem = ToDo(id, task, description, status)

                    MainScope().launch { viewModel.editToDo(toDoItem) }
                }
            }

        addButton.setOnClickListener {
            launcherAddActivity.launch(Intent(this, AddOrEditActivity::class.java))
        }


        ItemTouchHelper(
            SwipeToDeleteCallback(
                adapter,
                viewModel
            )
        ).attachToRecyclerView(recyclerView)

        adapter.setOnToDoClickListener(object : RecyclerViewAdapter.OnToDoClickListener {
            override fun onItemClick(toDo: ToDo) {
                val intent = Intent(this@MainActivity, AddOrEditActivity::class.java)
                intent.putExtra(ToDo.ID_NAME, toDo.id)
                intent.putExtra(ToDo.TASK_NAME, toDo.task)
                intent.putExtra(ToDo.DESCRIPTION_NAME, toDo.description)
                intent.putExtra(ToDo.STATUS_NAME, toDo.status)
                launcherEditActivity.launch(intent)
            }
        })
    }

    private fun bindRequirements() {
        bindViews()
        viewModel = ViewModelProvider(this).get(modelClass = ToDoViewModel::class.java)
        adapter = RecyclerViewAdapter(this, viewModel)
    }

    private fun bindViews() {
        recyclerView = findViewById(R.id.recyclerView)
        addButton = findViewById(R.id.addButton)
    }
}
