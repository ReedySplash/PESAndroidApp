package edu.pes.laresistencia.translator

import android.os.AsyncTask
import edu.pes.laresistencia.service.TranslatorService


class TranslateTask(private var delegate: OnTranslate) : AsyncTask<String, Void, Pair<Int?, String?>>() {

    interface OnTranslate {
        fun translateCompleted(result: Pair<Int?, String?>)
    }

    private val translatorService: TranslatorService = TranslatorService()

    override fun doInBackground(vararg params: String): Pair<Int?, String?> {
        try
        {
            return translatorService.getTranslation(params[0], params[1], params[2])
        }
        catch (e: Exception)
        {
            return Pair(900, null)
        }
    }

    override fun onPostExecute(result: Pair<Int?, String?>) {
        delegate.translateCompleted(result)
    }
}