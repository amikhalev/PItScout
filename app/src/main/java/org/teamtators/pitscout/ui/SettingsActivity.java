package org.teamtators.pitscout.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.util.Log;

import org.teamtators.pitscout.R;

public class SettingsActivity extends Activity {
    public static final String TEAM_LIST_FILE_PREFERENCE = "pref_team_list_file";
    public static final String TEAM_LIST_FILE_DEFUALT = Uri.parse(Environment
            .getExternalStorageDirectory()
            .getAbsolutePath() + "/teamlist.csv").getPath();
    public static final String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends android.preference.PreferenceFragment {
        private static final int TEAM_LIST_FILE_REQUEST_CODE = 21315321;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            updateTeamListSummary();
//            Preference teamListFile = findPreference(TEAM_LIST_FILE_PREFERENCE);
//            teamListFile.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                @Override
//                public boolean onPreferenceClick(Preference preference) {
//                    Intent intent;
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                        Log.d(TAG, ">=API 19, using ACTION_OPEN_DOCUMENT");
//                        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                    } else {
//                        Log.d(TAG, "<API 19, using ACTION_GET_CONTENT");
//                        intent = new Intent(Intent.ACTION_GET_CONTENT);
//                    }
//                    intent.addCategory(Intent.CATEGORY_OPENABLE);
//                    intent.setType("*/*");
//                    startActivityForResult(intent, TEAM_LIST_FILE_REQUEST_CODE);
//                    return false;
//                }
//            });
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            Log.d(TAG, "Got activity result. code: " + requestCode + ", data: " + data);
            if (requestCode == TEAM_LIST_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                String resultPath = data.getDataString();
                findPreference(TEAM_LIST_FILE_PREFERENCE)
                        .getEditor()
                        .putString(TEAM_LIST_FILE_PREFERENCE, resultPath)
                        .commit();
                updateTeamListSummary();
                return;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }

        private void updateTeamListSummary() {
            Preference preference = findPreference(TEAM_LIST_FILE_PREFERENCE);
            preference.setSummary(preference.getSharedPreferences()
                    .getString(TEAM_LIST_FILE_PREFERENCE, TEAM_LIST_FILE_DEFUALT));

        }
    }
}
