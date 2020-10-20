package com.baroque.lujo.activities.my_account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baroque.lujo.R;
import com.baroque.lujo.activities.login.UserModel;

import java.util.ArrayList;

import adapters.MembershipAdapter;
import utilities.Constants;

import static utilities.Constants.ACTION_TO_PERFORM;
import static utilities.Constants.PREF_FILE_NAME;
import static utilities.Key.KEY_CURRENT_USER;
import static utilities.Utility.getSavedObjectFromPreference;

public class MembershipActivity extends AppCompatActivity implements MembershipAdapter.OnItemClickListener,View.OnClickListener {
    private MembershipAdapter membershipAdapter;
    RecyclerView recyclerView;
    UserModel currentUser;
    LinearSnapHelper snapHelper = new LinearSnapHelper();
    private int snapPosition = RecyclerView.SCROLLBAR_POSITION_DEFAULT;
    ArrayList<CreditCard> data= new ArrayList<>();  //credit cards data
    LinearLayout llNonDiningFeatures;
    Button btnPayNow;



    //we only wish to know what the final snap position is (i.e. When scroll state becomes idle)?
    enum Behavior {
        NOTIFY_ON_SCROLL,
        NOTIFY_ON_SCROLL_STATE_IDLE
    }


    //MembershipType tells which card user has selected
    Constants.MEMBERSHIP_TYPE membership_type = Constants.MEMBERSHIP_TYPE.FREE; //Default value
    //pageType is about user action e.g. View only or upgradeMembership
    Constants.MEMBERSHIP_PAGE_TYPE membershipPageType = Constants.MEMBERSHIP_PAGE_TYPE.NOT_UPGRADE; //Default value

    Behavior behavior = Behavior.NOTIFY_ON_SCROLL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);
        Intent intent = getIntent();
        //User is coming to upgrade its membership or not
        this.membershipPageType = (Constants.MEMBERSHIP_PAGE_TYPE) intent.getSerializableExtra(Constants.ACTION_TO_PERFORM);

        currentUser = getSavedObjectFromPreference(MembershipActivity.this, PREF_FILE_NAME, KEY_CURRENT_USER, UserModel.class);

        if (currentUser.getMembershipPlan() == null || currentUser.getMembershipPlan().getPlan().equals("")){
            membership_type = Constants.MEMBERSHIP_TYPE.FREE;
        }else if (currentUser.getMembershipPlan().getPlan().equalsIgnoreCase(Constants.MEMBERSHIP_TYPE.DINING.toString())  ){
            membership_type = Constants.MEMBERSHIP_TYPE.DINING;
        }
        else if (currentUser.getMembershipPlan().getPlan().equalsIgnoreCase(Constants.MEMBERSHIP_TYPE.ALL.toString()) ){
            membership_type = Constants.MEMBERSHIP_TYPE.ALL;
        }
        updateUIByMembership();
        //read this method description
        updateRecyclerView();
        btnPayNow = findViewById(R.id.btnPayNow);
        btnPayNow.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPayNow:
                Intent intent = new Intent(MembershipActivity.this, MembershipActivity.class);
                if (membershipPageType == Constants.MEMBERSHIP_PAGE_TYPE.NOT_UPGRADE) {
                    intent.putExtra(ACTION_TO_PERFORM, Constants.MEMBERSHIP_PAGE_TYPE.UPGRADE);
                    startActivity(intent);
                }
                break;
            case R.id.btnSignUp:
                break;
        }
    }

    private void updateUIByMembership(){
        LinearLayout llPaging = findViewById(R.id.llPaging);
        llNonDiningFeatures = findViewById(R.id.llNonDiningFeatures);
        View viewRefferalVerticalLine = findViewById(R.id.viewRefferalVerticalLine);
        LinearLayout llReferral = findViewById(R.id.llRefferal);
        LinearLayout llFees = findViewById(R.id.llFees);
        Button btnPayNow = findViewById(R.id.btnPayNow);
        TextView tvFees = findViewById(R.id.tvFees);
        LinearLayout llBottom = findViewById(R.id.llBottom);


        if (this.membership_type == Constants.MEMBERSHIP_TYPE.FREE){
            getSupportActionBar().setTitle(getString(R.string.title_purchase_membership));  //updating page title
            data.add(new CreditCard(R.drawable.cc_all_access, currentUser.getFullName()));
            data.add(new CreditCard(R.drawable.cc_dining, currentUser.getFullName()));
            //no need to show number of cards, as if paging is visible then cards are always 2
//            TextView tvTotalCards = findViewById(R.id.tvTotalCards);
//            tvTotalCards.setText(String.valueOf(data.size()));  //updating total number of cards
        }
        else if (this.membership_type == Constants.MEMBERSHIP_TYPE.DINING){
            //Current membership is dining and user want to upgrade it to All Access
            if (this.membershipPageType == Constants.MEMBERSHIP_PAGE_TYPE.UPGRADE) {
                getSupportActionBar().setTitle(getString(R.string.title_membersship_upgrade));  //updating page title
                data.add(new CreditCard(R.drawable.cc_all_access, currentUser.getFullName())); //dining want to upgrade to full access
                //fee to upgrade form dining to full access is 500
                tvFees.setText("$500");
                //upgrade button title to PURCHASE PLAN
                btnPayNow.setText("P U R C H A S E   P L A N");
            }else{
                getSupportActionBar().setTitle(getString(R.string.title_membersship_overview)); //updating page title
                data.add(new CreditCard(R.drawable.cc_dining, currentUser.getFullName())); //just want to see currrent membership details i.e. dining
                //hide the non dining features for dining card
                llNonDiningFeatures.setVisibility(View.GONE);
                //second bottom linearlayout which is showing fees must be hidden because user is just coming to view
                llFees.setVisibility(View.GONE);
                //upgrade button title to UPGRADE MEMBER SHIP
                btnPayNow.setText("U P G R A D E   M E M B E R S H I P");
            }
            //hide the paging because only one card is visible
            llPaging.setVisibility(View.GONE);  //hide the paging because only one card is visible
            //Hide the referral code, as no discount on upgradation
            viewRefferalVerticalLine.setVisibility(View.GONE);
            llReferral.setVisibility(View.GONE);
        }
        else if (this.membership_type == Constants.MEMBERSHIP_TYPE.ALL){
            getSupportActionBar().setTitle(getString(R.string.title_membersship_overview));
            //Setting credit card data
            data.add(new CreditCard( R.drawable.cc_all_access, currentUser.getFullName()));
            llPaging.setVisibility(View.GONE);  //hide the paging because only one card is visible
            //Full access member, so hide payment part
            llBottom.setVisibility(View.GONE);
        }

    }

    @Override
    public void onItemClick(int position, CreditCard data) {
        recyclerView.scrollToPosition(position);
        this.snapPosition = position;   //setting the snapPostion due to click

//      1.  First scroll RecyclerView to make target item visible.
//      2.  Than, take the object of target View and use SnapHelper to determine distance for the final snap.
//      3.  Finally scroll to target position.

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        recyclerView.scrollToPosition(position);
        recyclerView.post(() -> {
            View view = layoutManager.findViewByPosition(position);
            if (view == null) {
                Log.e("MemberShipActivity", "Cant find target View for initial Snap");
                return;
            }

            int[] snapDistance = snapHelper.calculateDistanceToFinalSnap(layoutManager, view);
            if (snapDistance[0] != 0 || snapDistance[1] != 0) {
                recyclerView.scrollBy(snapDistance[0], snapDistance[1]);
                maybeNotifySnapPositionChange(recyclerView,position);
            }
        });
    }


    //This method does many things
