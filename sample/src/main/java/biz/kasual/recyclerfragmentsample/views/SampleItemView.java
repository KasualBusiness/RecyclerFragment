package biz.kasual.recyclerfragmentsample.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import biz.kasual.recyclerfragment.views.BasicCardView;
import biz.kasual.recyclerfragmentsample.R;
import biz.kasual.recyclerfragmentsample.models.Sample;

/**
 * Created by Stephen Vinouze on 09/11/2015.
 */
public class SampleItemView extends BasicCardView {

    private TextView mNameTextView;
    private TextView mTickIconView;

    public SampleItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initViews(context);
    }

    public SampleItemView(Context context) {
        super(context);
        initViews(context);
    }

    public void bind(Sample sample, boolean isToggled) {
        if (sample != null) {
            mNameTextView.setText(sample.getName());
        }

        mTickIconView.setVisibility(isToggled ? VISIBLE : GONE);
    }

    private void initViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.sample_item_view, this);
        mNameTextView = (TextView) view.findViewById(R.id.sample_item_name_text_view);
        mTickIconView = (TextView) view.findViewById(R.id.sample_item_name_tick_view);
        mTickIconView.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/fontawesome-webfont.ttf"));
    }
}
