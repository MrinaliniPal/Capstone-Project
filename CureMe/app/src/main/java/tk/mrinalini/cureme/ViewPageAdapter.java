package tk.mrinalini.cureme;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class ViewPageAdapter extends FragmentPagerAdapter {

    private final CharSequence Titles[];
    private final int NoOfPages;

    ViewPageAdapter(FragmentManager manager, CharSequence Titles[], int NoofPages) {
        super(manager);
        this.Titles = Titles;
        this.NoOfPages = NoofPages;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return new Disease_Information();
        else if (position == 1)
            return new Home_Remedy();
        else
            return new Emergency_Condition();
    }

    @Override
    public int getCount() {
        return NoOfPages;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }
}