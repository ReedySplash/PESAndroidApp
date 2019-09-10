package edu.pes.laresistencia.register

import android.graphics.Picture
import android.net.Uri
import android.support.v4.graphics.drawable.RoundedBitmapDrawable

interface IRegisterView {

    fun hideFirstPassword()
    fun showFirstPassword()
    fun hideSecondPassword()
    fun showSecondPassword()

    fun setProfilePictureDefault(id: Int)
    fun setProfilePicture(path: Uri)

    fun showMissingNameError()
    fun showMissingSurnameError()
    fun showMissingCountryError()
    fun showMissingEmailError()
    fun showMissingPassword1Error()
    fun showMissingPassword2Error()

    fun showEmailNotValid()
    fun showCountryNotValid()
    fun showDateDialog()
    fun showDateNotValid()

    fun setBirthdayDate(day: Int, month: Int, year: Int)

    fun showError(error: String)
    fun endView()
    fun closeDialog()
    fun showProgressDialog(text: String)


}