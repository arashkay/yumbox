package geekhouse.ir.yumbox.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import geekhouse.ir.yumbox.ui.MainActivity;
import geekhouse.ir.yumbox.ui.fragments.MainActivityFragment;

public class MainActivityPagerAdapter extends FragmentPagerAdapter {

    public MainActivityPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int pos) {

        switch (pos) {
            case 0:
                return MainActivityFragment.newInstance(0);
            case 1:
                return MainActivityFragment.newInstance(1);
            case 2:
                return MainActivityFragment.newInstance(2);

            default:
                return new MainActivityFragment();
        }
    }

    @Override
    public int getCount() {
        return MainActivity.numViews;
    }

}