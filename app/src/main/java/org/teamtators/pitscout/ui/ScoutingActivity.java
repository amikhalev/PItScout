package org.teamtators.pitscout.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.teamtators.pitscout.DataPopulator;
import org.teamtators.pitscout.PitScoutBaseActivity;
import org.teamtators.pitscout.R;
import org.teamtators.pitscout.ScoutingData;

import java.io.IOException;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;


public class ScoutingActivity extends PitScoutBaseActivity implements ActionBar.TabListener {
    SectionsPagerAdapter mSectionsPagerAdapter;
    @InjectView(R.id.pager)
    @Optional
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scouting_layout);
        ButterKnife.inject(this);

        if (mViewPager != null) {
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
            mViewPager.setAdapter(mSectionsPagerAdapter);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scouting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.action_done:
                done();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public Fragment findFragmentByPosition(int position) {
        return getSupportFragmentManager()
                .findFragmentByTag("android:switcher:" + mViewPager.getId() + ":" + mSectionsPagerAdapter.getItemId(position));
    }

    private void done() {
        ScoutingData data = new ScoutingData();
        boolean isPaned = getResources().getBoolean(R.bool.is_paned);
        DataPopulator[] populators;
        if (isPaned) {
            FragmentManager fm = getSupportFragmentManager();
            populators = new DataPopulator[]{(DataPopulator) fm.findFragmentById(R.id.basic_robot_fragment),
                    (DataPopulator) fm.findFragmentById(R.id.auto_fragment),
                    (DataPopulator) fm.findFragmentById(R.id.teleop_fragment)};
        } else {
            populators = new DataPopulator[]{(DataPopulator) findFragmentByPosition(0),
                    (DataPopulator) findFragmentByPosition(1),
                    (DataPopulator) findFragmentByPosition(2)};
        }
        for (DataPopulator populator : populators) {
            if (populator == null) {
                Toast.makeText(this, "Fill out all of the scouting pages before submitting", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!populator.populateScoutingData(data))
                return;
        }
        Log.i(getLocalClassName(), data.toCsvLine());
        try {
            data.appendToFile(this);
        } catch (IOException e) {
            Toast.makeText(this, "Failed to write scouting data to file", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return;
        }
        Intent intent = new Intent(getApplicationContext(), ScoutingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        if (mViewPager != null)
            mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public static final int NUM_FRAGMENTS = 3;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return BasicRobotFragment.newInstance();
                case 1:
                    return AutoFragment.newInstance();
                case 2:
                    return TeleopFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_FRAGMENTS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_basic_robot).toUpperCase(l);
                case 1:
                    return getString(R.string.title_auto).toUpperCase(l);
                case 2:
                    return getString(R.string.title_teleop).toUpperCase(l);
            }
            return null;
        }


    }

}
