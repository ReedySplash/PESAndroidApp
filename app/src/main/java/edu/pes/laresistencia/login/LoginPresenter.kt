package edu.pes.laresistencia.login

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.AsyncTask
import com.example.api.laresistencia.R
import edu.pes.laresistencia.config.ConfigView
import edu.pes.laresistencia.home.HomeView
import edu.pes.laresistencia.injection.component.DaggerLoginInjector
import edu.pes.laresistencia.injection.modules.AppModule
import edu.pes.laresistencia.injection.modules.NetworkModule
import edu.pes.laresistencia.location.LocationView
import edu.pes.laresistencia.model.User
import edu.pes.laresistencia.network.OAuthAPI
import edu.pes.laresistencia.network.UserAPI
import edu.pes.laresistencia.register.RegisterView
import edu.pes.laresistencia.service.LoginService
import edu.pes.laresistencia.storage.InternalStorage
import edu.pes.laresistencia.methods.NetworkMethods
import edu.pes.laresistencia.network.UserResponse
import edu.pes.laresistencia.register.UserRequest
import javax.inject.Inject



class LoginPresenter(val context: Context, val view: ILoginView) : LoginService.OnLoginCompleted
{
    @Inject
    lateinit var oauthAPI: OAuthAPI
    @Inject
    lateinit var userAPI: UserAPI
    private val loggingService: LoginService
    private var internalStorage: InternalStorage

    private var email: String? = null


    init {
        val injector = DaggerLoginInjector
                .builder()
                .appModule(AppModule(context))
                .networkModule(NetworkModule)
                .build()
        injector.inject(this)
        loggingService = LoginService(this, oauthAPI,userAPI)
        internalStorage = InternalStorage()
    }

    private fun checkRequiredFields(mail_text: String, password_text: String): Boolean {
        var miss = false

        if (mail_text.isEmpty()) {
            miss = true
            view.showMissingMailError(context.getString(R.string.register_email_required))
        }

        if (password_text.isEmpty()) {
            miss = true
            view.showMissingPasswordError(context.getString(R.string.register_password_required))
        }
        return miss
    }

    private fun handleResponse(response: Int): Boolean {
        when (response) {
            in 200..299 -> return true
            in 400..499 -> view.showError(context.getString(R.string.login_incorrect_parameters))
            900 -> view.showError(context.getString(R.string.error_internal))
            else -> view.showError(context.getString(R.string.login_server_error))
        }
        view.closeDialog()
        return false
    }

    override fun loginCompleted(code: Int?)
    {
        if (code != null && handleResponse(code))
        {
            internalStorage
                    .storeToken(NetworkModule
                    .authenticatorInterceptor
                    .authToken!!, context)
            view.closeDialog()
            view.showProgressDialog(context.getString(R.string.infoUser_progress))
            loggingService.getInfoUser("/user/$email", NetworkModule
                    .authenticatorInterceptor
                    .authToken)
        }
    }

    override fun getUserInfoCompleted(response: Pair<Int?, UserRequest?>)
    {
        if (response.first != null && handleResponse(response.first!!))
        {
            if (response.second != null)
            {
                internalStorage.storeUser(context, response.second!!)
                view.closeDialog()
                val intent = Intent()
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.setClass(context, HomeView::class.java)
                context.startActivity(intent)
            }
            else
                view.closeDialog()
        }
    }

    fun loginButtonPressed(mail_text: String, password_text: String) {
        if (!checkRequiredFields(mail_text, password_text)) {
            view.showProgressDialog(context.getString(R.string.login_progress))
            email = mail_text
            loggingService.login(mail_text, password_text)
        }
    }

    fun createButtonPressed() {
        val intent = Intent()
        intent.setClass(context, RegisterView::class.java)
        context.startActivity(intent)
    }

    fun stoping() {
        loggingService.cancel()
        view.closeDialog()
    }
}