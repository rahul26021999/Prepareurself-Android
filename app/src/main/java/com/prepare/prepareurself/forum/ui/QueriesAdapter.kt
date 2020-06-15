package com.prepare.prepareurself.forum.ui

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.prepare.prepareurself.R
import com.prepare.prepareurself.forum.data.QueryModel
import com.prepare.prepareurself.utils.Constants
import com.prepare.prepareurself.utils.Utility
import kotlinx.android.synthetic.main.queries_adapter_layout.view.*

class QueriesAdapter(var context:Context,var listener:QueriesListener) : RecyclerView.Adapter<QueriesAdapter.QueriesViewHolder>(){

    private var data:List<QueryModel>?=null

    interface QueriesListener{
        fun onViewReplies(queryModel: QueryModel)
        fun onDoReply(queryModel: QueryModel)
    }

    fun setData(list: List<QueryModel>){
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
        val q = data?.get(position)
        holder.bindView(q, context)
        holder.itemView.tv_view_replies.setOnClickListener {
            q?.let { it1 -> listener.onViewReplies(it1) }
        }
        holder.itemView.tv_do_reply.setOnClickListener {
            q?.let { it1 -> listener.onDoReply(it1) }
        }
    }

    class QueriesViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bindView(q: QueryModel?, context: Context) {
            itemView.tv_query_question.text = Html.fromHtml(q?.query)
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
        }

    }

}