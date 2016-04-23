package geekhouse.ir.yumbox.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import geekhouse.ir.yumbox.R;
import geekhouse.ir.yumbox.ui.fragments.IntroFragment;

public class IntroPagerAdapter extends FragmentPagerAdapter {

    public IntroPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int pos) {
        switch(pos) {

            case 0:
                return IntroFragment.newInstance("غذایی سالم با طعم خانگی");
            case 1:
                return IntroFragment.newInstance("پخت روزانه با مواد تازه");
            case 2:
                return IntroFragment.newInstance(null);

            default:
                return IntroFragment.newInstance(null);
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}