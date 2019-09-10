package edu.pes.laresistencia.listchats

import android.content.Context
import android.content.Intent
import com.example.api.laresistencia.R
import edu.pes.laresistencia.chat.ChatView
import edu.pes.laresistencia.injection.component.DaggerListChatsInjector
import edu.pes.laresistencia.injection.modules.AppModule
import edu.pes.laresistencia.injection.modules.NetworkModule
import edu.pes.laresistencia.network.FriendshipAPI
import edu.pes.laresistencia.network.FriendshipResponse
import edu.pes.laresistencia.network.FriendshipStatus
import edu.pes.laresistencia.otherprofile.OtherProfileView
import edu.pes.laresistencia.service.FriendshipService
import javax.inject.Inject

class ListChatsPresenter(val context: Context, private val view: ListChatsView):
        FriendshipService.OnFriendshipRequest
{

    @Inject
    lateinit var friendshipAPI: FriendshipAPI
    private var friendshipService: FriendshipService
    private var listChatsFriends = ArrayList<FriendshipResponse>()
    private var listChatsPending = ArrayList<FriendshipResponse>()
    private var listChatsToAccept = ArrayList<FriendshipResponse>()
    private var listChatsNone = ArrayList<FriendshipResponse>()

    init {
        val injector = DaggerListChatsInjector
                .builder()
                .appModule(AppModule(context))
                .networkModule(NetworkModule)
                .build()
        injector.inject(this)
        friendshipService = FriendshipService(this, friendshipAPI)
    }

    fun fetch()
    {
        friendshipService.getListChats()
    }

    private fun handleResponde(code: Int): Boolean
    {
        when (code)
        {
            in 200..299 -> return true
            900 -> view.showToast(context.resources.getString(R.string.error_internal))
            else -> view.showToast(context.resources.getString(R.string.listchats_error))
        }
        return false
    }

    override fun chatsReceived(code: Int?, list: List<FriendshipResponse>?)
    {
        cleanOriginalLists()
        if (code != null && handleResponde(code))
            if (list != null)
                fillLists(list)
        view.reloadFragmentsViewPager()
    }

    private fun cleanOriginalLists()
    {
        listChatsFriends.clear()
        listChatsPending.clear()
        listChatsToAccept.clear()
        listChatsNone.clear()
    }

    private fun fillLists(data: List<FriendshipResponse>)
    {
        cleanOriginalLists()
        for (i in data)
        {
            when (i.status)
            {
                FriendshipStatus.NONE -> listChatsNone.add(i)
                FriendshipStatus.PENDING -> listChatsPending.add(i)
                FriendshipStatus.TO_ACCEPT -> listChatsToAccept.add(i)
                FriendshipStatus.SETTLED -> listChatsFriends.add(i)
            }
        }
        listChatsPending.addAll(listChatsToAccept)
    }

    fun getFriends(): ArrayList<FriendshipResponse> = listChatsFriends
    fun getPending(): ArrayList<FriendshipResponse> = listChatsPending
    fun getNone(): ArrayList<FriendshipResponse> = listChatsNone

    fun stoping()
    {
        friendshipService.cancel()
    }

    fun goOtherProfile(email: String)
    {
        val intent = Intent()
        intent.putExtra("email", email)
        intent.setClass(context, OtherProfileView::class.java)
        context.startActivity(intent)
    }

    fun requestFriendship(email: String)
    {
        friendshipService.requestFriendship(friendshipAPI, email)
    }

    fun addToFriend(email: String)
    {
        friendshipService.acceptFriendship(friendshipAPI, email)
    }

    fun cancelRequest(email: String)
    {
        friendshipService.removeRequestFriendship(friendshipAPI, email)
    }

    fun removeRequest(email: String)
    {
        friendshipService.removeRequestFriendship(friendshipAPI, email)
    }

    fun goChat(email: String, name: String)
    {
        listChatsFriends.first { it.email == email }.nonReadMessage = true // bad boolean representation, don't touch it
        val intent = Intent()
        intent.putExtra("email", email)
        intent.putExtra("name", name)
        intent.setClass(context, ChatView::class.java)
        context.startActivity(intent)
    }

    override fun getFriendlyStatusCompleted(code: Int?, body: FriendshipResponse?)
    {
        //nothing to do
    }

    override fun requestFriendshipCompleted(code: Int?)
    {
        if (code != null && handleResponde(code))
            friendshipService.getListChats()
    }

    override fun acceptFriendshipCompleted(code: Int?)
    {
        if (code != null && handleResponde(code))
            friendshipService.getListChats()
    }

    override fun removeFriendshipCompleted(code: Int?)
    {
        if (code != null && handleResponde(code))
            friendshipService.getListChats()
    }

}