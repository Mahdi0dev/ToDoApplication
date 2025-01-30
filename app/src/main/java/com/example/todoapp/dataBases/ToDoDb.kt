package com.example.todoapp.dataBases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todoapp.dao.ToDoDbDao
import com.example.todoapp.models.ToDo

@Database(entities = [ToDo::class], version = 4)
abstract class ToDoDb : RoomDatabase() {
    abstract fun getDao(): ToDoDbDao

    // Singleton pattern
    companion object {
        private var instance: ToDoDb? = null

        fun getDb(context: Context): ToDoDb {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(context, ToDoDb::class.java, "ToDo-DataBase").build()
            }

            return instance as ToDoDb
        }
    }
}
