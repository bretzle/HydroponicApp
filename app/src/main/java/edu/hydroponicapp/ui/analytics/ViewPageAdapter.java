package edu.hydroponicapp.ui.analytics;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import edu.hydroponicapp.ui.analytics.tabs.tab0_overview;
import edu.hydroponicapp.ui.analytics.tabs.tab1_ph;
import edu.hydroponicapp.ui.analytics.tabs.tab2_temp;
import edu.hydroponicapp.ui.analytics.tabs.tab3_humidity;

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
                return new tab1_ph();

            case 2:
                return new tab2_temp();
            case 3:
                return new tab3_humidity();
        }
        return new tab0_overview();
    }


    @Override
    public int getItemCount() {
        return 4;
    }

}