package com.prepare.prepareurself.chatbot.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prepare.prepareurself.R
import com.prepare.prepareurself.chatbot.BotModel
import kotlinx.android.synthetic.main.chat_bot_bot_reply_layout.view.*
import kotlinx.android.synthetic.main.chat_bot_user_msg_layout.view.*
import java.lang.IllegalArgumentException

class ChatBotAdapter : RecyclerView.Adapter<ChatBotAdapter.BaseViewHolder<*>>() {

    private var data = ArrayList<BotModel>()

    fun setData(list: ArrayList<BotModel>){
        data = list
        notifyDataSetChanged()
    }

    fun addData(botModel: BotModel):Int{
        data.add(botModel)
        notifyItemInserted(data.size-1)
        return data.size -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when(viewType){
            1 -> MessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.chat_bot_user_msg_layout, parent,false))
            2 -> BotReplyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.chat_bot_bot_reply_layout, parent,false))
            else -> throw IllegalArgumentException("Invalid View Type")
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = data[position]
        when(holder){
            is MessageViewHolder -> holder.bind(element)
            is BotReplyViewHolder -> holder.bind(element)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(data[position].viewType){
            1-> 1
            2-> 2
            else -> throw IllegalArgumentException("Illegal type of data at $position")
        }
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    inner class MessageViewHolder(itemView: View): BaseViewHolder<BotModel>(itemView){
        override fun bind(item: BotModel) {
            itemView.tv_user_msg.text = item.text
        }
    }

    inner class BotReplyViewHolder(itemView: View): BaseViewHolder<BotModel>(itemView){
        override fun bind(item: BotModel) {
            itemView.tv_bot_reply.text = item.text
        }
    }


}