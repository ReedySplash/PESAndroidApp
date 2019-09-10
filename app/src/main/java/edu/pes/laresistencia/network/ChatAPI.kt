package edu.pes.laresistencia.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ChatAPI {
    @GET("/chat/{email}")
    fun history(@Path("email") email: String): Call<List<Chat>>
}

class Chat(
        val sender: String,
        val receiver: String,
        val message: String,
        val date: String
)