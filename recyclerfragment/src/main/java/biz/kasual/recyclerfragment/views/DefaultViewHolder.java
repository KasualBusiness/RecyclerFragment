package biz.kasual.recyclerfragment.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Stephen Vinouze on 04/11/2015.
 */
public class DefaultViewHolder extends RecyclerView.ViewHolder {

    private View mView;

    public DefaultViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public View getView() {
        return mView;
    }

}
