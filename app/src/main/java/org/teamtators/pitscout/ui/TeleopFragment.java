package org.teamtators.pitscout.ui;


import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;

import org.teamtators.pitscout.PitScoutBaseFragment;
import org.teamtators.pitscout.R;
import org.teamtators.pitscout.ScoutingData;
import org.teamtators.pitscout.ScoutingDataView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;

public class TeleopFragment extends PitScoutBaseFragment implements ScoutingDataView {
    private static final String KEY_DATA = "scoutingData";
    @InjectView(R.id.teleop_totes)
    NumberPicker teleopTotes;
    @InjectView(R.id.own_stack_height)
    NumberPicker ownStackHeight;
    @InjectView(R.id.others_stack_height)
    NumberPicker othersStackHeight;
    @InjectViews({R.id.ground_pick, R.id.slot_feed})
    CheckBox[] loadingMethodCheckBoxes;
    @InjectView(R.id.num_stacks)
    NumberPicker numStacks;
    @InjectView(R.id.coopertition)
    NumberPicker coopertition;
    @InjectView(R.id.straight_bin)
    Switch straightBin;
    @InjectView(R.id.manip_litter)
    Switch manipLitter;
    @InjectView(R.id.upside_down_totes)
    Switch upsideDownTotes;
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
        Bundle arguments = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teleop, container, false);
        ButterKnife.inject(this, view);

        teleopTotes.setMinValue(0);
        teleopTotes.setMaxValue(6);
        ownStackHeight.setMinValue(0);
        ownStackHeight.setMaxValue(6);
        othersStackHeight.setMinValue(0);
        othersStackHeight.setMaxValue(6);
        numStacks.setMinValue(0);
        numStacks.setMaxValue(10);
        coopertition.setMinValue(0);
        coopertition.setMaxValue(4);

        return view;
    }


    @Override
    public void modelToView(ScoutingData data) {
        Integer teleopTotes = data.getTeleopTotes();
        if (teleopTotes != null)
            this.teleopTotes.setValue(teleopTotes);
        Integer ownStackHeight = data.getOwnBinStackHeight();
        if (ownStackHeight != null)
            this.ownStackHeight.setValue(ownStackHeight);
        Integer othersStackHeight = data.getOthersBinStackHeight();
        if (othersStackHeight != null)
            this.othersStackHeight.setValue(othersStackHeight);
        String[] loadingMethodNames = getResources().getStringArray(R.array.loading_methods);
        String[] loadingMethods = data.getLoadingMethods();
        if (loadingMethods != null) {
            List<String> methods = Arrays.asList(loadingMethods);
            for (int i = 0; i < loadingMethodCheckBoxes.length; i++) {
                loadingMethodCheckBoxes[i].setChecked(methods.contains(loadingMethodNames[i]));
            }
        }
        Integer numStacks = data.getNumStacks();
        if (numStacks != null)
            this.numStacks.setValue(numStacks);
        Integer coopertitionHeight = data.getCoopertitionHeight();
        if (coopertitionHeight != null)
            coopertition.setValue(coopertitionHeight);
        Boolean straightensBin = data.getStraightensBin();
        if (straightensBin != null)
            straightBin.setChecked(straightensBin);
        Boolean manipulatesLitter = data.getManipulatesLitter();
        if (manipulatesLitter != null)
            manipLitter.setChecked(manipulatesLitter);
        Boolean upsideDownTotes = data.getUpsideDownTotes();
        if (upsideDownTotes != null)
            this.upsideDownTotes.setChecked(upsideDownTotes);
        Double driverPracticeTime = data.getDriverPracticeTime();
        if (driverPracticeTime != null)
            driverPractice.setText("" + driverPracticeTime);
        Double humanPlayerPracticeTime = data.getHumanPlayerPracticeTime();
        if (humanPlayerPracticeTime != null)
            humanPractice.setText("" + humanPlayerPracticeTime);
    }

    @Override
    public void viewToModel(ScoutingData data, List<String> missing) {
        data.setTeleopTotes(teleopTotes.getValue());
        data.setOthersBinStackHeight(othersStackHeight.getValue());
        data.setOwnBinStackHeight(ownStackHeight.getValue());
        data.setOthersBinStackHeight(othersStackHeight.getValue());
        List<String> loadingMethods = new ArrayList<>();
        String[] loadingMethodNames = getResources().getStringArray(R.array.loading_methods);
        for (int i = 0; i < loadingMethodCheckBoxes.length; ++i) {
            if (loadingMethodCheckBoxes[i].isChecked()) {
                loadingMethods.add(loadingMethodNames[i]);
            }
        }
        data.setLoadingMethods(loadingMethods.toArray(new String[loadingMethods.size()]));
        data.setNumStacks(numStacks.getValue());
        data.setCoopertitionHeight(coopertition.getValue());
        data.setStraightensBin(straightBin.isChecked());
        data.setManipulatesLitter(manipLitter.isChecked());
        data.setUpsideDownTotes(upsideDownTotes.isChecked());
        Resources res = getResources();
        try {
            data.setDriverPracticeTime(Double.parseDouble(driverPractice.getText().toString()));
        } catch (NumberFormatException e) {
            missing.add(res.getString(R.string.label_driver_practice));
        }
        try {
            data.setHumanPlayerPracticeTime(Double.parseDouble(humanPractice.getText().toString()));
        } catch (NumberFormatException e) {
            missing.add(res.getString(R.string.label_human_practice));
        }
    }
}
