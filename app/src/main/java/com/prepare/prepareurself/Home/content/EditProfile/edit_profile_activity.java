package com.prepare.prepareurself.Home.content.EditProfile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.system.StructUtsname;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.prepare.prepareurself.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class edit_profile_activity extends AppCompatActivity implements RecyclerItemSelectedListener, View.OnClickListener {
    private RecyclerView recyclerView;
    private  RecyclerAdapter radapter;
    private List<techstack> techstackList=new ArrayList<>();

    private EditText userInput;
    private ChipGroup coursechipgroup;
    private List<techstack> newtechstacklist=new ArrayList<>();
    //techstack t;
    String[] user_courses={"m","n","o","q"}; //use rpreferences
    int i;
    private List<techstack> tempTechstackList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_activity);

        recyclerView=findViewById(R.id.recyclerView);
        userInput=findViewById(R.id.edit_stackname);
        coursechipgroup=findViewById(R.id.chipGroup);
        setusercourse();


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getCourses();
        radapter=new RecyclerAdapter(this, techstackList);
        recyclerView.setAdapter(radapter);

        //
        userInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String userinput=s.toString();
                //List<techstack> newtechstacklist=new ArrayList<>();


             for (com.prepare.prepareurself.Home.content.EditProfile.techstack t : techstackList){
                 if (t.getCourse_name().toLowerCase().trim().contains(userinput)){
                     newtechstacklist.add(t);
                 }
             }
             radapter=new RecyclerAdapter(edit_profile_activity.this, newtechstacklist);
             recyclerView.setAdapter(radapter);

            }

            @Override
            public void afterTextChanged(Editable s) {

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

    @Override
    public void onItemSelected(techstack tstack, int position) {
        tempTechstackList.add(position, tstack);
        Chip chip=new Chip(this);
        chip.setText(tstack.getCourse_name());
        //chip.setChipIcon(ContextCompat.getDrawable(this,R.drawable.active_dot_drawable));
        chip.setCheckable(false);
        chip.setCloseIconVisible(true);
        chip.setClickable(true);
        coursechipgroup.addView(chip);
        coursechipgroup.setVisibility(View.VISIBLE);
        chip.setOnCloseIconClickListener(this);



    }

    private void getCourses(){
        String[] courses={"p","a","l","n"};

       // List<String> courses= Arrays.asList(getResources().getStringArray(R.array.array_techstacks));
        //List<String> courses=
        //int count =0;
        for(String Course : courses){
            techstackList.add(new techstack(Course));
           // count++;

        }


    }

    @Override
    public void onClick(View v) {
        Chip chip=(Chip) v;
        coursechipgroup.removeView(chip);
    }
    public  void updateListitem(){
        techstack t=new techstack(user_courses[i]);
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
