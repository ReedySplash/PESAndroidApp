package edu.pes.laresistencia.config

import android.content.Context
import android.content.Intent
import com.example.api.laresistencia.R
import edu.pes.laresistencia.changepassword.ChangePasswordView
import edu.pes.laresistencia.language.LanguageView
import edu.pes.laresistencia.injection.component.DaggerConfigInjector
import edu.pes.laresistencia.injection.modules.AppModule
import edu.pes.laresistencia.injection.modules.NetworkModule
import edu.pes.laresistencia.login.LoginView
import edu.pes.laresistencia.model.User
import edu.pes.laresistencia.network.UserAPI
import edu.pes.laresistencia.service.SettingsService
import edu.pes.laresistencia.storage.InternalStorage
import javax.inject.Inject

class ConfigPresenter(val context: Context, val view: IConfigView): SettingsService.OnSettingsChange{

    @Inject
    lateinit var userAPI: UserAPI
    private val settingsService: SettingsService
    private var internalStorage: InternalStorage
    init {
        val injector = DaggerConfigInjector
                .builder()
                .appModule(AppModule(context))
                .networkModule(NetworkModule)
                .build()
        injector.inject(this)
        settingsService = SettingsService(this, userAPI)
        internalStorage = InternalStorage()
    }

    private fun processFinishedDeleteAccountTask(result: Int): Boolean {
        when (result)
        {
            in 200..299 ->
            {
                return true
            }
            900 ->
                view.showError(context.getString(R.string.error_internal))
            else ->
                    view.showError(context.getString(R.string.login_server_error))
        }
        view.closeProgressDialog()
        return false
    }

    fun deleteAccount() {
        view.showProgressDialog(context.getString(R.string.delete_account))
        settingsService.deleteUser(User.email!!, NetworkModule.authenticatorInterceptor.authToken)
    }

    fun deleteButtonPressed() {
        view.showDeleteDialog()
    }

    fun changePasswordButtonPressed() {
        val intent = Intent()
        intent.setClass(context, ChangePasswordView::class.java)
        context.startActivity(intent)
    }


    fun changeIdiomButtonPressed() {
        val intent = Intent()
        intent.setClass(context, LanguageView::class.java)
        context.startActivity(intent)
    }

    override fun changePasswordCompleted(code: Int?)
    {
        //nothing to do
    }

    override fun deleteAccountCompleted(code: Int?) {
        if (code != null && processFinishedDeleteAccountTask(code)) {
            view.closeProgressDialog()
            internalStorage.deleteUser(context)
            internalStorage.deleteToken(context)
            val intent = Intent()
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.setClass(context, LoginView::class.java)
            context.startActivity(intent)
        }
    }

    fun stopping() {
        settingsService.cancel()
        view.closeProgressDialog()
    }

}