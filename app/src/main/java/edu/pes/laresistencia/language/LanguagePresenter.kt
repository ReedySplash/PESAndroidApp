package edu.pes.laresistencia.language

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import java.util.*
import edu.pes.laresistencia.home.HomeView
import android.app.Application



@SuppressLint("Registered")
class LanguagePresenter(var context: Context, val view: ILanguageView): Application() {
    @SuppressLint("ObsoleteSdkInt")


    fun setCatala() {
        super.attachBaseContext(updateBaseContextLocale(context, "ca"))
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.setClass(context, HomeView::class.java)
        context.startActivity(intent)
    }

    fun setSpanish() {
        super.attachBaseContext(updateBaseContextLocale(context, "es"))
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.setClass(context, HomeView::class.java)
        context.startActivity(intent)
    }

    fun setEnglish() {
        super.attachBaseContext(updateBaseContextLocale(context, "en"))
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.setClass(context, HomeView::class.java)
        context.startActivity(intent)
    }

    private fun updateBaseContextLocale(context: Context, language: String): Context? {
        val locale = Locale(language)
        Locale.setDefault(locale)
        return updateResourcesLocaleLegacy(context, locale)
    }

    private fun updateResourcesLocaleLegacy(context: Context, locale: Locale): Context {
        val resources = context.resources
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }
}