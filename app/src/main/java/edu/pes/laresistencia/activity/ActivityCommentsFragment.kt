package edu.pes.laresistencia.activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.api.laresistencia.R


class ActivityCommentsFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.activity_comments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val activityView = activity as ActivityView
        activityView.initComments()
        activityView.initCommentButton()
        activityView.initReloadComments()
    }

}