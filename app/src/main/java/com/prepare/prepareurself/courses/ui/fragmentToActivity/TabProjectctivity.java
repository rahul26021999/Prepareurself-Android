package com.prepare.prepareurself.courses.ui.fragmentToActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import com.prepare.prepareurself.courses.data.model.ProjectResponse;
import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.courses.ui.activity.ProjectsActivity;
import com.prepare.prepareurself.courses.ui.adapters.ProjectsRvAdapter;
import com.prepare.prepareurself.courses.viewmodels.ProjectsViewModel;
import com.prepare.prepareurself.favourites.data.model.LikedProjectsModel;
import com.prepare.prepareurself.resources.data.model.ResourceLikesResponse;
import com.prepare.prepareurself.utils.BaseActivity;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.DividerItemDecoration;
import com.prepare.prepareurself.utils.PrefManager;
import com.prepare.prepareurself.utils.Utility;

import java.io.IOException;
import java.util.List;

public class TabProjectctivity extends BaseActivity implements ProjectsRvAdapter.ProjectsRvInteractor , View.OnClickListener {
    private ProjectsViewModel mViewModel;
    private RecyclerView recyclerView;
    private PrefManager prefManager;
    private Boolean isScrolling = false;
    private int rvCurrentItems, rvTotalItems, rvScrolledOutItems, rvLastPage, rvCurrentPage=1;
    private TextView tvComingSoon;
    private ProjectsRvAdapter adapter;
    private int courseId = -1;
    private EditText searchEdit;
    private ImageView closeSearch, menu;
    //here tootlbar name chnge work
    private TextView toolbarTitle;

    private static final String TAG = "ToolbarFragment";
    private static final int STANDARD_APPBAR = 0;
    private static final int SEARCH_APPBAR = 1;
    private int mAppBarState;

    private AppBarLayout viewContactsBar, searchBar;

    private SearchFragment searchFragment;

    private RelativeLayout relMainResource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_projectctivity);
        recyclerView = findViewById(R.id.rv_projects);
        tvComingSoon = findViewById(R.id.tv_coming_soon);

        viewContactsBar =  findViewById(R.id.viewContactsToolbar);
        searchBar =  findViewById(R.id.searchToolbar);
        relMainResource = findViewById(R.id.lin_resource_main);
        menu = findViewById(R.id.menu);
        menu.setOnClickListener(this);
        searchFragment = SearchFragment.newInstance();
        toolbarTitle =findViewById(R.id.toolbarheading);
        toolbarTitle.setText("Project");
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

        mViewModel = ViewModelProviders.of(this).get(ProjectsViewModel.class);
        prefManager = new PrefManager(this);

        Intent intent = getIntent();
        courseId = intent.getIntExtra(Constants.COURSEID,-1);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new ProjectsRvAdapter(TabProjectctivity.this, this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(this,R.drawable.theory_resource_divider);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapter);

        if(courseId!=-1){
            mViewModel.fetchProjects(prefManager.getString(Constants.JWTTOKEN),
                    courseId,
                    "",
                    10,
                    rvCurrentPage);
            rvCurrentPage+=1;
        }


        mViewModel.getProjectByCourseId(courseId).observe(this, new Observer<List<ProjectsModel>>() {
            @Override
            public void onChanged(List<ProjectsModel> projectsModels) {
                if (projectsModels!=null && !projectsModels.isEmpty()){
                    tvComingSoon.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter.setProjects(projectsModels);
                    adapter.notifyDataSetChanged();
                }else{
                    tvComingSoon.setText("Coming soon...");
                    tvComingSoon.setVisibility(View.VISIBLE);

                    recyclerView.setVisibility(View.GONE);
                }

            }
        });

        mViewModel.getProjectResponseMutableLiveData().observe(this, new Observer<ProjectResponse>() {
            @Override
            public void onChanged(final ProjectResponse projectResponse) {
                if (projectResponse!=null){
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

                            rvLastPage = projectResponse.getLast_page();

                            if (isScrolling && (rvCurrentItems + rvScrolledOutItems) == rvTotalItems && rvCurrentPage <= rvLastPage){
                                isScrolling = false;
                                mViewModel.fetchProjects(prefManager.getString(Constants.JWTTOKEN),
                                        courseId,
                                        "",
                                        10,
                                        rvCurrentPage);
                                rvCurrentPage+=1;

                            }

                        }
                    });
                }
            }
        });
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
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onProjectClicked(ProjectsModel projectsModel) {
        Intent intent = new Intent(TabProjectctivity.this, ProjectsActivity.class);
        intent.putExtra(Constants.PROJECTID,projectsModel.getId());
        startActivity(intent);
    }

    @Override
    public void onProjectShared(Bitmap bitmap, String text) {
        try {
            Uri bitmapUri = Utility.getUriOfBitmap(bitmap, this);
            Utility.shareContent(this,bitmapUri,text);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onProjectLiked(final ProjectsModel projectsModel, final int liked) {
        mViewModel.likeProject(prefManager.getString(Constants.JWTTOKEN),projectsModel.getId(), liked)
                .observe(this, new Observer<ResourceLikesResponse>() {
                    @Override
                    public void onChanged(ResourceLikesResponse resourceLikesResponse) {
                        if (resourceLikesResponse!=null){
                            if (!resourceLikesResponse.getSuccess()){
                                Utility.showToast(TabProjectctivity.this,"Unable to like at the moment");
                            }else{
                                if (liked == 0){
                                    projectsModel.setLike(1);
                                    projectsModel.setTotal_likes(projectsModel.getTotal_likes()+1);
                                }else if (liked == 1){
                                    projectsModel.setLike(0);
                                    projectsModel.setTotal_likes(projectsModel.getTotal_likes()-1);
                                }
                                mViewModel.saveProject(projectsModel);
                                adapter.notifyDataSetChanged();
                                LikedProjectsModel likedProjectsModel = new LikedProjectsModel();
                                likedProjectsModel.setId(projectsModel.getId());
                                likedProjectsModel.setName(projectsModel.getName());
                                likedProjectsModel.setDescription(projectsModel.getDescription());
                                likedProjectsModel.setType(projectsModel.getType());
                                likedProjectsModel.setLevel(projectsModel.getLevel());
                                likedProjectsModel.setImage_url(projectsModel.getImage_url());
                                likedProjectsModel.setLink(projectsModel.getLink());
                                likedProjectsModel.setPlaylist(projectsModel.getPlaylist());
                                likedProjectsModel.setCourse_id(projectsModel.getCourse_id());
                                likedProjectsModel.setAdmin_id(projectsModel.getAdmin_id());
                                likedProjectsModel.setCreated_at(projectsModel.getCreated_at());
                                likedProjectsModel.setUpdated_at(projectsModel.getUpdated_at());
                                likedProjectsModel.setLike(projectsModel.getLike());
                                likedProjectsModel.setTotal_likes(projectsModel.getTotal_likes());
                                likedProjectsModel.setView(projectsModel.getView());
                                likedProjectsModel.setSequence(projectsModel.getSequence());

                                if (liked == 1){
                                    mViewModel.deleteLikedProject(likedProjectsModel);
                                }else if (liked == 0){
                                    mViewModel.insertLikedProject(likedProjectsModel);
                                }

                            }
                        }
                    }
                });
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
    @Override
    public void onBackPressed() {
        if (mAppBarState == SEARCH_APPBAR){
            toggleToolBarState();
        }else{
            super.onBackPressed();
        }
    }
}

