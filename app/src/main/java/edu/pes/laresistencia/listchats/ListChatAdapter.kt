package edu.pes.laresistencia.listchats

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.pes.laresistencia.network.FriendshipResponse

abstract class ListChatAdapter(private val context: Context,
                               private var items: ArrayList<FriendshipResponse>,
                               private val delegate: OnButtonClicked,
                               private val listener: ((FriendshipResponse) -> Unit)?):
        RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    interface OnButtonClicked
    {
        fun imageClicked(item: FriendshipResponse)
        fun addClicked(item: FriendshipResponse)
        fun denyClicked(item: FriendshipResponse)
        fun acceptClicked(item: FriendshipResponse)
        fun cancelClicked(item: FriendshipResponse)
    }

    interface Binder
    {
        fun bind(item: FriendshipResponse, pos: Int, delegate: OnButtonClicked,
                 listener: ((FriendshipResponse) -> Unit)?, context: Context)
    }

    abstract fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder
    abstract fun getLayoutId(position: Int, item: FriendshipResponse): Int

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as Binder).bind(items[position], position, delegate, listener, context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return getViewHolder(LayoutInflater.from(context)
                .inflate(viewType, parent, false), viewType)
    }

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = getLayoutId(position, items[position])

}