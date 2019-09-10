package edu.pes.laresistencia.chat

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.api.laresistencia.R
import edu.pes.laresistencia.model.User
import kotlinx.android.synthetic.main.message_received.view.*
import kotlinx.android.synthetic.main.message_sended.view.*
import java.util.*

class ChatAdapter(private val context: Context, private var items: ArrayList<ChatMessage>):
        RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
    {
        if (items[position].sender == User.email)
            (holder as ViewHolderSender).bind(items[position])
        else
            (holder as ViewHolderReceive).bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        if (items[viewType].sender == User.email)
            return ViewHolderSender(LayoutInflater
                    .from(context)
                    .inflate(R.layout.message_sended, parent, false))
        else
            return ViewHolderReceive(LayoutInflater
                    .from(context)
                    .inflate(R.layout.message_received, parent, false))
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position

    class ViewHolderSender(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        fun bind(item: ChatMessage)
        {
            itemView.xat_messageSend.text = item.content
            itemView.xat_timeSend.text = item.date
        }
    }

    class ViewHolderReceive(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        fun bind(item: ChatMessage)
        {
            itemView.xat_messageReceived.text = item.content
            itemView.xat_timeReceived.text = item.date
        }
    }
}