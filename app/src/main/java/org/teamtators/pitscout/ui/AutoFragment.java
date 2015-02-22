package org.teamtators.pitscout.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.Spinner;

import org.teamtators.pitscout.DataPopulator;
import org.teamtators.pitscout.PitScoutBaseFragment;
import org.teamtators.pitscout.R;
import org.teamtators.pitscout.ScoutingData;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AutoFragment extends PitScoutBaseFragment implements DataPopulator {
    private static final String[] STARTING_POSITIONS = {""};
    @InjectView(R.id.robot_set)
    CheckBox robotSet;
    @InjectView(R.id.auto_totes)
    NumberPicker autoTotes;
    @InjectView(R.id.auto_bins)
    NumberPicker autoBins;
    @InjectView(R.id.starting_position)
    Spinner startingPosition;

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

        ArrayAdapter<String> startingPositionAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, STARTING_POSITIONS);
        startingPosition.setAdapter(startingPositionAdapter);

        return view;
    }

    @Override
    public boolean populateScoutingData(ScoutingData data) {
        data.setRobotSet(robotSet.isChecked());
        data.setAutoTotes(autoTotes.getValue());
        data.setAutoBins(autoBins.getValue());
        return true;
    }
}
