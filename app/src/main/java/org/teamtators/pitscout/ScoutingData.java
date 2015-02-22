package org.teamtators.pitscout;

import java.util.Arrays;

public class ScoutingData {
    private int teamNumber;
    private String pitContact;
    private String driveTrain;
    private String[] wheels;
    private double width;
    private double length;
    private double height;
    private boolean robotSet;
    private int autoTotes;
    private int autoBins;

    public ScoutingData() {
    }

    public static String getCsvHeader() {
        return "Team Number,Pit Contact,Drive Train,Wheels,Width,Length,Height,Robot Set,Auto Totes,Auto Bins";
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

    public void setRobotSet(boolean robotSet) {
        this.robotSet = robotSet;
    }

    public void setAutoTotes(int autoTotes) {
        this.autoTotes = autoTotes;
    }

    public void setAutoBins(int autoBins) {
        this.autoBins = autoBins;
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
        sb.append(',').append(robotSet);
        sb.append(',').append(autoTotes);
        sb.append(',').append(autoBins);
        return sb.toString();
    }
}
