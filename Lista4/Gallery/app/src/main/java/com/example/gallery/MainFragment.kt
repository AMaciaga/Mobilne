package com.example.gallery

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MainFragment : Fragment() {

    var pictureList :ArrayList<MyPicture> = ArrayList()
    var myadapter : MyArrayAdapter? = null
    var descSort = true

    val REQUEST_TAKE_PHOTO = 1

    var currentPhotoPath: String = ""

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES) as File
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(activity?.packageManager!!)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        context!!,
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(savedInstanceState?.getParcelableArrayList<MyPicture>("data")  == null){
            var pictures = resources.getStringArray(R.array.pictures)
            for(pic in pictures){
                val id = resources.getIdentifier(pic,"drawable",activity?.packageName)

                val picture = MyPicture(pic,id,null,0f,"this is short description")
                pictureList.add(picture)
            }
        }
        else{
            pictureList = savedInstanceState.getParcelableArrayList<MyPicture>("data") as ArrayList<MyPicture>
            descSort = savedInstanceState.getBoolean("sortBy")
        }
        sort()
        myadapter = MyArrayAdapter(context!!, pictureList)
        listView.adapter = myadapter
        listView.setOnItemClickListener { _,_, index, _ ->

            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                val myintent = Intent(activity, Main2Activity::class.java )
                myintent.putExtra("data", pictureList[index])
                startActivityForResult(myintent, index)
            } else {
                var frag = fragmentManager!!.findFragmentById(R.id.main2fragment) as Main2Fragment
                frag.display(pictureList[index])
                frag.index = index
            }

        }
        sortButton.setOnClickListener { onClickSort(it) }
        addPicture.setOnClickListener { addPicture(it) }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != AppCompatActivity.RESULT_CANCELED){
            if(data?.getParcelableExtra<MyPicture>("wynik") == null){
                if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
                val picture = MyPicture("camera",null,currentPhotoPath,0f,"this is short description")
                pictureList.add(picture)
                myadapter?.notifyDataSetChanged()
                }
            }
            else{
                val task = data?.getParcelableExtra<MyPicture>("wynik")
                pictureList[requestCode] = task as MyPicture
                sort()
                myadapter?.notifyDataSetChanged()
            }

        }

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("data", pictureList)
        outState.putBoolean("sortBy",descSort)
    }

    fun onClickSort(view: View){
        descSort = !descSort
        sort()

    }
    fun sort(){
        if(descSort){
           sortDes()
            sortButton.text = resources.getString(R.string.sortuj_rosnaco)
        }
        else{
            sortAsc()
            sortButton.text = resources.getString(R.string.sortuj_malejaco)
        }

    }
    fun sortAsc(){
        pictureList.sortWith(compareBy {it.rating})
        myadapter?.notifyDataSetChanged()
    }
    fun sortDes(){
        pictureList.sortWith(compareByDescending {it.rating})
        myadapter?.notifyDataSetChanged()
    }
    fun addPicture(view: View){
        dispatchTakePictureIntent()
    }

}
