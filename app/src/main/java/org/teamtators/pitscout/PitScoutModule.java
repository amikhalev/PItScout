package org.teamtators.pitscout;

import org.teamtators.pitscout.ui.AutoFragment;
import org.teamtators.pitscout.ui.BasicRobotFragment;
import org.teamtators.pitscout.ui.CommentsActivity;
import org.teamtators.pitscout.ui.ScoutingActivity;
import org.teamtators.pitscout.ui.SettingsActivity;
import org.teamtators.pitscout.ui.SignInActivity;
import org.teamtators.pitscout.ui.TeleopFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by alex on 2/21/15.
 */
@Module(
        injects = {
                SignInActivity.class,
                ScoutingActivity.class,
                SettingsActivity.class,
                BasicRobotFragment.class,
                AutoFragment.class,
                TeleopFragment.class,
                CommentsActivity.class
        },
        complete = false
)
public class PitScoutModule {
    @Provides
    @Singleton
    public TeamList provideTeamList() {
        return new TeamList();
    }

    @Provides
    @Singleton
    public DataManager provideDataManager() {
        return new DataManager();

    }
}
