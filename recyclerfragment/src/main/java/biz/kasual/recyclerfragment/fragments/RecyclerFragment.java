package biz.kasual.recyclerfragment.fragments;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import biz.kasual.recyclerfragment.adapters.RecyclerAdapter;
import biz.kasual.recyclerfragment.adapters.RecyclerSectionAdapter;
import biz.kasual.recyclerfragment.callbacks.OnRecyclerTouchCallback;
import biz.kasual.recyclerfragment.views.RefreshableRecyclerView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Stephen Vinouze on 09/11/2015.
 */
public abstract class RecyclerFragment<T> extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String PAGE_QUERY_KEY = "page";
    private static final String PER_PAGE_QUERY_KEY = "per_page";

    private boolean mHasNextPage;
    private boolean mIsLoading;
    private boolean mIsPaginable;
    private boolean mIsRefreshable;
    private int mCurrentPage = 1;
    private int mPerPage = 20;
    private Map<String, Object> mFetchParams;
    private Callback<List<T>> mCallback;
    private RefreshableRecyclerView mRefreshableRecyclerView;
    private RecyclerAdapter<T> mAdapter;
    private RecyclerSectionAdapter<T> mSectionAdapter;

    public abstract String sortSectionMethod();
    public abstract void buildRequest(Map<String, Object> params, Callback<List<T>> callback);

    public RecyclerAdapter<T> getAdapter() {
        return mAdapter;
    }

    public RefreshableRecyclerView getKBRecyclerView() {
        return mRefreshableRecyclerView;
    }

    public void configureFragment(@NonNull RefreshableRecyclerView refreshableRecyclerView, @NonNull RecyclerAdapter<T> adapter) {
        configureFragment(refreshableRecyclerView, adapter, null);
    }

    /**
     * The primary method to configure your fragment by indicating your inflated RefrRefreshableRecyclerView and your RecyclerAdapter<T>
     * You may provide as well a RecyclerSectionAdapter<T> depending of your section customization needs
     * Note that this method must be called before attempting to display any item on the list. Attempting so will throw a IllegalStateException
     * @param refreshableRecyclerView The refreshable RecyclerView that you are using in your layout
     * @param adapter Your adapter overriding RecyclerAdapter<T>
     * @param sectionAdapter Your optional section adapter overriding RecyclerSectionAdapter<T>
     */
    public void configureFragment(@NonNull RefreshableRecyclerView refreshableRecyclerView, @NonNull RecyclerAdapter<T> adapter, @Nullable RecyclerSectionAdapter<T> sectionAdapter) {
        mRefreshableRecyclerView = refreshableRecyclerView;
        mAdapter = adapter;

        RecyclerView recyclerView = mRefreshableRecyclerView.getRecyclerView();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

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

        enablePagination(false);
        enableRefresh(false);
    }

    /**
     * Enable your list to be paginable. It is based on the perPage parameters that you may need to set in order to make it work (default is 20)
     * This perPage parameter MUST MATCH your requirements as it is based on this paramater to determine whether there is another page to be fetched
     * Note that pagination will be ignore whether you are using sections. Same if you are using a LayoutManager that does not extend LinearLayoutManager.
     * @see #setPerPage(int perPage)
     * @param isPaginable A boolean to enable pagination
     */
    public void enablePagination(boolean isPaginable) {
        if (mRefreshableRecyclerView != null) {

            mIsPaginable = isPaginable;

            if (sortSectionMethod() != null && isPaginable) {

                RecyclerView recyclerView = mRefreshableRecyclerView.getRecyclerView();
                final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

                if (layoutManager instanceof LinearLayoutManager) {
                    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            if (!mIsLoading && mHasNextPage && (((LinearLayoutManager) layoutManager).findLastVisibleItemPosition()) > getPaginationTrigger(layoutManager.getItemCount())) {
                                fetchNextPage();
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

    /**
     * Enable the PullToRefresh feature directly embedded inside the RefreshableRecyclerView
     */
    public void enableRefresh(boolean isRefreshable) {
        if (mRefreshableRecyclerView != null) {

            mIsRefreshable = isRefreshable;

            SwipeRefreshLayout refreshLayout = mRefreshableRecyclerView.getRefreshLayout();
            if (isRefreshable) {
                refreshLayout.setEnabled(true);
                refreshLayout.setOnRefreshListener(this);
            }
            else {
                refreshLayout.setEnabled(false);
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
        if (mRefreshableRecyclerView != null) {
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
            itemTouchHelper.attachToRecyclerView(mRefreshableRecyclerView.getRecyclerView());
        }
        else {
            throw new IllegalStateException("The fragment has not been initialized. Use configureFragment() method");
        }
    }

    public int getPerPage() {
        return mPerPage;
    }

    public void setPerPage(int perPage) {
        mPerPage = perPage;
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
     * Display your items inside your configured adapter and let it fill it depending on the pagination configuration
     * @param newResults The items to be displayed in your list
     */
    public void displayItems(@Nullable List<T> newResults) {
        if (mAdapter != null) {

            if (newResults != null) {
                if (mCurrentPage == 1) {
                    mAdapter.setItems(newResults);
                }
                else {
                    mAdapter.addItems(newResults, mAdapter.getItemCount());
                }

                mHasNextPage = (newResults.size() == mPerPage);
            }
            else {
                mHasNextPage = false;
            }

            if (mIsRefreshable) {
                mRefreshableRecyclerView.getRefreshLayout().setRefreshing(false);
            }

            if (mIsPaginable && mCurrentPage > 1) {
                // TODO : Remove footer loader view
            }
        }
        else {
            throw new IllegalStateException("The fragment has not been initialized. Use configureFragment() method");
        }
    }

    public void fetchItems(@Nullable Map<String, Object> params, Callback<List<T>> callback) {
        mFetchParams = params;
        mCallback = callback;

        fetchItemsAtPage(1);
    }

    private void fetchItemsAtPage(final int page) {
        if (!mIsLoading) {
            mIsLoading = true;
            mCurrentPage = page;

            final Map<String, Object> params = new HashMap<>();

            if (mIsPaginable) {
                params.put(PAGE_QUERY_KEY, Integer.toString(page));
                params.put(PER_PAGE_QUERY_KEY, Integer.toString(mPerPage));
            }

            if (mFetchParams != null && !mFetchParams.isEmpty()) {
                params.putAll(mFetchParams);
            }

            buildRequest(params, new Callback<List<T>>() {
                @Override
                public void success(List<T> items, Response response) {
                    mIsLoading = false;

                    if (mCallback != null) {
                        mCallback.success(items, response);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    mIsLoading = false;

                    if (mCallback != null) {
                        mCallback.failure(error);
                    }
                }
            });
        }
    }

    private void fetchNextPage() {
        // TODO : Add footer loader view
        fetchItemsAtPage(mCurrentPage + 1);
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

    @Override
    public void onRefresh() {
        fetchItemsAtPage(1);
    }

}
