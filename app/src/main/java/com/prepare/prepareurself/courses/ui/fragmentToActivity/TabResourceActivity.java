package com.prepare.prepareurself.courses.ui.fragmentToActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.prepare.prepareurself.Home.ui.SearchFragment;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.courses.data.model.TopicsModel;
import com.prepare.prepareurself.courses.data.model.TopicsResponseModel;
import com.prepare.prepareurself.courses.ui.adapters.ResourcesRvAdapter;
import com.prepare.prepareurself.courses.viewmodels.TopicViewModel;
import com.prepare.prepareurself.resources.ui.activity.ResourcesActivity;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.GridSpaceItemDecoration;
import com.prepare.prepareurself.utils.PrefManager;

import java.util.List;

public class TabResourceActivity extends AppCompatActivity implements ResourcesRvAdapter.ResourceRvInteractor,
        View.OnClickListener {
    private TopicViewModel mViewModel;
    private RecyclerView recyclerView;
    private ResourcesRvAdapter adapter;
    private Boolean isScrolling = false;
    private int rvCurrentItems, rvTotalItems, rvScrolledOutItems, rvLastPage, rvCurrentPage=1;
    private PrefManager prefManager;
    private TextView tvComingSoon;
    private int courseId = -1;
    //
    private EditText searchEdit;
    private ImageView closeSearch, menu;

    private static final String TAG = "ToolbarFragment";
    private static final int STANDARD_APPBAR = 0;
    private static final int SEARCH_APPBAR = 1;
    private int mAppBarState;

    private AppBarLayout viewContactsBar, searchBar;

    private SearchFragment searchFragment;

    private RelativeLayout relMainResource;

//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_resource);
        recyclerView= findViewById(R.id.rv_resources);
        tvComingSoon=findViewById(R.id.tv_coming_soon);
        //
        viewContactsBar =  findViewById(R.id.viewContactsToolbar);
        searchBar =  findViewById(R.id.searchToolbar);
        relMainResource = findViewById(R.id.lin_resource_main);
        menu = findViewById(R.id.menu);
        menu.setOnClickListener(this);
//
        mViewModel = ViewModelProviders.of(this).get(TopicViewModel.class);

        prefManager = new PrefManager(this);
//
        searchFragment = SearchFragment.newInstance();

        setAppBaeState(STANDARD_APPBAR);
        Log.d(TAG, "app bar"+STANDARD_APPBAR);


        ImageView ivSearchContact = findViewById(R.id.ivSearchIcon);
        ivSearchContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: clicked searched icon");
                toggleToolBarState();
            }
        });

        ImageView ivBackArrow = findViewById(R.id.ivBackArrow);
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: clicked back arrow.");
                toggleToolBarState();

            }
        });

        searchEdit = findViewById(R.id.etSearch);
        closeSearch = findViewById(R.id.closeSearch);
        closeSearch.setOnClickListener(this);
//
        adapter = new ResourcesRvAdapter(this, this);
        //final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        final GridLayoutManager layoutManager = new GridLayoutManager(this,2,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpaceItemDecoration(30));
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        courseId = intent.getIntExtra(Constants.COURSEID,-1);

        Log.d("course_id", courseId+"");
        if (courseId!=-1){
            searchFragment.courseSearch = true;
            searchFragment.courseId = courseId;
            mViewModel.getCourseById(prefManager.getString(Constants.JWTTOKEN),
                    courseId,
                    10,
                    rvCurrentPage);
            rvCurrentPage+=1;
        }

        mViewModel.getTopicsResponseModelLiveData().observe(this, new Observer<TopicsResponseModel>() {
            @Override
            public void onChanged(final TopicsResponseModel topicsResponseModel) {
                if (topicsResponseModel!=null){
                    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                                isScrolling = true;
                            }
                        }

                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            rvCurrentItems = layoutManager.getChildCount();
                            rvTotalItems = layoutManager.getItemCount();
                            rvScrolledOutItems = layoutManager.findFirstVisibleItemPosition();

                            rvLastPage = topicsResponseModel.getLast_page();

                            Log.d("paging_debug",rvCurrentPage+","+rvLastPage);

                            if (isScrolling && (rvCurrentItems + rvScrolledOutItems) == rvTotalItems && rvCurrentPage<=rvLastPage){
                                isScrolling = false;
                                mViewModel.getCourseById(prefManager.getString(Constants.JWTTOKEN),
                                        courseId,
                                        10,
                                        rvCurrentPage);
                                rvCurrentPage+=1;
                            }

                        }
                    });
                }
            }
        });

        mViewModel.getLiveData(courseId).observe(this, new Observer<List<TopicsModel>>() {
            @Override
            public void onChanged(List<TopicsModel> topicsModels) {

                if (topicsModels!=null && !topicsModels.isEmpty()){
                    tvComingSoon.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter.setTopics(topicsModels);
                    adapter.notifyDataSetChanged();
                }else{
                    tvComingSoon.setText("Coming Soon...");
                    tvComingSoon.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }


            }
        });
//
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    closeSearch.setVisibility(View.VISIBLE);
                }
                else{
                    closeSearch.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchFragment.performSearch(searchEdit.getText().toString());
                    return true;
                }
                return false;
            }
        });

    }

    private void toggleToolBarState() {
        Log.d("TAG", "toggleToolBarState: toggling AppBarState.");
        if (mAppBarState == STANDARD_APPBAR) {
            setAppBaeState(SEARCH_APPBAR);
            searchEdit.setPressed(true);
            searchEdit.requestFocus();
        } else {
            setAppBaeState(STANDARD_APPBAR);
        }
    }

    private void setAppBaeState(int state) {

        Log.d(TAG, "setAppBaeState: changing app bar state to: " + state);

        mAppBarState = state;
        if (mAppBarState == STANDARD_APPBAR) {
            searchBar.setVisibility(View.GONE);
            viewContactsBar.setVisibility(View.VISIBLE);
            closeSearchFragment();
            View view = getWindow().getDecorView();
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            try {
                im.hideSoftInputFromWindow(view.getWindowToken(), 0); // make keyboard hide
            } catch (NullPointerException e) {
                Log.d(TAG, "setAppBaeState: NullPointerException: " + e);
            }
        } else if (mAppBarState == SEARCH_APPBAR) {
            viewContactsBar.setVisibility(View.GONE);
            searchBar.setVisibility(View.VISIBLE);
            openSearchFragment();
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0); // make keyboard popup

        }
    }

    private void closeSearchFragment() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount()>0){
            fm.popBackStack();
        }
        relMainResource.setVisibility(View.VISIBLE);
    }

    private void openSearchFragment() {
        relMainResource.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.search_container,searchFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu:
                finish();
                break;
            case R.id.closeSearch:
                searchEdit.setText("");
                break;
        }
    }
//
    @Override
    public void onResourceClicked(TopicsModel topicsModel) {
        Intent intent =new Intent(TabResourceActivity.this, ResourcesActivity.class);
        intent.putExtra(Constants.TOPICID,topicsModel.getId());
        intent.putExtra(Constants.COURSEID, topicsModel.getCourse_id());
        startActivity(intent);
    }
//
    @Override
    public void onBackPressed() {
        if (mAppBarState == SEARCH_APPBAR){
            toggleToolBarState();
        }else{
            super.onBackPressed();
        }
    }
}
