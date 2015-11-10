package biz.kasual.recyclerfragmentsample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;

import biz.kasual.recyclerfragment.callbacks.OnRecyclerTouchCallback;
import biz.kasual.recyclerfragmentsample.adapters.SampleAdapter;
import biz.kasual.recyclerfragmentsample.models.Sample;
import retrofit.Callback;

/**
 * Created by Stephen Vinouze on 06/11/2015.
 */
public class GestureRecyclerFragment extends AbstractRecyclerFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = super.onCreateView(inflater, container, savedInstanceState);

        configureFragment(mRecyclerView, mSampleAdapter);
        configureGestures(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, new OnRecyclerTouchCallback() {
            @Override
            public boolean onMoved(int beginPosition, int endPosition) {
                return false;
            }

            @Override
            public void onSwiped(int position, int direction) {

            }
        });

        List<Sample> samples = SampleAdapter.buildSamples();

        displayItems(samples);

        return contentView;
    }

    @Override
    public void buildRequest(Map<String, Object> params, Callback<List<Sample>> callback) {}

    @Override
    public String sortSectionMethod() {
        //return null;
        return "getRate";
    }
}
