package com.prepare.prepareurself.Home.content.resources.ui.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prepare.prepareurself.Home.content.resources.data.model.ResourceModel;
import com.prepare.prepareurself.Home.content.resources.ui.activity.ResourcesActivity;
import com.prepare.prepareurself.Home.content.resources.ui.adapter.TheoryResourcesRvAdapter;
import com.prepare.prepareurself.Home.content.resources.viewmodel.ResourceViewModel;

import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.Utility;

import java.util.List;

public class TheoryResourceFragment extends Fragment implements TheoryResourcesRvAdapter.TheoryResourceRvInteractor {

    private ResourceViewModel mViewModel;
    private RecyclerView rvTheoryResources;
    private TheoryResourcesRvAdapter adapter1;

    public static TheoryResourceFragment newInstance() {
        return new TheoryResourceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
    View view1=inflater.inflate(R.layout.theory_resource_fragment,container,false);
    rvTheoryResources=view1.findViewById(R.id.rv_theory_resources);
        return view1;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ResourceViewModel.class);

        adapter1=new TheoryResourcesRvAdapter(getActivity(), this);
        LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        rvTheoryResources.setLayoutManager(linearLayoutManager1);
        rvTheoryResources.setAdapter(adapter1);

       mViewModel.getListLiveData(ResourcesActivity.topicID,Constants.THEORY).observe(getActivity(), new Observer<List<ResourceModel>>() {
           @Override
           public void onChanged(List<ResourceModel> resourceModels) {
               adapter1.setResourcesList(resourceModels);
               adapter1.notifyDataSetChanged();
           }
       });
    }

    @Override
    public void onResourceClicked(ResourceModel resource) {
        Utility.redirectUsingCustomTab(getActivity(),resource.getLink());
    }
}
