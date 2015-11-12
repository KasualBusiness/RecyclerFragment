package biz.kasual.recyclerfragment.fragments;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.List;

import biz.kasual.recyclerfragment.adapters.RecyclerAdapter;
import biz.kasual.recyclerfragment.adapters.RecyclerSectionAdapter;
import biz.kasual.recyclerfragment.callbacks.OnRecyclerFetchCallback;
import biz.kasual.recyclerfragment.callbacks.OnRecyclerTouchCallback;

/**
 * Created by Stephen Vinouze on 09/11/2015.
 */
public abstract class RecyclerFragment<T> extends Fragment {

    private boolean mHasNextPage;
    private boolean mIsLoading;
    private int mCurrentPage = 1;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter<T> mAdapter;
    private RecyclerSectionAdapter<T> mSectionAdapter;

    public abstract String sortSectionMethod();

    public void configureFragment(@NonNull RecyclerView recyclerView, @NonNull RecyclerAdapter<T> adapter) {
        configureFragment(recyclerView, adapter, null);
    }

    /**
     * The primary method to configure your fragment by indicating your inflated RefrRefreshableRecyclerView and your RecyclerAdapter<T>
     * You may provide as well a RecyclerSectionAdapter<T> depending of your section customization needs
     * Note that this method must be called before attempting to display any item on the list. Attempting so will throw a IllegalStateException
     * @param recyclerView The RecyclerView that you are using in your layout
     * @param adapter Your adapter overriding RecyclerAdapter<T>
     * @param sectionAdapter Your optional section adapter overriding RecyclerSectionAdapter<T>
     */
    public void configureFragment(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerAdapter<T> adapter,
                                  @Nullable RecyclerSectionAdapter<T> sectionAdapter) {
        mRecyclerView = recyclerView;
        mAdapter = adapter;

        recyclerView.setHasFixedSize(true);

        String sortName = sortSectionMethod();
        if (sortName != null) {

            if (sectionAdapter == null) {
                sectionAdapter = new RecyclerSectionAdapter<>(getActivity());
            }

            mSectionAdapter = sectionAdapter;

            sectionAdapter.setBaseAdapter(adapter);
            sectionAdapter.setSortMethodName(sortName);
            recyclerView.setAdapter(sectionAdapter);
        }
        else {
            recyclerView.setAdapter(adapter);
        }

        setLayoutManager(new LinearLayoutManager(getActivity()));
        setItemAnimator(new DefaultItemAnimator());
    }

    public void setLayoutManager(@NonNull RecyclerView.LayoutManager layoutManager) {
        if (mRecyclerView != null) {
            mRecyclerView.setLayoutManager(layoutManager);
        }
        else {
            throw new IllegalStateException("The fragment has not been initialized. Use configureFragment() method");
        }
    }

