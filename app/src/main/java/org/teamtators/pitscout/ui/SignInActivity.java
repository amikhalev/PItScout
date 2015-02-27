package org.teamtators.pitscout.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.teamtators.pitscout.PitScoutBaseActivity;
import org.teamtators.pitscout.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SignInActivity extends PitScoutBaseActivity {
    public static final String PREFERENCES_NAME = "scouter_info";
    public static final String KEY_SCOUTER_NAME = "scouter_name";
    public static final String KEY_COMPETITION = "competition";
    @InjectView(R.id.scouter_name)
    protected EditText scouterName;
    @InjectView(R.id.competition_name)
    protected Spinner competition;
    protected SharedPreferences preferences;
    private ArrayAdapter<String> competitionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.inject(this);
        competitionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.competitions));
        competition.setAdapter(competitionAdapter);
        preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
    }

    @Override
    protected void onStart() {
        scouterName.setText(preferences.getString(KEY_SCOUTER_NAME, ""));
        int position = competitionAdapter.getPosition(preferences.getString(KEY_COMPETITION, ""));
        if (position != -1)
            competition.setSelection(position);
        super.onStart();
    }

    @OnClick(R.id.button_next)
    protected void onNextClicked() {
        if (((TextView) findViewById(R.id.scouter_name)).getText().length() == 0) {
            Toast.makeText(SignInActivity.this, getString(R.string.error_enter_name), Toast.LENGTH_SHORT).show();
        } else {
            preferences.edit()
                    .putString(KEY_SCOUTER_NAME, scouterName.getText().toString())
                    .putString(KEY_COMPETITION, (String) competition.getSelectedItem())
                    .apply();
            SignInActivity.this.startActivity(new Intent(SignInActivity.this, ScoutingActivity.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_global, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
