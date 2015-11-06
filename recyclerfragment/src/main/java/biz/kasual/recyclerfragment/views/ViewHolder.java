package biz.kasual.recyclerfragment.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Stephen Vinouze on 04/11/2015.
 */
public class ViewHolder extends RecyclerView.ViewHolder {

    private View mView;

    public ViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public View getView() {
        return mView;
    }

}
