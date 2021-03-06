package org.teamtators.pitscout;

import android.content.Context;
import android.os.Environment;

import org.teamtators.pitscout.ui.SignInActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class ScoutingData {
    private Integer teamNumber;
    private String pitContact;
    private String driveTrain;
    private String[] wheels;
    private Double width;
    private Double length;
    private Double height;
    private Double weight;
    private Boolean drivesOnPlatform;
    private Boolean stuckOnNoodle;
    private Boolean robotSet;
    private Integer autoTotes;
    private Integer autoRcs;
    private String startingPosition;
    private Integer teleopTotes;
    private Integer selfRcStackHeight;
    private Integer partnerRcStackHeight;
    private String[] loadingMethods;
    private Integer numStacks;
    private Integer coopertitionHeight;
    private Boolean sidewaysBin;
    private Boolean manipulatesLitter;
    private Boolean upsideDownTotes;
    private Double driverPracticeTime;
    private Double humanPlayerPracticeTime;
    private String comments;
    private Integer autoStepRcs;

    public ScoutingData() {
    }

    public static String getCsvHeader() {
        return "Team Number,Pit Contact,Drive Train,Wheels,Width,Length,Height,Weight," +
                "Drives on Platform,Stuck on Noodle,Robot Set,Auto Totes,Auto RCs," +
                "Starting Position,Teleop Tote Height,Self RC Stack Height,Partner RC Stack Height," +
                "Loading Methods,Number of Stacks,Coopertition Height,Picks Sideways Bin," +
                "Manipulates Litter,Upside Down Totes,Driver Practice Time," +
                "Human Player Practice Time,Comments,Auto Step RCs,Scouter Name";
    }

    public Integer getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(Integer teamNumber) {
        this.teamNumber = teamNumber;
    }

    public String getPitContact() {
        return pitContact;
    }

    public void setPitContact(String pitContact) {
        this.pitContact = pitContact;
    }

    public String getDriveTrain() {
        return driveTrain;
    }

    public void setDriveTrain(String driveTrain) {
        this.driveTrain = driveTrain;
    }

    public String[] getWheels() {
        return wheels;
    }

    public void setWheels(String[] wheels) {
        this.wheels = wheels;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Boolean getDrivesOnPlatform() {
        return drivesOnPlatform;
    }

    public void setDrivesOnPlatform(Boolean drivesOnPlatform) {
        this.drivesOnPlatform = drivesOnPlatform;
    }

    public Boolean getStuckOnNoodle() {
        return stuckOnNoodle;
    }

    public void setStuckOnNoodle(Boolean stuckOnNoodle) {
        this.stuckOnNoodle = stuckOnNoodle;
    }

    public Boolean getRobotSet() {
        return robotSet;
    }

    public void setRobotSet(Boolean robotSet) {
        this.robotSet = robotSet;
    }

    public Integer getAutoTotes() {
        return autoTotes;
    }

    public void setAutoTotes(Integer autoTotes) {
        this.autoTotes = autoTotes;
    }

    public Integer getAutoRcs() {
        return autoRcs;
    }

    public void setAutoRcs(Integer autoBins) {
        this.autoRcs = autoBins;
    }

    public String getStartingPosition() {
        return startingPosition;
    }

    public void setStartingPosition(String startingPosition) {
        this.startingPosition = startingPosition;
    }

    public Integer getTeleopTotes() {
        return teleopTotes;
    }

    public void setTeleopTotes(Integer teleopTotes) {
        this.teleopTotes = teleopTotes;
    }

    public Integer getSelfRcStackHeight() {
        return selfRcStackHeight;
    }

    public void setSelfRcStackHeight(Integer selfRcStackHeight) {
        this.selfRcStackHeight = selfRcStackHeight;
    }

    public Integer getPartnerRcStackHeight() {
        return partnerRcStackHeight;
    }

    public void setPartnerRcStackHeight(Integer partnerRcStackHeight) {
        this.partnerRcStackHeight = partnerRcStackHeight;
    }

    public String[] getLoadingMethods() {
        return loadingMethods;
    }

    public void setLoadingMethods(String[] loadingMethods) {
        this.loadingMethods = loadingMethods;
    }

    public Integer getNumStacks() {
        return numStacks;
    }

    public void setNumStacks(Integer numStacks) {
        this.numStacks = numStacks;
    }

    public Integer getCoopertitionHeight() {
        return coopertitionHeight;
    }

    public void setCoopertitionHeight(Integer coopertitionHeight) {
        this.coopertitionHeight = coopertitionHeight;
    }

    public Boolean getSidewaysBin() {
        return sidewaysBin;
    }

    public void setSidewaysBin(Boolean sidewaysBin) {
        this.sidewaysBin = sidewaysBin;
    }

    public Boolean getManipulatesLitter() {
        return manipulatesLitter;
    }

    public void setManipulatesLitter(Boolean manipulatesLitter) {
        this.manipulatesLitter = manipulatesLitter;
    }

    public Boolean getUpsideDownTotes() {
        return upsideDownTotes;
    }

    public void setUpsideDownTotes(Boolean upsideDownTotes) {
        this.upsideDownTotes = upsideDownTotes;
    }

    public Double getDriverPracticeTime() {
        return driverPracticeTime;
    }

    public void setDriverPracticeTime(Double driverPracticeTime) {
        this.driverPracticeTime = driverPracticeTime;
    }

    public Double getHumanPlayerPracticeTime() {
        return humanPlayerPracticeTime;
    }

    public void setHumanPlayerPracticeTime(Double humanPlayerPracticeTime) {
        this.humanPlayerPracticeTime = humanPlayerPracticeTime;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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
        sb.append(',').append(drivesOnPlatform);
        sb.append(',').append(stuckOnNoodle);
        sb.append(',').append(robotSet);
        sb.append(',').append(autoTotes);
        sb.append(',').append(autoRcs);
        sb.append(',').append(startingPosition);
        sb.append(',').append(teleopTotes);
        sb.append(',').append(selfRcStackHeight);
        sb.append(',').append(partnerRcStackHeight);
        sb.append(',');
        for (int i = 0; i < loadingMethods.length; ++i) {
            if (i > 0)
                sb.append(" & ");
            sb.append(loadingMethods[i]);
        }
        sb.append(',').append(numStacks);
        sb.append(',').append(coopertitionHeight);
        sb.append(',').append(sidewaysBin);
        sb.append(',').append(manipulatesLitter);
        sb.append(',').append(upsideDownTotes);
        sb.append(',').append(driverPracticeTime);
        sb.append(',').append(humanPlayerPracticeTime);
        sb.append(',').append(comments);
        sb.append(',').append(autoStepRcs);
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
        String scouterName = context.getSharedPreferences(SignInActivity.PREFERENCES_NAME, Context.MODE_PRIVATE)
                .getString(SignInActivity.KEY_SCOUTER_NAME, "");
        writer.write(',' + scouterName);
        writer.write('\n');
        writer.close();
    }

    public Integer getAutoStepRcs() {
        return autoStepRcs;
    }

    public void setAutoStepRcs(Integer autoStepRcs) {
        this.autoStepRcs = autoStepRcs;
    }
}
