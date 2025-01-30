package com.example.todoapp.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.todoapp.models.ToDo
import com.example.todoapp.repositories.ToDoRepo

class ToDoViewModel(private val application: Application): AndroidViewModel(application) {
    private var toDoRepo = ToDoRepo(application)

    suspend fun addNewToDo(toDo: ToDo) {
        if (checkToDo(toDo)) {
            toDoRepo.addToDo(toDo)
        } else return
    }

    suspend fun editToDo(toDo: ToDo) {
        if (checkToDo(toDo)) {
            toDoRepo.changeToDo(toDo)
        } else return
    }

    suspend fun removeToDo(toDo: ToDo) {
        if (checkToDo(toDo)) {
            toDoRepo.removeToDo(toDo)
            Toast.makeText(application, "\"${toDo.task}\" task Removed.", Toast.LENGTH_SHORT).show()
        } else return
    }

    fun readAllToDoes(): LiveData<List<ToDo>> {
        return toDoRepo.getAllToDoes()
    }

    private fun checkToDo(toDo: ToDo): Boolean {
        if (toDo.task.isBlank() || toDo.task.isEmpty()) {
            Toast.makeText(application, "Error: The title was Empty!", Toast.LENGTH_SHORT).show()
            return false
        }
        if (toDo.description.isBlank() || toDo.description.isEmpty()) {
            toDo.description = ""
        }
        if (toDo.id < 0) {
            Toast.makeText(application, "Error: Unknown problem!", Toast.LENGTH_SHORT).show()
            Log.e("DataBase", "The \"ToDo id\" is ${toDo.id}.")
            return false
        }
        return true
    }
}