//    1.    it attaches snaphelper so that full credit card should be visible, not partial positioning
//    2.    it displays credit card to the centre of the screen
//    3.    when user stops the scroll from scrolling it checks the position
//    4.    It sets the adapter to the recylerview
    private void updateRecyclerView()
    {
        recyclerView = findViewById(R.id.rvCreditCards);
        //To show an item fully, in the centre
        snapHelper.attachToRecyclerView(recyclerView);
        //This is used to center first and last item on screen
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildViewHolder(view).getAdapterPosition();
                if (position == 0 || position == state.getItemCount() - 1) {
                    int elementWidth = (int)getResources().getDimension(R.dimen.list_element_width);
                    int elementMargin = (int)getResources().getDimension(R.dimen.small_margin);

                    int padding = (Resources.getSystem().getDisplayMetrics().widthPixels  - ( elementWidth + elementMargin*2) )/ 2;

                    if (position == 0) {
                        outRect.left = padding;
                    } else {
                        outRect.right = padding;
                    }
                }
            }
        });
        //Changing values on UI at scroll change
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE ) {
                    int currentSnapPosition = getSnapPosition(recyclerView);
                    maybeNotifySnapPositionChange(recyclerView,currentSnapPosition);
                }
            }
        });
        //Setting adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(MembershipActivity.this, LinearLayoutManager.HORIZONTAL, false));
        membershipAdapter = new MembershipAdapter(getApplicationContext(),data);
        membershipAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(membershipAdapter);
        membershipAdapter.notifyDataSetChanged();
    }


    //after performing the null validations, it just checks the position of the credit card
    public int getSnapPosition(@NonNull RecyclerView recyclerView){
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if ( layoutManager == null)
            return RecyclerView.NO_POSITION;
        View snapView = snapHelper.findSnapView(layoutManager);
        if ( snapView == null)
            return RecyclerView.NO_POSITION;
        int snapPosition = layoutManager.getPosition(snapView);
        return snapPosition;
    }

    public void maybeNotifySnapPositionChange(@NonNull RecyclerView recyclerView, int currentSnapPosition) {
        //checking if old position of the snap is different then the current one
        Boolean snapPositionChanged = this.snapPosition != currentSnapPosition;
        if (snapPositionChanged) {
            this.snapPosition = currentSnapPosition;
            //snapPositionChange();
            TextView tvCurrentPage = findViewById(R.id.tvCurrentCardNumber);
            if(this.snapPosition == 0){     //full access
                tvCurrentPage.setText("1");
                llNonDiningFeatures.setVisibility(View.VISIBLE);
//                // Prepare the View for the animation
//                llNonDiningFeatures.setVisibility(View.VISIBLE);
//                llNonDiningFeatures.setAlpha(0.0f);
//                // Start the animation
//                llNonDiningFeatures.animate()
//                    .translationY(0)    //llNonDiningFeatures.getHeight()
//                    .alpha(1.0f)
//                    .setListener(null);
            }else if(this.snapPosition == 1){   //dining
                tvCurrentPage.setText("2");
                llNonDiningFeatures.setVisibility(View.GONE);
//                //Hiding by animationg
//                llNonDiningFeatures.animate()
//                    .translationY(llNonDiningFeatures.getHeight())
//                    .alpha(0.0f)
//                    .setListener(new AnimatorListenerAdapter() {
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            super.onAnimationEnd(animation);
//                            llNonDiningFeatures.setVisibility(View.GONE);
//                        }
//                    });
            }
        }

    }

}