package com.prepare.prepareurself.forum.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.prepare.prepareurself.R
import com.prepare.prepareurself.forum.data.OpenForumAttachment
import com.prepare.prepareurself.utils.Constants
import com.prepare.prepareurself.utils.Utility
import kotlinx.android.synthetic.main.queries_adapter_layout.view.*
import kotlinx.android.synthetic.main.rv_image_attachment_query_layout.view.*
import kotlinx.android.synthetic.main.rv_image_attachment_query_layout.view.img_query_attachment

class QueryImageAttachmentAdapter(var context: Context, var listener:AttachmentListenet) : RecyclerView.Adapter<QueryImageAttachmentAdapter.ImageAttachmentViewHolder>() {

    private var data : List<OpenForumAttachment>?=null
    private var type = -1

    interface AttachmentListenet{
        fun onImageClicked(attachment: List<String>, position: Int)
    }

    fun setData(list: List<OpenForumAttachment>, t:Int){
        data = list
        type = t
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAttachmentViewHolder {
        return ImageAttachmentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_image_attachment_query_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: ImageAttachmentViewHolder, position: Int) {
        val d = data?.get(position)

        holder.bindView(d, context, type)
        holder.itemView.setOnClickListener {
            d?.let { it1 ->
                val list = ArrayList<String>()
                data?.forEach {
                    it.file?.let { it2 -> list.add(it2) }
                }
                data?.let { it2 -> listener.onImageClicked(list,position) }
            }
        }

    }

    class ImageAttachmentViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        fun bindView(d: OpenForumAttachment?, context:Context, type:Int) {

            if (d?.file!=null && d.file?.isNotEmpty()!!){
                var imageUrl = ""
                if (type==0){
                    imageUrl = "${Constants.QUERYATTACHMENTBASEURL}${d.file}"
                }else if (type == 1){
                    imageUrl = "${Constants.REPLYATTACHMENTBASEURL}${d.file}"
                }

                if (imageUrl.endsWith(".svg")){
                    Utility.loadSVGImage(context, imageUrl, itemView.img_person_queries)
                }else{
                    Glide.with(context)
                            .load(imageUrl)
                            .placeholder(R.drawable.placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .into(itemView.img_query_attachment)
                }
            }

        }

    }
}