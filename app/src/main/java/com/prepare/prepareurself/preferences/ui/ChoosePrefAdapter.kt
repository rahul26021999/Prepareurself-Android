package com.prepare.prepareurself.preferences.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.prepare.prepareurself.R
import com.prepare.prepareurself.dashboard.data.model.CourseModel
import com.prepare.prepareurself.preferences.data.PreferencesModel
import kotlinx.android.synthetic.main.choose_pref_adapter_layout.view.*
import java.util.*
import kotlin.collections.ArrayList

class ChoosePrefAdapter(var listener:ChoosePrefListener):RecyclerView.Adapter<ChoosePrefAdapter.ChooseViewHolder>(), Filterable {

    private var data : List<PreferencesModel>?=null
    private var filteredData : List<PreferencesModel>?=null

    interface ChoosePrefListener{
        fun onItemClicked(courseModel: PreferencesModel)
    }

    fun setData(list: List<PreferencesModel>){
        data =list
        filteredData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseViewHolder {
        return ChooseViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.choose_pref_adapter_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return filteredData?.size ?: 0
    }

    override fun onBindViewHolder(holder: ChooseViewHolder, position: Int) {
        val courseModel  = filteredData?.get(position)
        holder.bindView(courseModel)
        holder.itemView.setOnClickListener {
            courseModel?.let { it1 -> listener.onItemClicked(it1) }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()){
                    filteredData = data
                }else{
                    val resultList = ArrayList<PreferencesModel>()
                    data?.forEach {
                        if (it.name?.toLowerCase(Locale.ROOT)?.contains(charSearch.toLowerCase(Locale.ROOT))!!){
                            resultList.add(it)
                        }
                    }
                    filteredData = resultList
                }

                val filteredResults = FilterResults()
                filteredResults.values = filteredData
                return filteredResults

            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                filteredData = results?.values as List<PreferencesModel>?
                notifyDataSetChanged()

            }
        }
    }

    class ChooseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bindView(courseModel: PreferencesModel?){
            itemView.tv_choose_pref_name.text = courseModel?.name
        }

    }

}