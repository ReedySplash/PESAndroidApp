package edu.pes.laresistencia.service

import edu.pes.laresistencia.network.FriendshipAPI
import edu.pes.laresistencia.network.FriendshipResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendshipService(private val delegate: OnFriendshipRequest, private val friendshipAPI: FriendshipAPI)
{
    interface OnFriendshipRequest
    {
        fun getFriendlyStatusCompleted (code: Int?, body: FriendshipResponse?)
        fun requestFriendshipCompleted(code: Int?)
        fun acceptFriendshipCompleted(code: Int?)
        fun chatsReceived(code: Int?, list: List<FriendshipResponse>?)
        fun removeFriendshipCompleted(code: Int?)
    }

    private var callbackLists: Call<List<FriendshipResponse>>? = null
    private var callbackFriendlySatus: Call<FriendshipResponse>? = null
    private var callbackFriendlyRequestorAccept: Call<Void>? = null
    private var callbackRequest: Call<Void>? = null
    private var callbackAccept: Call<Void>? = null
    private var callbackRemove: Call<Void>? = null

    fun getFriendly(email: String) {
        callbackFriendlySatus = friendshipAPI.status("/friendship/$email/status")
        callbackFriendlySatus?.enqueue(object: Callback<FriendshipResponse> {
            override fun onFailure(call: Call<FriendshipResponse>?, t: Throwable?) {
                if (call != null && call.isCanceled)
                {
                    //do nothing
                }
                else if (t is Exception)
                {
                    callbackFriendlySatus = null
                    delegate.getFriendlyStatusCompleted(900, null)
                }
                callbackFriendlySatus = null
            }

            override fun onResponse(call: Call<FriendshipResponse>?, response: Response<FriendshipResponse>?) {
                callbackFriendlySatus = null
                delegate.getFriendlyStatusCompleted(response?.code(), response?.body())
            }
        })
    }

    fun requestFriendship(friendshipAPI: FriendshipAPI, email: String)
    {
        callbackRequest = friendshipAPI.request("/friendship/$email/request")
        callbackRequest?.enqueue(object: Callback<Void> {
            override fun onFailure(call: Call<Void>?, t: Throwable?)
            {
                if (call != null && call.isCanceled)
                {
                    //do nothing
                }
                else if (t is Exception)
                {
                    callbackRequest = null
                    delegate.requestFriendshipCompleted(900)
                }
                callbackRequest = null
            }

            override fun onResponse(call: Call<Void>?, response: Response<Void>?)
            {
                callbackRequest = null
                delegate.requestFriendshipCompleted(response?.code())
            }

        })
    }

    fun acceptFriendship(friendshipAPI: FriendshipAPI, email: String)
    {
        callbackAccept = friendshipAPI.settle("/friendship/$email/settle")
        callbackAccept?.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>?, t: Throwable?)
            {
                if (call != null && call.isCanceled)
                {
                    //do nothing
                }
                else if (t is Exception)
                {
                    callbackAccept = null
                    delegate.acceptFriendshipCompleted(900)
                }
                callbackAccept = null

            }

            override fun onResponse(call: Call<Void>?, response: Response<Void>?)
            {
                callbackAccept = null
                delegate.acceptFriendshipCompleted(response?.code())
            }

        })
    }

    fun getListChats()
    {
        callbackLists = friendshipAPI.friendships()
        callbackLists?.enqueue(object : Callback<List<FriendshipResponse>>
        {
            override fun onFailure(call: Call<List<FriendshipResponse>>?, t: Throwable?)
            {
                if (call != null && call.isCanceled)
                {
                    //do nothing view does no exists anymore
                }
                else if (t is Exception)
                {
                    callbackLists = null
                    delegate.chatsReceived(900, null)
                }
                callbackLists = null
            }

            override fun onResponse(call: Call<List<FriendshipResponse>>?,
                                    response: Response<List<FriendshipResponse>>?)
            {
                callbackLists = null
                delegate.chatsReceived(response?.code(), response?.body())
            }

        })
    }

    fun removeRequestFriendship(friendshipAPI: FriendshipAPI, email: String) {
        callbackRemove = friendshipAPI.delete("/friendship/$email")
        callbackRemove?.enqueue(object: Callback<Void>{
            override fun onFailure(call: Call<Void>?, t: Throwable?)
            {
                if (call != null && call.isCanceled)
                {
                    //do nothing view does no exists anymore
                }
                else if (t is Exception)
                {
                    callbackRemove = null
                    delegate.removeFriendshipCompleted(900)
                }
                callbackRemove = null
            }

            override fun onResponse(call: Call<Void>?, response: Response<Void>?)
            {
                callbackRemove = null
                delegate.removeFriendshipCompleted(response?.code())
            }
        })
    }

    fun cancel()
    {
        if (callbackFriendlyRequestorAccept != null)
            callbackFriendlyRequestorAccept?.cancel()
        if (callbackFriendlySatus != null)
            callbackFriendlySatus?.cancel()
        if (callbackAccept != null)
            callbackAccept?.cancel()
        if (callbackRequest != null)
            callbackRequest?.cancel()
        if (callbackLists != null)
            callbackLists?.cancel()
        if (callbackRemove != null)
            callbackRemove?.cancel()
    }
}