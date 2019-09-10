package edu.pes.laresistencia.home

import android.content.Context
import android.content.Intent
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.Menu
import android.view.MenuItem
import com.example.api.laresistencia.R
import edu.pes.laresistencia.about.AboutView
import edu.pes.laresistencia.config.ConfigView
import edu.pes.laresistencia.injection.modules.NetworkModule
import edu.pes.laresistencia.listactivities.ListActivitiesView
import edu.pes.laresistencia.listchats.ListChatsView
import edu.pes.laresistencia.location.LocationView
import edu.pes.laresistencia.login.LoginView
import edu.pes.laresistencia.model.User
import edu.pes.laresistencia.profile.ProfileView
import edu.pes.laresistencia.register.Gender
import edu.pes.laresistencia.register.UserRequest
import edu.pes.laresistencia.storage.InternalStorage
import edu.pes.laresistencia.translator.TranslatorView

class HomePresenter(private val context: Context, private val view: IHomeView):
        NavigationView.OnNavigationItemSelectedListener {

    private var internalStorage: InternalStorage = InternalStorage()
    private lateinit var actualFragment: Fragment

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_location -> {
                if (!item.isChecked) {
                    view.setHeightToolbar()
                    item.isChecked = true
                    actualFragment = LocationView()
                    (context as FragmentActivity).supportFragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                            .replace(R.id.home_frameFragment, actualFragment)
                            .commit()
                }
            }
            R.id.nav_chat -> {
                if (!item.isChecked) {
                    view.setHeightToolbar()
                    actualFragment = ListChatsView()
                    item.isChecked = true
                    (context as FragmentActivity).supportFragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                            .replace(R.id.home_frameFragment, actualFragment)
                            .commit()
                }
            }
            R.id.nav_translator -> {

                if (!item.isChecked) {
                    view.setHeightToolbar()
                    item.isChecked = true
                    actualFragment = TranslatorView()
                    (context as FragmentActivity).supportFragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                            .replace(R.id.home_frameFragment, actualFragment)
                            .commit()
                }
            }
            R.id.nav_activities -> {
                if (!item.isChecked) {
                    view.setHeightToolbar()
                    item.isChecked = true
                    (context as FragmentActivity).supportFragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                            .replace(R.id.home_frameFragment, ListActivitiesView() as Fragment)
                            .commit()
                }
            }
            R.id.nav_profile -> {
                if (!item.isChecked) {

                    profileClicked(item)
                }
            }
            R.id.nav_settings -> {
                view.setHeightToolbar()
                settingsClicked()
            }
            R.id.nav_about -> {
                view.setHeightToolbar()
                aboutClicked()
            }
        }
        view.closeDrawable()
        return true
    }

    fun getInfoUser()
    {
        User.setData(internalStorage.getUser(context)!!)
        view.initHeader(User.name + " " + User.surname, User.email!!,
                "${NetworkModule.baseUrl}/user/${User.email}/photo")
    }

    fun profileClicked(item: MenuItem) {
        item.isChecked = true
        view.setHeightToolbar()
        actualFragment = ProfileView()
        (context as FragmentActivity).supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                .replace(R.id.home_frameFragment, actualFragment)
                .commit()
        view.closeDrawable()
    }

    fun settingsClicked()
    {
        val intent = Intent()
        intent.setClass(context, ConfigView::class.java)
        context.startActivity(intent)
    }

    fun aboutClicked() {
        val intent = Intent()
        intent.setClass(context, AboutView::class.java)
        context.startActivity(intent)
    }


    fun logout() {
        internalStorage.deleteUser(context)
        internalStorage.deleteToken(context)
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.setClass(context, LoginView::class.java)
        context.startActivity(intent)
    }

    private fun saveNewProfile()
    {
        internalStorage.deleteUser(context)
        val userRequest: UserRequest =
                UserRequest(
                        name = User.name!!,
                        surname = User.surname!!,
                        email = User.email!!,
                        birthDate = User.birthDate!!,
                        country = User.country!!,
                        gender = Gender.valueOf(User.gender.toString()),
                        password = "",
                        photo = ""

                )
        internalStorage.storeUser(context, userRequest)
    }

    fun reloadPicture()
    {
        saveNewProfile()
        view.setProfilePicture("${NetworkModule.baseUrl}/user/${User.email}/photo")
    }
}