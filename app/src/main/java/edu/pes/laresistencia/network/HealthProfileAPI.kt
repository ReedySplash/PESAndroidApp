package edu.pes.laresistencia.network

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.*

data class AllANDDisRequest constructor(val email: String, val bloodtype: String, val weight: String, val height: String, val allergies: Array<Any>, var diseases: Array<Any>)

interface HealthProfileAPI {

    @GET("/user/healthProfile/{email}")
    fun getAllANDDis(@Path("email") email: String): Call<AllANDDisRequest>
}