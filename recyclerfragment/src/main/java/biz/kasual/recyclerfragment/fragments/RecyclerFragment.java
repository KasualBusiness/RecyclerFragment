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
import biz.kasual.recyclerfragment.interfaces.OnRecyclerTouchListener;
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

    public void configureGestures(int dragDirections, int swipeDirections, @Nullable final OnRecyclerTouchListener touchListener) {
        if (mRefreshableRecyclerView != null) {
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(dragDirections, swipeDirections) {
                @Override
                public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                    int position = viewHolder.getLayoutPosition();
                    return (mSectionAdapter != null && mSectionAdapter.isSectionAt(position)) ? 0 : super.getSwipeDirs(recyclerView, viewHolder);
                }

                @Override
                public int getDragDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                    int position = viewHolder.getLayoutPosition();
                    return (mSectionAdapter != null && mSectionAdapter.isSectionAt(position)) ? 0 : super.getDragDirs(recyclerView, viewHolder);
                }

                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    int beginPosition = viewHolder.getLayoutPosition();
                    int endPosition = target.getLayoutPosition();
                    if (mSectionAdapter != null) {
                        beginPosition = mSectionAdapter.sectionPositionToPosition(beginPosition);
                        endPosition = mSectionAdapter.sectionPositionToPosition(endPosition);
                    }

                    mAdapter.moveItem(beginPosition, endPosition);

                    return touchListener != null && touchListener.onMove(recyclerView, beginPosition, endPosition);
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                    int position = viewHolder.getLayoutPosition();
                    if (mSectionAdapter != null) {
                        position = mSectionAdapter.sectionPositionToPosition(position);
                    }

                    mAdapter.removeItem(position);

                    if (touchListener != null) {
                        touchListener.onSwiped(position, swipeDir);
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
