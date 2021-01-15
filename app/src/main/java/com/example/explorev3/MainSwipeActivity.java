package com.example.explorev3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.explorev3.adapter.SlidePagerAdapter;
import com.example.explorev3.fragment.FavoritesFragment;
import com.example.explorev3.fragment.HomeFragment;
import com.example.explorev3.fragment.PostFragment;
import com.example.explorev3.fragment.SearchFragment;
import com.example.explorev3.pojo.FilterData;

import java.util.ArrayList;
import java.util.List;

public class MainSwipeActivity extends AppCompatActivity implements FilterSearchDialog.FilterDialogListener {

    private ViewPager viewPager;
    private LinearLayout mDotLayout;
    private RelativeLayout mActionBar;
    private TextView[] mDots;
    private TextView fragmentTitle;

    private PagerAdapter pagerAdapter;

    private FilterData filterData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_swipe);

        fragmentTitle = findViewById(R.id.fragment_title_banner);
        mActionBar = findViewById(R.id.action_bar);

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
            String[] screenTitle = {"Home", "eXplore", "My Favorites", "Share Hall"};
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

}