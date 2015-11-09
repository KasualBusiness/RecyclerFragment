package biz.kasual.recyclerfragment.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

import biz.kasual.recyclerfragment.R;

/**
 * Created by Stephen Vinouze on 09/11/2015.
 */
public class BasicCardView extends CardView {

    public BasicCardView(Context context) {
        super(context);
        initView();
    }

    public BasicCardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    private void initView() {
        setUseCompatPadding(true);
        setCardElevation(8);
        setRadius(5);

        int[] attrs = new int[] { R.attr.selectableItemBackground};
        TypedArray ta = getContext().obtainStyledAttributes(attrs);
        Drawable drawableFromTheme = ta.getDrawable(0);
        setForeground(drawableFromTheme);
        ta.recycle();
    }

}
