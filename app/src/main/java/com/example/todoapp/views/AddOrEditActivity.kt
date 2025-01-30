package com.example.todoapp.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.R
import com.example.todoapp.models.ToDo

class AddOrEditActivity : AppCompatActivity() {
    private lateinit var taskEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var addBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_or_edit)
        init()
    }

    private fun init() {
        bindViews()

        val dataIntent = intent

        if (dataIntent.hasExtra(ToDo.ID_NAME)) {
            addBtn.text = "Edit"
            taskEditText.setText(dataIntent.getStringExtra(ToDo.TASK_NAME))
            descriptionEditText.setText(dataIntent.getStringExtra(ToDo.DESCRIPTION_NAME))
        }

        addBtn.setOnClickListener {
            val outIntent = Intent()

            if (taskEditText.text.isBlank() || taskEditText.text.isEmpty()) {
                Toast.makeText(this, "Please write your task.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val task = taskEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val id = dataIntent.getIntExtra(ToDo.ID_NAME, 0)
            val status = dataIntent.getBooleanExtra(ToDo.STATUS_NAME, false)

            outIntent.putExtra(ToDo.TASK_NAME, task)
            outIntent.putExtra(ToDo.DESCRIPTION_NAME, description)

            outIntent.putExtra(ToDo.ID_NAME, id)
            outIntent.putExtra(ToDo.STATUS_NAME, status)

            setResult(Activity.RESULT_OK, outIntent)

            if (addBtn.text.toString() == "Edit") {
                Toast.makeText(this, "\"$task\" task Edited.", Toast.LENGTH_SHORT).show()
            }

            finish()
        }
    }

    private fun bindViews() {
        taskEditText = findViewById(R.id.editTextTask)
        descriptionEditText = findViewById(R.id.editTextDescriptionMultiLine)
        addBtn = findViewById(R.id.buttonAdd)
    }
}
