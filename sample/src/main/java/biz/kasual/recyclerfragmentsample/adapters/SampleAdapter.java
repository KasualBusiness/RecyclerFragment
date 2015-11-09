package biz.kasual.recyclerfragmentsample.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import biz.kasual.recyclerfragment.adapters.RecyclerAdapter;
import biz.kasual.recyclerfragmentsample.models.Sample;
import biz.kasual.recyclerfragmentsample.views.SampleItemView;

/**
 * Created by Stephen Vinouze on 09/11/2015.
 */
public class SampleAdapter extends RecyclerAdapter<Sample> {

    public SampleAdapter(Context context) {
        super(context);
    }

    public static List<Sample> buildSamples() {
        List<Sample> samples = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Sample sample = new Sample();
            sample.setId(i);
            sample.setRate(i < 3 ? 1 : (i > 7 ? 4 : 2));
            sample.setName("Sample N°" + (i + 1));
            samples.add(sample);
        }

        return samples;
    }

    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        return new SampleItemView(mContext);
    }

    @Override
    protected void onBindItemView(View v, int position) {
        SampleItemView sampleItemView = (SampleItemView)v;
        sampleItemView.bind(getItemAt(position));
    }

}
