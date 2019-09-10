package edu.pes.laresistencia.service

import android.os.StrictMode
import edu.pes.laresistencia.activity.ActivityRequest
import edu.pes.laresistencia.activity.CommentsRequest
import edu.pes.laresistencia.listactivities.ActivityData
import edu.pes.laresistencia.network.ActivityAPI
import edu.pes.laresistencia.network.ActivityCommentsRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ActivityService(private val delegate: OnActivitiesCompleted, private val activityAPI: ActivityAPI) {

    interface OnActivitiesCompleted
    {
        fun activitiesCompleted(code: Int?, allActivities: List<ActivityData>?)
    }

    private var callbackActivitiesCompleted: Call<List<ActivityData>>? = null

    fun getAllActivities() {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        callbackActivitiesCompleted = activityAPI.getAllActivities()

        callbackActivitiesCompleted?.enqueue(object : Callback<List<ActivityData>>
        {
            override fun onFailure(call: Call<List<ActivityData>>?, t: Throwable?) {
                if (call != null && call.isCanceled)
                {
                    //do nothing view does no exists anymore
                }
                else if (t is Exception)
                {
                    callbackActivitiesCompleted = null
                    delegate.activitiesCompleted(900, null)
                }
                callbackActivitiesCompleted = null
            }

            override fun onResponse(call: Call<List<ActivityData>>?, response: Response<List<ActivityData>>?) {
                callbackActivitiesCompleted = null
                delegate.activitiesCompleted(response?.code(), response?.body())
            }
        })

    }

    fun getActivityById(id: String) : Pair<Int, ActivityRequest?> {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val response = activityAPI.getActivityById("/activities/" + id)
                .execute()
        return Pair(response.code(), response.body())
    }

    fun postComment(id_activity: String, comment: String, token: String): Int {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val request = ActivityCommentsRequest(comment = comment)
        val response = activityAPI.postComment("/activities/" + id_activity + "/comments", "bearer " + token, request)
                .execute()
        return response.code()
    }

    fun getActivityComments(id: String): Pair<Int, List<CommentsRequest>?> {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val response = activityAPI.getActivityComments("/activities/" + id + "/comments")
                .execute()
        val a = 3
        return Pair(response.code(), response.body())
    }

    fun deleteActivityComment(activity_id: String, comment_id: String, token: String): Int {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val response = activityAPI.deleteActivityComment("/activities/" + activity_id + "/comments/" + comment_id, token)
                .execute()
        return response.code()
    }

}