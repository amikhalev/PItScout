package org.teamtators.pitscout.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.EditText;
import android.widget.Toast;

import org.teamtators.pitscout.DataManager;
import org.teamtators.pitscout.PitScoutBaseActivity;
import org.teamtators.pitscout.R;
import org.teamtators.pitscout.ScoutingData;
import org.teamtators.pitscout.ScoutingDataView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;


public class ScoutingActivity extends PitScoutBaseActivity implements ActionBar.TabListener {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    SectionsPagerAdapter mSectionsPagerAdapter;
    @InjectView(R.id.pager)
    @Optional
    ViewPager mViewPager;
    @Inject
    DataManager dataManager;
    private ScoutingData scoutingData;
    @Inject
    Context context;

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
            case R.id.action_take_picture:
                takePicture();
                break;
            case R.id.action_comments:
                openComments();
                break;
            case R.id.action_done:
                done();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openComments() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_action_communication_comment_inv);
        builder.setTitle(getResources().getString(R.string.label_comments));
        final EditText view = new EditText(this);
        view.setLines(5);
        String comments = scoutingData.getComments();
        if (comments != null)
            view.setText(comments);
        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scoutingData.setComments(view.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    public Fragment findFragmentByPosition(int position) {
        return getSupportFragmentManager()
                .findFragmentByTag("android:switcher:" + mViewPager.getId() + ":" + mSectionsPagerAdapter.getItemId(position));
    }

    private void done() {
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
        try {
            scoutingData.appendToFile(this);
        } catch (IOException e) {
            Toast.makeText(this, getString(R.string.error_write_failed), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return;
        }
        Toast.makeText(context, getString(R.string.message_data_written), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(context, ScoutingActivity.class));
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

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File imageFile;
            try {
                imageFile = getImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show();
                return;
            }
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(context, getString(R.string.error_pictures_unsupported), Toast.LENGTH_LONG).show();
        }
    }

    protected File getImageFile() throws IOException {
        String competition = context
                .getSharedPreferences(SignInActivity.PREFERENCES_NAME, Context.MODE_PRIVATE)
                .getString(SignInActivity.KEY_COMPETITION, "");
        Integer teamNumber = scoutingData.getTeamNumber();
        String timeStamp = new SimpleDateFormat("HH.mm.ss", Locale.US).format(new Date());
        String imageFileName = competition + "_" + teamNumber + "_" + timeStamp;
        File directory = new File(Environment.getExternalStorageDirectory(), context.getPackageName());
        return File.createTempFile(imageFileName, ".jpg", directory);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
