package edu.pes.laresistencia.chat

import edu.pes.laresistencia.network.Chat
import okhttp3.Response

interface IChatView {

    fun receivedMessage(message: ChatMessage)

    fun chatOpened()

    fun chatClosed()

    fun failureHappened(throwable: Throwable, response: Response?)

    fun failureReceivingHistory()

    fun history(body: List<Chat>)
}