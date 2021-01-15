package com.example.explorev3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.explorev3.adapter.SlidePagerAdapter;
import com.example.explorev3.fragment.FavoritesFragment;
import com.example.explorev3.fragment.HomeFragment;
import com.example.explorev3.fragment.PostFragment;
import com.example.explorev3.fragment.SearchFragment;
import com.example.explorev3.pojo.FilterData;
import com.example.explorev3.pojo.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainSwipeActivity extends AppCompatActivity implements FilterSearchDialog.FilterDialogListener, View.OnClickListener {

    private ViewPager viewPager;
    private LinearLayout mDotLayout;
    private RelativeLayout mActionBar;
    private TextView[] mDots;
    private TextView fragmentTitle;
    private CircularImageView userAvatar;
    private PagerAdapter pagerAdapter;

    private FilterData filterData;

    String userName, userEmail;

    private FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_swipe);

        userAvatar = findViewById(R.id.user_avatar);
        fragmentTitle = findViewById(R.id.fragment_title_banner);
        mActionBar = findViewById(R.id.action_bar);

        userAvatar.setOnClickListener(this);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new SearchFragment());
        fragmentList.add(new FavoritesFragment());
        fragmentList.add(new PostFragment());

        // Swipe fragment using view pager
        mDotLayout = findViewById(R.id.dots_indicator);
        viewPager = findViewById(R.id.view_pager);
        pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(), fragmentList);

        viewPager.setAdapter(pagerAdapter);

        addDotIndicator(0);
        viewPager.addOnPageChangeListener(viewPagerListener);

        // get user data from firebase
        getUserData();
    }

    public void addDotIndicator(int position) {
        mDots = new TextView[4];
        mDotLayout.removeAllViews();

        int dotTransparent, dotSolid, backgroundBanner, titleColor;

        if (position == 1 || position == 2) {
            backgroundBanner = getResources().getColor(R.color.background_grey);
            dotTransparent = getResources().getColor(R.color.dot_transparent_black);
            dotSolid = getResources().getColor(R.color.dot_solid_black);
            titleColor = getResources().getColor(R.color.black);
        } else {
            backgroundBanner = getResources().getColor(R.color.background_blue);
            dotTransparent = getResources().getColor(R.color.dot_transparent);
            dotSolid = getResources().getColor(R.color.white);
            titleColor = getResources().getColor(R.color.white);
        }

        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(dotTransparent);
            mDots[i].setPadding(5, 0, 5, 0);

            mDotLayout.addView(mDots[i]);
        }

        if (mDots.length > 0) {
            String[] screenTitle = {"Home", "eXplore", "My Favorites", "Community's Post"};
            mDots[position].setTextColor(dotSolid);
//            mActionBar.setBackgroundColor(backgroundBanner);
            fragmentTitle.setText(screenTitle[position]);
            fragmentTitle.setTextColor(titleColor);
        }
    }

    ViewPager.OnPageChangeListener viewPagerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void applyText(String latt, String lon, String radius, ArrayList<String> checkResult) {
        filterData = new FilterData(radius, latt, lon, checkResult);
    }

    public FilterData getData() {
        ArrayList<String> type = new ArrayList<>();
        type.add("interesting_places");
        if (filterData == null) {
            return new FilterData("", "", "", type);
        }
        return filterData;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_avatar:
                Intent intent = new Intent(this, UserProfileActivity.class);
                intent.putExtra("USER_NAME", userName);
                intent.putExtra("USER_EMAIL", userEmail);
                startActivity(intent);
                break;
        }
    }

    private void getUserData() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            userUID = user.getUid();

            // get user data from firebase realtime database
            reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(userUID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User userProfile = snapshot.getValue(User.class);

                    if (userProfile != null) {
                        userName = userProfile.name;
                        userEmail = userProfile.email;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainSwipeActivity.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            // No user is signed in
            Random rand = new Random();
            int randNum = rand.nextInt(1000);

            userUID = "Guest" + randNum;
            userName = "Guest";
            userEmail = "Guest@xplore.com";
        }
    }
}