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


/**
 * A simple {@link Fragment} subclass.
 */
public class GraphFragment extends Fragment {
    View mView;
    Adapter mAdapter;
    TabLayout mTabLayout;
    ViewPager mViewPager;



    public GraphFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_graph, container, false);
        //新建适配器
        mAdapter=new Adapter(getChildFragmentManager());
        //注册viewpager
        mViewPager=(ViewPager)mView.findViewById(R.id.graph_view_pager);
        //为ViewPager设置adapter
        mViewPager.setAdapter(mAdapter);
        //注册TabLayout
        mTabLayout=(TabLayout)mView.findViewById(R.id.graph_tab_layout);
        //为TabLayout设置ViewPager
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
                    GraphPie graphPie=new GraphPie();
                    return graphPie;
                case 1:
                    GraphBar graphBar=new GraphBar();
                    return graphBar;
                case 2:
                    GraphOther graphOther=new GraphOther();
                    return graphOther;
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
                case 0:return "饼状图";
                case 1:return "柱状图";
                case 2:return "其他用户";
            }
            return null;
        }
    }

}
