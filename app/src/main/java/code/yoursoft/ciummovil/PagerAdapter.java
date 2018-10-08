package code.yoursoft.ciummovil;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FragInicio tab1 = new FragInicio();
                return tab1;
            case 1:
                FragRecursos tab2 = new FragRecursos();
                return tab2;
            case 2:
                FragCalidad tab3 = new FragCalidad();
                return tab3;
            case 3:
                FragZona tab4 = new FragZona();
                return tab4;
            case 4:
                FragPendientes tab5 = new FragPendientes();
                return tab5;
            case 5:
                FragConfig tab6 = new FragConfig();
                return tab6;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}