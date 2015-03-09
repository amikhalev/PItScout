package org.teamtators.pitscout.ui;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import org.teamtators.pitscout.PitScoutBaseFragment;
import org.teamtators.pitscout.R;
import org.teamtators.pitscout.ScoutingData;
import org.teamtators.pitscout.ScoutingDataView;
import org.teamtators.pitscout.TeamList;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;


public class BasicRobotFragment extends PitScoutBaseFragment implements ScoutingDataView {
    public static final List<Integer> DEFAULT_TEAM_LIST = Arrays.asList(0, 254, 1717, 2122);
    public static final String TAG = "BasicRobotFragment";
    public static final String KEY_DATA = "scoutingData";
    @InjectView(R.id.team_number)
    protected Spinner teamNumber;
    @InjectView(R.id.pit_contact)
    protected EditText pitContact;
    @InjectView(R.id.drive_train)
    protected AutoCompleteTextView driveTrain;
    @InjectViews({R.id.wheel_traction, R.id.wheel_omni, R.id.wheel_mecanum, R.id.wheel_tank_tread})
    protected CheckBox[] wheelCheckBoxes;
    @InjectView(R.id.robot_width)
    protected EditText robotWidth;
    @InjectView(R.id.robot_length)
    protected EditText robotLength;
    @InjectView(R.id.robot_height)
    protected EditText robotHeight;
    @InjectView(R.id.robot_weight)
    protected EditText robotWeight;
    @InjectView(R.id.drive_platform)
    protected Switch drivePlatform;
    @InjectView(R.id.stuck_on_noodle)
    protected Switch stuckOnNoodle;
    @Inject
    protected Context context;
    @Inject
    protected TeamList teamList;
    private View view;
    private ArrayAdapter<Integer> teamNumberAdapter;

    public BasicRobotFragment() {
    }

    public static BasicRobotFragment newInstance() {
        BasicRobotFragment fragment = new BasicRobotFragment();
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
        view = inflater.inflate(R.layout.fragment_basic_robot, container, false);
        ButterKnife.inject(this, view);

        updateTeamList();

        ArrayAdapter<String> driveTrainAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.drive_trains));
        driveTrain.setAdapter(driveTrainAdapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        view = null;
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        if (view != null)
            updateTeamList();
        super.onStart();
    }

    private void updateTeamList() {
        List<Integer> teamNumbers;
        try {
            List<TeamList.Team> teams = teamList.getTeamList(context);
            teamNumbers = new ArrayList<>();
            teamNumbers.add(0);
            for (TeamList.Team team : teams)
                teamNumbers.add(team.getNumber());
        } catch (IOException | SecurityException e) {
            Log.w(TAG, "Error loading team list", e);
            Toast.makeText(context, getResources().getString(R.string.team_list_io_error), Toast.LENGTH_LONG)
                    .show();
            teamNumbers = DEFAULT_TEAM_LIST;
        } catch (ParseException e) {
            Log.w(TAG, "Error loading team list", e);
            Toast.makeText(context, getResources().getString(R.string.team_list_parse_error), Toast.LENGTH_LONG)
                    .show();
            teamNumbers = DEFAULT_TEAM_LIST;
        }
        Collections.sort(teamNumbers);
        teamNumberAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item,
                teamNumbers);
        teamNumber.setAdapter(teamNumberAdapter);
    }

    @Override
    public void modelToView(ScoutingData data) {
        teamNumber.setSelection(teamNumberAdapter.getPosition(data.getTeamNumber()));
        pitContact.setText(data.getPitContact());
        driveTrain.setText(data.getDriveTrain());
        String[] wheelNames = getResources().getStringArray(R.array.wheels);
        String[] wheels = data.getWheels();
        if (wheels != null) {
            List<String> wheels_ = Arrays.asList(wheels);
            for (int i = 0; i < wheelCheckBoxes.length; i++) {
                wheelCheckBoxes[i].setChecked(wheels_.contains(wheelNames[i]));
            }
        }
        Double width = data.getWidth();
        if (width != null)
            robotWidth.setText("" + width);
        Double length = data.getLength();
        if (length != null)
            robotLength.setText("" + length);
        Double height = data.getHeight();
        if (height != null)
            robotHeight.setText("" + height);
        Double weight = data.getWeight();
        if (weight != null)
            robotWeight.setText("" + weight);
        Boolean drivesOnPlatform = data.getDrivesOnPlatform();
        if (drivesOnPlatform != null)
            drivePlatform.setChecked(drivesOnPlatform);
        Boolean stuckOnNoodles = data.getStuckOnNoodle();
        if (stuckOnNoodles != null)
            stuckOnNoodle.setChecked(stuckOnNoodles);
    }

    @Override
    public void viewToModel(ScoutingData data, List<String> missing) {
        Resources res = getResources();
        Integer teamNumber = (Integer) this.teamNumber.getSelectedItem();
        if (teamNumber.equals(0)) {
            missing.add(res.getString(R.string.label_team_number));
        } else {
            data.setTeamNumber(teamNumber);
        }
        String pitCont = pitContact.getText().toString();
        if (pitCont.isEmpty()) {
            missing.add(res.getString(R.string.label_pit_contact));
        }
        data.setPitContact(pitCont);
        data.setDriveTrain(driveTrain.getText().toString());
        List<String> wheels = new ArrayList<>();
        String[] wheelNames = res.getStringArray(R.array.wheels);
        for (int i = 0; i < wheelCheckBoxes.length; ++i) {
            if (wheelCheckBoxes[i].isChecked()) {
                wheels.add(wheelNames[i]);
            }
        }
        if (wheels.size() == 0) {
            missing.add(res.getString(R.string.label_wheel_type));
        }
        data.setWheels(wheels.toArray(new String[wheels.size()]));
        try {
            data.setWidth(Double.parseDouble(robotWidth.getText().toString()));
        } catch (NumberFormatException e) {
            missing.add(res.getString(R.string.label_robot_width));
        }
        try {
            data.setLength(Double.parseDouble(robotLength.getText().toString()));
        } catch (NumberFormatException e) {
            missing.add(res.getString(R.string.label_robot_length));
        }
        try {
            data.setHeight(Double.parseDouble(robotHeight.getText().toString()));
        } catch (NumberFormatException e) {
            missing.add(res.getString(R.string.label_robot_height));
        }
        try {
            data.setWeight(Double.parseDouble(robotWeight.getText().toString()));
        } catch (NumberFormatException e) {
            missing.add(res.getString(R.string.label_robot_weight));
        }
        data.setDrivesOnPlatform(drivePlatform.isChecked());
        data.setStuckOnNoodle(stuckOnNoodle.isChecked());
    }
}
