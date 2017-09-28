package cn.lltw.nodelist;


import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;


public class SettingFragment extends PreferenceFragment {


    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
    }
}
