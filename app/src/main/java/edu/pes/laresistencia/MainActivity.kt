package edu.pes.laresistencia

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.api.laresistencia.R
import edu.pes.laresistencia.activity.ActivityView
import edu.pes.laresistencia.home.HomeView
import edu.pes.laresistencia.injection.modules.NetworkModule
import edu.pes.laresistencia.login.LoginView
import edu.pes.laresistencia.storage.InternalStorage

class MainActivity : AppCompatActivity() {

    private val internalStorage: InternalStorage

    init {
        internalStorage = InternalStorage()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        //startActivity(Intent(this, ActivityView::class.java))

        var token = internalStorage.getToken(this)
        if (token != null) {
            NetworkModule.authenticatorInterceptor.authToken = token
            startActivity(Intent(this, HomeView::class.java))
        }
        else
            startActivity(Intent(this, LoginView::class.java))
        finish()

    }
}

