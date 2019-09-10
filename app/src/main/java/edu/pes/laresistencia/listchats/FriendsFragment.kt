package edu.pes.laresistencia.listchats

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.signature.StringSignature
import com.example.api.laresistencia.R
import edu.pes.laresistencia.injection.modules.NetworkModule
import edu.pes.laresistencia.methods.ImageMethods
import edu.pes.laresistencia.network.FriendshipResponse
import kotlinx.android.synthetic.main.itemlistfriends.view.*

class FriendsFragment: ListChatFragments(), ListChatAdapter.OnButtonClicked
{

    override fun createAdapter(): ListChatAdapter {
        return object : ListChatAdapter(activity, listInfo, this, {
            (parentFragment as OnActionClicked).chatClicked(it.email, it.name)
            adapter.notifyDataSetChanged()
        })
        {
            override fun getLayoutId(position: Int, item: FriendshipResponse): Int
                    = R.layout.itemlistfriends

            override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder
                    = FriendsFragment.ViewHolder(view)
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
        //nothing to do
    }

    override fun acceptClicked(item: FriendshipResponse)
    {
        //nothing to do
    }

    override fun cancelClicked(item: FriendshipResponse)
    {
        //nothing to do
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), ListChatAdapter.Binder
    {
        override fun bind(item: FriendshipResponse, pos: Int, delegate: ListChatAdapter.OnButtonClicked,
                          listener: ((FriendshipResponse) -> Unit)?, context: Context)
        {
            useGlide(item.email, context)
            itemView.listchats_photo.setOnClickListener {
                delegate.imageClicked(item)
            }
            itemView.listchats_name.text = item.name
            itemView.listchats_message.text = item.lastMessage
            itemView.pending_message.visibility =
                    if (!item.nonReadMessage) ImageView.VISIBLE else ImageView.INVISIBLE
            itemView.listchats_card.setOnClickListener {
                listener?.invoke(item)
            }
        }

        private fun useGlide(email: String, context: Context)
        {
            Glide.with(context)
                    .load("${NetworkModule.baseUrl}/user/$email/photo")
                    .asBitmap()
                    .centerCrop()
                    .signature(StringSignature(System.currentTimeMillis().toString()))
                    .into(object : BitmapImageViewTarget(itemView.listchats_photo) {
                        override fun setResource(resource: Bitmap?) {
                            if (resource != null) {
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
}