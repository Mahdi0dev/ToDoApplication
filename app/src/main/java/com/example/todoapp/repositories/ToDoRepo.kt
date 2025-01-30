package com.example.todoapp.repositories

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.example.todoapp.dao.ToDoDbDao
import com.example.todoapp.dataBases.ToDoDb
import com.example.todoapp.models.ToDo

class ToDoRepo(private val application: Application) {
    private var toDoDao: ToDoDbDao
    private var toDoesList: LiveData<List<ToDo>>

    init {
        val db = ToDoDb.getDb(application)
        toDoDao = db.getDao()
        toDoesList = toDoDao.read()
    }

    suspend fun addToDo(toDo: ToDo) {
        if (toDoExists(toDo)) {
            Toast.makeText(application, "This task already exists!", Toast.LENGTH_SHORT).show()
            return
        } else {
            toDoDao.insert(toDo)
            Toast.makeText(application, "\"${toDo.task}\" task Added.", Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun changeToDo(toDo: ToDo) {
        toDoDao.update(toDo)
    }

    suspend fun removeToDo(toDo: ToDo) {
        toDoDao.delete(toDo)
    }

    fun getAllToDoes(): LiveData<List<ToDo>> {
        return toDoesList
    }

    private fun toDoExists(toDo: ToDo): Boolean {
        var result = false
        toDoesList.observeForever { list ->
            if (list.any { otherToDo ->
                    otherToDo.task == toDo.task && otherToDo.description == toDo.description
                }) {
                result = true
            }
        }
        return result
    }
}
