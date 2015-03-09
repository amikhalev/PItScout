package org.teamtators.pitscout;

import java.util.List;

public interface ScoutingDataView {
    public void modelToView(ScoutingData data);

    public void viewToModel(ScoutingData data, List<String> missing);
}
