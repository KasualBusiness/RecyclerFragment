package biz.kasual.recyclerfragmentsample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import biz.kasual.recyclerfragment.fragments.RecyclerFragment;
import biz.kasual.recyclerfragment.views.RefreshableRecyclerView;
import biz.kasual.recyclerfragmentsample.R;
import biz.kasual.recyclerfragmentsample.adapters.SimpleSampleAdapter;
import biz.kasual.recyclerfragmentsample.models.Sample;
import retrofit.Callback;

/**
 * Created by Stephen Vinouze on 06/11/2015.
 */
public class SingleChoiceRecyclerFragment extends RecyclerFragment<Sample> {

    private RefreshableRecyclerView mRecyclerView;
    private SimpleSampleAdapter mSimpleSampleAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.single_choice_recycler_layout, container, false);

        mRecyclerView = (RefreshableRecyclerView)contentView.findViewById(R.id.single_choice_recycler_view);
        mSimpleSampleAdapter = new SimpleSampleAdapter(getActivity());

        configureFragment(mRecyclerView, mSimpleSampleAdapter);

        List<Sample> samples = new ArrayList<>();
        Sample sample;
        for (int i = 0; i < 20; i++) {
            sample = new Sample();
            sample.setId(i);
            sample.setName("Sample N°" + (i + 1));
            samples.add(sample);
        }

        displayItems(samples);

        return contentView;
    }

    @Override
    public void buildRequest(Map<String, Object> params, Callback<List<Sample>> callback) {}

    @Override
    public String sortSectionMethod() {
        return null;
    }
}
