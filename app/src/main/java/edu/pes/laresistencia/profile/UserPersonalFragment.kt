package edu.pes.laresistencia.profile

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.api.laresistencia.R
import edu.pes.laresistencia.model.User
import kotlinx.android.synthetic.main.fragment_user_personal.*

class UserPersonalFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_user_personal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        profile_frag_name.text = User.name + " " + User.surname
        profile_frag_gender.text = User.gender.toString()
        profile_frag_country.text = User.country
        profile_frag_birthday.text = User.birthDate
        profile_frag_email.text = User.email
    }

}
