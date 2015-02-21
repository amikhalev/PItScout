package org.teamtators.pitscout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public class AutoFragment extends Fragment implements DataPopulator {
    public AutoFragment() {
    }

    public static AutoFragment newInstance() {
        AutoFragment fragment = new AutoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auto, container, false);
        ButterKnife.inject(this, view);



        return view;
    }

    @Override
    public boolean populateScoutingData(ScoutingData data) {
        return true;
    }
}
