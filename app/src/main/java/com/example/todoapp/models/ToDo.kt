package com.example.todoapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ToDo(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "number") var id: Int = 0,
    var task: String,
    var description: String,
    @ColumnInfo(name = "done?") var status: Boolean = false
) {
    companion object {
        const val ID_NAME = "ID"
        const val TASK_NAME = "TASK"
        const val DESCRIPTION_NAME = "DESCRIPTION"
        const val STATUS_NAME = "STATUS"

    }
}
