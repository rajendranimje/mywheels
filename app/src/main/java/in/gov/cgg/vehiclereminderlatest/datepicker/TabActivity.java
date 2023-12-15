package in.gov.cgg.vehiclereminderlatest.datepicker;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import in.gov.cgg.vehiclereminderlatest.R;


public class TabActivity extends AppCompatActivity {
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_date);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new FromFragment(), "From");
        adapter.addFragment(new ToFragment(), "To");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    /*@Override
    public void sendData(int day, int month, int year) {
        String tag = "android:switcher:" + R.id.viewPager + ":" + 1;
        ToFragment f = (ToFragment) getSupportFragmentManager().findFragmentByTag(tag);
        f.displayReceivedData(day,month,year);
    }*/
}