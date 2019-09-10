package edu.pes.laresistencia.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.signature.StringSignature
import com.example.api.laresistencia.R
import edu.pes.laresistencia.injection.modules.NetworkModule
import edu.pes.laresistencia.methods.ImageMethods
import edu.pes.laresistencia.model.User
import kotlinx.android.synthetic.main.comment_item.view.*
import java.util.concurrent.CompletionException
import android.content.DialogInterface
import edu.pes.laresistencia.service.ActivityService


class ListCommentsAdapter(val context: Context, var listComments: List<CommentsRequest>, val activityService: ActivityService, val view: ActivityView) : RecyclerView.Adapter<CommentsViewHolder>() {

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {

        holder.comment.text = listComments[position].comment
        val name = listComments[position].userName + " " + listComments[position].userSurname
        holder.user.text = name
        holder.date.text = listComments[position].creationDate
        setCommentImage(position, holder)
        shouldSetDeleteButton(holder, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        return CommentsViewHolder(LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false))
    }

    override fun getItemCount(): Int {
        return listComments.size
    }

    fun updateData(list: List<CommentsRequest>) {
        listComments = list
    }

    private fun setCommentImage(position: Int, holder: CommentsViewHolder) {
        Glide.with(context)
                .load("${NetworkModule.baseUrl}/user/${listComments[position].userEmail}/photo")
                .asBitmap()
                .centerCrop()
                .signature(StringSignature(System.currentTimeMillis().toString()))
                .into(object: BitmapImageViewTarget(holder.image)
                {
                    override fun setResource(resource: Bitmap?) {
                        if (resource != null)
                        {
                            holder.image.setImageDrawable(ImageMethods
                                    .getRoundedBitmap(context, resource))
                            holder.image.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
                        }
                    }
                })
    }

    private fun shouldSetDeleteButton(holder: CommentsViewHolder, position: Int) {
        if (listComments[position].userEmail == User.email) {
            holder.deleteButton.alpha = 1.toFloat()
            holder.deleteButton.isEnabled = true
            holder.deleteButton.setOnClickListener {
                val alertDialog = AlertDialog.Builder(context)
                alertDialog.setTitle(R.string.activity_delete_comment_title)
                alertDialog.setMessage(R.string.activity_delete_comment_description)

                alertDialog.setPositiveButton(R.string.activity_delete_button, DialogInterface.OnClickListener { dialog, which ->
                    val token = NetworkModule.authenticatorInterceptor.authToken!!
                    activityService.deleteActivityComment(listComments[position].activity_id, listComments[position].id, token)
                    view.updateComments()
                })
                alertDialog.setNegativeButton(R.string.activity_cancel_button, DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

                alertDialog.create()
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert)
                alertDialog.show()
            }
        }
        else {
            holder.deleteButton.alpha = 0.toFloat()
            holder.deleteButton.isEnabled = false
        }
    }

}

class CommentsViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val comment = view.comments_text
    val user = view.comments_user
    val date = view.comments_date
    val image = view.comments_image
    val deleteButton = view.comments_delete_button
}