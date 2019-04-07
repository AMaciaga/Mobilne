package com.example.todolist

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    var taskList :ArrayList<Task> = ArrayList()
    var myadapter : MyArrayAdapter? = null
    var sortedBy = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myadapter = MyArrayAdapter(this, taskList)
        listview.adapter = myadapter
        listview.setOnItemClickListener { _,_, index, _ ->
            val myintent = Intent(this, EditTask::class.java )
            myintent.putExtra("data", taskList[index])
            startActivityForResult(myintent, index)
        }
        listview.setOnItemLongClickListener { _, _, index, _ ->
            val builder = AlertDialog.Builder(this)
            val title = taskList[index].title
            builder.setTitle("Czy napewno chcesz usunąć zadanie $title?")
            builder.setPositiveButton("tak"
            ) { _, _ ->
                taskList.removeAt(index)
                myadapter?.notifyDataSetChanged()
            }
            builder.setNegativeButton("nie"
            ) { _, _ ->
                // User cancelled the dialog
            }

            val dialog = builder.create()
            dialog.show()
             true
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_CANCELED){
            val save = data?.getBooleanExtra("save",false) as Boolean
            if(save){
                val task = data.getParcelableExtra<Task>("wynik")
                taskList[requestCode] = task as Task
                sort()
                myadapter?.notifyDataSetChanged()
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelableArrayList("data", taskList)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        taskList = savedInstanceState?.getParcelableArrayList<Task>("data") as ArrayList<Task>
        myadapter = MyArrayAdapter(this, taskList)
        listview.adapter = myadapter
    }

    fun addTask(view: View){
        val text = taskTitle.text.toString()
        val id = resources.getIdentifier("home","drawable",this.packageName)
        val task = Task(text,id, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),"Średni")
        taskList.add(task)
        taskTitle.text.clear()
        sort()
        myadapter?.notifyDataSetChanged()
    }
    fun sort(){
        when(sortedBy){
            0 -> sortByDateAsc()
            1 -> sortByDateDesc()
            2 -> sortByPriorityAsc()
            3 -> sortByPriorityDesc()
            4 -> sortByIcon()
        }
    }
    fun sortBy(view: View){
        val builder = AlertDialog.Builder(this,R.style.MyDialogTheme)
        builder.setTitle("Sortuj po:")
        val sortTypes = resources.getStringArray(R.array.sortTypes)
        builder.setItems(sortTypes) { _, which ->
            sortedBy = which
            sort()
        }
        val dialog = builder.create()
        dialog.show()
    }
    fun sortByDateAsc(){
        taskList.sortWith(Comparator { x: Task, y: Task -> x.date.compareTo(y.date) })
        myadapter?.notifyDataSetChanged()
    }
    fun sortByDateDesc(){
        taskList.sortWith(Comparator { x: Task, y: Task -> y.date.compareTo(x.date) })
        myadapter?.notifyDataSetChanged()
    }
    fun sortByPriorityAsc(){
        val priorities = resources.getStringArray(R.array.priorities)
        taskList.sortWith(Comparator { x: Task, y: Task -> priorities.indexOf(x.priority) - priorities.indexOf(y.priority) })
        myadapter?.notifyDataSetChanged()
    }
    fun sortByPriorityDesc(){
        val priorities = resources.getStringArray(R.array.priorities)
        taskList.sortWith(Comparator { x: Task, y: Task -> priorities.indexOf(y.priority) - priorities.indexOf(x.priority) })
        myadapter?.notifyDataSetChanged()
    }
    fun sortByIcon(){
        taskList.sortWith(compareBy {it.icon})
        myadapter?.notifyDataSetChanged()
    }


}
