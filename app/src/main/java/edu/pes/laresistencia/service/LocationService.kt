package edu.pes.laresistencia.service

import android.os.StrictMode
import edu.pes.laresistencia.location.Location
import edu.pes.laresistencia.network.LocationAPI


class LocationService(private val locationAPI: LocationAPI) {

    fun getAllPoints(): List<Location>? {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val response = locationAPI.getLocationPoints()
                .execute()
        return response.body()
    }

}