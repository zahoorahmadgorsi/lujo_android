package com.baroque.lujo.activities.ui.mybookings;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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

public class MyBookingsFragment extends Fragment {

    private MyBookingsViewModel mViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(MyBookingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_bookings, container, false);
        final TextView textView = root.findViewById(R.id.text_my_bookings);
        mViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        //Updating toolbar title
        ((HomeActivity)getActivity()).setToolbarTitle("My Bookings");
        return root;
    }

}