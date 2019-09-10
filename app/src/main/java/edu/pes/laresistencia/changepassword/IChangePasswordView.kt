package edu.pes.laresistencia.changepassword

interface IChangePasswordView {
    fun showError(string: String?)
    fun showAlert(i: Int)
    fun showProgressDialog(string: String?)
    fun closeProgressDialog()
    fun showPasswordChanged(string: String?)
    fun endView()

}
