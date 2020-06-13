package com.prepare.prepareurself.profile.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prepare.prepareurself.R;
import com.prepare.prepareurself.preferences.data.PreferencesModel;
import com.prepare.prepareurself.profile.data.model.MyPreferenceTechStack;
import com.prepare.prepareurself.profile.data.model.PreferredTechStack;

import java.util.List;

public class UserPrefernceAdapter extends RecyclerView.Adapter<UserPrefernceAdapter.UserPreferenceViewHolder> {

    private Context context;
    private List<PreferencesModel> preferredTechStacks;

    public UserPrefernceAdapter(Context context) {
        this.context = context;
    }

    public void setPreferredTechStacks(List<PreferencesModel> preferredTechStacks){
        this.preferredTechStacks = preferredTechStacks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserPreferenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_preferences_adapter_layout,parent,false);
        return new UserPreferenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserPreferenceViewHolder holder, int position) {
        PreferencesModel preferredTechStack = preferredTechStacks.get(position);
        holder.bindView(preferredTechStack);
    }

    @Override
    public int getItemCount() {
        if (preferredTechStacks!=null)
            return preferredTechStacks.size();
        else
            return 0;
    }

    public void clearList() {
        if (preferredTechStacks!=null)
            preferredTechStacks.clear();
    }

    public class UserPreferenceViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public UserPreferenceViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_preference_name);
        }

        public void bindView(PreferencesModel preferredTechStack){
            textView.setText(preferredTechStack.getName());
        }
    }

}
