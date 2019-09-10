package edu.pes.laresistencia.config

interface IConfigView {
    fun showDeleteDialog()
    fun showError(error: String?)
    fun showProgressDialog(text: String?)
    fun closeProgressDialog()
    fun endView()
}