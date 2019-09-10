package edu.pes.laresistencia.service

import android.os.StrictMode
import edu.pes.laresistencia.network.AllANDDisRequest
import edu.pes.laresistencia.network.HealthProfileAPI

class HealthProfileService(private val healthProfileAPI: HealthProfileAPI) {

    fun getAllANDDis(email: String): AllANDDisRequest? {

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val healthProfile = healthProfileAPI.getAllANDDis(email)
                .execute()

        return healthProfile.body()
    }

}