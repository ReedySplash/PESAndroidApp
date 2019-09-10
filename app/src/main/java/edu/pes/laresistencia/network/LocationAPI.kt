package edu.pes.laresistencia.network

import edu.pes.laresistencia.location.Location
import retrofit2.Call
import retrofit2.http.GET


interface LocationAPI {

    @GET("/pointsLocs/all")
    fun getLocationPoints(): Call<List<Location>>

}