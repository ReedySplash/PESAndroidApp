package edu.pes.laresistencia.listchats

import android.content.Context
import android.os.Bundle
import android.support.v4.app.*
import com.example.api.laresistencia.R
import edu.pes.laresistencia.network.FriendshipResponse

class ListChatsPager(fm: FragmentManager, context: Context?): FragmentStatePagerAdapter(fm)
{
    private var listTitles: ArrayList<String> = arrayListOf(context?.resources?.getString(R.string.Friends).toString(),
            context?.resources?.getString(R.string.Pending).toString(),
            context?.resources?.getString(R.string.Others).toString())
    private var listFragment: ArrayList<Fragment?> = arrayListOf(null, null, null)
    private lateinit var listFriends: ArrayList<FriendshipResponse>
    private lateinit var listPending: ArrayList<FriendshipResponse>
    private lateinit var listNone: ArrayList<FriendshipResponse>

    override fun getItem(position: Int): Fragment
    {
        return listFragment[position]!!
    }

    override fun getCount(): Int
    {
        return listTitles.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return listTitles[position]
    }

    fun initiateFragments()
    {
        var bundle = Bundle()
        bundle.putParcelableArrayList("infoChats", listFriends)
        val fragment = FriendsFragment()
        fragment.arguments = bundle
        listFragment[0] = fragment

        bundle = Bundle()
        bundle.putParcelableArrayList("infoChats", listPending)
        val fragment2 = PendingFragment()
        fragment2.arguments = bundle
        listFragment[1] = fragment2

        bundle = Bundle()
        bundle.putParcelableArrayList("infoChats", listNone)
        val fragment3 = OthersFragment()
        fragment3.arguments = bundle
        listFragment[2] = fragment3
    }

    fun hideFragments(childFragmentManager: FragmentManager)
    {
        val ft = childFragmentManager.beginTransaction()
        ft.hide(listFragment[0])
        ft.hide(listFragment[1])
        ft.hide(listFragment[2])
        ft.commit()
    }

    fun showFragments(childFragmentManager: FragmentManager)
    {
        val ft = childFragmentManager.beginTransaction()
        ft.show(listFragment[0])
        ft.show(listFragment[1])
        ft.show(listFragment[2])
        ft.commit()
    }

    fun setFriends(friends: ArrayList<FriendshipResponse>)
    {
        listFriends = friends
    }

    fun setPending(pending: ArrayList<FriendshipResponse>)
    {
        listPending = pending
    }

    fun setNone(none: ArrayList<FriendshipResponse>)
    {
        listNone = none
    }

    fun communicateDataChanged()
    {
        var f: ListChatFragments? = null
        if (listFragment[0] != null)
            f = (listFragment[0] as FriendsFragment)
        if (f?.view != null) f.updateData()

        if (listFragment[1] != null)
            f = (listFragment[1] as PendingFragment)
        if (f?.view != null) f.updateData()

        if (listFragment[2] != null)
            f = (listFragment[2] as OthersFragment)
        if (f?.view != null) f.updateData()
    }

    fun applyFilter(newText: String?, page: Int)
    {
        var f: ListChatFragments? = null
        if (listFragment[page] != null)
        {
            when (page)
            {
                0 -> f = (listFragment[page] as FriendsFragment)
                1 -> f = (listFragment[page] as PendingFragment)
                2 -> f = (listFragment[page] as OthersFragment)
            }
        }
        if (f?.view != null) f.applyFilter(newText)
    }

    fun resetData(page: Int)
    {
        var f: ListChatFragments? = null
        if (listFragment[page] != null)
        {
            when (page)
            {
                0 -> f = (listFragment[page] as FriendsFragment)
                1 -> f = (listFragment[page] as PendingFragment)
                2 -> f = (listFragment[page] as OthersFragment)
            }
        }
        if (f?.view != null) f.resetData()
    }
}