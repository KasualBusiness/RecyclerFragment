package biz.kasual.recyclerfragment.callbacks;

/**
 * Created by Stephen Vinouze on 10/11/2015.
 */
public abstract class ClickCallback {

    public abstract void onItemClick(int position);

    public boolean onItemLongClick(int position) {
        return false;
    }

}
