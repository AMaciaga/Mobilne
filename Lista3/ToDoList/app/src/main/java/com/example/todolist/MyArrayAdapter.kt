package com.example.todolist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView


class MyArrayAdapter(context: Context, var data: ArrayList<Task>) :
        ArrayAdapter<Task>(context, R.layout.my_layout_item, data) {

    // ViewHolder!!

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.my_layout_item, parent, false)
        }
        view!!.findViewById<TextView>(R.id.title).text = data[position].title
        view!!.findViewById<ImageView>(R.id.icon).setImageResource(data[position].icon)
        view!!.findViewById<TextView>(R.id.date).text = data[position].date.toLocalDate().toString()
        view!!.findViewById<TextView>(R.id.time).text = data[position].date.toLocalTime().toString()
        view!!.findViewById<TextView>(R.id.priority).text = data[position].priority
        return view
    }
}