    public void setItemAnimator(@NonNull RecyclerView.ItemAnimator itemAnimator) {
        if (mRecyclerView != null) {
            mRecyclerView.setItemAnimator(itemAnimator);
        }
        else {
            throw new IllegalStateException("The fragment has not been initialized. Use configureFragment() method");
        }
    }

//    /**
//     * Enable your list to be paginable. It is based on the perPage parameters that you may need to set in order to make it work (default is 20)
//     * This perPage parameter MUST MATCH your requirements as it is based on this paramater to determine whether there is another page to be fetched
//     * Note that pagination will be ignore whether you are using sections. Same if you are using a LayoutManager that does not extend LinearLayoutManager.
//     * @see #setPerPage(int perPage)
//     * @param isPaginable A boolean to enable pagination
//     */
    public void configurePagination(@NonNull final OnRecyclerFetchCallback<T> callback) {
        if (mRecyclerView != null) {

            if (sortSectionMethod() == null) {

                final RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();

                if (layoutManager instanceof LinearLayoutManager) {
                    mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            if (!mIsLoading && mHasNextPage && (((LinearLayoutManager) layoutManager).findLastVisibleItemPosition()) > getPaginationTrigger(layoutManager.getItemCount())) {
                                callback.fetchNextPage(++mCurrentPage);
                            }
                        }
                    });
                }
            }
        }
        else {
            throw new IllegalStateException("The fragment has not been initialized. Use configureFragment() method");
        }
    }

    public void configureGestures(int dragDirections, int swipeDirections) {
        configureGestures(dragDirections, swipeDirections, null);
    }

    /**
     * Configure your gestures if any for both move and swipe gestures.
     * You can also implement the OnRecyclerTouchCallback to be notified of the move/swipe events as well as enabling/disabling these gestures for some specific items
     * @param dragDirections The move directions. Can be either ItemTouchHelper.LEFT, ItemTouchHelper.RIGHT, ItemTouchHelper.UP or ItemTouchHelper.DOWN
     * @param swipeDirections The swipe directions. Can be either ItemTouchHelper.LEFT, ItemTouchHelper.RIGHT, ItemTouchHelper.UP or ItemTouchHelper.DOWN
     * @param callback The gesture callbacks
     */
    public void configureGestures(int dragDirections, int swipeDirections, @Nullable final OnRecyclerTouchCallback callback) {
        if (mRecyclerView != null) {
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(dragDirections, swipeDirections) {
                @Override
                public int getDragDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                    int position = viewHolder.getLayoutPosition();
                    boolean isMovable = !(mSectionAdapter != null && mSectionAdapter.isSectionAt(position));

                    if (callback != null) {
                        isMovable = callback.canMoveAt(position);
                    }

                    return isMovable ? super.getDragDirs(recyclerView, viewHolder) : 0;
                }

                @Override
                public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                    int position = viewHolder.getLayoutPosition();
                    boolean isSwipeable = !(mSectionAdapter != null && mSectionAdapter.isSectionAt(position));

                    if (callback != null) {
                       isSwipeable = callback.canSwipeAt(position);
                    }

                    return isSwipeable ? super.getSwipeDirs(recyclerView, viewHolder) : 0;
                }

                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    int fromPosition = viewHolder.getLayoutPosition();
                    int toPosition = target.getLayoutPosition();
                    if (mSectionAdapter != null) {

                        if (mSectionAdapter.isSectionAt(toPosition)) {
                            mSectionAdapter.notifyDataSetChanged();
                            return false;
                        }

                        fromPosition = mSectionAdapter.sectionPositionToPosition(fromPosition);
                        toPosition = mSectionAdapter.sectionPositionToPosition(toPosition);
                    }

                    mAdapter.moveItem(fromPosition, toPosition);

                    return callback == null || callback.onMove(fromPosition, toPosition);
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                    int position = viewHolder.getLayoutPosition();
                    if (mSectionAdapter != null) {
                        position = mSectionAdapter.sectionPositionToPosition(position);
                    }

                    mAdapter.removeItem(position);

                    if (callback != null) {
                        callback.onSwiped(position, swipeDir);
                    }
                }
            });
            itemTouchHelper.attachToRecyclerView(mRecyclerView);
        }
        else {
            throw new IllegalStateException("The fragment has not been initialized. Use configureFragment() method");
        }
    }

    public void displayItems(@Nullable List<T> items) {
        displayItems(items, 1);
    }

    /**
     * Display your items inside your configured adapter and let it fill it depending on the pagination configuration
     * @param items The items to be displayed in your list
     * @param page The page to display
     */
    public void displayItems(@Nullable List<T> items, int page) {
        if (mAdapter != null) {

            mCurrentPage = page;

            if (items != null) {
                if (page == 1) {
                    mAdapter.setItems(items);
                }
                else {
                    mAdapter.addItems(items, mAdapter.getItemCount());
                }

                mHasNextPage = (items.size() > 0);
            }
            else {
                mHasNextPage = false;
            }
        }
        else {
            throw new IllegalStateException("The fragment has not been initialized. Use configureFragment() method");
        }
    }

    public void clearItems() {
        if (mAdapter != null) {
            mAdapter.clearItems();
        }
        else {
            throw new IllegalStateException("The fragment has not been initialized. Use configureFragment() method");
        }
    }

    /**
     * Allow smart pagination to give a smooth user experience while paginating by triggering the pagination given the total amount of items in the list
     * @param totalItemCount Total amount of items in the list
     * @return The computed pagination trigger
     */
    private int getPaginationTrigger(int totalItemCount) {
        float offset = 0.6f;
        if (totalItemCount > 50 && totalItemCount <= 100) {
            offset = 0.7f;
        }
        else if (totalItemCount > 100 && totalItemCount <= 150) {
            offset = 0.8f;
        }
        else if (totalItemCount > 150) {
            offset = 0.9f;
        }
        return (int)Math.floor(offset * totalItemCount);
    }

}
