package com.sillars.imagescroll;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by demo on 3/4/15.
 */
public class MyScrollView extends ScrollView {
//http://stackoverflow.com/questions/8181828/android-detect-when-scrollview-stops-scrolling

    private Runnable scrollerTask;
    private int initialPosition;

    private int newCheck = 100;
    private static final String TAG = "MyScrollView";

    public interface OnScrollStoppedListener {
        void onScrollStopped();
    }

    private OnScrollStoppedListener onScrollStoppedListener;

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

        scrollerTask = new Runnable() {

            public void run() {

                int newPosition = getScrollY();
                if (initialPosition - newPosition == 0) {//has stopped

                    if (onScrollStoppedListener != null) {

                        onScrollStoppedListener.onScrollStopped();
                    }
                } else {
                    initialPosition = getScrollY();
                    MyScrollView.this.postDelayed(scrollerTask, newCheck);
                }
            }
        };
    }

    public void setOnScrollStoppedListener(MyScrollView.OnScrollStoppedListener listener) {
        onScrollStoppedListener = listener;
    }

    public void startScrollerTask() {

        initialPosition = getScrollY();
        MyScrollView.this.postDelayed(scrollerTask, newCheck);
    }

}
