package com.example.maps2

import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_maps.*
import java.io.IOException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, AdapterView.OnItemSelectedListener {

    private lateinit var mMap: GoogleMap
    private var cities : ArrayList<City> = ArrayList()
    private var dropDown : ArrayList<String> = ArrayList()
    private lateinit var fb : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {

        fb = FirebaseDatabase.getInstance().reference
        val locations = fb.child("/")
        locations.addValueEventListener( object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("am2019", "ups: ${p0}")
            }

            override fun onDataChange(p0: DataSnapshot) {
                Log.d("am2019", "${p0.key} -> ${p0.value}")
                getCities(p0)
                createSpinner()
            }
        })


        mMap = googleMap

        button.setOnClickListener{
            searchLocation()
        }

    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(cities[pos].lat,cities[pos].lng)))
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }

    fun createSpinner(){
        val arrayAdapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,dropDown)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter
        if(dropDown.isNotEmpty()){
            mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(cities[0].lat,cities[0].lng)))
        }
        spinner.onItemSelectedListener = this
    }

    fun getCities(p: DataSnapshot){
        cities  = ArrayList()
        dropDown  = ArrayList()
        p.children.forEach {
            val title = it.key.toString()
            var lat = 0.0
            var lng = 0.0
            it.children.forEach{ it2 ->
                if(it2.key == "x"){
                    lat = it2.value.toString().toDouble()

                }else{
                    lng = it2.value.toString().toDouble()
                }
            }
            val city = City(title,lat,lng)
            cities.add(city)
            dropDown.add(title)
            mMap.addMarker(MarkerOptions().position(LatLng(city.lat,city.lng)).title(city.name))
        }

    }

    private fun searchLocation() {

        val addressList: List<Address>?

        if (editText.text.isBlank()) {
            Toast.makeText(this,"Nie podano adresu", Toast.LENGTH_SHORT).show()
        }
        else{
            val geoCoder = Geocoder(this)
            try {
                val location = editText.text.toString()
                addressList = geoCoder.getFromLocationName(location, 1)
                Log.d("am2019", addressList.toString())

                if (addressList.isNotEmpty()) {
                    addressList.forEach {

                        val newCity = DatabaseCity(it.latitude, it.longitude)
                        fb.child(location).setValue(newCity)
                    }
                }
                else {
                    Toast.makeText(this, "Nie znaleziono adresu", Toast.LENGTH_SHORT).show()
                }
                editText.clearFocus()
                editText.text.clear()

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    class City(
        var  name: String,
        var  lat : Double,
        var lng : Double
    )
    class DatabaseCity(
        var x: Double,
        var y: Double
    )
}
