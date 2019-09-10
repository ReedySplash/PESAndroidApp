package edu.pes.laresistencia.activity
import android.content.Context
import java.util.*
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.util.Log
import edu.pes.laresistencia.injection.modules.AppModule
import edu.pes.laresistencia.injection.modules.NetworkModule
import edu.pes.laresistencia.injection.component.DaggerActivityInjector
import edu.pes.laresistencia.listactivities.ActivityData
import edu.pes.laresistencia.network.ActivityAPI
import edu.pes.laresistencia.service.ActivityService
import javax.inject.Inject


class ActivityPresenter(val context: Context, val view: IActivityView): ActivityService.OnActivitiesCompleted {

    @Inject
    lateinit var activityAPI: ActivityAPI

    private val activityService: ActivityService
    private var activity: ActivityRequest
    private var listComments: List<CommentsRequest>? = null

    init {
        val injector = DaggerActivityInjector
                .builder()
                .appModule(AppModule(context))
                .networkModule(NetworkModule)
                .build()
        injector.inject(this)
        activityService = ActivityService(this, activityAPI)
        activity = ActivityRequest(
                title = "",
                date = "",
                description = "",
                id = "",
                from = "",
                to = "",
                location = ""
        )
    }

    private fun handleResponse(response: Pair<Int, ActivityRequest?>) {
        when (response.first) {
            in 200..299 -> {
                if (response.second != null) {
                    activity = response.second!!
                    view.showActivity(activity)
                }
                else
                    view.showServerError()
            }
            else -> view.showServerError()
        }
    }

    override fun activitiesCompleted(code: Int?, allActivities: List<ActivityData>?) {
        //Not called
    }


    fun initActivity(id: String?) {
        if (id != null) {
            val response = activityService.getActivityById(id)
            handleResponse(response)
        }
        else
            view.showServerError()
    }

    private fun handleFirstCommentResponse(response: Pair<Int, List<CommentsRequest>?>): Boolean {
        when (response.first) {
            in 200..299 -> {
                if (response.second != null) {
                    listComments = response.second
                    return true
                }
                else
                    return false
            }
        }
        return false
    }

    fun initComments(): List<CommentsRequest>? {
        val response = activityService.getActivityComments(activity.id)
        if (handleFirstCommentResponse(response))
            return listComments
        return null
    }

    private fun parseDateHour(date: String, time: String): ArrayList<Int> {
        val parsed = ArrayList<Int>()
        val month = date.substringBefore("/")
        val year = date.substringAfterLast("/")
        val aux = date.substringBefore("/" + year)
        val day = aux.substringAfter(month + "/")
        val hour = time.substringBefore(":")
        val minute = time.substringAfter(":")
        parsed.add(0, year.toInt())
        parsed.add(1, month.toInt() - 1)
        parsed.add(2, day.toInt())
        parsed.add(3, hour.toInt())
        parsed.add(4, minute.toInt())
        return parsed
    }

    private fun getTime(date: String, hour: String): Calendar {
        val time = Calendar.getInstance()
        time.clear()
        val parsed = parseDateHour(date, hour)
        time.set(parsed[0], parsed[1], parsed[2], parsed[3], parsed[4])
        return time
    }

    private fun createIntent(beginTime: Calendar, endTime: Calendar) {
        val intent = Intent(Intent.ACTION_EDIT)
        intent.type = "vnd.android.cursor.item/event"
        intent.putExtra("beginTime", beginTime.timeInMillis)
        intent.putExtra("endTime", endTime.timeInMillis)
        intent.putExtra("title", activity.title)
        intent.putExtra("eventLocation", activity.location)
        startActivity(context, intent, null)
    }

    fun addCalendarButtonPressed() {
        val beginTime = getTime(activity.date, activity.from)
        val endTime = getTime(activity.date, activity.to)
        createIntent(beginTime, endTime)
    }

    fun getDescription(): String? {
        return activity.description
    }

    private fun handleCommentResponse(response: Pair<Int, List<CommentsRequest>?>) {
        when (response.first) {
            in 200..299 -> {
                if (response.second != null) {
                    listComments = response.second
                    view.reloadRecyclerView(listComments!!)
                }
            }
            else -> view.showGetCommentsError()
        }
    }

    private fun reloadCommentsList() {
        val response = activityService.getActivityComments(activity.id)
        handleCommentResponse(response)
    }

    private fun handlePostCommentResponse(response: Int) {
        when (response) {
            in 200..299 -> {
                reloadCommentsList()
                view.resetCommentEditText()
            }
            else -> view.showPostCommentError()
        }
    }

    fun commentButtonPressed(comment: String) {
        if (comment.isNotBlank()) {
            val token = NetworkModule.authenticatorInterceptor.authToken!!
            val response = activityService.postComment(activity.id, comment, token)
            handlePostCommentResponse(response)
        }
    }

    fun getActivityService(): ActivityService {
        return activityService
    }

    fun optionsButtonPressed() {

    }

    fun updateComments() {
        reloadCommentsList()
    }

}