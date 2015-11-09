package biz.kasual.recyclerfragment.views;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import biz.kasual.recyclerfragment.R;

/**
 * Created by Stephen Vinouze on 06/11/2015.
 */
public class RefreshableRecyclerView extends FrameLayout {

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;

    public RefreshableRecyclerView(Context context) {
        super(context);
        initViews(context);
    }

    public RefreshableRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    private void initViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.refreshable_recycler_view, this);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.kb_refresh_layout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.kb_recycler_view);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public SwipeRefreshLayout getRefreshLayout() {
        return mRefreshLayout;
    }

}
