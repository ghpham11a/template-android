package com.example.template.ui.screens.map

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.template.networking.GoogleMaps
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val placesClient: PlacesClient
): ViewModel() {

    private val _mapOrigin = MutableStateFlow<LatLng>(LatLng(1.35, 103.87))
    val mapOrigin: StateFlow<LatLng> = _mapOrigin

    private val _marker = MutableStateFlow<LatLng?>(null)
    val marker: StateFlow<LatLng?> = _marker

    private val _location = MutableStateFlow<List<AutocompletePrediction>>(emptyList())
    val location: StateFlow<List<AutocompletePrediction>> = _location

    fun updateMapOrigin(origin: LatLng) {
        _mapOrigin.value = origin
    }

    fun searchLocations(origin: LatLng, query: String) {
        val placeFields: List<Place.Field> = listOf(Place.Field.ID, Place.Field.ADDRESS, Place.Field.NAME)
        val circle = CircularBounds.newInstance(origin, 5000.0)
        val autocompletePlacesRequest = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .setLocationRestriction(circle)
            .build()

        placesClient.findAutocompletePredictions(autocompletePlacesRequest)
            .addOnSuccessListener { response ->
                val predictions: List<AutocompletePrediction> = response.autocompletePredictions
                _location.value = predictions
            }
            .addOnFailureListener { exception ->

            }
    }

    fun readPlaceDetails(key: String, placeId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = GoogleMaps.api.readPlaceDetails(placeId, key)
                response.results.first().geometry.location.lat
                val coordinates = LatLng(response.results.first().geometry.location.lat, response.results.first().geometry.location.lng)
                _mapOrigin.value = coordinates
                _marker.value = coordinates
            } catch (e: Exception) {

            }
        }
    }
}