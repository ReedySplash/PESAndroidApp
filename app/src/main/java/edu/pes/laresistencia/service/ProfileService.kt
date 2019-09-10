package edu.pes.laresistencia.service

import edu.pes.laresistencia.network.UserAPI
import edu.pes.laresistencia.profile.ChangePhotoReq
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileService (private val delegate: OnChangeProfileCompleted, private val userAPI: UserAPI) {

    interface OnChangeProfileCompleted
    {
        fun changePhotoCompleted(code: Int?)
    }

    private var callbackPhotoChange: Call<Void>? = null

    fun changePhoto(token: String?, photo: String, email: String) {

        callbackPhotoChange = userAPI.updatePhoto(ChangePhotoReq(photo),
                "/user/$email/change-photo",  "bearer " + token!!)
        callbackPhotoChange?.enqueue(object : Callback<Void>
        {
            override fun onFailure(call: Call<Void>?, t: Throwable?)
            {
                if (call != null && call.isCanceled)
                {
                    //do nothing view does no exists anymore
                }
                else if (t is Exception)
                {
                    callbackPhotoChange = null
                    delegate.changePhotoCompleted(900)
                }
                callbackPhotoChange = null
            }

            override fun onResponse(call: Call<Void>?, response: Response<Void>?)
            {
                callbackPhotoChange = null
                delegate.changePhotoCompleted(response?.code())
            }

        })
    }

    fun cancel() {
        if (callbackPhotoChange != null)
            callbackPhotoChange?.cancel()
    }
}