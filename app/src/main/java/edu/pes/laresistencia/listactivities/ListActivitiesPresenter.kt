package edu.pes.laresistencia.listactivities

import android.content.Context
import android.content.Intent
import com.example.api.laresistencia.R
import edu.pes.laresistencia.activity.ActivityRequest
import edu.pes.laresistencia.activity.ActivityView
import edu.pes.laresistencia.injection.component.DaggerListActivitiesInjector
import edu.pes.laresistencia.injection.modules.AppModule
import edu.pes.laresistencia.injection.modules.NetworkModule
import edu.pes.laresistencia.network.ActivityAPI
import edu.pes.laresistencia.service.ActivityService
import javax.inject.Inject


class ListActivitiesPresenter(val context: Context, val view: IListActivitiesView): ActivityService.OnActivitiesCompleted {

    @Inject
    lateinit var activityAPI: ActivityAPI

    private val activityService: ActivityService
    var listActivities: List<ActivityData>? = null

    init {
        val injector = DaggerListActivitiesInjector
                .builder()
                .appModule(AppModule(context))
                .networkModule(NetworkModule)
                .build()
        injector.inject(this)
        activityService = ActivityService(this, activityAPI)
    }

    private fun handleResponse(response: Pair<Int?, List<ActivityData>?>) {
        when(response.first) {

            200 -> {
                if (response.second != null)
                    listActivities = response.second
                else
                    view.showServerError()
            }
            else -> view.showServerError()
        }
    }

    override fun activitiesCompleted(code: Int?, allActivities: List<ActivityData>?) {
        handleResponse(Pair(code, allActivities))
        view.initActivities(listActivities)
    }

    fun initActivities() {
        activityService.getAllActivities()
    }

    fun activityPressed(position: Int) {
        val intent = Intent()
        intent.setClass(context, ActivityView::class.java)
        if (listActivities != null) {
            intent.putExtra("id", listActivities!![position].id)
            context.startActivity(intent)
        }
    }

}