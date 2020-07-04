package com.prepare.prepareurself.forum.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prepare.prepareurself.R
import kotlinx.android.synthetic.main.image_attached_rv_layout.view.*

class ImageAttachedAdapter(var listener:AttachmentListener) : RecyclerView.Adapter<ImageAttachedAdapter.AttachmentViewHolder>(){

    private var data: List<String>?=null

    interface AttachmentListener{
        fun onCancelled(position: Int)
        fun onItemClicked(list: List<String>,position: Int)
    }

    fun setData(list: List<String>){
        data = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentViewHolder {
        return AttachmentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.image_attached_rv_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: AttachmentViewHolder, position: Int) {
        val d = data?.get(position)

        holder.bindView(d)
        holder.itemView.cancel_attached_image.setOnClickListener {
            listener.onCancelled(position)
        }
        holder.itemView.tv_image_attached_confrm.setOnClickListener {
            data?.let { it1 -> listener.onItemClicked(it1,position) }
        }

    }

    class AttachmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindView(d: String?) {
            itemView.tv_image_attached_confrm.text = d
        }

    }

}