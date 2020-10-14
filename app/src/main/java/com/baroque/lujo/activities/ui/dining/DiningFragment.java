package com.baroque.lujo.activities.ui.dining;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.baroque.lujo.R;
import com.baroque.lujo.activities.HomeActivity;


public class DiningFragment extends Fragment {

    private DiningViewModel diningViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        diningViewModel = new ViewModelProvider(this).get(DiningViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dining, container, false);
        final TextView textView = root.findViewById(R.id.text_dining);
        diningViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        //Updating toolbar title
        ((HomeActivity)getActivity()).setToolbarTitle("Dining");
        return root;
    }
}