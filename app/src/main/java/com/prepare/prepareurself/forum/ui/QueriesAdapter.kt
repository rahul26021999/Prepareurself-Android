package com.prepare.prepareurself.forum.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prepare.prepareurself.R
import com.prepare.prepareurself.forum.data.QueryModel
import kotlinx.android.synthetic.main.queries_adapter_layout.view.*

class QueriesAdapter : RecyclerView.Adapter<QueriesAdapter.QueriesViewHolder>(){

    private var data:List<QueryModel>?=null

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
        holder.bindView(q)
    }

    class QueriesViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bindView(q: QueryModel?) {
            itemView.tv_query_question.text = q?.query
        }

    }

}