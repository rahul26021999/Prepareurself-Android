package com.prepare.prepareurself.Home.content.profile.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.prepare.prepareurself.Home.content.profile.ui.adapter.PreferrenceRecyclerAdapter;
import com.prepare.prepareurself.Home.content.profile.data.model.PreferredTechStack;
import com.prepare.prepareurself.Home.content.profile.viewmodel.ProfileViewModel;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.PrefManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditPreferencesActivity extends AppCompatActivity implements RecyclerItemSelectedListener {
    private RecyclerView recyclerView;
    private PreferrenceRecyclerAdapter radapter;

    private EditText userInput;
    private ChipGroup courseChipGroup;

    private List<PreferredTechStack> userPrefrence=new ArrayList<>();
    private List<PreferredTechStack> allStack = new ArrayList<>();

    private ProfileViewModel profileViewModel;
    private PrefManager prefManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        prefManager = new PrefManager(this);

        recyclerView=findViewById(R.id.recyclerView);
        userInput=findViewById(R.id.edit_stackname);
        courseChipGroup=findViewById(R.id.chipGroup);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        radapter=new PreferrenceRecyclerAdapter(this);
        recyclerView.setAdapter(radapter);

        userInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                radapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                radapter.getFilter().filter(s.toString());
            }
        });

        setUserPrefrenced(userPrefrence);

        profileViewModel.getPreferredTechStacks().observe(this, new Observer<HashMap<String,PreferredTechStack>>() {
            @Override
            public void onChanged(HashMap<String, PreferredTechStack> preferredTechStacks) {
                allStack =new ArrayList<>(preferredTechStacks.values());
                radapter.setList(allStack);
                radapter.notifyDataSetChanged();

            }
        });
    }

    private void setUserPrefrenced(List<PreferredTechStack> userPrefrence) {
        courseChipGroup.removeAllViews();
        userPrefrence=userPrefrence;
        for (int i=0;i<userPrefrence.size();i++) {
            addChipToUserPreference(userPrefrence.get(i));
        }
    }

    @Override
    public void onItemSelected (int position) {
        addChipToUserPreference(allStack.get(position));
        userPrefrence.add(allStack.get(position));
        allStack.remove(position);
        radapter.notifyDataSetChanged();
    }

    private void addChipToUserPreference(final PreferredTechStack preferredTechStack) {
        final Chip chip=new Chip(this);
        chip.setText(preferredTechStack.getCourse_name());
        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseChipGroup.removeView(v);
                userPrefrence.remove(preferredTechStack);
                allStack.add(preferredTechStack);
                radapter.notifyDataSetChanged();
            }
        });
        courseChipGroup.addView(chip);
    }

    @Override
    public  boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.edit_preferences_menu,menu);
        MenuItem menuItem=menu.findItem(R.id.save);
        //menuItem.getActionView();
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getApplicationContext(),"save clicked",Toast.LENGTH_SHORT).show();
//                profileViewModel.updatePrefernces(prefManager.getString(Constants.JWTTOKEN),finalListIds);
                return true;
            }
        });
        return true;
    }

}
