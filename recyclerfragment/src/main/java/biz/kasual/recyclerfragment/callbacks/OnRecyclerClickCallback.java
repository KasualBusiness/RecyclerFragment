package biz.kasual.recyclerfragment.callbacks;

/**
 * Created by Stephen Vinouze on 10/11/2015.
 */
public abstract class OnRecyclerClickCallback<T> {
    public abstract void onItemClick(T t);
    public abstract boolean onItemLongClick(T t);
}
