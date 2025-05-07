package com.example.restaurantapp.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.restaurantapp.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class DirectionsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_directions, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val startInput = view.findViewById<EditText>(R.id.edittext_start_location)
        val goButton = view.findViewById<Button>(R.id.button_get_directions)

        goButton.setOnClickListener {
            val address = startInput.text.toString().trim()
            if (address.isEmpty()) {
                Toast.makeText(context, "Please enter a starting point", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val destination = "King Umberto Elmont"
            val uriString = "https://www.google.com/maps/dir/?api=1" +
                    "&origin=${Uri.encode(address)}" +
                    "&destination=${Uri.encode(destination)}" +
                    "&travelmode=driving"
            val gmmUri = Uri.parse(uriString)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmUri).apply {
                setPackage("com.google.android.apps.maps")
            }
            if (mapIntent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(mapIntent)
            } else {
                Toast.makeText(context, "Google Maps app not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val restaurantLocation = LatLng(40.70809076274047, -73.69009490356159) // King Umberto Elmont coordinates
        mMap.addMarker(
            MarkerOptions()
                .position(restaurantLocation)
                .title("City Pizza")
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurantLocation, 15f))

        mMap.uiSettings.isZoomControlsEnabled = true
    }
}