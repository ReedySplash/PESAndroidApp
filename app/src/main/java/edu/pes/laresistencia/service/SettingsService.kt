package edu.pes.laresistencia.service

import edu.pes.laresistencia.network.UserAPI
import edu.pes.laresistencia.changepassword.ChangePasswordReq
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingsService(private val delegate: OnSettingsChange, private val userAPI: UserAPI)
{

    interface OnSettingsChange
    {
        fun changePasswordCompleted(code: Int?)
        fun deleteAccountCompleted(code: Int?)
    }

    private var callbackPasswordChange: Call<Void>? = null
    private var callbackDeleteAccount: Call<Void>? = null

    fun deleteUser(email: String, token: String?) {
        callbackDeleteAccount = userAPI.deleteUser("/user/$email", "bearer " + token!!)
        callbackDeleteAccount?.enqueue(object: Callback<Void> {
            override fun onFailure(call: Call<Void>?, t: Throwable?) {
                if (call != null && call.isCanceled)
                {
                    //do nothing
                }
                else if (t is Exception)
                {
                    callbackDeleteAccount = null
                    delegate.deleteAccountCompleted(900)
                }
                callbackDeleteAccount = null
            }

            override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                callbackDeleteAccount = null
                delegate.deleteAccountCompleted(response?.code())
            }
        } )
    }

    fun changePassword(email: String, original: String, new: String, token: String) {
        val password = ChangePasswordReq(original,new)
        callbackPasswordChange = userAPI.updatePassword(password, "/user/$email/change-password", "bearer " + token)
        callbackPasswordChange?.enqueue(object: Callback<Void> {
            override fun onFailure(call: Call<Void>?, t: Throwable?) {
                if (call != null && call.isCanceled)
                {
                    //do nothing
                }
                else if (t is Exception)
                {
                    callbackPasswordChange = null
                    delegate.deleteAccountCompleted(900)
                }
                callbackPasswordChange = null
            }

            override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                callbackPasswordChange = null
                delegate.changePasswordCompleted(response?.code())
            }
        })
    }

    fun cancel()
    {
        if (callbackDeleteAccount != null)
            callbackDeleteAccount?.cancel()
        if (callbackPasswordChange != null)
            callbackPasswordChange?.cancel()
    }
}