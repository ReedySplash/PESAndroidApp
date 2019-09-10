package edu.pes.laresistencia.profile

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.api.laresistencia.R
import kotlinx.android.synthetic.main.healthprofile_view.*


class HealthProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.healthprofile_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data: Bundle? = arguments
        if (data != null)
        {
            if (data.getString("weight") == null){
                w_tit.visibility = View.INVISIBLE
                h_tit.visibility = View.INVISIBLE
                b_tit.visibility = View.INVISIBLE
                a_tit.visibility = View.INVISIBLE
                d_tit.visibility = View.INVISIBLE
                weight.visibility = View.INVISIBLE
                height.visibility = View.INVISIBLE
                bloodType.visibility = View.INVISIBLE
                line_1.visibility = View.INVISIBLE
                line_2.visibility = View.INVISIBLE
                line_3.visibility = View.INVISIBLE
                line_4.visibility = View.INVISIBLE
            }
            else {
                info_med.visibility = View.INVISIBLE
                weight.text = data.getString("weight") + " kg"
                height.text = data.getString("height") + " cm"
                bloodType.text = data.getString("bloodtype")
                if (data.getStringArrayList("allergies") != null) {
                    val adapter = RecyclerAdapter(data.getStringArrayList("allergies"))
                    allergies_recyclerView.layoutManager = LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false)
                    allergies_recyclerView.adapter = adapter
                }
                if (data.getStringArrayList("diseases") != null) {
                    val adapter = RecyclerAdapter(data.getStringArrayList("diseases"))
                    diseases_recyclerView.layoutManager = LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false)
                    diseases_recyclerView.adapter = adapter
                }
            }

        }
    }


}