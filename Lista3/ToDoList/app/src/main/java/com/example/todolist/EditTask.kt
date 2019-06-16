package com.example.todolist

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.activity_edit_task.*
import kotlinx.android.synthetic.main.custom_title.view.*
import kotlinx.android.synthetic.main.icon_dialog.view.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


class EditTask : AppCompatActivity() {

    var task : Task? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        task = intent.getParcelableExtra<Task>("data")
        titleEdit.setText(task?.title)
        val text = "Priorytet: " + task?.priority
        priorityShow.text = text
        dateShow.text = task!!.date.toLocalDate().toString()
        timeShow.text = task!!.date.toLocalTime().toString()
        iconEdit.setImageResource(task!!.icon )

    }

    fun save(view: View) {
        task?.title = titleEdit.text.toString()
        val myintent = Intent()
        myintent.putExtra("wynik", task)
        myintent.putExtra("save",true)
        setResult(Activity.RESULT_OK, myintent)
        finish()
    }
    fun restore(view: View) {
        val myintent = Intent()
        myintent.putExtra("save",false)
        setResult(Activity.RESULT_OK, myintent)
        finish()
    }
    fun changePriority(view: View){
        val builder = AlertDialog.Builder(this,R.style.MyDialogTheme)
        builder.setTitle("Wybierz priorytet")
        val priorities = resources.getStringArray(R.array.priorities)
        builder.setItems(priorities) { _, which ->
            task?.priority = priorities[which]
            val text = "Priorytet: " + task?.priority
            priorityShow.text =  text
        }

        val dialog = builder.create()
        dialog.show()
    }
    fun changeDate(view: View){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            task?.date = LocalDateTime.of(LocalDate.of(year,monthOfYear+1,dayOfMonth),task!!.date.toLocalTime())
            dateShow.text = task!!.date.toLocalDate().toString()
        }, year, month, day)

        dpd.show()
    }
    fun changeTime(view: View){
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        val tpd = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            // Display Selected date in textbox
            task?.date = LocalDateTime.of(task!!.date.toLocalDate(), LocalTime.of(hourOfDay,minute))
            timeShow.text = task!!.date.toLocalTime().toString()
        }, hour, minute,true)

        tpd.show()
    }
    fun changeIcon(view: View){
        val dialogView = LayoutInflater.from(this).inflate(R.layout.icon_dialog,null)
        val titleView = LayoutInflater.from(this).inflate(R.layout.custom_title,null)
        titleView.dialogTitle.text = "Wybierz ikonke zadania"
        val builder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCustomTitle(titleView)
        val dialog = builder.show()
        dialogView.homeIcon.setOnClickListener{
            dialog.dismiss()
            val id = resources.getIdentifier("home","drawable",this.packageName)
            task?.icon = id
            iconEdit.setImageResource(task!!.icon )
        }
        dialogView.workIcon.setOnClickListener{
            dialog.dismiss()
            val id = resources.getIdentifier("work","drawable",this.packageName)
            task?.icon = id
            iconEdit.setImageResource(task!!.icon )
        }
        dialogView.socialIcon.setOnClickListener{
            dialog.dismiss()
            val id = resources.getIdentifier("social","drawable",this.packageName)
            task?.icon = id
            iconEdit.setImageResource(task!!.icon )
        }
        dialogView.shopIcon.setOnClickListener{
            dialog.dismiss()
            val id = resources.getIdentifier("shop","drawable",this.packageName)
            task?.icon = id
            iconEdit.setImageResource(task!!.icon )
        }
    }
}
