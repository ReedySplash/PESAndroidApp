package edu.pes.laresistencia.about

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.example.api.laresistencia.R
import com.example.api.laresistencia.R.id.settings_toolbar
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.change_password_view.*
import kotlinx.android.synthetic.main.configuration_view.*

class AboutView  : AppCompatActivity(), IAboutView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        initToolbar()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initToolbar() {
        setSupportActionBar(about_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

}