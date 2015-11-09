package biz.kasual.recyclerfragmentsample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import biz.kasual.recyclerfragmentsample.adapters.SampleAdapter;
import biz.kasual.recyclerfragmentsample.models.Sample;
import retrofit.Callback;

/**
 * Created by Stephen Vinouze on 06/11/2015.
 */
public class SectionRecyclerFragment extends AbstractRecyclerFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = super.onCreateView(inflater, container, savedInstanceState);

        configureFragment(mRecyclerView, mSampleAdapter);

        List<Sample> samples = SampleAdapter.buildSamples();

        Collections.sort(samples, new Comparator<Sample>() {
            @Override
            public int compare(Sample lhs, Sample rhs) {
                try {
                    return lhs.getRate() - rhs.getRate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

        displayItems(samples);

        return contentView;
    }

    @Override
    public void buildRequest(Map<String, Object> params, Callback<List<Sample>> callback) {}

    @Override
    public String sortSectionMethod() {
        return "getRate";
    }
}
