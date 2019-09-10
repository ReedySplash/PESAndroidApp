package edu.pes.laresistencia.listchats

import android.support.v4.view.ViewPager
import edu.pes.laresistencia.network.FriendshipResponse

interface IListChatsView {
    fun showToast(text: String)

    fun getPager(): ViewPager
    fun reloadFragmentsViewPager()
}