package org.teamtators.pitscout;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by alex on 2/21/15.
 */
public abstract class PitScoutBaseActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((PitScoutApplication) getApplication()).inject(this);
    }
}
