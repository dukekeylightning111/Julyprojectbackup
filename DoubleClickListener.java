package com.example.mysportfriends_school_project;


import android.view.View;

public class DoubleClickListener implements View.OnClickListener {
    private static final long DOUBLE_CLICK_TIME_DELTA = 300;
    private long lastClickTime = 0;
    private final OnDoubleClickListener onDoubleClickListener;

    public DoubleClickListener(OnDoubleClickListener onDoubleClickListener) {
        this.onDoubleClickListener = onDoubleClickListener;
    }

    @Override
    public void onClick(View view) {
        long clickTime = System.currentTimeMillis();
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
            onDoubleClickListener.onDoubleClick(view);
        }
        lastClickTime = clickTime;
    }

    public interface OnDoubleClickListener {
        void onDoubleClick(View view);
    }
}