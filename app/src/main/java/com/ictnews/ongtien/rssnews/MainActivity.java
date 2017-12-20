package com.ictnews.ongtien.rssnews;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set ActionBar
        /*AppBarLayout toolbar = (AppBarLayout) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        FragmentPagerAdapter fragmentPagerAdapter = new PagerAdapter(getSupportFragmentManager(), MainActivity.this);
        viewPager.setAdapter(fragmentPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    //create menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //handle menu items selected
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (Utils.IsConnected(MainActivity.this)) {
            switch (item.getItemId()) {
                case R.id.thoi_su:
                    UrlLink = getResources().getString(R.string.thoi_su_url);
                    break;
                case R.id.cntt:
                    UrlLink = getResources().getString(R.string.cntt_url);
                    break;
                case R.id.kinh_doanh:
                    UrlLink = getResources().getString(R.string.kinh_doanh_url);
                    break;
                case R.id.khoi_nghiep:
                    UrlLink = getResources().getString(R.string.khoi_nghiep_url);
                    break;
                case R.id.internet:
                    UrlLink = getResources().getString(R.string.internet_url);
                    break;
                case R.id.vien_thong:
                    UrlLink = getResources().getString(R.string.vien_thong_url);
                    break;
                case R.id.the_gioi_so:
                    UrlLink = getResources().getString(R.string.the_gioi_so_url);
                    break;
                case R.id.cong_nghe360:
                    UrlLink = getResources().getString(R.string.cong_nghe360_url);
            }
            connectStatus_TextView.setVisibility(GONE);
            new FetchFeedTask().execute();
        }
        else showTextDisconnect();
        return super.onOptionsItemSelected(item);
    }*/

}
