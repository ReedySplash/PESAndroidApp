package edu.pes.laresistencia.network

import edu.pes.laresistencia.changepassword.ChangePasswordReq
import edu.pes.laresistencia.profile.ChangePhotoReq
import edu.pes.laresistencia.register.UserRequest
import retrofit2.Call
import retrofit2.http.*

data class UserResponse constructor(val name: String, val email: String)

interface UserAPI {
    @GET("/user")
    fun getUsers(): Call<List<UserResponse>>

    @POST("user/register")
    fun register(@Body registerRequest: UserRequest): Call<Unit>

    @GET()
    fun getInfoUser(@Url url: String, @Header("Authorization") authorization: String) : Call<UserRequest>

    @DELETE()
    fun deleteUser(@Url url: String, @Header("Authorization") authorization: String): Call<Void>

    @PUT()
    fun updatePhoto(@Body photo: ChangePhotoReq, @Url url: String, @Header("Authorization") authorization: String): Call<Void>

    @PUT()
    fun updatePassword(@Body password: ChangePasswordReq, @Url url: String, @Header("Authorization") authorization: String): Call<Void>

}