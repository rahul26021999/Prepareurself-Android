package com.prepare.prepareurself.forum.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prepare.prepareurself.R
import com.prepare.prepareurself.utils.Utility
import kotlinx.android.synthetic.main.bot_list_layout.view.*

class LinksAdapter(var context:Context):RecyclerView.Adapter<LinksAdapter.LinksViewHolder>(){

    private var data:ArrayList<String>?=null

    fun setData(list: ArrayList<String>){
        data = list
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinksViewHolder {
        return LinksViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bot_list_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: LinksViewHolder, position: Int) {
        holder.bindView(data?.get(position))
        holder.itemView.setOnClickListener {
            data?.get(position)?.let { it1 ->
                Utility.redirectUsingCustomTab(context,it1)
            }
        }
    }

    class LinksViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)  {
        fun bindView(data: String?) {
            itemView.tv_list_url.text = data
        }

    }

}