package biz.kasual.recyclerfragment.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import biz.kasual.recyclerfragment.views.ViewHolder;

/*
 * Created by stephenvinouze on 08/07/2015.
 */
public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    protected List<T> mItems = new ArrayList<>();
    private ChoiceMode mChoiceMode = ChoiceMode.SINGLE_CHOICE;
    private SparseBooleanArray selectedItemViews = new SparseBooleanArray();

    private OnRecyclerItemListener<T> mListener;

    public enum ChoiceMode { SINGLE_CHOICE, MULTIPLE_CHOICE }

    public interface OnRecyclerItemListener<T> {
        void onItemClick(T t);
        boolean onItemLongClick(T t);
    }

    public void setOnRecyclerItemListener(OnRecyclerItemListener<T> listener) {
        mListener = listener;
    }

    public T getItemAt(int position) {
        return mItems.get(position);
    }

    public List<T> getItems() {
        return mItems;
    }

    public void setItems(List<T> items) {
        if (items != null) {
            mItems = items;
            notifyDataSetChanged();
        }
    }

    public void addItems(List<T> items, int position) {
        if (items != null) {
            mItems.addAll(position, items);
            notifyItemRangeInserted(position, items.size());
        }
    }

    public void addItem(T item, int position) {
        if (item != null) {
            mItems.add(position, item);
            notifyItemInserted(position);
        }
    }

    public void moveItem(int from, int to) {
        Collections.swap(mItems, from, to);
        notifyItemMoved(from, to);
    }

    public void removeItem(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
        try {
            notifyItemRangeChanged(position, getItemCount());
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public void clearItems() {
        mItems.clear();
        clearSelectedItemViews();
    }

    public ChoiceMode getChoiceMode() {
        return mChoiceMode;
    }

    public void setChoiceMode(ChoiceMode mChoiceMode) {
        this.mChoiceMode = mChoiceMode;
    }

    public boolean isItemViewToggled(int position) {
        return selectedItemViews.get(position, false);
    }

    public int getSelectedItemViewCount() {
        return selectedItemViews.size();
    }

    public List<Integer> getSelectedItemViews() {
        List<Integer> items = new ArrayList<>();
        for (int i = 0; i < selectedItemViews.size(); i++) {
            items.add(selectedItemViews.keyAt(i));
        }
        return items;
    }

    public void toggleItemView(int position) {
        switch (mChoiceMode) {
            case SINGLE_CHOICE:
                clearSelectedItemViews();
                selectedItemViews.put(position, true);
                break;

            case MULTIPLE_CHOICE:
                if (isItemViewToggled(position)) {
                    selectedItemViews.delete(position);
                }
                else {
                    selectedItemViews.put(position, true);
                }
                break;
        }
        notifyItemChanged(position);
    }

    public void clearSelectedItemViews() {
        selectedItemViews.clear();
        notifyDataSetChanged();
    }

    protected abstract View onCreateItemView(ViewGroup parent, int viewType);
    protected abstract void onBindItemView(View v, int position);

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(onCreateItemView(parent, viewType));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final View itemView = holder.getView();
        final T item = getItemAt(position);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(mChoiceMode == ChoiceMode.SINGLE_CHOICE && isItemViewToggled(position))) {
                    toggleItemView(position);

                    if (mListener != null) {
                        mListener.onItemClick(item);
                    }
                }
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return (mListener != null) && mListener.onItemLongClick(item);
            }
        });

        onBindItemView(itemView, position);
    }

}
