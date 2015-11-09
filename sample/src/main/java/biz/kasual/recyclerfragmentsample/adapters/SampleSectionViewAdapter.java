package biz.kasual.recyclerfragmentsample.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import biz.kasual.recyclerfragment.adapters.RecyclerSectionAdapter;
import biz.kasual.recyclerfragmentsample.models.Sample;
import biz.kasual.recyclerfragmentsample.views.SampleSectionItemView;

/**
 * Created by Stephen Vinouze on 09/11/2015.
 */
public class SampleSectionViewAdapter extends RecyclerSectionAdapter<Sample> {

    public SampleSectionViewAdapter(Context context) {
        super(context);
    }

    @Override
    public View onCreateSectionItemView(ViewGroup parent, int viewType) {
        return new SampleSectionItemView(mContext);
    }

    @Override
    public void onBindSectionItemView(View v, int sectionPosition) {
        SampleSectionItemView sampleSectionItemView = (SampleSectionItemView)v;
        sampleSectionItemView.bind(getFirstItemInSection(sectionPosition));
    }
}
