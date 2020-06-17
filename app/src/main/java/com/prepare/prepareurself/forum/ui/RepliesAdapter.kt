package com.prepare.prepareurself.forum.ui

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.prepare.prepareurself.R
import com.prepare.prepareurself.forum.data.OpenForumAttachment
import com.prepare.prepareurself.forum.data.QueryModel
import com.prepare.prepareurself.utils.Utility
import kotlinx.android.synthetic.main.queries_adapter_layout.view.*
import kotlinx.android.synthetic.main.replies_adapter_layout.view.*

class RepliesAdapter(var context:Context, var listener:RepliesListener) : RecyclerView.Adapter<RepliesAdapter.RepliesViewHolder>(),QueryImageAttachmentAdapter.AttachmentListenet{

    private var data:List<QueryModel>?=null

    interface RepliesListener{
        fun onImageClicked(attachment: OpenForumAttachment)
        fun onClapped(i: Int,position: Int, queryModel: QueryModel)
    }

    fun setData(list: List<QueryModel>){
        data = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepliesViewHolder {
        return RepliesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.replies_adapter_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: RepliesViewHolder, position: Int) {
        val q = data?.get(position)
        holder.bindView(q, context, this)
        holder.itemView.tv_doclap_reply.setOnClickListener {
            if (q?.clap == 0){
               listener.onClapped(0,position, q)
            }else if (q?.clap == 1){
                listener.onClapped(1,position, q)
            }
        }
    }

    override fun onImageClicked(attachment: OpenForumAttachment) {
        listener.onImageClicked(attachment)
    }

    class RepliesViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bindView(q: QueryModel?, context: Context, listener: QueryImageAttachmentAdapter.AttachmentListenet) {
            val query = Html.fromHtml(q?.reply).trim()
            itemView.tv_reply_answer.text = query
            if (q?.user?.profile_image!=null && q.user?.profile_image?.isNotEmpty()!!){
                val imagUrl = q.user?.profile_image
                if (imagUrl?.endsWith(".svg")!!){
                    Utility.loadSVGImage(context, imagUrl, itemView.img_person_replies)
                }else{
                    Glide.with(context)
                            .load(imagUrl)
                            .placeholder(R.drawable.person_placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .into(itemView.img_person_replies)
                }
            }
            itemView.tv_name_reply_user.text = "@${q?.user?.username}"
            if (q?.clap == 1){
                itemView.tv_doclap_reply.text = "Clapped"
                itemView.tv_doclap_reply.setTextColor(context.resources.getColor(R.color.colorPrimaryDark))
            }else{
                itemView.tv_doclap_reply.text = "Clap"
                itemView.tv_doclap_reply.setTextColor(context.resources.getColor(R.color.dark_grey))
            }
            itemView.tv_total_claps_reply.text = "${q?.total_claps} Claps"
            val adapter = QueryImageAttachmentAdapter(context, listener)
            itemView.rv_attachment_reply.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
            itemView.rv_attachment_reply.adapter = adapter
            q?.open_forum_attachment?.let { adapter.setData(it, 1) }
        }

    }

}