package edu.pes.laresistencia.listactivities

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import android.widget.Toast
import com.example.api.laresistencia.R
import kotlinx.android.synthetic.main.list_activities.*
import edu.pes.laresistencia.listactivities.ActivityData




class ListActivitiesView: Fragment(), IListActivitiesView, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var presenter: ListActivitiesPresenter
    private lateinit var activity: Activity


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.list_activities, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.title = activity.getString(R.string.list_activities_title)
        init()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId)
        {
            R.id.action_settings -> {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        presenter = ListActivitiesPresenter(activity, this)
        presenter.initActivities()

    }

    private fun initRecyclerView(listActivities: List<ActivityData>?) {
        if (listActivities != null) {
            activities_recyclerView.layoutManager = LinearLayoutManager(context)
            activities_recyclerView.adapter = ListActivitiesAdapter(context!!, listActivities)
            activities_recyclerView.addOnItemClickListener(object : OnItemClickListener {
                override fun onItemClicked(position: Int, view: View) {
                    presenter.activityPressed(position)
                }
            })
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activity = context as (Activity)
    }

    override fun showServerError() {
        Toast.makeText(activity, getString(R.string.list_activities_not_load), Toast.LENGTH_SHORT).show()
    }

    override fun onRefresh() {
        //val listActivities = presenter.initActivities()
        //initRecyclerView(listActivities)
        activitylist_reloadLayout.isRefreshing = false
    }

    private fun initRefreshLayout()
    {
        activitylist_reloadLayout.setOnRefreshListener(this)
        activitylist_reloadLayout.setColorSchemeResources(R.color.colorAccent)
    }

    override fun initActivities(listactivities: List<ActivityData>?) {
        initRecyclerView(listactivities)
        initRefreshLayout()
    }


}