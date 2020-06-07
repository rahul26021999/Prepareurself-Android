package com.prepare.prepareurself.preferences.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prepare.prepareurself.R
import com.prepare.prepareurself.dashboard.data.model.CourseModel
import com.prepare.prepareurself.preferences.data.PreferencesModel
import kotlinx.android.synthetic.main.choose_pref_adapter_layout.view.*

class ChoosePrefAdapter(var listener:ChoosePrefListener):RecyclerView.Adapter<ChoosePrefAdapter.ChooseViewHolder>() {

    private var data : List<PreferencesModel>?=null

    interface ChoosePrefListener{
        fun onItemClicked(courseModel: PreferencesModel)
    }

    fun setData(list: List<PreferencesModel>){
        data =list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseViewHolder {
        return ChooseViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.choose_pref_adapter_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: ChooseViewHolder, position: Int) {
        val courseModel  = data?.get(position)
        holder.bindView(courseModel)
        holder.itemView.setOnClickListener {
            courseModel?.let { it1 -> listener.onItemClicked(it1) }
        }
    }

    class ChooseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bindView(courseModel: PreferencesModel?){
            itemView.tv_choose_pref_name.text = courseModel?.name
        }

    }

}