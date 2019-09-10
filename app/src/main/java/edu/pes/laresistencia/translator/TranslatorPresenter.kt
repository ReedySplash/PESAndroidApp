package edu.pes.laresistencia.translator

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.MenuItem
import com.example.api.laresistencia.R
import edu.pes.laresistencia.location.LocationView

class TranslatorPresenter(private val context: Context, private val view: ITranslatorView):
        GetLanguagesTask.OnGetLanguages, TranslateTask.OnTranslate
{

    private var languagesISO: HashMap<String, String>
    private var languagesNames: ArrayList<String>
    private var translateTask: TranslateTask?
    private var getLanguagesTask: GetLanguagesTask?
    private var from: Boolean? = null

    init {
        languagesISO = HashMap()
        languagesNames = ArrayList()
        translateTask = null
        getLanguagesTask = null
        getLanguagesTask = GetLanguagesTask(this)
        getLanguagesTask?.execute()
    }

    fun swapButtonPressed()
    {
        view.swapLanguages()
    }

    fun translateButtonPressed(textToTranslate: String, langFrom: String, langTo: String)
    {
        if (textToTranslate != "" && translateTask == null)
        {
            translateTask = TranslateTask(this)
            translateTask!!.execute(textToTranslate, languagesISO[langFrom], languagesISO[langTo])
        }
        else
            view.showErrorToast(context.getString(R.string.translator_missingText))
    }

    fun pressedButtonLanguageFrom()
    {
        if (!languagesNames.isEmpty())
            view.showDialogLanguages(languagesNames, true)
        else
        {
            from = true
            getLanguagesTask = GetLanguagesTask(this)
            getLanguagesTask?.execute()
        }
    }

    fun pressedButtonLanguageTo()
    {
        if (!languagesNames.isEmpty())
            view.showDialogLanguages(languagesNames, false)
        else
        {
            from = false
            getLanguagesTask = GetLanguagesTask(this)
            getLanguagesTask?.execute()
        }
    }

    fun pressedLanguageFrom(item: String)
    {
        view.setLanguageFrom(item)
    }

    fun pressedLanguageTo(item: String)
    {
        view.setLanguageTo(item)
    }

    private fun handleResponse(code: Int): Boolean
    {
        when(code)
        {
            200 -> return true
            401 -> view.showErrorToast(context.getString(R.string.translator_invalidkey))
            402 -> view.showErrorToast(context.getString(R.string.translator_blockedkey))
            404 -> view.showErrorToast(context.getString(R.string.translator_limitDaily))
            412 -> view.showErrorToast(context.getString(R.string.translator_textSize))
            422 -> view.showErrorToast(context.getString(R.string.translator_textNoTranslate))
            501 -> view.showErrorToast(context.getString(R.string.translator_direction))
            900 -> view.showErrorToast(context.getString(R.string.error_internal))
        }
        return false
    }

    override fun getLanguagesCompleted(result: Pair<Int?, HashMap<String, String>?>)
    {
        getLanguagesTask = null
        if (result.first != null && handleResponse(result.first!!))
        {
            if (result.second != null)
            {
                languagesISO = result.second!!
                languagesNames = ArrayList(languagesISO.keys)
                languagesNames.sort()
                if (from != null)
                    view.showDialogLanguages(languagesNames, from!!)
            }
        }
    }

    override fun translateCompleted(result: Pair<Int?, String?>)
    {
        translateTask = null
        if (result.first != null && handleResponse(result.first!!))
        {
            if (result.second != null)
                view.setTextTranslated(result.second!!)
        }
    }

    fun stoping() {
        if (translateTask?.status == AsyncTask.Status.RUNNING)
        {
            translateTask?.cancel(true)
        }
        else if (getLanguagesTask?.status == AsyncTask.Status.RUNNING)
        {
            getLanguagesTask?.cancel(true)

        }
    }

}