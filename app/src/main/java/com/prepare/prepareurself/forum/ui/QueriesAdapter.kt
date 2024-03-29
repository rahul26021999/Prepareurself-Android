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
import com.prepare.prepareurself.utils.Constants
import com.prepare.prepareurself.utils.Utility
import kotlinx.android.synthetic.main.queries_adapter_layout.view.*
import kotlinx.android.synthetic.main.replies_adapter_layout.view.*

class QueriesAdapter(var context:Context,var listener:QueriesListener) : RecyclerView.Adapter<QueriesAdapter.QueriesViewHolder>(),QueryImageAttachmentAdapter.AttachmentListenet{

    private var data:ArrayList<QueryModel>?=null
    private var gradColor = ""

    interface QueriesListener{
        fun onViewReplies(queryModel: QueryModel)
        fun onImageClicked(attachment: List<String>, position: Int)
        fun onBottomReached()
    }

    fun addData(queryModel: QueryModel){
        data?.add(0,queryModel)
        notifyItemInserted(0)
    }

    fun setData(list: ArrayList<QueryModel>, color: String){
        data = list
        gradColor = color
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
        holder.bindView(q, context, this, gradColor)
        holder.itemView.setOnClickListener {
            q?.let { it1 -> listener.onViewReplies(it1) }
        }
    }

    class QueriesViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bindView(q: QueryModel?, context: Context, listener:QueryImageAttachmentAdapter.AttachmentListenet, gradColor:String) {
            val query = Html.fromHtml(q?.query).trim()
            if (query.isEmpty()){
                itemView.tv_query_question.visibility = View.GONE
            }else{
                itemView.tv_query_question.visibility = View.VISIBLE
                itemView.tv_query_question.text = query
            }
            val firstName = "${q?.user?.first_name}"
            val lastName = q?.user?.last_name
            var name = ""
            name = if (lastName!=null){
                "$firstName $lastName"
            }else{
                firstName
            }
            if (q?.user?.profile_image!=null && q.user?.profile_image?.isNotEmpty()!!){
                itemView.rel_query_img_placeholder.visibility = View.GONE
                itemView.img_person_queries.visibility = View.VISIBLE
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
            }else{
                val colors = arrayOf("#dd310c","#5b0cdd", "#cd0cdd","#dd0c0c","#0cdd58","#0c75dd","#ba0cdd","#dd780c","#878085","#3e1b26")
                val pos = (0..9).random()
                itemView.rel_query_img_placeholder.visibility = View.VISIBLE
                itemView.img_person_queries.visibility = View.GONE
                val shape = GradientDrawable()
                shape.shape = GradientDrawable.OVAL
                shape.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
                shape.setColor(Color.parseColor(colors[pos]))
                itemView.rel_query_img_placeholder.background = shape
                var text = ""
                text = if (lastName!=null){
                    "${firstName[0]}${lastName[0]}"
                }else{
                    "${firstName[0]}"
                }

                itemView.tv_query_img_placeholder.text = text
            }
            itemView.tv_name_qury_user.text = name
            itemView.tv_email_user.text = "@${q?.user?.email}"
            val adapter = QueryImageAttachmentAdapter(context, listener)
            itemView.rv_attachment_query.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            itemView.rv_attachment_query.adapter = adapter
            q?.open_forum_attachment?.let { adapter.setData(it,0) }
        }

    }

    override fun onImageClickedQuery(attachment: List<String>, position: Int) {
        listener.onImageClicked(attachment, position)
    }
}