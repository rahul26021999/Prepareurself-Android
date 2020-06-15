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
import com.prepare.prepareurself.utils.Utility
import kotlinx.android.synthetic.main.queries_adapter_layout.view.*
import kotlinx.android.synthetic.main.replies_adapter_layout.view.*

class RepliesAdapter(var context:Context) : RecyclerView.Adapter<RepliesAdapter.RepliesViewHolder>(){

    private var data:List<QueryModel>?=null

    interface QueriesListener{
        fun onViewReplies(queryModel: QueryModel)
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
        holder.bindView(q, context)
    }

    class RepliesViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bindView(q: QueryModel?, context: Context) {
            itemView.tv_reply_answer.text = Html.fromHtml(q?.reply)
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
        }

    }

}