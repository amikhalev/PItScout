package org.teamtators.pitscout.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.Spinner;

import org.teamtators.pitscout.PitScoutBaseFragment;
import org.teamtators.pitscout.R;
import org.teamtators.pitscout.ScoutingData;
import org.teamtators.pitscout.ScoutingDataView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AutoFragment extends PitScoutBaseFragment implements ScoutingDataView {
    private static final String KEY_DATA = "scoutingData";
    @InjectView(R.id.robot_set)
    CheckBox robotSet;
    @InjectView(R.id.auto_totes)
    NumberPicker autoTotes;
    @InjectView(R.id.auto_rcs)
    NumberPicker autoRcs;
    @InjectView(R.id.auto_step_rcs)
    NumberPicker autoStepRcs;
    @InjectView(R.id.starting_position)
    Spinner startingPosition;
    private ArrayAdapter<String> startingPositionAdapter;

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
        Bundle arguments = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auto, container, false);
        ButterKnife.inject(this, view);

        autoTotes.setMinValue(0);
        autoTotes.setMaxValue(3);
        autoRcs.setMinValue(0);
        autoRcs.setMaxValue(3);
        autoStepRcs.setMinValue(0);
        autoStepRcs.setMaxValue(4);

        startingPositionAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.starting_positions));
        startingPosition.setAdapter(startingPositionAdapter);

        return view;
    }

    @Override
    public void modelToView(ScoutingData data) {
        Boolean robotSet = data.getRobotSet();
        if (robotSet != null)
            this.robotSet.setChecked(robotSet);
        Integer autoTotes = data.getAutoTotes();
        if (autoTotes != null)
            this.autoTotes.setValue(autoTotes);
        Integer autoRcs = data.getAutoRcs();
        if (autoRcs != null)
            this.autoRcs.setValue(autoRcs);
        Integer autoStepRcs = data.getAutoStepRcs();
        if (autoStepRcs != null)
            this.autoStepRcs.setValue(autoStepRcs);
        startingPosition.setSelection(startingPositionAdapter.getPosition(data.getStartingPosition()));
    }

    @Override
    public void viewToModel(ScoutingData data, List<String> missing) {
        data.setRobotSet(robotSet.isChecked());
        data.setAutoTotes(autoTotes.getValue());
        data.setAutoRcs(autoRcs.getValue());
        data.setAutoStepRcs(autoStepRcs.getValue());
        String startingPosition = (String) this.startingPosition.getSelectedItem();
        data.setStartingPosition(startingPosition);
        if (startingPosition.equals(getResources().getString(R.string.item_select_one))) {
            missing.add(getResources().getString(R.string.label_starting_position));
        }
    }
}
