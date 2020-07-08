package com.prepare.prepareurself.chatbot.ui

import android.app.Activity
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prepare.prepareurself.R
import com.prepare.prepareurself.chatbot.BotModel
import com.prepare.prepareurself.forum.ui.LinksAdapter
import com.prepare.prepareurself.utils.Utility
import kotlinx.android.synthetic.main.chat_bot_bot_reply_layout.view.*
import kotlinx.android.synthetic.main.chat_bot_user_msg_layout.view.*
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import me.saket.bettermovementmethod.BetterLinkMovementMethod.OnLinkClickListener

class ChatBotAdapter(var activity: Activity,var listener:BotListener) : RecyclerView.Adapter<ChatBotAdapter.BaseViewHolder<*>>() {

    private var data = ArrayList<BotModel>()

    interface BotListener{
        fun onLinkClicked(url:String)
    }

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
            var reply = ""
            when {
                item.text.contains("http") || item.text.contains("https") -> {
    //                item.text.replace("\n","\n\n\n")
                    val list = item.text.split("\n")
                    reply = "Here are some links that might help you"
                    displayList(list,reply)
                }
                else -> {
                    reply = item.text
                    itemView.tv_bot_reply.text = reply
                    itemView.links_list.visibility = View.GONE
                }
            }
 //           itemView.tv_bot_reply.movementMethod = BetterLinkMovementMethod.newInstance()
//            Linkify.addLinks(itemView.tv_bot_reply, Linkify.WEB_URLS)
//
//            BetterLinkMovementMethod.linkify(Linkify.ALL, activity)
//                    .setOnLinkClickListener { _, url ->
//                        //listener.onLinkClicked(url)
//                        Utility.redirectUsingCustomTab(activity,url)
//                        return@setOnLinkClickListener true
//                    }
        }

        private fun displayList(list: List<String>, reply: String) {
            itemView.tv_bot_reply.text = reply
            itemView.links_list.visibility = View.VISIBLE
            val adapter = LinksAdapter(activity)
            itemView.links_list.layoutManager = LinearLayoutManager(activity)
            itemView.links_list.adapter = adapter
            adapter.setData(list as ArrayList<String>)
        }
    }


}