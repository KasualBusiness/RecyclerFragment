package biz.kasual.recyclerfragmentsample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import biz.kasual.recyclerfragment.callbacks.OnRecyclerFetchCallback;
import biz.kasual.recyclerfragment.fragments.RecyclerFragment;
import biz.kasual.recyclerfragmentsample.R;
import biz.kasual.recyclerfragmentsample.adapters.SampleAdapter;
import biz.kasual.recyclerfragmentsample.models.Sample;

/**
 * Created by Stephen Vinouze on 06/11/2015.
 */
public class PaginationRecyclerFragment extends RecyclerFragment<Sample> {

    private SwipeRefreshLayout mRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.pagination_recycler_layout, container, false);

        RecyclerView recyclerView = (RecyclerView)contentView.findViewById(R.id.recycler_view);
        mRefreshLayout = (SwipeRefreshLayout)contentView.findViewById(R.id.refresh_layout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fillList(1);
            }
        });

        SampleAdapter sampleAdapter = new SampleAdapter(getActivity());

        configureFragment(recyclerView, sampleAdapter);
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

        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public String sortSectionMethod() {
        return null;
    }
}
