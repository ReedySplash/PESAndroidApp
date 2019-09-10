package edu.pes.laresistencia.activity
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.api.laresistencia.R
import kotlinx.android.synthetic.main.activity_description.*


class ActivityDescriptionFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.activity_description, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val activityView = activity as ActivityView
        val desc = activityView.getDescription()
        if (desc != null)
            description_tab.text = desc
    }

}