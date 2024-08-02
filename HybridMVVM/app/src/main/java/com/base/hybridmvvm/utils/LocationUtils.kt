package com.base.hybridmvvm.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

object LocationUtils {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    /**
     * getLastLocation
     * 마지막 위치 가져오기
     * @param context
     * @param callback
     */
    @SuppressLint("MissingPermission")
    fun getLastLocation(context: Context, callback: (Location?) -> Unit) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            10000
        ).apply {
            setWaitForAccurateLocation(false)
            setMinUpdateIntervalMillis(5000)
            setMaxUpdateDelayMillis(10000)
        }.build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { callback(it) }
                fusedLocationProviderClient.removeLocationUpdates(this)
            }
        }

        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    callback(location)
                } else {
                    fusedLocationProviderClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.getMainLooper()
                    )
                }
            }.addOnFailureListener {
                callback(null)
            }

    }

    /**
     * getCurrentLocation
     * 현재 위치 가져오기
     * @param context
     * @param callback
     */
    @SuppressLint("MissingPermission")
    fun getCurrentLocation(context: Context, callback: (Location?) -> Unit) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            0
        ).apply {
            setWaitForAccurateLocation(true)
            setMinUpdateIntervalMillis(1000)
            setMaxUpdateDelayMillis(5000)
            setMaxUpdates(1)
        }.build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult.lastLocation == null) {
                    callback(null)
                } else {
                    callback(locationResult.lastLocation)
                }
                fusedLocationProviderClient.removeLocationUpdates(this)
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        ).addOnFailureListener {
            callback(null)
        }
    }
}