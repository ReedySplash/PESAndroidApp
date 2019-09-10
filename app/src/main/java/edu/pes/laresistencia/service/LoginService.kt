package edu.pes.laresistencia.service

import edu.pes.laresistencia.injection.modules.NetworkModule
import edu.pes.laresistencia.network.JWT
import edu.pes.laresistencia.network.OAuthAPI
import edu.pes.laresistencia.network.UserAPI
import edu.pes.laresistencia.register.UserRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginService(private val delegate: OnLoginCompleted,
                   private val oAuthAPI: OAuthAPI, private val userAPI: UserAPI) {

    interface OnLoginCompleted {
        fun loginCompleted(code: Int?)
        fun getUserInfoCompleted(response: Pair<Int?, UserRequest?>)
    }

    private var callbackLogin: Call<JWT>? = null
    private var callbackInfo: Call<UserRequest>? = null

    fun login(email: String, password: String)
    {
        callbackLogin = oAuthAPI.login("Basic Y2xpZW50OnBhc3N3b3Jk", email, password,
                "password")
        callbackLogin?.enqueue(object : Callback<JWT>
        {
            override fun onFailure(call: Call<JWT>?, t: Throwable?)
            {
                if (call != null && call.isCanceled)
                {
                    //do nothing view does no exists anymore
                }
                else if (t is Exception)
                {
                    callbackLogin = null
                    delegate.loginCompleted(900)
                }
                callbackLogin = null
            }

            override fun onResponse(call: Call<JWT>?, response: Response<JWT>?)
            {
                if (response?.code() in 200..299)
                {
                    val accessToken = response?.body()?.accessToken
                    NetworkModule.authenticatorInterceptor.authToken = accessToken
                }
                callbackLogin = null
                delegate.loginCompleted(response?.code())
            }
        })
    }

    fun getInfoUser(url: String, token: String?) {
        callbackInfo = userAPI.getInfoUser(url, "bearer $token")
        callbackInfo?.enqueue(object : Callback<UserRequest>
        {
            override fun onFailure(call: Call<UserRequest>?, t: Throwable?)
            {
                if (call != null && call.isCanceled)
                {
                    //do nothing view does no exists anymore
                }
                else if (t is Exception)
                {
                    callbackInfo = null
                    delegate.getUserInfoCompleted(Pair(900, null))
                }
                callbackInfo = null
            }

            override fun onResponse(call: Call<UserRequest>?, response: Response<UserRequest>?)
            {
                delegate.getUserInfoCompleted(Pair(response?.code(), response?.body()))
            }

        })
    }

    fun cancel()
    {
        if (callbackInfo != null)
            callbackInfo?.cancel()
        if (callbackLogin != null)
            callbackLogin?.cancel()

    }
}