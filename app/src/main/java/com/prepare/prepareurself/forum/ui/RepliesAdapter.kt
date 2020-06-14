package com.prepare.prepareurself.forum.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prepare.prepareurself.R
import com.prepare.prepareurself.forum.data.QueryModel
import kotlinx.android.synthetic.main.queries_adapter_layout.view.*
import kotlinx.android.synthetic.main.replies_adapter_layout.view.*

class RepliesAdapter() : RecyclerView.Adapter<RepliesAdapter.RepliesViewHolder>(){

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
        holder.bindView(q)
    }

    class RepliesViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bindView(q: QueryModel?) {
            itemView.tv_reply_answer.text = q?.reply
        }

    }

}