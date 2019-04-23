package com.example.gallery


import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.android.synthetic.main.fragment_main2.*
import java.io.File


class Main2Fragment : Fragment() {

    var pic : MyPicture? = null
    var index = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main2, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity?.intent != null) {
            var extra = activity!!.intent.getParcelableExtra<MyPicture>("data")
            display(extra)
        }
        ratingBarSet.setOnRatingBarChangeListener { _, rating, _ ->
            pic?.rating = rating
        }
        button.setOnClickListener { save(it) }
    }

    fun display(p : MyPicture?) {
        if (p != null){
            pic = p
            container.visibility=View.VISIBLE
            title.setText(p.title)
            if(p.icon == null){
                var imgFile = File(p.direct)

                if(imgFile.exists()){

                    var myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath())

                    view!!.findViewById<ImageView>(R.id.icon).setImageBitmap(myBitmap)

                }
            }
            else{
                icon.setImageResource(p.icon as Int)
            }

            ratingBarSet.rating = p.rating
            desciption.setText(p.description)
        }

    }
    fun save(view: View){
        pic?.title = title.text.toString()
        pic?.description = desciption.text.toString()
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            val myintent = Intent()
            myintent.putExtra("wynik", pic)
            activity?.setResult(Activity.RESULT_OK, myintent)
            activity?.finish()
        } else {
            var frag = fragmentManager!!.findFragmentById(R.id.mainfragment) as MainFragment
            frag.pictureList[index] = pic as MyPicture
            frag.sort()
            frag.myadapter?.notifyDataSetChanged()
        }
    }
}
