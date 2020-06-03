package com.prepare.prepareurself.preferences.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prepare.prepareurself.R
import com.prepare.prepareurself.preferences.data.PreferencesModel
import kotlinx.android.synthetic.main.preferences_adapter_layout.view.*

class PreferenceRvAdapter(var listener:PrefListener):RecyclerView.Adapter<PreferenceRvAdapter.PrefViewHolder>(){

    private var data : List<PreferencesModel>?=null

    fun setData(list:List<PreferencesModel>){
        data = list
        notifyDataSetChanged()
    }

    interface PrefListener{
        fun onPrefCanceled(prefModel: PreferencesModel?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrefViewHolder {
        return PrefViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.preferences_adapter_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: PrefViewHolder, position: Int) {
        val prefModel = data?.get(position)
        holder.bindView(prefModel, position)
        holder.itemView.img_cancel_pref.setOnClickListener {
            listener.onPrefCanceled(prefModel)
        }
    }

    class PrefViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        fun bindView(prefModel: PreferencesModel?, position: Int) {
            itemView.tv_pref_count.text =  "${position+1}"
            itemView.tv_pref_name.text = prefModel?.name
        }
    }

}