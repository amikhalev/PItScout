package org.teamtators.pitscout;

import android.content.Context;
import android.net.Uri;
import android.preference.PreferenceManager;

import org.teamtators.pitscout.ui.SettingsActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 2/19/15.
 */
public class TeamList {
    private List<Team> teamList;

    public TeamList() {

    }

    private List<Team> readTeamList(Reader r) throws IOException, ParseException {
        List<Team> result = new ArrayList<>();
        BufferedReader reader = new BufferedReader(r);
        int offset = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"); // magic csv regex!
            if (parts.length != 2)
                throw new ParseException("Expected two parts in line", offset);
            String name = parts[1];
            int number;
            try {
                number = Integer.parseInt(parts[0]);
            } catch (NumberFormatException e) {
                throw new ParseException("Invalid team number", offset);
            }
            result.add(new Team(number, name));
            offset += line.length();
        }
        return result;
    }

    public void loadTeamList(Context context) throws IOException, ParseException, SecurityException {
        String fileName = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(SettingsActivity.TEAM_LIST_FILE_PREFERENCE, SettingsActivity.TEAM_LIST_FILE_DEFUALT);
        Uri uri = Uri.parse("file://" + fileName);
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        teamList = readTeamList(new InputStreamReader(inputStream));
    }

    public List<Team> getTeamList(Context context) throws IOException, ParseException {
        if (teamList == null) {
            loadTeamList(context);
        }
        return teamList;
    }

    public static class Team {
        private int number;
        private String name;

        public Team(int number, String name) {
            this.number = number;
            this.name = name;
        }

        public int getNumber() {
            return number;
        }

        public String getName() {
            return name;
        }
    }
}
