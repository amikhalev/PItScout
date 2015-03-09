package org.teamtators.pitscout;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by alex on 2/21/15.
 */
public abstract class PitScoutBaseFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((PitScoutApplication) getActivity().getApplication()).inject(this);
    }
}
