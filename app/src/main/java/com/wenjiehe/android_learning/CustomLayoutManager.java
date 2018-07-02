package com.wenjiehe.android_learning;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

public class CustomLayoutManager extends RecyclerView.LayoutManager implements RecyclerView.SmoothScroller.ScrollVectorProvider {
    private static final String TAG = "CustomLayoutManager";
    private static final int MIN_VY = 300;

    private int mPosition = 0;
    private int mPositionOffset = 0;
    private int mMinVy = 0;
    private Context mContext;
    //private boolean highSpeedScroll =false;

    public CustomLayoutManager(Context context) {
        mContext = context;
        mMinVy = (int) mContext.getResources().getDisplayMetrics().density * MIN_VY;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        final int before = mPosition * getItemHeightPositon() + mPositionOffset;

        mPositionOffset += dy;
        checkPosition(state);
        final int after = mPosition * getItemHeightPositon() + mPositionOffset;

        final int ans = after - before;
        fill(recycler, state);

        return ans;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {

        //如果没有item，直接返回
        if (getItemCount() <= 0) {
            return;
        }
        // 跳过preLayout，preLayout主要用于支持动画
        if (state.isPreLayout()) {
            return;
        }
        fill(recycler, state);
        //在布局之前，将所有的子View先Detach掉，放入到Scrap缓存中
        /*detachAndScrapAttachedViews(recycler);

        //定义竖直方向的偏移量
        int offsetY = 0;

        for (int i = 0; i < getItemCount(); i++) {
            //这里就是从缓存里面取出
            View view = recycler.getViewForPosition(i);
            //将View加入到RecyclerView中
            addView(view);
            //对子View进行测量
            measureChildWithMargins(view, 0, 0);
            //把宽高拿到，宽高都是包含ItemDecorate的尺寸
            int width = getDecoratedMeasuredWidth(view);
            int height = getDecoratedMeasuredHeight(view);
            //最后，将View布局
            layoutDecorated(view, 0, offsetY, width, offsetY + height);
            //将竖直方向偏移量增大height
            offsetY += height;
            totalHeight += height;
        }
        //如果所有子View的高度和没有填满RecyclerView的高度，
        // 则将高度设置为RecyclerView的高度
        totalHeight = Math.max(totalHeight, getVerticalSpace());*/
    }

    private void fill(RecyclerView.Recycler recycler, RecyclerView.State state) {

        checkPosition(state);

        View primary;
        View pre = null;
        View next = null;

        detachAndScrapAttachedViews(recycler);

        primary = recycler.getViewForPosition(mPosition);

        if (mPosition + 1 < state.getItemCount()) {
            next = recycler.getViewForPosition(mPosition + 1);
        }
        if (mPosition - 1 >= 0) {
            pre = recycler.getViewForPosition(mPosition - 1);
        }
        View secondary = null;

        if (mPositionOffset > 0) {
            secondary = next;
        } else if (mPositionOffset < 0) {
            secondary = pre;
        }
        if (mPositionOffset != 0 && secondary != null) {
            addView(secondary);
            measureChildWithMargins(secondary, 0, 0);
            layoutDecorated(secondary, 0, 0, getWidth(), getHeight());
        }

        if (pre != null && secondary !=pre) {
            recycler.recycleView(pre);
        }
        if (next != null && secondary !=next) {
            recycler.recycleView(next);
        }

        addView(primary);
        measureChildWithMargins(primary, 0, 0);
        layoutDecorated(primary, 0, 0, getWidth(), getHeight());

        if (primary instanceof FlipCard && (secondary == null || secondary instanceof FlipCard)) {
            final float percent = (float) mPositionOffset / getItemHeightPositon();
            Log.d(TAG, "fill: "+percent);
            if (secondary != null) {
                ((FlipCard) secondary).setState(false, percent);
                ((FlipCard) primary).setState(true, percent);
            } else {
                ((FlipCard) primary).setState(true, percent);
            }
        } else {
            throw new IllegalStateException("view should be FlipCard");
        }
    }


    private int getItemHeightPositon() {
        return getHeight() * 2 / 3;
    }

    private void checkPosition(RecyclerView.State state) {
        final int itemHeight = getItemHeightPositon();
        final int current = mPosition * itemHeight + mPositionOffset;
        final int max = itemHeight * (state.getItemCount() - 1) + itemHeight * 2 / 5;

        int pos = Math.max(-itemHeight * 2 / 5, Math.min(current, max));
        mPosition = Math.round(pos / (float)itemHeight);
        mPosition = mPosition >= 0 ? mPosition : 0;
        mPositionOffset = pos - mPosition * itemHeight;
    }

    public int getmPosition(){
        return mPosition;
    }

    @Override
    public void scrollToPosition(int position) {
        mPosition = position;
        mPositionOffset = 0;
        requestLayout();
        Log.d(TAG, "scrollToPosition " + position + " position " + mPosition + " positionOffset " + mPositionOffset);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        Log.d(TAG, "smoothScrollTo " + position + " position " + mPosition + " positionOffset " + mPositionOffset);

        FlipScroller scroller = new FlipScroller(recyclerView.getContext());
        scroller.setTargetPosition(position);
        startSmoothScroll(scroller);

    }

    private class FlipScroller extends LinearSmoothScroller {
        private static final String TAG = "FlipScroller";

        public FlipScroller(Context context) {
            super(context);
        }

        @Override
        protected void onStop() {
            super.onStop();
            Log.d(TAG, "onStop: ");
            //highSpeedScroll =false;
        }

        @Override
        public int calculateDyToMakeVisible(View view, int snapPreference) {
            final int position = getPosition(view);
            final int now = mPositionOffset + mPosition * getItemHeightPositon();
            final int to = position * getItemHeightPositon();
            Log.d(TAG, "calculateDyToMakeVisible: position " + position + " ans " + (to - now));
            return (now - to);
        }

        @Override
        public int calculateDxToMakeVisible(View view, int snapPreference) {
            return 0;
        }

        @Override
        protected int calculateTimeForScrolling(int dx) {
               /*
                   控制距离, 然后根据上面那个方(calculateSpeedPerPixel())提供的速度算出时间,

                   默认一次 滚动 TARGET_SEEK_SCROLL_DISTANCE_PX = 10000个像素,

                   在此处可以减少该值来达到减少滚动时间的目的.
                */

            //间接计算时提高速度，也可以直接在calculateSpeedPerPixel提高

            Log.d(TAG, "calculateTimeForScrolling: ");
            int time = super.calculateTimeForScrolling(dx*5);

            return time;
        }

    }

    @Override
    public PointF computeScrollVectorForPosition(int targetPosition) {
        int dir = 0;
        int now = mPosition * getItemHeightPositon() + mPositionOffset;
        int to = targetPosition * getItemHeightPositon();
        if (now > to) {
            dir = -1;
        } else if (now < to) {
            dir = 1;
        }

        Log.d(TAG, "computeScrollVector " + dir + " now " + mPosition + " target " + targetPosition);

        return new PointF(0, dir);
    }
    public int calculateDistance(View view) {
        int pos = getPosition(view);
        final int now = getItemHeightPositon() * mPosition + mPositionOffset;
        final int to = getItemHeightPositon() * pos;

        return to - now;
    }

    public int findTargetPosition(int vY) {
        int ans = mPosition;
        Log.d(TAG, "findTargetPosition: "+vY+"~"+mPositionOffset);
        int absV = vY > 0 ? vY : -vY;
        if (absV > mMinVy) {
            if (vY * mPositionOffset > 0) {
                // 速度与位置偏移同向
                int d = vY > 0 ? 1 : -1;
                ans += d;
            } else {
                ans = mPosition;
            }
        } else {
            ans = mPosition;
        }

        int count = getItemCount();
        if (count == 0) {
            return 0;
        }

        ans = Math.min(count - 1, Math.max(0, ans));
        return ans;
    }

    public View findSnapView() {
        Log.d(TAG, "findSnapView: "+getChildCount()+"~"+getItemCount());
        for (int i = 0;i < getChildCount(); i ++ ){
            View child = getChildAt(i);
            if (getPosition(child) == mPosition) {
                return child;
            }
        }

        return null;
    }
}
