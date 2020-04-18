package com.prepare.prepareurself.Home.content.EditProfile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prepare.prepareurself.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>
{   private Context context;
    private List<techstack> techstackList;
    private  RecyclerItemSelectedListener itemSelectedListener;


    public RecyclerAdapter(Context context, List<techstack> techstackList) {
        this.context = context;
        this.techstackList = techstackList;
        itemSelectedListener= (RecyclerItemSelectedListener) context;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.course_name.setText(techstackList.get(position).getCourse_name());

    }


    @Override
    public int getItemCount() {
        return techstackList.size();
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
            itemSelectedListener.onItemSelected(techstackList.get(getAdapterPosition()), getAdapterPosition());

        }
    }
    public  void
     updateList(techstack newtechstackitem){
        techstackList.clear();
        techstackList.add(newtechstackitem);
        notifyDataSetChanged();
    }
}
