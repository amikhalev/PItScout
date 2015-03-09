package org.teamtators.pitscout.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.teamtators.pitscout.DataManager;
import org.teamtators.pitscout.PitScoutBaseActivity;
import org.teamtators.pitscout.R;
import org.teamtators.pitscout.ScoutingData;
import org.teamtators.pitscout.ScoutingDataView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;


public class ScoutingActivity extends PitScoutBaseActivity implements ActionBar.TabListener {
    SectionsPagerAdapter mSectionsPagerAdapter;
    @InjectView(R.id.pager)
    @Optional
    ViewPager mViewPager;
    @Inject
    DataManager dataManager;
    private ScoutingData scoutingData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scouting_layout);
        ButterKnife.inject(this);

        scoutingData = dataManager.getData();
        if (mViewPager != null) {
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
            mViewPager.setAdapter(mSectionsPagerAdapter);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
                next();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public Fragment findFragmentByPosition(int position) {
        return getSupportFragmentManager()
                .findFragmentByTag("android:switcher:" + mViewPager.getId() + ":" + mSectionsPagerAdapter.getItemId(position));
    }

    private void next() {
        scoutingData = dataManager.getData();
        List<String> missing = new ArrayList<>();
        if (!populateModel(missing)) return;
        if (missing.isEmpty()) {
            succeed();
        } else {
            displayMissingDatas(missing);
        }
    }

    private boolean populateModel(List<String> missing) {
        for (Fragment view : getFragments()) {
            if (view == null) {
                Toast.makeText(this, getString(R.string.error_fill_pages), Toast.LENGTH_SHORT).show();
                return false;
            }
            ((ScoutingDataView) view).viewToModel(scoutingData, missing);
        }
        return true;
    }

    private void succeed() {
        Log.i(getLocalClassName(), scoutingData.toCsvLine());
        startActivity(new Intent(getApplicationContext(), CommentsActivity.class));
    }

    private void displayMissingDatas(List<String> missing) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_action_alert_warning);
        builder.setTitle(getString(R.string.error_not_inputed));
        ArrayAdapter<String> alertAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, missing);
        DialogInterface.OnClickListener noop = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        };
        builder.setNegativeButton(getString(R.string.label_go_back), noop);
        builder.setPositiveButton(getString(R.string.label_continue), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                succeed();
            }
        });
        builder.setAdapter(alertAdapter, noop);
        builder.show();
    }

    private Fragment[] getFragments() {
        Fragment[] fragments;
        if (mViewPager == null) {
            FragmentManager fm = getSupportFragmentManager();
            fragments = new Fragment[]{fm.findFragmentById(R.id.basic_robot_fragment),
                    fm.findFragmentById(R.id.auto_fragment),
                    fm.findFragmentById(R.id.teleop_fragment)};
        } else {
            fragments = new Fragment[]{findFragmentByPosition(0),
                    findFragmentByPosition(1),
                    findFragmentByPosition(2)};
        }
        return fragments;
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
