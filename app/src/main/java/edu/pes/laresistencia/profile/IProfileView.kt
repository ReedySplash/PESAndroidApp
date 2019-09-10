package edu.pes.laresistencia.profile

import android.net.Uri

interface IProfileView {
    fun initHeader(name: String, email: String)
    fun setProfilePictureFromUrl(path: String)
    fun showToast(message: String)
    fun showProgressDialog(text: String)
    fun closeDialog()
}