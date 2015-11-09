package biz.kasual.recyclerfragmentsample.adapters;

import android.content.Context;

import biz.kasual.recyclerfragment.adapters.RecyclerSectionAdapter;
import biz.kasual.recyclerfragmentsample.models.Sample;

/**
 * Created by Stephen Vinouze on 09/11/2015.
 */
public class SampleSectionTitleAdapter extends RecyclerSectionAdapter<Sample> {

    public SampleSectionTitleAdapter(Context context) {
        super(context);
    }

    @Override
    public String titleForSection(int sectionPosition) {
        Sample firstSampleInSection = getFirstItemInSection(sectionPosition);
        return "Section with rate " + firstSampleInSection.getRate();
    }
}
