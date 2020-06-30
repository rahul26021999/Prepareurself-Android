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
import com.prepare.prepareurself.utils.Constants
import com.prepare.prepareurself.utils.Utility
import kotlinx.android.synthetic.main.queries_adapter_layout.view.*

class QueriesAdapter(var context:Context,var listener:QueriesListener) : RecyclerView.Adapter<QueriesAdapter.QueriesViewHolder>(),QueryImageAttachmentAdapter.AttachmentListenet{

    private var data:ArrayList<QueryModel>?=null

    interface QueriesListener{
        fun onViewReplies(queryModel: QueryModel)
        fun onImageClicked(attachment: List<OpenForumAttachment>, position: Int)
        fun onBottomReached()
    }

    fun addData(queryModel: QueryModel){
        data?.add(0,queryModel)
        notifyItemInserted(0)
    }

    fun setData(list: ArrayList<QueryModel>){
        data = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QueriesViewHolder {
        return QueriesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.queries_adapter_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: QueriesViewHolder, position: Int) {
        if (position == data?.size?.minus(1)){
            listener.onBottomReached()
        }
        val q = data?.get(position)
        holder.bindView(q, context, this)
        holder.itemView.setOnClickListener {
            q?.let { it1 -> listener.onViewReplies(it1) }
        }
    }

    class QueriesViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bindView(q: QueryModel?, context: Context, listener:QueryImageAttachmentAdapter.AttachmentListenet) {
            val query = Html.fromHtml(q?.query).trim()
            itemView.tv_query_question.text = query
            if (q?.user?.profile_image!=null && q.user?.profile_image?.isNotEmpty()!!){
                val imagUrl = q.user?.profile_image
                if (imagUrl?.endsWith(".svg")!!){
                    Utility.loadSVGImage(context, imagUrl, itemView.img_person_queries)
                }else{
                    Glide.with(context)
                            .load(imagUrl)
                            .placeholder(R.drawable.person_placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .into(itemView.img_person_queries)
                }
            }
            itemView.tv_name_qury_user.text = "@${q?.user?.username}"
            val adapter = QueryImageAttachmentAdapter(context, listener)
            itemView.rv_attachment_query.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            itemView.rv_attachment_query.adapter = adapter
            q?.open_forum_attachment?.let { adapter.setData(it,0) }
        }

    }

    override fun onImageClicked(attachment: List<OpenForumAttachment>, position: Int) {
        listener.onImageClicked(attachment, position)
    }
}