package org.teamtators.pitscout.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.teamtators.pitscout.DataPopulator;
import org.teamtators.pitscout.ForApplication;
import org.teamtators.pitscout.PitScoutBaseFragment;
import org.teamtators.pitscout.R;
import org.teamtators.pitscout.ScoutingData;
import org.teamtators.pitscout.TeamList;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;


public class BasicRobotFragment extends PitScoutBaseFragment implements DataPopulator {
    public static final List<Integer> DEFAULT_TEAM_LIST = Arrays.asList(254, 1717, 2122);
    public static final String TAG = "BasicRobotFragment";
    public static final String[] DRIVE_TRAINS = new String[]{"Tank", "Swerve", "Slide", "Jump"};
    private static final String[] WHEEL_NAMES = {"Traction", "Omni", "Mecanum"};
    @InjectView(R.id.team_number)
    protected Spinner teamNumber;
    @InjectView(R.id.pit_contact)
    protected EditText pitContact;
    @InjectView(R.id.drive_train)
    protected AutoCompleteTextView driveTrain;
    @InjectViews({R.id.wheel_traction, R.id.wheel_omni, R.id.wheel_mecanum})
    protected CheckBox[] wheelCheckboxes;
    @InjectView(R.id.robot_width)
    protected EditText robotWidth;
    @InjectView(R.id.robot_length)
    protected EditText robotLength;
    @InjectView(R.id.robot_height)
    protected EditText robotHeight;
    @Inject
    @ForApplication
    protected Context context;
    @Inject
    protected TeamList teamList;
    private View view;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_basic_robot, container, false);
        ButterKnife.inject(this, view);

        AutoCompleteTextView driveTrain = (AutoCompleteTextView) view.findViewById(R.id.drive_train);
        ArrayAdapter driveTrainAdapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_dropdown_item_1line, DRIVE_TRAINS);
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
            List<TeamList.Team> teams = teamList.getTeamList(getActivity());
            teamNumbers = new ArrayList<>();
            for (TeamList.Team team : teams)
                teamNumbers.add(team.getNumber());
        } catch (IOException | SecurityException e) {
            Log.w(TAG, "Error loading team list", e);
            Toast.makeText(getActivity(), getResources().getString(R.string.team_list_io_error), Toast.LENGTH_LONG)
                    .show();
            teamNumbers = DEFAULT_TEAM_LIST;
        } catch (ParseException e) {
            Log.w(TAG, "Error loading team list", e);
            Toast.makeText(getActivity(), getResources().getString(R.string.team_list_parse_error), Toast.LENGTH_LONG)
                    .show();
            teamNumbers = DEFAULT_TEAM_LIST;
        }
        Collections.sort(teamNumbers);
        ArrayAdapter teamNumberAdapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_dropdown_item_1line, teamNumbers);
        teamNumber.setAdapter(teamNumberAdapter);
    }

    @Override
    public boolean populateScoutingData(ScoutingData data) {
        data.setTeamNumber((Integer) teamNumber.getSelectedItem());
        String pitCont = pitContact.getText().toString();
        if (pitCont.isEmpty()) {
            makeErrorToast("Pit Contact").show();
            return false;
        }
        data.setPitContact(pitCont);
        data.setDriveTrain(driveTrain.getText().toString());
        List<String> wheels = new ArrayList<>();
        for (int i = 0; i < wheelCheckboxes.length; ++i) {
            if (wheelCheckboxes[i].isChecked())
                wheels.add(WHEEL_NAMES[i]);
        }
        if (wheels.size() == 0) {
            makeErrorToast("Wheel Type").show();
            return false;
        }
        data.setWheels(wheels.toArray(new String[wheels.size()]));
        try {
            data.setWidth(Double.parseDouble(robotWidth.getText().toString()));
        } catch (NumberFormatException e) {
            makeErrorToast("Robot Width").show();
            return false;
        }
        try {
            data.setLength(Double.parseDouble(robotLength.getText().toString()));
        } catch (NumberFormatException e) {
            makeErrorToast("Robot Length").show();
            return false;
        }
        try {
            data.setHeight(Double.parseDouble(robotHeight.getText().toString()));
        } catch (NumberFormatException e) {
            makeErrorToast("Robot Height").show();
            return false;
        }
        return true;
    }

    protected Toast makeErrorToast(String missing) {
        return Toast.makeText(getActivity(), "Please enter a valid " + missing, Toast.LENGTH_SHORT);
    }
}
