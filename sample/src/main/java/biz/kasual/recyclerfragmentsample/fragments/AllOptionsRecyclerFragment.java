package biz.kasual.recyclerfragmentsample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import biz.kasual.recyclerfragment.callbacks.OnRecyclerClickCallback;
import biz.kasual.recyclerfragment.fragments.RecyclerFragment;
import biz.kasual.recyclerfragmentsample.R;
import biz.kasual.recyclerfragmentsample.adapters.SampleAdapter;
import biz.kasual.recyclerfragmentsample.adapters.SampleSectionViewAdapter;
import biz.kasual.recyclerfragmentsample.models.Sample;

/**
 * Created by Stephen Vinouze on 06/11/2015.
 */
public class AllOptionsRecyclerFragment extends RecyclerFragment<Sample> {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.recycler_layout, container, false);

        RecyclerView recyclerView = (RecyclerView)contentView.findViewById(R.id.recycler_view);
        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout)contentView.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

        SampleAdapter sampleAdapter = new SampleAdapter(getActivity());
        sampleAdapter.setOnRecyclerClickCallback(new OnRecyclerClickCallback<Sample>() {
            @Override
            public void onItemClick(Sample sample) {
                Toast.makeText(getActivity(), "Click detected for item " + sample.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onItemLongClick(Sample sample) {
                Toast.makeText(getActivity(), "Long click detected for item " + sample.getName(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        configureFragment(recyclerView, sampleAdapter, new SampleSectionViewAdapter(getActivity()));
        configureGestures(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);

        List<Sample> samples = SampleAdapter.buildSamples();

        Collections.sort(samples, new Comparator<Sample>() {
            @Override
            public int compare(Sample lhs, Sample rhs) {
                return lhs.getRate() - rhs.getRate();
            }
        });

        displayItems(samples);

        return contentView;
    }

    @Override
    public String sortSectionMethod() {
        return "getRate";
    }
}
