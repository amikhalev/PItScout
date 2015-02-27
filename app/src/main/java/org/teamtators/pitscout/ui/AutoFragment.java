package org.teamtators.pitscout.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.Spinner;

import org.teamtators.pitscout.DataPopulator;
import org.teamtators.pitscout.PitScoutBaseFragment;
import org.teamtators.pitscout.R;
import org.teamtators.pitscout.ScoutingData;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;

public class AutoFragment extends PitScoutBaseFragment implements DataPopulator {
    @InjectView(R.id.robot_set)
    CheckBox robotSet;
    @InjectView(R.id.auto_totes)
    NumberPicker autoTotes;
    @InjectView(R.id.auto_bins)
    NumberPicker autoBins;
    @InjectView(R.id.starting_position)
    Spinner startingPosition;
    @InjectViews({R.id.ground_pick, R.id.slot_feed})
    CheckBox[] loadingMethodCheckBoxes;

    public AutoFragment() {
    }

    public static AutoFragment newInstance() {
        AutoFragment fragment = new AutoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auto, container, false);
        ButterKnife.inject(this, view);

        autoTotes.setMinValue(0);
        autoTotes.setMaxValue(3);
        autoBins.setMinValue(0);
        autoBins.setMaxValue(3);

        return view;
    }

    @Override
    public boolean populateScoutingData(ScoutingData data) {
        data.setRobotSet(robotSet.isChecked());
        data.setAutoTotes(autoTotes.getValue());
        data.setAutoBins(autoBins.getValue());
        data.setStartingPosition((String) startingPosition.getSelectedItem());
        List<String> loadingMethods = new ArrayList<>();
        String[] loadingMethodNames = getResources().getStringArray(R.array.loading_methods);
        for (int i = 0; i < loadingMethodCheckBoxes.length; ++i) {
            if (loadingMethodCheckBoxes[i].isChecked()) {
                loadingMethods.add(loadingMethodNames[i]);
            }
        }
        data.setLoadingMethods(loadingMethods.toArray(new String[loadingMethods.size()]));
        return true;
    }
}
