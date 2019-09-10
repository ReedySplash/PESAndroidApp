package edu.pes.laresistencia.chat

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import edu.pes.laresistencia.injection.component.DaggerChatInjector
import edu.pes.laresistencia.injection.modules.AppModule
import edu.pes.laresistencia.injection.modules.NetworkModule
import edu.pes.laresistencia.model.User
import edu.pes.laresistencia.network.Chat
import edu.pes.laresistencia.network.ChatAPI
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import retrofit2.Call
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ChatPresenter(activity: Activity, private val chatView: IChatView, private val receiver: String) {
    @Inject
    lateinit var okHttpClient: OkHttpClient
    @Inject
    lateinit var gson: Gson
    @Inject
    lateinit var chatApi: ChatAPI

    private val webSocket: WebSocket

    init {
        val injector = DaggerChatInjector
                .builder()
                .appModule(AppModule(activity as Context))
                .networkModule(NetworkModule)
                .build()
        injector.inject(this)

        chatApi.history(receiver).enqueue(object : Callback<List<Chat>> {
            override fun onFailure(call: Call<List<Chat>>?, t: Throwable) {
                chatView.failureReceivingHistory()
            }

            override fun onResponse(call: Call<List<Chat>>?, response: retrofit2.Response<List<Chat>>) {
                if (response.isSuccessful) {
                    chatView.history(response.body()!!)
                } else {
                    chatView.failureReceivingHistory()
                }
            }
        })

        val client = OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build()

        val request = Request.Builder()
                .url("${NetworkModule.baseUrl}/ws")
                .build()

        webSocket = client.newWebSocket(request, WebSocketListener())

        client.dispatcher().executorService().shutdown()
    }

    fun closeWebSocket() {
        webSocket.close(1000, "LEAVING")
    }

    inner class WebSocketListener : okhttp3.WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            Log.i("WebSocketListener", "onOpen")
            chatView.chatOpened()
            val chatMessage = ChatMessage()
            chatMessage.sender = User.email
            chatMessage.receiver = receiver
            chatMessage.type = ChatMessage.MessageType.JOIN
            webSocket.send(gson.toJson(chatMessage))
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            Log.i("WebSocketListener", "onFailure $response")
            chatView.failureHappened(t, response)
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
            Log.i("WebSocketListener", "onClosing")
            chatView.chatClosed()
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            Log.i("WebSocketListener", "onMessage ($text)")
            chatView.receivedMessage(gson.fromJson(text, ChatMessage::class.java))
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            Log.i("WebSocketListener", "onClosed")
            chatView.chatClosed()
        }
    }

    fun sendMessage(text: String) {
        val dateFormat = SimpleDateFormat("HH:mm")
        if (text != "") {
            val message = ChatMessage()
            message.content = text
            message.sender = User.email
            message.receiver = receiver
            message.date = dateFormat.format(Date())
            message.type = ChatMessage.MessageType.CHAT
            webSocket.send(gson.toJson(message))
            chatView.receivedMessage(message)
        }
    }
}