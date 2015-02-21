package org.teamtators.pitscout;

import android.content.Context;
import android.os.Environment;
import android.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 2/19/15.
 */
public class TeamList {
    private static final String DEFAULT_TEAM_LIST_PATH = Environment
            .getExternalStorageDirectory()
            .getAbsolutePath() + "/teamlist.csv";
    private static List<Team> teamList = null;

    public static List<Team> readTeamList(Reader r) throws IOException, ParseException {
        List<Team> result = new ArrayList<>();
        BufferedReader reader = new BufferedReader(r);
        int offset = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length != 2)
                throw new ParseException("Line had more than two parts", offset);
            int number;
            try {
                number = Integer.parseInt(parts[0]);
            } catch (NumberFormatException e) {
                throw new ParseException("Invalid team number", offset);
            }
            String name = parts[1];
            result.add(new Team(number, name));
            offset += line.length();
        }
        return result;
    }

    public static List<Team> loadFromFile(File file) throws IOException, ParseException {
        return readTeamList(new InputStreamReader(new FileInputStream(file)));
    }

    public static List<Team> getTeamList(Context context) throws IOException, ParseException {
        if (teamList == null) {
            String fileName = PreferenceManager
                    .getDefaultSharedPreferences(context)
                    .getString("teamListFileName", DEFAULT_TEAM_LIST_PATH);
            teamList = loadFromFile(new File(fileName));
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

        public void setNumber(int number) {
            this.number = number;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
