package com.wenjiehe.android_learning;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;


public class MySnap extends SnapHelper {
    private static final String TAG = "MySnap";

    @Nullable
    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {
        if (layoutManager instanceof CustomLayoutManager) {
            return new int[]{0, ((CustomLayoutManager) layoutManager).calculateDistance(targetView)};
        } else {
            throw new RuntimeException();
        }
    }

    @Nullable
    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        CustomLayoutManager flipLayoutManager = (CustomLayoutManager) layoutManager;

        return flipLayoutManager.findSnapView();
    }

    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        if (layoutManager instanceof CustomLayoutManager) {
            return ((CustomLayoutManager) layoutManager).findTargetPosition(velocityY);
        } else {
            throw new RuntimeException();
        }
    }
}
