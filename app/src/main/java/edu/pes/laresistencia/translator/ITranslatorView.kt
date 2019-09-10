package edu.pes.laresistencia.translator


interface ITranslatorView {

    fun setTextTranslated(text: String)
    fun setLanguageFrom(lang: String)
    fun setLanguageTo(lang: String)
    fun swapLanguages()
    fun showDialogLanguages(languages: ArrayList<String>, from: Boolean)
    fun showErrorToast(error: String)
}