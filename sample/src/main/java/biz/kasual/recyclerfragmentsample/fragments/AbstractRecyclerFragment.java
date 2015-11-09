package biz.kasual.recyclerfragmentsample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import biz.kasual.recyclerfragment.fragments.RecyclerFragment;
import biz.kasual.recyclerfragment.views.RefreshableRecyclerView;
import biz.kasual.recyclerfragmentsample.R;
import biz.kasual.recyclerfragmentsample.adapters.SampleAdapter;
import biz.kasual.recyclerfragmentsample.models.Sample;

/**
 * Created by Stephen Vinouze on 09/11/2015.
 */
public abstract class AbstractRecyclerFragment extends RecyclerFragment<Sample> {

    protected RefreshableRecyclerView mRecyclerView;
    protected SampleAdapter mSampleAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.recycler_layout, container, false);

        mRecyclerView = (RefreshableRecyclerView)contentView.findViewById(R.id.refreshable_recycler_view);
        mSampleAdapter = new SampleAdapter(getActivity());

        return contentView;
    }

}
