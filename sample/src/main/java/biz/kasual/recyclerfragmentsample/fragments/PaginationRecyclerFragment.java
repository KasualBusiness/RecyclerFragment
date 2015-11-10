package biz.kasual.recyclerfragmentsample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import biz.kasual.recyclerfragment.callbacks.OnRecyclerFetchCallback;
import biz.kasual.recyclerfragmentsample.adapters.SampleAdapter;
import biz.kasual.recyclerfragmentsample.models.Sample;

/**
 * Created by Stephen Vinouze on 06/11/2015.
 */
public class PaginationRecyclerFragment extends AbstractRecyclerFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = super.onCreateView(inflater, container, savedInstanceState);

        configureFragment(mRecyclerView, mSampleAdapter);
        configurePagination(new OnRecyclerFetchCallback<Sample>() {
            @Override
            public void fetchNextPage(int nextPage) {
                fillList(nextPage);
            }
        });

        fillList(1);

        return contentView;
    }

    private void fillList(int page) {
        List<Sample> samples = SampleAdapter.buildSamples();

        displayItems(samples, page);
    }

    @Override
    public String sortSectionMethod() {
        return null;
    }
}
