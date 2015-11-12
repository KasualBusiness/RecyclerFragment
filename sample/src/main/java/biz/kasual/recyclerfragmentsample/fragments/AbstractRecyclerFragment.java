package biz.kasual.recyclerfragmentsample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import biz.kasual.recyclerfragment.callbacks.OnRecyclerClickCallback;
import biz.kasual.recyclerfragment.fragments.RecyclerFragment;
import biz.kasual.recyclerfragmentsample.R;
import biz.kasual.recyclerfragmentsample.adapters.SampleAdapter;
import biz.kasual.recyclerfragmentsample.models.Sample;

/**
 * Created by Stephen Vinouze on 09/11/2015.
 */
public abstract class AbstractRecyclerFragment extends RecyclerFragment<Sample> {

    protected RecyclerView mRecyclerView;
    protected SampleAdapter mSampleAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.recycler_layout, container, false);

        mRecyclerView = (RecyclerView)contentView.findViewById(R.id.recycler_view);

        mSampleAdapter = new SampleAdapter(getActivity());
        mSampleAdapter.setOnRecyclerClickCallback(new OnRecyclerClickCallback<Sample>() {
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

        return contentView;
    }

}
