package org.teamtators.pitscout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;


public class BasicRobotFragment extends Fragment implements DataPopulator {
    private static final String[] wheelNames = {"Traction", "Omni", "Mecanum"};
    public static final List<Integer> DEFAULT_TEAM_LIST = Arrays.asList(254, 1717, 2122);
    @InjectView(R.id.team_number)
    protected Spinner teamNumber;
    @InjectView(R.id.pit_contact)
    protected EditText pitContact;
    @InjectView(R.id.drive_train)
    protected AutoCompleteTextView driveTrain;
    @InjectViews({R.id.wheel_traction, R.id.wheel_omni, R.id.wheel_mecanum})
    protected CheckBox[] wheelCheckboxes;
    @InjectView(R.id.robot_width)
    EditText robotWidth;
    @InjectView(R.id.robot_length)
    EditText robotLength;
    @InjectView(R.id.robot_height)
    EditText robotHeight;

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
        View view = inflater.inflate(R.layout.fragment_basic_robot, container, false);
        ButterKnife.inject(this, view);

        List<Integer> teamNumbers;
        try {
            List<TeamList.Team> teams = TeamList.getTeamList(getActivity());
            teamNumbers = new ArrayList<>();
            for (TeamList.Team team : teams)
                teamNumbers.add(team.getNumber());
        } catch (IOException e) {
            Toast.makeText(getActivity(), getResources().getString(R.string.team_list_io_error), Toast.LENGTH_LONG)
                    .show();
            teamNumbers = DEFAULT_TEAM_LIST;
        } catch (ParseException e) {
            Toast.makeText(getActivity(), getResources().getString(R.string.team_list_parse_error), Toast.LENGTH_LONG)
                    .show();
            teamNumbers = DEFAULT_TEAM_LIST;
        }
        ArrayAdapter teamNumberAdapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_dropdown_item_1line, teamNumbers);
        teamNumber.setAdapter(teamNumberAdapter);

        AutoCompleteTextView driveTrain = (AutoCompleteTextView) view.findViewById(R.id.drive_train);
        String[] driveTrains = {"Tank", "Swerve", "Slide", "Jump"};
        ArrayAdapter driveTrainAdapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_dropdown_item_1line, driveTrains);
        driveTrain.setAdapter(driveTrainAdapter);

        return view;
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
                wheels.add(wheelNames[i]);
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
