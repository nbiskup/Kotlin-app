package hr.algebra.nasa

import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*


@Suppress("DEPRECATION")
class MapsFragment : Fragment() {
    private lateinit var cityNameText: EditText
    private lateinit var searchButton: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)
        cityNameText = view.findViewById(R.id.city_name_text)
        searchButton = view.findViewById(R.id.search_button)

        searchButton.setOnClickListener {
            val cityName = cityNameText.text.toString()
            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(searchForCity(cityName))
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(searchForCity(getString(R.string.zadar)
        ))
    }

    private fun searchForCity(cityName: String): OnMapReadyCallback {

        return OnMapReadyCallback { googleMap ->

            val geocoder = Geocoder(requireContext(), Locale.getDefault())

            var cityLatLng: LatLng? = null

            try {
                val addressList = geocoder.getFromLocationName(cityName, 1)
                if (addressList != null) {
                    if (addressList.isNotEmpty()) {
                        val address = addressList[0]
                        cityLatLng = LatLng(address.latitude, address.longitude)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            cityLatLng?.let {
                googleMap.addMarker(MarkerOptions().position(it).title("Marker in $cityName"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(it))
            }
            googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.maps)
            )
        }

    }
}

