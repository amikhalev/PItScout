package org.teamtators.pitscout.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.Toast;

import org.teamtators.pitscout.DataPopulator;
import org.teamtators.pitscout.PitScoutBaseFragment;
import org.teamtators.pitscout.R;
import org.teamtators.pitscout.ScoutingData;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TeleopFragment extends PitScoutBaseFragment implements DataPopulator {
    @InjectView(R.id.teleop_totes)
    NumberPicker teleopTotes;
    @InjectView(R.id.teleop_bins)
    NumberPicker teleopBins;
    @InjectView(R.id.straight_bin)
    Switch straightBin;
    @InjectView(R.id.manip_litter)
    Switch manipLitter;
    @InjectView(R.id.driver_practice)
    EditText driverPractice;
    @InjectView(R.id.human_practice)
    EditText humanPractice;

    public TeleopFragment() {
    }

    public static TeleopFragment newInstance() {
        TeleopFragment fragment = new TeleopFragment();
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
        View view = inflater.inflate(R.layout.fragment_teleop, container, false);
        ButterKnife.inject(this, view);

        teleopTotes.setMinValue(0);
        teleopTotes.setMaxValue(6);
        teleopBins.setMinValue(0);
        teleopBins.setMaxValue(6);

        return view;
    }


    @Override
    public boolean populateScoutingData(ScoutingData data) {
        data.setTeleopTotes(teleopTotes.getValue());
        data.setTeleopBins(teleopBins.getValue());
        data.setStraightensBin(straightBin.isChecked());
        data.setManipulatesLitter(manipLitter.isChecked());
        try {
            data.setDriverPracticeTime(Double.parseDouble(driverPractice.getText().toString()));
        } catch (NumberFormatException e) {
            makeErrorToast("Driver Practice Time").show();
            return false;
        }
        try {
            data.setHumanPlayerPracticeTime(Double.parseDouble(humanPractice.getText().toString()));
        } catch (NumberFormatException e) {
            makeErrorToast("Human Player Practice Time").show();
            return false;
        }
        return true;
    }

    protected Toast makeErrorToast(String missing) {
        return Toast.makeText(getActivity(), "Please enter a valid " + missing, Toast.LENGTH_SHORT);
    }
}
