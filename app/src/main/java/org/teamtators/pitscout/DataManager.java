package org.teamtators.pitscout;

/**
 * Created by alex on 3/4/15.
 */
public class DataManager {
    private ScoutingData data;

    public DataManager() {

    }

    public ScoutingData getData() {
        if (data == null) resetData();
        return data;
    }

    public void resetData() {
        data = new ScoutingData();
    }
}
