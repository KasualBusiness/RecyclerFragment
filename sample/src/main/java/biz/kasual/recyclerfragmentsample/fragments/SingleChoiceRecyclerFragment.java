package biz.kasual.recyclerfragmentsample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import biz.kasual.recyclerfragment.callbacks.ClickCallback;
import biz.kasual.recyclerfragmentsample.adapters.SampleAdapter;
import biz.kasual.recyclerfragmentsample.models.Sample;

/**
 * Created by Stephen Vinouze on 06/11/2015.
 */
public class SingleChoiceRecyclerFragment extends AbstractRecyclerFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = super.onCreateView(inflater, container, savedInstanceState);

        mSampleAdapter.setClickCallback(new ClickCallback() {
            @Override
            public void onItemClick(int position) {
                Sample sample = mSampleAdapter.getItemAt(position);
                Toast.makeText(getActivity(), "Item clicked : " + sample.getName() + " (" + mSampleAdapter.getSelectedItemViewCount() + " selected)", Toast.LENGTH_SHORT).show();
            }
        });

        configureFragment(mRecyclerView, mSampleAdapter);

        List<Sample> samples = SampleAdapter.buildSamples();

        displayItems(samples);

        return contentView;
    }

    @Override
    public String sortSectionMethod() {
        return null;
    }
}
