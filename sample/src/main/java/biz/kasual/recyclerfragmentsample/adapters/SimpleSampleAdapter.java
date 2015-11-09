package biz.kasual.recyclerfragmentsample.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import biz.kasual.recyclerfragment.adapters.RecyclerAdapter;
import biz.kasual.recyclerfragmentsample.models.Sample;
import biz.kasual.recyclerfragmentsample.views.SampleItemView;

/**
 * Created by Stephen Vinouze on 09/11/2015.
 */
public class SimpleSampleAdapter extends RecyclerAdapter<Sample> {

    public SimpleSampleAdapter(Context context) {
        super(context);
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
