package edu.pes.laresistencia.profile

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.api.laresistencia.R
import kotlinx.android.synthetic.main.list_aller_dis_item.view.*


class RecyclerAdapter(private val alldiss: ArrayList<String>) : RecyclerView.Adapter<RecyclerAdapter.AllDisHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllDisHolder{
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_aller_dis_item, parent, false)
        return AllDisHolder(v)
    }

    override fun getItemCount() = alldiss.size

    override fun onBindViewHolder(holder: RecyclerAdapter.AllDisHolder, position: Int) {
        holder.bindItems(alldiss[position])
    }


    class AllDisHolder(v: View) : RecyclerView.ViewHolder(v){
        //2
        fun bindItems(alldis: String) {
            itemView.item_disease.text = alldis
        }

    }
}

private fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

