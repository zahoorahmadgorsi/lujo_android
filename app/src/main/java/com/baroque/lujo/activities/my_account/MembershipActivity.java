package com.baroque.lujo.activities.my_account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.baroque.lujo.R;
import com.baroque.lujo.activities.country.CountriesActivity;
import com.baroque.lujo.activities.login.UserModel;

import java.lang.reflect.Member;
import java.util.ArrayList;

import adapters.CountryAdapter;
import adapters.MembershipAdapter;
import utilities.Constants;

import static utilities.Constants.PREF_FILE_NAME;
import static utilities.Key.KEY_CURRENT_USER;
import static utilities.Utility.getSavedObjectFromPreference;

public class MembershipActivity extends AppCompatActivity implements MembershipAdapter.OnItemClickListener {
    private MembershipAdapter membershipAdapter;
    UserModel currentUser;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);
        currentUser = getSavedObjectFromPreference(MembershipActivity.this, PREF_FILE_NAME, KEY_CURRENT_USER, UserModel.class);

        if (currentUser.getMembershipPlan().getPlan() == Constants.MemberShipPlan.All.toString()){
            getSupportActionBar().setTitle(getString(R.string.title_activity_for_members));
        }else if (currentUser.getMembershipPlan().getPlan() == Constants.MemberShipPlan.Dining.toString()){
            getSupportActionBar().setTitle(getString(R.string.title_activity_for_members));
        }

        //Setting credit card data
        ArrayList<CreditCard> data= new ArrayList<>();
        data.add(new CreditCard( R.drawable.cc_all_access, "Image 1"));
        data.add(new CreditCard( R.drawable.cc_dining, "Image 2"));
        recyclerView = findViewById(R.id.rvCreditCards);
        recyclerView.setLayoutManager(new LinearLayoutManager(MembershipActivity.this, LinearLayoutManager.HORIZONTAL, false));
        membershipAdapter = new MembershipAdapter(getApplicationContext(),data);
        membershipAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(membershipAdapter);
        membershipAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position, CreditCard data) {

    }
}