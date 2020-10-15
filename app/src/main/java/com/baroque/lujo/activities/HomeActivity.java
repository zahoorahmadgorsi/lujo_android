package com.baroque.lujo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.baroque.lujo.R;
import com.baroque.lujo.activities.login.UserModel;
import com.baroque.lujo.activities.my_account.MyAccountActivity;
import com.baroque.lujo.activities.ui.aviation.AviationFragment;
import com.baroque.lujo.activities.ui.chat.ChatFragment;
import com.baroque.lujo.activities.ui.dining.DiningFragment;
import com.baroque.lujo.activities.ui.discover.DiscoverFragment;
import com.baroque.lujo.activities.ui.mybookings.MyBookingsFragment;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import static utilities.Constants.PREF_FILE_NAME;
import static utilities.Key.KEY_CURRENT_USER;
import static utilities.Utility.getSavedObjectFromPreference;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    Toolbar toolbar;
    ImageView imgUser, imgSearch;
    UserModel user;

    public void setToolbarTitle(String toolbarTitle) {
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(toolbarTitle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // Display icon in the toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);   // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavigationUI.setupWithNavController(navView, navController);

//      Passing each menu ID as a set of Ids because each menu should be considered as top level destinations. else do it like below
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_discover, R.id.navigation_dining, R.id.navigation_chat,R.id.navigation_aviation, R.id.navigation_my_bookings)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        //Toolbar imageViews on click listener
        imgUser = toolbar.findViewById(R.id.imgUser);
        imgUser.setOnClickListener(this);
        imgSearch = toolbar.findViewById(R.id.imgSearch);
        imgSearch.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        //reloading user object
        user = getSavedObjectFromPreference(getApplication(),PREF_FILE_NAME,KEY_CURRENT_USER,UserModel.class);
        Glide.with(HomeActivity.this)
                .load(user.getAvatar())
                .placeholder(R.drawable.ic_placeholder)
                .into(imgUser);
    }
    @Override
    public void onBackPressed() {
        //disabling back button
        return;
    }

    @Override
    public void onClick(View view) {
        Fragment foregroundFragment = getForegroundFragment();
        if (foregroundFragment == null)
            return;
        switch (view.getId()) {
            case R.id.imgUser:
//                if (foregroundFragment.getClass() == DiscoverFragment.class){
//                    System.out.println("User Clicked From disvoer");
//                }else if (foregroundFragment.getClass() == DiningFragment.class){
//                    System.out.println("User Clicked from dining");
//                }else if (foregroundFragment.getClass() == ChatFragment.class){
//                    System.out.println("User Clicked chat");
//                }else if (foregroundFragment.getClass() == AviationFragment.class){
//                    System.out.println("User Clicked aviation");
//                }else if (foregroundFragment.getClass() == MyBookingsFragment.class){
//                    System.out.println("User Clicked my booking");
//                }
                Intent intent = new Intent(HomeActivity.this , MyAccountActivity.class);
                startActivity(intent);
                break;
            case R.id.imgSearch:
                if (foregroundFragment.getClass() == DiscoverFragment.class){
                    System.out.println("Search Clicked From disvoer");
                }else if (foregroundFragment.getClass() == DiningFragment.class){
                    System.out.println("Search Clicked from dining");
                }else if (foregroundFragment.getClass() == ChatFragment.class){
                    System.out.println("Search Clicked chat");
                }else if (foregroundFragment.getClass() == AviationFragment.class){
                    System.out.println("Search Clicked aviation");
                }else if (foregroundFragment.getClass() == MyBookingsFragment.class){
                    System.out.println("Search Clicked my booking");
                }
                break;
        }
    }

    public Fragment getForegroundFragment(){
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        return navHostFragment == null ? null : navHostFragment.getChildFragmentManager().getFragments().get(0);
    }

//    public Fragment getVisibleFragment(){
//        FragmentManager fragmentManager = this.getSupportFragmentManager();
//        List<Fragment> fragments = fragmentManager.getFragments();
//        if(fragments != null){
//            for(Fragment fragment : fragments){
//                if(fragment != null && fragment.isVisible())
//                    return fragment;
//            }
//        }
//        return null;
//    }
    // Menu icons are inflated just as they were with actionbar
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return true;
//    }

}