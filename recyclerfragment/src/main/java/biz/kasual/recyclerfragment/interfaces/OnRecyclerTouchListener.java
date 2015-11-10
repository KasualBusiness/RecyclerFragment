package biz.kasual.recyclerfragment.interfaces;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Stephen Vinouze on 10/11/2015.
 */
public interface OnRecyclerTouchListener {
    boolean onMove(RecyclerView recyclerView, int beginPosition, int endPosition);
    void onSwiped(int position, int direction);
}
