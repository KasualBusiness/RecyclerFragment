package biz.kasual.recyclerfragment.interfaces;

/**
 * Created by Stephen Vinouze on 10/11/2015.
 */
public interface OnRecyclerItemListener<T> {
    void onItemClick(T t);
    boolean onItemLongClick(T t);
}
