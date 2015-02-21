package org.teamtators.pitscout;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;


import java.util.List;

public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragment {
        public static final String TEAM_LIST_FILE_PREFERENCE = "teamListFileName";
        public static final int TEAM_LIST_FILE_RESULT_CODE = 21315321;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            Preference teamListFile = findPreference(TEAM_LIST_FILE_PREFERENCE);
            teamListFile.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    startActivityForResult(intent, TEAM_LIST_FILE_RESULT_CODE);
                    return false;
                }
            });
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode == TEAM_LIST_FILE_RESULT_CODE) {
                String resultPath = data.getDataString();
                findPreference(TEAM_LIST_FILE_PREFERENCE)
                        .getEditor()
                        .putString(TEAM_LIST_FILE_PREFERENCE, resultPath)
                        .commit();
                return;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
