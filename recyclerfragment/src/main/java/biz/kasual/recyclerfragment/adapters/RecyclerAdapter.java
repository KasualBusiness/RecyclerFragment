package biz.kasual.recyclerfragment.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import biz.kasual.recyclerfragment.callbacks.ClickCallback;
import biz.kasual.recyclerfragment.views.DefaultViewHolder;

/**
 * Created by Stephen Vinouze on 09/11/2015.
 */
public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<DefaultViewHolder> {

    protected Context mContext;
    protected List<T> mItems = new ArrayList<>();
    private ChoiceMode mChoiceMode = ChoiceMode.SINGLE_CHOICE;
    private SparseBooleanArray selectedItemViews = new SparseBooleanArray();
    private ClickCallback mCallback;

    public enum ChoiceMode { SINGLE_CHOICE, MULTIPLE_CHOICE }

    public RecyclerAdapter(Context context) {
        mContext = context;
    }

    public void setClickCallback(ClickCallback callback) {
        mCallback = callback;
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
        moveSelectedItemView(from, to);

        Collections.swap(mItems, from, to);
        notifyItemMoved(from, to);
    }

    public void remoteItems(List<T> items, int position) {
        List<Integer> selectedPositions = new ArrayList<>();
        for (T item : items) {
            selectedPositions.add(mItems.indexOf(item));
        }

        removeSelectedItemViews(selectedPositions);

        mItems.removeAll(items);
        notifyItemRangeRemoved(position, items.size());
    }

    public void removeItem(int position) {
        removeSelectedItemView(position);

        mItems.remove(position);
        notifyItemRemoved(position);
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

    public void moveSelectedItemView(int from, int to) {
        if (isItemViewToggled(from) && !isItemViewToggled(to)) {
            selectedItemViews.delete(from);
            selectedItemViews.put(to, true);
        }
        else if (!isItemViewToggled(from) && isItemViewToggled(to)) {
            selectedItemViews.delete(to);
            selectedItemViews.put(from, true);
        }
    }

    public void removeSelectedItemViews(List<Integer> positions) {
        List<Integer> selectedPositions = getSelectedItemViews();
        selectedPositions.removeAll(positions);

        selectedItemViews.clear();
        for (Integer selectedPosition : selectedPositions) {
            selectedItemViews.put(selectedPosition - 1, true);
        }
    }

    public void removeSelectedItemView(int position) {
        List<Integer> selectedPositions = getSelectedItemViews();
        selectedPositions.remove(selectedPositions.indexOf(position));

        selectedItemViews.clear();
        for (Integer selectedPosition : selectedPositions) {
            selectedItemViews.put(position > selectedPosition ? selectedPosition : selectedPosition - 1, true);
        }
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
    public DefaultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DefaultViewHolder(onCreateItemView(parent, viewType));
    }

    @Override
    public void onBindViewHolder(DefaultViewHolder holder, final int position) {
        final View itemView = holder.getView();

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleItemView(position);

                if (mCallback != null) {
                    mCallback.onItemClick(position);
                }
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return (mCallback != null) && mCallback.onItemLongClick(position);
            }
        });

        onBindItemView(itemView, position);
    }

}
