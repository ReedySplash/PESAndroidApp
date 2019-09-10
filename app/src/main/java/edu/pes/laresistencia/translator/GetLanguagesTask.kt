package edu.pes.laresistencia.translator

import android.os.AsyncTask
import edu.pes.laresistencia.service.TranslatorService

class GetLanguagesTask(private var delegate: OnGetLanguages) :
        AsyncTask<Void, Void, Pair<Int?, HashMap<String, String>?>>() {

    interface OnGetLanguages {
        fun getLanguagesCompleted(result: Pair<Int?, HashMap<String, String>?>)
    }

    private val translatorService: TranslatorService = TranslatorService()

    override fun onCancelled()
    {
        //nothing to do view does not exists
    }

    override fun doInBackground(vararg params: Void?): Pair<Int?, HashMap<String, String>?> {
        try
        {
            return translatorService.getSupportedLanguages()
        }
        catch (e: Exception)
        {
            return Pair(900, null)

        }
    }

    override fun onPostExecute(result: Pair<Int?, HashMap<String, String>?>) {
        delegate.getLanguagesCompleted(result)
    }

}