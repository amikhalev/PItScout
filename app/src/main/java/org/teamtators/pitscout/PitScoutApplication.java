package org.teamtators.pitscout;

import android.app.Application;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

/**
 * Created by alex on 2/21/15.
 */
public class PitScoutApplication extends Application {
    private ObjectGraph graph;

    @Override
    public void onCreate() {
        super.onCreate();

        graph = ObjectGraph.create(getModules().toArray());
    }

    private List<Object> getModules() {
        return Arrays.asList(
                new AndroidModule(this),
                new PitScoutModule()
        );
    }

    public void inject(Object object) {
        graph.inject(object);
    }
}
