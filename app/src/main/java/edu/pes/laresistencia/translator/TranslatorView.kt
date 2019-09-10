package edu.pes.laresistencia.translator

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import com.example.api.laresistencia.R
import android.os.Build
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.text.method.LinkMovementMethod
import android.widget.ArrayAdapter
import android.text.style.URLSpan
import android.text.Spannable
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_translator_view.*


class TranslatorView : Fragment(), ITranslatorView
{

    private lateinit var presenter: TranslatorPresenter
    private lateinit var activity: Activity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_translator_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        (activity as AppCompatActivity).supportActionBar?.title = activity.getString(R.string.translator_title)
        presenter = TranslatorPresenter(activity, this)
        init()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun init()
    {
        initSwapButton()
        initTranslateButton()
        initSpinnerFrom()
        initSpinnerTo()
        initReferenceYandex()
    }

    private fun initSwapButton()
    {
        translator_buttonSwap.setOnClickListener {
            presenter.swapButtonPressed()
        }
    }

    private fun initTranslateButton()
    {
        translator_buttonTranslate.setOnClickListener {
            val inputMethodManager: InputMethodManager = activity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity.currentFocus.windowToken, 0)
            presenter.translateButtonPressed(translator_textToTranslate.text.toString(),
                    translator_languageFrom.text.toString(),
                    translator_languageTo.text.toString())
        }
    }

    private fun initSpinnerFrom()
    {
        translator_languageFrom.setOnClickListener {
            presenter.pressedButtonLanguageFrom()
        }
    }

    private fun initSpinnerTo()
    {
         translator_languageTo.setOnClickListener {
             presenter.pressedButtonLanguageTo()
         }
    }

    private fun initReferenceYandex()
    {
        translator_yandex.movementMethod = LinkMovementMethod.getInstance()
        val html: String = "<a href='http://translate.yandex.com/' style=''> Powered by Yandex.Translate </a>"
        if (Build.VERSION.SDK_INT >= 24) translator_yandex.text = Html.fromHtml(html, FROM_HTML_MODE_LEGACY)
        else translator_yandex.text = Html.fromHtml(html)
        removeUnderlines(translator_yandex.text as Spannable)
    }

    override fun setLanguageFrom(lang: String) {
        translator_languageFrom.text = lang
    }

    override fun setLanguageTo(lang: String) {
        translator_languageTo.text = lang
    }

    override fun setTextTranslated(text: String) {
        translator_textTranslated.text = text
    }

    override fun swapLanguages() {
        var aux: String = translator_languageFrom.text.toString()
        translator_languageFrom.text = translator_languageTo.text
        translator_languageTo.text = aux
        aux = translator_textToTranslate.text.toString()
        translator_textToTranslate.setText(translator_textTranslated.text.toString())
        translator_textTranslated.text = aux
    }

    override fun showDialogLanguages(languages: ArrayList<String>, from: Boolean) {
        val adapter = ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, languages)
        val dialogLang: AlertDialog.Builder = AlertDialog.Builder(activity)
        dialogLang.setTitle("Select a language")
        dialogLang.setSingleChoiceItems(adapter, -1, { dialog, item ->
            if (from) presenter.pressedLanguageFrom(adapter.getItem(item))
            else presenter.pressedLanguageTo(adapter.getItem(item))
            dialog.dismiss()// dismiss the alertbox after chose option
        })
        val dlg = dialogLang.create()
        dlg.show()
    }

    override fun showErrorToast(error: String) {
        Toast.makeText(activity, error, Toast.LENGTH_SHORT).show()
    }

    override fun onStop()
    {
        presenter.stoping()
        super.onStop()
    }

    private fun removeUnderlines(p_Text: Spannable) {
        val spans = p_Text.getSpans(0, p_Text.length, URLSpan::class.java)

        for (span in spans) {
            val start = p_Text.getSpanStart(span)
            val end = p_Text.getSpanEnd(span)
            p_Text.removeSpan(span)
            var span2 = URLSpanNoUnderline(span.url)
            p_Text.setSpan(span2, start, end, 0)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activity = context as (Activity)
    }

}
