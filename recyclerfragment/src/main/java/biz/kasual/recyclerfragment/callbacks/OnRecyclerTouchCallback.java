package biz.kasual.recyclerfragment.callbacks;

/**
 * Created by Stephen Vinouze on 10/11/2015.
 */
public abstract class OnRecyclerTouchCallback {

    public boolean canMoveAt(int position) {
        return true;
    }

    public boolean canSwipeAt(int position) {
        return true;
    }

    public abstract boolean onMoved(int beginPosition, int endPosition);
    public abstract void onSwiped(int position, int direction);
}
