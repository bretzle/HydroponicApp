package edu.hydroponicapp.ui.analytics;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

import edu.hydroponicapp.ui.analytics.tabs.tab0;
import edu.hydroponicapp.ui.analytics.tabs.tab1;
import edu.hydroponicapp.ui.analytics.tabs.tab2;
import edu.hydroponicapp.ui.analytics.tabs.tab3;

public class ViewPageAdapter extends FragmentStateAdapter {
    public ViewPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public ViewPageAdapter(AnalyticsFragment analyticsFragment) {
        super(analyticsFragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int i) {
        switch (i) {
            case 1:
                return new tab1();

            case 2:
                return new tab2();
            case 3:
                return new tab3();
        }
        return new tab0();
    }


    @Override
    public int getItemCount() {
        return 4;
    }

}