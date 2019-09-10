package edu.pes.laresistencia.listchats

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.api.laresistencia.R
import kotlinx.android.synthetic.main.activity_list_chats.*

class ListChatsView : Fragment(), IListChatsView, SearchView.OnQueryTextListener,
        ListChatFragments.OnActionClicked, ListChatFragments.OnRefreshLists {

    private lateinit var presenter: ListChatsPresenter
    private lateinit var activity: Activity
    private lateinit var viewPager: ListChatsPager
    private var page: Int = 0
    private lateinit var searchView: SearchView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_list_chats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).
                supportActionBar?.
                title = context!!.
                getString(R.string.chat_title)
        (activity as AppCompatActivity).supportActionBar?.elevation = 0f
        presenter = ListChatsPresenter(context as Activity, this)
        initViewPagerAndTabs()
        presenter.fetch()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?)
    {
        menu?.clear()
        inflater?.inflate(R.menu.listchats_toolbar, menu)
        searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.queryHint = activity.resources.getString(R.string.listchats_search)
        searchView.setOnQueryTextListener(this)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(query: String?): Boolean
    {
        //nothing
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean
    {
        viewPager.applyFilter(newText, page)
        return true
    }

    private fun initViewPagerAndTabs()
    {
        viewPager = ListChatsPager(((context as FragmentActivity).
                supportFragmentManager), context)
        viewPager.setFriends(presenter.getFriends())
        viewPager.setPending(presenter.getPending())
        viewPager.setNone(presenter.getNone())
        viewPager.initiateFragments()
        viewPager.hideFragments(childFragmentManager)
        xat_pages.offscreenPageLimit = 2
        xat_pages.adapter = viewPager
        xat_pages.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener()
        {
            override fun onPageSelected(position: Int) {
                if (xat_load.visibility == View.GONE)
                {
                    closeKeyBoard()
                    viewPager.resetData(page)
                }
                page = position
            }
        })
        xat_tabs.setupWithViewPager(xat_pages)
    }

    private fun closeKeyBoard()
    {
        val inputMethodManager: InputMethodManager = activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus.windowToken, 0)
    }

    override fun getPager(): ViewPager {
        return xat_pages
    }

    override fun showToast(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
    }

    override fun reloadLists() {
        presenter.fetch()
    }

    override fun reloadFragmentsViewPager()
    {
        viewPager.showFragments(childFragmentManager)
        viewPager.communicateDataChanged()
        if (xat_load.visibility == View.VISIBLE)
            xat_load.visibility = View.GONE
    }

    override fun imageClicked(email: String)
    {
        presenter.goOtherProfile(email)
    }

    override fun addButtonClicked(email: String)
    {
        presenter.requestFriendship(email)
    }

    override fun acceptButtonClicked(email: String)
    {
        presenter.addToFriend(email)
    }

    override fun denyButtonClicked(email: String)
    {
        presenter.removeRequest(email)
    }

    override fun chatClicked(email: String, name: String)
    {
        presenter.goChat(email, name)
    }

    override fun cancelButtonClicked(email: String)
    {
        presenter.cancelRequest(email)
    }

    override fun onAttach(context: Context)
    {
        super.onAttach(context)
        activity = context as Activity
    }

    override fun onResume() {
        (activity as AppCompatActivity).supportActionBar?.elevation = 0f
        super.onResume()
    }

    override fun onStop() {
        presenter.stoping()
        super.onStop()
    }
}