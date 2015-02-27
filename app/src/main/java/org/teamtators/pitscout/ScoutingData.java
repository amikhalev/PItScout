package org.teamtators.pitscout;

import android.content.Context;
import android.os.Environment;

import org.teamtators.pitscout.ui.SignInActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class ScoutingData {
    private int teamNumber;
    private String pitContact;
    private String driveTrain;
    private String[] wheels;
    private double width;
    private double length;
    private double height;
    private double weight;
    private boolean robotSet;
    private int autoTotes;
    private int autoBins;
    private String startingPosition;
    private String[] loadingMethods;
    private int teleopTotes;
    private int teleopBins;
    private boolean straightensBin;
    private boolean manipulatesLitter;

    public ScoutingData() {
    }

    public static String getCsvHeader() {
        return "Team Number,Pit Contact,Drive Train,Wheels,Width,Length,Height,Weight," +
                "Robot Set,Auto Totes,Auto Bins,Starting Position,Loading Methods,Tote Height,Bin Height," +
                "Straightens Bin,Manipulates Litter";
    }

    public void setTeamNumber(int teamNumber) {
        this.teamNumber = teamNumber;
    }

    public void setDriveTrain(String driveTrain) {
        this.driveTrain = driveTrain;
    }

    public void setWheels(String[] wheels) {
        this.wheels = wheels;
    }

    public void setPitContact(String pitContact) {
        this.pitContact = pitContact;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setRobotSet(boolean robotSet) {
        this.robotSet = robotSet;
    }

    public void setAutoTotes(int autoTotes) {
        this.autoTotes = autoTotes;
    }

    public void setAutoBins(int autoBins) {
        this.autoBins = autoBins;
    }

    public void setStartingPosition(String startingPosition) {
        this.startingPosition = startingPosition;
    }

    public void setLoadingMethods(String[] loadingMethods) {
        this.loadingMethods = loadingMethods;
    }

    public void setTeleopTotes(int teleopTotes) {
        this.teleopTotes = teleopTotes;
    }

    public void setTeleopBins(int teleopBins) {
        this.teleopBins = teleopBins;
    }

    public void setStraightensBin(boolean straightensBin) {
        this.straightensBin = straightensBin;
    }

    public void setManipulatesLitter(boolean manipulatesLitter) {
        this.manipulatesLitter = manipulatesLitter;
    }

    public String toCsvLine() {
        final StringBuilder sb = new StringBuilder();
        sb.append(teamNumber);
        sb.append(',').append(pitContact);
        sb.append(',').append(driveTrain);
        sb.append(',');
        for (int i = 0; i < wheels.length; ++i) {
            if (i > 0)
                sb.append(" & ");
            sb.append(wheels[i]);
        }
        sb.append(',').append(width);
        sb.append(',').append(length);
        sb.append(',').append(height);
        sb.append(',').append(weight);
        sb.append(',').append(robotSet);
        sb.append(',').append(autoTotes);
        sb.append(',').append(autoBins);
        sb.append(',').append(startingPosition);
        sb.append(',');
        for (int i = 0; i < loadingMethods.length; ++i) {
            if (i > 0)
                sb.append(" & ");
            sb.append(loadingMethods[i]);
        }
        sb.append(',').append(teleopTotes);
        sb.append(',').append(teleopBins);
        sb.append(',').append(straightensBin);
        sb.append(',').append(manipulatesLitter);
        return sb.toString();
    }

    public void appendToFile(Context context) throws IOException {
        String competition = context
                .getSharedPreferences(SignInActivity.PREFERENCES_NAME, Context.MODE_PRIVATE)
                .getString(SignInActivity.KEY_COMPETITION, "");
        String scoutingFileName = String.format("%s/%s/%s_scouting.csv",
                Environment.getExternalStorageDirectory(), context.getPackageName(), competition);
        File scoutingFile = new File(scoutingFileName);
        OutputStreamWriter writer;
        if (!scoutingFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            scoutingFile.getParentFile().mkdirs();
            //noinspection ResultOfMethodCallIgnored
            scoutingFile.createNewFile();
            writer = new OutputStreamWriter(new FileOutputStream(scoutingFile));
            writer.write(ScoutingData.getCsvHeader());
            writer.write('\n');
        } else {
            writer = new OutputStreamWriter(new FileOutputStream(scoutingFile, true));
        }
        writer.write(toCsvLine());
        writer.write('\n');
        writer.close();
    }
}
