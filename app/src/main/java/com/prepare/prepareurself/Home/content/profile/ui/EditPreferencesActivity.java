package com.prepare.prepareurself.Home.content.profile.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.LoginFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.prepare.prepareurself.Home.content.profile.ui.adapter.PreferrenceRecyclerAdapter;
import com.prepare.prepareurself.Home.content.profile.data.model.PreferredTechStack;
import com.prepare.prepareurself.Home.content.profile.viewmodel.ProfileViewModel;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

public class EditPreferencesActivity extends AppCompatActivity implements RecyclerItemSelectedListener, View.OnClickListener {
    private RecyclerView recyclerView;
    private PreferrenceRecyclerAdapter radapter;
    private List<PreferredTechStack> preferredTechStackList =new ArrayList<>();

    private EditText userInput;
    private ChipGroup coursechipgroup;
    private List<PreferredTechStack> newtechstacklist=new ArrayList<>();

    String[] user_courses={"m","n","o","q"}; //use rpreferences
    int i;
    private List<PreferredTechStack> existingStacks = new ArrayList<>();

    private ProfileViewModel profileViewModel;

    private List<Integer> finalListIds = new ArrayList<>();

    private PrefManager prefManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        prefManager = new PrefManager(this);

        recyclerView=findViewById(R.id.recyclerView);
        userInput=findViewById(R.id.edit_stackname);
        coursechipgroup=findViewById(R.id.chipGroup);


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



        profileViewModel.getPreferredTechStacks().observe(this, new Observer<List<PreferredTechStack>>() {
            @Override
            public void onChanged(final List<PreferredTechStack> preferredTechStacks) {
                radapter.setList(preferredTechStacks);
                radapter.notifyDataSetChanged();
            }
        });

        profileViewModel.getEditableStacks().observe(this, new Observer<List<PreferredTechStack>>() {
            @Override
            public void onChanged(List<PreferredTechStack> preferredTechStacks) {

                existingStacks = preferredTechStacks;

                Log.d("chip_debug","on chnged called");

                for (PreferredTechStack preferredTechStack : existingStacks){
                    finalListIds.add(preferredTechStack.getId());
                    addChip(preferredTechStack);
                }
            }
        });


    }

    private void setusercourse() {

        for ( i=0; i<user_courses.length; i++){
            Chip chip=new Chip(this);
            chip.setText(user_courses[i]);
            //chip.setChipIcon(ContextCompat.getDrawable(this,R.drawable.active_dot_drawable));
            chip.setCheckable(false);
            chip.setCloseIconVisible(true);
            chip.setClickable(true);
            coursechipgroup.addView(chip);
            coursechipgroup.setVisibility(View.VISIBLE);
            //chip.getId();

            Log.d("CHiiPS","iiiiiiddddddddd"+chip.getId());
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateListitem(); //kharaaab
                    //radapter.updateList(t);
                    Chip chip=(Chip) v;
                    coursechipgroup.removeView(chip);
                }
            });
        }
    }

    public void addChip(PreferredTechStack p){
        Chip chip=new Chip(this);
        chip.setText(p.getCourse_name());
        //chip.setChipIcon(ContextCompat.getDrawable(this,R.drawable.active_dot_drawable));
        chip.setCheckable(false);
        chip.setCloseIconVisible(true);
        chip.setClickable(true);
        coursechipgroup.addView(chip);
        coursechipgroup.setVisibility(View.VISIBLE);

    }

    @Override
    public void onItemSelected(final PreferredTechStack preferredTechStack, int position) {
        profileViewModel.addStacks(preferredTechStack);
    }

    private void getCourses(){
        String[] courses={"p","a","l","n"};

       // List<String> courses= Arrays.asList(getResources().getStringArray(R.array.array_techstacks));
        //List<String> courses=
        //int count =0;
        for(String Course : courses){
            preferredTechStackList.add(new PreferredTechStack());
           // count++;

        }


    }

    @Override
    public void onClick(View v) {
        Chip chip=(Chip) v;
        coursechipgroup.removeView(chip);
    }
    public  void updateListitem(){
        PreferredTechStack t=new PreferredTechStack();
        newtechstacklist.add(t);
        radapter.notifyDataSetChanged();
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
                profileViewModel.updatePrefernces(prefManager.getString(Constants.JWTTOKEN),finalListIds);
                return true;
            }
        });
      /*  Button btn=(Button)menuItem.getActionView();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        return true;
    }

}
