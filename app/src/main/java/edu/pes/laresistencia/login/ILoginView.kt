package edu.pes.laresistencia.login


interface ILoginView {
    fun showMissingMailError(error: String)
    fun showMissingPasswordError(error: String)
    fun showError(error: String)
    fun endView()
    fun showProgressDialog(text: String)
    fun closeDialog()
}