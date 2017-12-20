package com.ictnews.ongtien.rssnews;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by TNT on 11/6/2017.
 *
 */

class PagerAdapter extends FragmentPagerAdapter{
    private Context mContext;
    PagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return PagerFragment.newInstance(mContext.getResources().getString(R.string.thoi_su_url));
            case 1:
                return PagerFragment.newInstance(mContext.getResources().getString(R.string.cntt_url));
            case 2:
                return PagerFragment.newInstance(mContext.getResources().getString(R.string.kinh_doanh_url));
            case 3:
                return PagerFragment.newInstance(mContext.getResources().getString(R.string.khoi_nghiep_url));
            case 4:
                return PagerFragment.newInstance(mContext.getResources().getString(R.string.internet_url));
            case 5:
                return PagerFragment.newInstance(mContext.getResources().getString(R.string.vien_thong_url));
            case 6:
                return PagerFragment.newInstance(mContext.getResources().getString(R.string.the_gioi_so_url));
            case 7:
                return PagerFragment.newInstance(mContext.getResources().getString(R.string.cong_nghe360_url));
            default:
                return PagerFragment.newInstance(mContext.getResources().getString(R.string.thoi_su_url));
        }
    }

    @Override
    public int getCount() {
        return 8;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return mContext.getResources().getString(R.string.thoi_su);
            case 1:
                return mContext.getResources().getString(R.string.cntt);
            case 2:
                return mContext.getResources().getString(R.string.kinh_doanh);
            case 3:
                return mContext.getResources().getString(R.string.khoi_nghiep);
            case 4:
                return mContext.getResources().getString(R.string.internet);
            case 5:
                return mContext.getResources().getString(R.string.vien_thong);
            case 6:
                return mContext.getResources().getString(R.string.the_gioi_so);
            case 7:
                return mContext.getResources().getString(R.string.cong_nghe360);
            default:
                return mContext.getResources().getString(R.string.thoi_su);
        }
    }
}
