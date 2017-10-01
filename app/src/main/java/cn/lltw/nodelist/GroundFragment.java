package cn.lltw.nodelist;


import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.lltw.nodelist.Ground.GroundMyFragment;
import cn.lltw.nodelist.Ground.GroundRecommendFragment;
import cn.lltw.nodelist.Ground.GroundTypeFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroundFragment extends Fragment{

    View mView;
    Adapter mAdapter;
    TabLayout mTabLayout;
    ViewPager mViewPager;

    public GroundFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_ground, container, false);
        mAdapter=new Adapter(getChildFragmentManager());
        mViewPager=(ViewPager)mView.findViewById(R.id.ground_view_pager);
        mViewPager.setAdapter(mAdapter);
        mTabLayout=(TabLayout)mView.findViewById(R.id.ground_tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);
        return mView;
    }

    static class Adapter extends FragmentPagerAdapter{
        public Adapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    GroundRecommendFragment groundRecommendFragment=new GroundRecommendFragment();
                    return  groundRecommendFragment;
                case 1:
                    GroundTypeFragment groundTypeFragment=new GroundTypeFragment();
                    return groundTypeFragment;
                case 2:
                    GroundMyFragment groundMyFragment=new GroundMyFragment();
                    return groundMyFragment;
                default:return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:return "推荐";
                case 1:return "全部分类";
                case 2:return "我的分享";
            }
            return null;
        }
    }
}
