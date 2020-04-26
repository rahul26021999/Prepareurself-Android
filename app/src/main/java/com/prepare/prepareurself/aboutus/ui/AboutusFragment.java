package com.prepare.prepareurself.aboutus.ui;

import androidx.lifecycle.ViewModelProviders;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.prepare.prepareurself.R;

public class AboutusFragment extends Fragment {

    private AboutusViewModel mViewModel;

    public static AboutusFragment newInstance() {
        return new AboutusFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.aboutus_fragment, container, false);
        ImageView imageView = view.findViewById(R.id.imagevw);
        Glide.with(getActivity())
                .load(R.drawable.aboutuspicture)
                .into(imageView);
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AboutusViewModel.class);
        // TODO: Use the ViewModel
    }

}
