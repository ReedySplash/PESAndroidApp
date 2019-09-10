package edu.pes.laresistencia.language

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.example.api.laresistencia.R
import kotlinx.android.synthetic.main.languages_view.*

class LanguageView: AppCompatActivity(), ILanguageView {
    override fun showToast() {
        Toast.makeText(this, getString(R.string.language_changed), Toast.LENGTH_LONG).show()

    }

    private lateinit var presenter: LanguagePresenter

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.languages_view)
        init()
    }

    fun init() {
        presenter = LanguagePresenter(this,this)
        initToolbar()
        initCatalaButton()
        initSpanishButton()
        initEnglishButton()
    }

    private fun initToolbar() {
        setSupportActionBar(app_bar_languages)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun initEnglishButton() {
        english_language.setOnClickListener {
            presenter.setEnglish()
        }
    }

    private fun initSpanishButton() {
        spanish_layout.setOnClickListener {
            presenter.setSpanish()
        }
    }

    private fun initCatalaButton() {
        catala_language.setOnClickListener {
            presenter.setCatala()
        }
    }
}