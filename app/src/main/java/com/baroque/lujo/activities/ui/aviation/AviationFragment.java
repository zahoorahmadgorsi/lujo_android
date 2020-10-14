package com.baroque.lujo.activities.ui.aviation;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baroque.lujo.R;
import com.baroque.lujo.activities.HomeActivity;
import com.baroque.lujo.activities.ui.chat.ChatViewModel;

public class AviationFragment extends Fragment {

    private AviationViewModel mViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(AviationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_aviation, container, false);
        final TextView textView = root.findViewById(R.id.text_aviation);
        mViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        //Updating toolbar title
        ((HomeActivity)getActivity()).setToolbarTitle("Aviation");
        return root;
    }

}