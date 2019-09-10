package edu.pes.laresistencia.listchats

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.bumptech.glide.Glide
import com.example.api.laresistencia.R
import edu.pes.laresistencia.methods.NetworkMethods
import edu.pes.laresistencia.network.FriendshipResponse
import kotlinx.android.synthetic.main.listchats_layout.*

abstract class ListChatFragments: Fragment(), SwipeRefreshLayout.OnRefreshListener
{
    interface OnRefreshLists
    {
        fun reloadLists()
    }

    interface OnActionClicked
    {
        fun imageClicked(email: String)
        fun addButtonClicked(email: String)
        fun acceptButtonClicked(email: String)
        fun denyButtonClicked(email: String)
        fun cancelButtonClicked(email: String)
        fun chatClicked(email: String, name: String)
    }

    protected lateinit var activity: Activity
    protected lateinit var adapter: ListChatAdapter
    protected lateinit var listInfo: ArrayList<FriendshipResponse>
    private var copyInfo: ArrayList<FriendshipResponse> = ArrayList()

    abstract fun createAdapter(): ListChatAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.listchats_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        val data: Bundle? = arguments
        if (data != null)
        {
            list_friends.layoutManager = LinearLayoutManager(activity)
            listInfo = data.getParcelableArrayList("infoChats")
            copyInfo.addAll(listInfo)
            setVisibleElements()
            adapter = createAdapter()
            list_friends.adapter = adapter
            list_reloadLayout.isEnabled = false
            list_reloadLayout.setOnRefreshListener(this)
            list_reloadLayout.setColorSchemeResources(R.color.colorAccent)
            setHasOptionsMenu(true)
        }
    }

    override fun onRefresh()
    {
        (parentFragment as OnRefreshLists).reloadLists()
    }


    override fun onAttach(context: Context?)
    {
        super.onAttach(context)
        activity = context as Activity
    }

    private fun setVisibleElements() {
        if (listInfo.isEmpty())
        {
            list_friends.visibility = View.GONE
            list_layoutProblems.visibility = View.VISIBLE
            if (NetworkMethods.isNetworkAvailable(activity))
            {
                Glide.with(activity)
                        .load("")
                        .centerCrop()
                        .error(R.drawable.ic_list)
                        .into(list_imageRecycle)
                list_textRecycle.text = resources.getString(R.string.listchats_noFriends)
            }
            else
            {
                Glide.with(activity)
                        .load("")
                        .centerCrop()
                        .error(R.drawable.ic_wifioff)
                        .into(list_imageRecycle)
                list_textRecycle.text = resources.getString(R.string.listchats_noInternet)
            }
        }
        else
        {
            list_friends.visibility = View.VISIBLE
            list_layoutProblems.visibility = View.GONE
        }
    }

    protected fun refreshIconVisible(refresh: Boolean)
    {
        list_friends.isClickable = !list_friends.isClickable
        list_reloadLayout.isRefreshing = refresh
    }
    fun updateData()
    {
        if (!list_reloadLayout.isEnabled) list_reloadLayout.isEnabled = true
        setVisibleElements()
        copyInfo.clear()
        copyInfo.addAll(listInfo)
        adapter.notifyDataSetChanged()
        list_reloadLayout.isRefreshing = false
    }

    fun applyFilter(newText: String?)
    {
        listInfo.clear()
        if (newText == null || newText.isEmpty())
            listInfo.addAll(copyInfo)
        else
        {
            for (item in copyInfo)
                if (item.name.toLowerCase().contains(newText.toLowerCase()))
                    listInfo.add(item)
        }
        adapter.notifyDataSetChanged()
    }

    fun resetData()
    {
        listInfo.clear()
        listInfo.addAll(copyInfo)
        adapter.notifyDataSetChanged()
    }


}