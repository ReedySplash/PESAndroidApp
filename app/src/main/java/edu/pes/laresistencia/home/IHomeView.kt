package edu.pes.laresistencia.home

interface IHomeView {
    fun closeDrawable()
    fun initHeader(name: String, email: String, url: String)
    fun removeChecked()
    fun setProfilePicture(url: String)
    fun setHeightToolbar()
}