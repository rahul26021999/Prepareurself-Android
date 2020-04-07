package com.example.prepareurself.Home.content.courses.ui.fragments;

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

import com.example.prepareurself.Home.content.courses.model.TheoryResources;
import com.example.prepareurself.Home.content.courses.ui.adapter.TheoryResourcesRvAdapter;
import com.example.prepareurself.Home.content.courses.viewmodel.TheoryResourceViewModel;
import com.example.prepareurself.R;

import java.util.List;

public class TheoryResourceFragment extends Fragment {

    private TheoryResourceViewModel mViewModel;
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
        mViewModel = ViewModelProviders.of(this).get(TheoryResourceViewModel.class);
        // TODO: Use the ViewModel
        //added code
        adapter1=new TheoryResourcesRvAdapter(getActivity());
        LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        rvTheoryResources.setLayoutManager(linearLayoutManager1);
        rvTheoryResources.setAdapter(adapter1);

        mViewModel.getListLiveData().observe(getActivity(), new Observer<List<TheoryResources>>() {
            @Override
            public void onChanged(List<TheoryResources> theoryResources) {
                adapter1.setTheoryResources(theoryResources);
                adapter1.notifyDataSetChanged();
            }
        });
    }

}
