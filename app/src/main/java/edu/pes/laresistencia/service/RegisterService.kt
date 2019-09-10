package edu.pes.laresistencia.service

import edu.pes.laresistencia.network.UserAPI
import edu.pes.laresistencia.register.UserRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterService(private val delegate: OnRegisterCompleted, private val userAPI: UserAPI) {

    interface OnRegisterCompleted {
        fun registerCompleted(code: Int?)
    }

    private var callback: Call<Unit>? = null

    fun register(request: UserRequest) {
        callback = userAPI.register(request)
        callback?.enqueue(object : Callback<Unit>
        {
            override fun onFailure(call: Call<Unit>?, t: Throwable?)
            {
                if (call != null && call.isCanceled)
                {
                    //do nothing view does no exists anymore
                }
                else if (t is Exception)
                    delegate.registerCompleted(900)
                callback = null
            }

            override fun onResponse(call: Call<Unit>?, response: Response<Unit>?)
            {
                callback = null
                delegate.registerCompleted(response?.code())
            }
        })
    }

    fun cancel()
    {
        if (callback != null)
            callback!!.cancel()
    }
}