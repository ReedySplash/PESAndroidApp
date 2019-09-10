package edu.pes.laresistencia.changepassword

import android.content.Context
import com.example.api.laresistencia.R
import edu.pes.laresistencia.injection.component.DaggerChangePasswordInjector
import edu.pes.laresistencia.injection.modules.AppModule
import edu.pes.laresistencia.injection.modules.NetworkModule
import edu.pes.laresistencia.model.User
import edu.pes.laresistencia.network.UserAPI
import edu.pes.laresistencia.service.SettingsService
import javax.inject.Inject

class ChangePasswordPresenter(val context: Context, val view: IChangePasswordView): 
        SettingsService.OnSettingsChange {

    @Inject
    lateinit var userAPI: UserAPI
    private val settingsService: SettingsService
    init {
        val injector = DaggerChangePasswordInjector
                .builder()
                .appModule(AppModule(context))
                .networkModule(NetworkModule)
                .build()
        injector.inject(this)
        settingsService = SettingsService(this, userAPI)
    }

    private fun processFinishedChangePasswordTask(result: Int): Boolean {
       when (result)
       {
           in 200..299 -> return true
           403 ->
               view.showError(context.getString(R.string.password_original_not_equal))
           900 ->
               view.showError(context.getString(R.string.error_internal))
           else -> view.showError(context.getString(R.string.login_server_error))
       }
        view.closeProgressDialog()
        return false
   }

    private fun checkRequiredFields(original: String, new: String, new_repeated: String): Boolean {
        var miss = false

        if (original.isEmpty()) {
            miss = true
            view.showAlert(0)
        }

        if (new.isEmpty()) {
            miss = true
            view.showAlert(1)
        }

        if (new_repeated.isEmpty()) {
            miss = true
            view.showAlert(2)
        }

        if (miss) view.showError(context.getString(R.string.register_missing_parameters))

        else if (!original.isEmpty() && !new.isEmpty() && !new_repeated.isEmpty() && new != new_repeated) {
            miss = true
            view.showError(context.getString(R.string.new_pass_not_equal))
        }
        return miss
    }


    fun changePassword(original: String, new: String, new_repeated: String) {
        if (!checkRequiredFields(original,new,new_repeated)) {
            view.showProgressDialog(context.getString(R.string.change_password_progress))
            settingsService.changePassword(User.email!!, original, new, 
                    NetworkModule.authenticatorInterceptor.authToken!!)

        }
    }

    override fun changePasswordCompleted(code: Int?) {
        if (code != null && processFinishedChangePasswordTask(code)) {
            view.closeProgressDialog()
            view.showPasswordChanged(context.getString(R.string.password_changed))
            view.endView()
        }
    }

    override fun deleteAccountCompleted(code: Int?)
    {
    }

    fun stopping() {
        settingsService.cancel()
        view.closeProgressDialog()
    }
}