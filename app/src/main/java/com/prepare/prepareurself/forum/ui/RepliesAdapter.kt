package com.prepare.prepareurself.forum.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.Html
import android.util.Log
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
import kotlinx.android.synthetic.main.activity_chat_bot.*
import kotlinx.android.synthetic.main.layout_topbar.*
import kotlinx.android.synthetic.main.queries_adapter_layout.view.*
import kotlinx.android.synthetic.main.replies_adapter_layout.view.*

class RepliesAdapter(var context:Context, var listener:RepliesListener) : RecyclerView.Adapter<RepliesAdapter.RepliesViewHolder>(),QueryImageAttachmentAdapter.AttachmentListenet{

    private var data:List<QueryModel>?=null
    private var gradColor = ""

    interface RepliesListener{
        fun onImageClicked(attachment: List<String>, position: Int)
        fun onClapped(i: Int,position: Int, queryModel: QueryModel)
        fun onBottomReached()
    }

    fun setData(list: List<QueryModel>, color:String){
        data = list
        gradColor = color
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepliesViewHolder {
        return RepliesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.replies_adapter_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: RepliesViewHolder, position: Int) {
        if (position == data?.size?.minus(1)){
            listener.onBottomReached()
        }
        val q = data?.get(position)
        holder.bindView(q, context, this, gradColor)
        holder.itemView.tv_doclap_reply.setOnClickListener {
            if (q?.clap == 0){
               listener.onClapped(0,position, q)
            }else if (q?.clap == 1){
                listener.onClapped(1,position, q)
            }
        }
    }

    override fun onImageClickedQuery(attachment: List<String>, position: Int) {
        listener.onImageClicked(attachment, position)
    }

    class RepliesViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bindView(q: QueryModel?, context: Context, listener: QueryImageAttachmentAdapter.AttachmentListenet, gradColor:String) {
            val query = Html.fromHtml(q?.reply).trim()
            if (query.isEmpty()){
                itemView.tv_reply_answer.visibility = View.GONE
            }else{
                itemView.tv_reply_answer.visibility = View.VISIBLE
                itemView.tv_reply_answer.text = query
            }
            val firstName = "${q?.user?.first_name}"
            val lastName = q?.user?.last_name
            var name = ""
            name = if (lastName!=null){
                "$firstName $lastName"
            }else{
                firstName
            }
            itemView.tv_name_reply_user.text = name
            itemView.tv_user_email.text = "@${q?.user?.email}"
            if (q?.user?.profile_image!=null && q.user?.profile_image?.isNotEmpty()!!){
                itemView.rel_reply_img_placeholder.visibility = View.GONE
                itemView.img_person_replies.visibility = View.VISIBLE
                val imagUrl = q.user?.profile_image
                if (imagUrl?.endsWith(".svg")!!){
                    Utility.loadSVGImage(context, imagUrl, itemView.img_person_replies)
                }else{
                    Glide.with(context)
                            .load(imagUrl)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .into(itemView.img_person_replies)
                }
            }else{
                if (gradColor != "") {
                    itemView.rel_reply_img_placeholder.visibility = View.VISIBLE
                    itemView.img_person_replies.visibility = View.GONE
                    val list = gradColor.split(",".toRegex()).toTypedArray()
                    Log.i("Colors", gradColor + list.size)
                    val colors = IntArray(list.size)
                    for (i in list.indices) {
                        colors[i] = Color.parseColor(list[i])
                    }
                    val myGradBg = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors)
                    myGradBg.cornerRadii = floatArrayOf(90f, 90f, 90f, 90f, 90f, 90f, 90f, 90f)
                    itemView.rel_reply_img_placeholder.background = myGradBg
                    var text = ""
                    text = if (lastName!=null){
                        "${firstName[0]}${lastName[0]}"
                    }else{
                        "${firstName[0]}"
                    }

                    itemView.tv_reply_img_placeholder.text = text
                }

            }

            if (q?.clap == 1){
                itemView.tv_doclap_reply.setImageDrawable(context.resources.getDrawable(R.drawable.ic_clapped))
            }else{
                itemView.tv_doclap_reply.setImageDrawable(context.resources.getDrawable(R.drawable.ic_clapping))
            }
            var clapsText= ""
            val claps = q?.total_claps
            if (claps != null) {
                clapsText = if (claps==1){
                    "$claps clap"
                }else{
                    "$claps claps"
                }
            }
            itemView.tv_total_claps_reply.text = clapsText
            val adapter = QueryImageAttachmentAdapter(context, listener)
            itemView.rv_attachment_reply.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
            itemView.rv_attachment_reply.adapter = adapter
            q?.open_forum_attachment?.let { adapter.setData(it, 1) }
        }

    }

}