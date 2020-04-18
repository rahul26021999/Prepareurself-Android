package com.prepare.prepareurself.Home.content.profile.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prepare.prepareurself.Home.content.profile.ui.RecyclerItemSelectedListener;
import com.prepare.prepareurself.Home.content.profile.data.model.PreferredTechStack;
import com.prepare.prepareurself.R;

import java.util.ArrayList;
import java.util.List;

public class PreferrenceRecyclerAdapter extends RecyclerView.Adapter<PreferrenceRecyclerAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<PreferredTechStack> preferredTechStacks;
    private RecyclerItemSelectedListener itemSelectedListener;
    private List<PreferredTechStack> preferredTechStacksFiltered;

    public PreferrenceRecyclerAdapter(Context context) {
        this.context = context;
        itemSelectedListener= (RecyclerItemSelectedListener) context;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_preferences,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.course_name.setText(preferredTechStacksFiltered.get(position).getCourse_name());

    }


    @Override
    public int getItemCount() {
        if (preferredTechStacksFiltered !=null)
            return preferredTechStacksFiltered.size();
        else
            return 0;
    }

    class MyViewHolder extends  RecyclerView.ViewHolder implements  View.OnClickListener{
        TextView course_name;
        LinearLayout rootview;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            course_name=itemView.findViewById(R.id.text_item);
            rootview=itemView.findViewById(R.id.rootview);
            rootview.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemSelectedListener.onItemSelected(preferredTechStacks.get(getAdapterPosition()), getAdapterPosition());

        }
    }

    public void setList(List<PreferredTechStack> preferredTechStacks){
        this.preferredTechStacksFiltered = preferredTechStacks;
        this.preferredTechStacks = preferredTechStacks;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (TextUtils.isEmpty(charString)){
                    preferredTechStacksFiltered = preferredTechStacks;
                }else{
                    List<PreferredTechStack> filteredList = new ArrayList<>();
                    for (PreferredTechStack preferredTechStack : preferredTechStacks){
                        if (preferredTechStack.getCourse_name().toLowerCase().contains(charString.toLowerCase())){
                            filteredList.add(preferredTechStack);
                        }
                    }
                    preferredTechStacksFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = preferredTechStacksFiltered;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                preferredTechStacksFiltered = (List<PreferredTechStack>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
