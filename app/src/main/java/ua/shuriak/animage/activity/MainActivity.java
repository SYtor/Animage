package ua.shuriak.animage.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import ua.shuriak.animage.R;
import ua.shuriak.animage.fragment.DanbooruFragment;
import ua.shuriak.animage.fragment.KonachanFragment;
import ua.shuriak.animage.fragment.SafebooruFragment;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    KonachanFragment konachanFragment;
    DanbooruFragment danbooruFragment;
    SafebooruFragment safebooruFragment;

    ViewPager viewPager;
    TabLayout tabLayout;
    TabsPagerAdapter pagerAdapter;

    public CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);

        konachanFragment = new KonachanFragment();
        danbooruFragment = new DanbooruFragment();
        safebooruFragment = new SafebooruFragment();

        pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager = (ViewPager)findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);

        tabLayout = (TabLayout)findViewById(R.id.sliding_tabs);
        tabLayout.setTabsFromPagerAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info:
                showDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setMessage(R.string.info);
        builder.show();
    }

    public class TabsPagerAdapter extends FragmentPagerAdapter {

        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return konachanFragment;
                case 1:
                    return safebooruFragment;
                default:
                    return danbooruFragment;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Konachan";
                case 1:
                    return "Safebooru";
                default:
                    return "Danbooru";
            }
        }
    }
}
