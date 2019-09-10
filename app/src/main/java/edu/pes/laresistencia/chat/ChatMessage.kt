package edu.pes.laresistencia.chat

class ChatMessage {

    var type: MessageType? = null
    var content: String? = null
    var sender: String? = null
    var receiver: String? = null
    var date: String? = null

    enum class MessageType {
        CHAT, JOIN, LEAVE
    }
}
