package edu.pes.laresistencia.listchats

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AnimationUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.signature.StringSignature
import com.example.api.laresistencia.R
import edu.pes.laresistencia.injection.modules.NetworkModule
import edu.pes.laresistencia.methods.ImageMethods
import edu.pes.laresistencia.network.FriendshipResponse
import edu.pes.laresistencia.network.FriendshipStatus
import kotlinx.android.synthetic.main.itemlistpending.view.*
import kotlinx.android.synthetic.main.itemlisttoaccept.view.*

class PendingFragment: ListChatFragments(), ListChatAdapter.OnButtonClicked
{

    override fun createAdapter(): ListChatAdapter {
        return object : ListChatAdapter(activity, listInfo, this, null)
        {
            override fun getLayoutId(position: Int, item: FriendshipResponse): Int
            {
                when (item.status)
                {
                    FriendshipStatus.PENDING ->  return R.layout.itemlistpending
                    FriendshipStatus.TO_ACCEPT -> return R.layout.itemlisttoaccept
                    else -> return R.layout.itemlistpending
                }
            }

            override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder
            {
                when (viewType)
                {
                    R.layout.itemlisttoaccept -> return PendingFragment.ViewHolderToAccept(view)
                    R.layout.itemlistpending -> return PendingFragment.ViewHolderPending(view)
                    else -> return PendingFragment.ViewHolderPending(view)
                }
            }
        }
    }

    override fun imageClicked(item: FriendshipResponse)
    {
        (parentFragment as OnActionClicked).imageClicked(item.email)
    }
    override fun addClicked(item: FriendshipResponse)
    {
        //nothing to do
    }

    override fun denyClicked(item: FriendshipResponse)
    {
        refreshIconVisible(true)
        (parentFragment as OnActionClicked).denyButtonClicked(item.email)
        refreshIconVisible(false)
    }

    override fun acceptClicked(item: FriendshipResponse)
    {
        refreshIconVisible(true)
        (parentFragment as OnActionClicked).acceptButtonClicked(item.email)
        refreshIconVisible(false)
    }

    override fun cancelClicked(item: FriendshipResponse)
    {
        refreshIconVisible(true)
        (parentFragment as OnActionClicked).cancelButtonClicked(item.email)
        refreshIconVisible(false)
    }

    class ViewHolderToAccept(itemView: View) : RecyclerView.ViewHolder(itemView), ListChatAdapter.Binder
    {
        override fun bind(item: FriendshipResponse, pos: Int, delegate: ListChatAdapter.OnButtonClicked,
                          listener: ((FriendshipResponse) -> Unit)?, context: Context)
        {
            useGlide(item.email, context)
            itemView.listchats_photo.setOnClickListener {
                delegate.imageClicked(item)
            }
            itemView.listchats_name.text = item.name
            itemView.listchats_accept_button.setOnClickListener {
                delegate.acceptClicked(item)
            }
            itemView.listchats_deny_button.setOnClickListener {
                delegate.denyClicked(item)
            }
        }

        private fun useGlide(email: String, context: Context)
        {
            Glide.with(context)
                    .load("${NetworkModule.baseUrl}/user/$email/photo")
                    .asBitmap()
                    .centerCrop()
                    .signature(StringSignature(System.currentTimeMillis().toString()))
                    .into(object: BitmapImageViewTarget(itemView.listchats_photo)
                    {
                        override fun setResource(resource: Bitmap?) {
                            if (resource != null)
                            {
                                itemView.listchats_photo.setImageDrawable(ImageMethods
                                        .getRoundedBitmap(context, resource))
                                itemView.listchats_photo
                                        .startAnimation(AnimationUtils
                                                .loadAnimation(context, R.anim.fade_in))
                            }
                        }
                    })
        }
    }

    class ViewHolderPending(itemView: View): RecyclerView.ViewHolder(itemView), ListChatAdapter.Binder {

        override fun bind(item: FriendshipResponse, pos: Int, delegate: ListChatAdapter.OnButtonClicked,
                          listener: ((FriendshipResponse) -> Unit)?, context: Context)
        {
            useGlide(item.email, context)
            itemView.listchats_photoAccept.setOnClickListener {
                delegate.imageClicked(item)
            }
            itemView.listchats_nameAccept.text = item.name
            itemView.listchats_cancel_button.setOnClickListener {
                delegate.cancelClicked(item)
            }
        }

        private fun useGlide(email: String, context: Context)
        {
            Glide.with(context)
                    .load("${NetworkModule.baseUrl}/user/$email/photo")
                    .asBitmap()
                    .centerCrop()
                    .signature(StringSignature(System.currentTimeMillis().toString()))
                    .into(object: BitmapImageViewTarget(itemView.listchats_photoAccept)
                    {
                        override fun setResource(resource: Bitmap?) {
                            if (resource != null)
                            {
                                itemView.listchats_photoAccept.setImageDrawable(ImageMethods
                                        .getRoundedBitmap(context, resource))
                                itemView.listchats_photoAccept
                                        .startAnimation(AnimationUtils
                                                .loadAnimation(context, R.anim.fade_in))
                            }
                        }
                    })
        }
    }
}