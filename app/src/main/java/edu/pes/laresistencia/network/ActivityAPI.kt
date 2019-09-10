package edu.pes.laresistencia.network

import edu.pes.laresistencia.activity.ActivityRequest
import edu.pes.laresistencia.activity.CommentsRequest
import edu.pes.laresistencia.listactivities.ActivityData
import retrofit2.Call
import retrofit2.http.*

data class ActivityCommentsRequest(
        val comment: String
)

interface ActivityAPI {

    @GET()
    fun getActivityById(@Url url: String): Call<ActivityRequest>

    @GET("/activities")
    fun getAllActivities(): Call<List<ActivityData>>

    @POST()
    fun postComment(@Url url: String, @Header("Authorization") authorization: String, @Body comment: ActivityCommentsRequest): Call<Void>

    @GET()
    fun getActivityComments(@Url url: String): Call<List<CommentsRequest>>

    @DELETE()
    fun deleteActivityComment(@Url url: String, @Header("Authorization") authorization: String): Call<Void>

}