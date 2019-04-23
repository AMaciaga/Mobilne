package com.example.gallery

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import java.io.File


class MyArrayAdapter(context: Context, var data: ArrayList<MyPicture>) :
        ArrayAdapter<MyPicture>(context, R.layout.my_layout_item, data) {

    // ViewHolder!!

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.my_layout_item, parent, false)
        }
        view!!.findViewById<TextView>(R.id.title).text = data[position].title
        if(data[position].icon == null){
            var imgFile = File(data[position].direct)

            if(imgFile.exists()){

                var myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath())

                view!!.findViewById<ImageView>(R.id.icon).setImageBitmap(myBitmap)

            }
        }
        else{
            view!!.findViewById<ImageView>(R.id.icon).setImageResource(data[position].icon as Int)
        }
        view!!.findViewById<RatingBar>(R.id.ratingBar).rating = data[position].rating
        return view
    }
}