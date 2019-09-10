package edu.pes.laresistencia.otherprofile

import edu.pes.laresistencia.register.UserRequest

interface IOtherProfileView {
    fun showProgressDialog(text: String?)
    fun showError(error: String?)
    fun closeDialog()
    fun initParameters(user: UserRequest?)
    fun initFriendly(status: String)
    fun initButton()
    fun changeFriendlyStatus()
    fun setProfilePictureFromUrl(path: String)
    fun changeFriendlyRemoveStatus()
}