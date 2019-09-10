package edu.pes.laresistencia.listactivities

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.signature.StringSignature
import com.example.api.laresistencia.R
import edu.pes.laresistencia.injection.modules.NetworkModule
import edu.pes.laresistencia.methods.ImageMethods
import kotlinx.android.synthetic.main.list_activities_item.view.*


class ListActivitiesAdapter(val context: Context, val listActivities: List<ActivityData>) : RecyclerView.Adapter<ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title?.text = listActivities[position].title
        //holder?.image.setImageResource(R.drawable.bcn)
        holder.date.text = listActivities[position].date
        setActivityImage(listActivities[position].id, holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_activities_item, parent, false))
    }

    override fun getItemCount(): Int {
        return listActivities.size
    }

    private fun setActivityImage(id: String, holder: ViewHolder) {
        val url = "${NetworkModule.baseUrl}/activities/" + id + "/image"
        val glideUrl: GlideUrl = GlideUrl(url, LazyHeaders.Builder()
                .addHeader("Authorization", "bearer " + NetworkModule.authenticatorInterceptor.authToken).build())

        Glide.with(context)
                .load(glideUrl)
                .centerCrop()
                .into(holder.image)

    }

}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val title = view.listactivities_text
    val image = view.listactivities_image
    val date = view.listactivities_date
}

interface OnItemClickListener {
    fun onItemClicked(position:Int, view: View)
}


fun RecyclerView.addOnItemClickListener(onClickListener: OnItemClickListener) {
    this.addOnChildAttachStateChangeListener(object: RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewDetachedFromWindow(view: View?) {
            view?.setOnClickListener(null)
        }

        override fun onChildViewAttachedToWindow(view: View?) {
            view?.setOnClickListener({
                val holder = getChildViewHolder(view)
                onClickListener.onItemClicked(holder.adapterPosition, view)
            })
        }
    })
}