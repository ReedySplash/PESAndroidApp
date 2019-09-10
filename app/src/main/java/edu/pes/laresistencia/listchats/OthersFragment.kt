package edu.pes.laresistencia.listchats

import android.content.Context
import android.content.Intent
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
import edu.pes.laresistencia.otherprofile.OtherProfileView
import kotlinx.android.synthetic.main.itemlistothers.view.*

class OthersFragment: ListChatFragments(), ListChatAdapter.OnButtonClicked
{

    override fun createAdapter(): ListChatAdapter {
        return object : ListChatAdapter(activity, listInfo, this, null)
        {
            override fun getLayoutId(position: Int, item: FriendshipResponse): Int
                    = R.layout.itemlistothers

            override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder
                    = ViewHolder(view)
        }
    }

    override fun imageClicked(item: FriendshipResponse)
    {
        (parentFragment as OnActionClicked).imageClicked(item.email)
    }
    override fun addClicked(item: FriendshipResponse)
    {
        refreshIconVisible(true)
        (parentFragment as OnActionClicked).addButtonClicked(item.email)
        refreshIconVisible(false)
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

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), ListChatAdapter.Binder
    {
        override fun bind(item: FriendshipResponse, pos: Int, delegate: ListChatAdapter.OnButtonClicked,
                          listener: ((FriendshipResponse) -> Unit)?, context: Context)
        {
            useGlide(item.email, context)
            itemView.listchats_photo.setOnClickListener {
                delegate.imageClicked(item)
            }
            itemView.listchats_name.text = item.name
            itemView.listchats_add_button.setOnClickListener {
                delegate.addClicked(item)
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
}