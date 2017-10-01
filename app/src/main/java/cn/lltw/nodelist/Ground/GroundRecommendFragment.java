package cn.lltw.nodelist.Ground;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.lltw.nodelist.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroundRecommendFragment extends Fragment {


    public GroundRecommendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ground_recommend, container, false);
    }

}
