package com.toaker.framework.core.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;


import java.lang.reflect.Field;

/**
 * A utility class for calculating view real size according to the screen size.
 * Created by lizhennian on 2014/5/29.
 */
public class ScaleController {
    /**
     * the base screen size and density.
     */
    private static final float BASE_SCREEN_WIDTH = 1920.0f;
    private static final float BASE_SCREEN_HEIGHT = 1080.0f;
    private static final float BASE_SCREEN_DENSITY = 1.5f;

    /**
     * the current system screen size and density.
     */
    private static float mCurScreenWidth = 1280.0f;
    private static float mCurScreenHeight = 720.0f;
    private static float mCurScreenDensity = 1.5f;

    private static ScaleController mInstance;

    private ScaleController(Context context) {
        this.getScreenSize(context);
    }

    /**
     * This method must be invoked in onCreate method of the Application class.
     *
     * @param context
     * @return
     */
    public static ScaleController init(Context context) {
        if (context == null) {
            throw new IllegalArgumentException();
        }
        if (ScaleController.mInstance == null) {
            synchronized (ScaleController.class) {
                if (ScaleController.mInstance == null) {
                    ScaleController.mInstance = new ScaleController(context);
                }
            }
        }

        return ScaleController.mInstance;
    }

    public static ScaleController getInstance() {
        if (ScaleController.mInstance == null) {
//            throw new IllegalStateException("Not initialized!");
            return null;
        }

        return ScaleController.mInstance;
    }

    /**
     * The current absolute width of the display in pixels.
     *
     * @return
     */
    public final float getScreenWidth() {
        return ScaleController.mCurScreenWidth;
    }

    /**
     * The current absolute height of the display in pixels.
     *
     * @return
     */
    public final float getScreenHeight() {
        return ScaleController.mCurScreenHeight;
    }

    /**
     * Scale the font size
     *
     * @param pixel
     * @return
     */
    public final int scaleTextSize(float pixel) {
        if (pixel < 0.0F) {
            return 0;
        }
        if (this.isBaseSize()) {
            return (int) pixel;
        }
        if (ScaleController.mCurScreenWidth / ScaleController.BASE_SCREEN_WIDTH >= ScaleController.mCurScreenHeight
                / ScaleController.BASE_SCREEN_HEIGHT) {
            return this.scaleHeight((int) pixel);
        }
        return this.scaleWidth((int) pixel);
    }

    /**
     * Scale the ViewGroup attributes.
     *
     * @param viewGroup
     */
    public final void scaleViewGroup(ViewGroup viewGroup) {
        if (viewGroup == null || this.isBaseSize()) {
            return;
        }

        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            this.scaleView(viewGroup.getChildAt(i));
        }
    }

    /**
     * Scale the view attributes.
     *
     * @param view
     */
    public final void scaleView(View view) {
        if (view == null || this.isBaseSize()) {
            return;
        }

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null) {
            int height = layoutParams.height;
            int width = layoutParams.width;
            if (height > 0 && !this.isBaseHeight()) {
                height = this.scaleHeight(height);
                layoutParams.height = height;
            }
            if (width > 0 && !this.isBaseWidth()) {
                width = this.scaleWidth(width);
                layoutParams.width = width;
            }
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                this.scaleMargin((ViewGroup.MarginLayoutParams) layoutParams);
            }
        }
        this.scalePadding(view);
        this.scaleMinSize(view);
    }

    private final void scaleMargin(
            ViewGroup.MarginLayoutParams marginLayoutParams) {
        if (marginLayoutParams == null || this.isBaseSize()) {
            return;
        }
        if (!this.isBaseWidth()) {
            if (marginLayoutParams.leftMargin != 0) {
                marginLayoutParams.leftMargin = this
                        .scaleWidth(marginLayoutParams.leftMargin);
            }
            if (marginLayoutParams.rightMargin != 0) {
                marginLayoutParams.rightMargin = this
                        .scaleWidth(marginLayoutParams.rightMargin);
            }
        }

        if (!this.isBaseHeight()) {
            if (marginLayoutParams.topMargin != 0) {
                marginLayoutParams.topMargin = this
                        .scaleHeight(marginLayoutParams.topMargin);
            }
            if (marginLayoutParams.bottomMargin != 0) {
                marginLayoutParams.bottomMargin = this
                        .scaleHeight(marginLayoutParams.bottomMargin);
            }
        }
    }

    private final int reflectMinWH(TextView textView, String field) {
        Field f;
        try {
            f = TextView.class.getDeclaredField(field);
            f.setAccessible(true);
            return (Integer) f.get(textView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private final int reflectMinWH(View view, String field) {
        Field f;
        try {
            f = View.class.getDeclaredField(field);
            f.setAccessible(true);
            return (Integer) f.get(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @SuppressLint("NewApi")
    private final int getMinWidth(TextView textView) {
        if (Build.VERSION.SDK_INT < 16) {
            return this.reflectMinWH(textView, "mMinWidth");
        } else {
            return textView.getMinWidth();
        }
    }

    @SuppressLint("NewApi")
    private final int getMinHeight(TextView textView) {
        if (Build.VERSION.SDK_INT < 16) {
            return this.reflectMinWH(textView, "mMinimum");
        } else {
            return textView.getMinHeight();
        }
    }

    @SuppressLint("NewApi")
    private final int getMinimumWidth(View view) {
        if (Build.VERSION.SDK_INT < 16) {
            return this.reflectMinWH(view, "mMinWidth");
        } else {
            return view.getMinimumWidth();
        }
    }

    @SuppressLint("NewApi")
    private final int getMinimumHeight(View view) {
        if (Build.VERSION.SDK_INT < 16) {
            return this.reflectMinWH(view, "mMinHeight");
        } else {
            return view.getMinimumHeight();
        }
    }

    private final void scaleMinSize(View view) {
        if (view == null || this.isBaseSize()) {
            return;
        }
        if (view instanceof TextView) {
            if (!this.isBaseWidth()) {
                int miniWidth = this.getMinWidth((TextView) view);
                if (miniWidth > 0) {
                    miniWidth = this.scaleWidth(miniWidth);
                    ((TextView) view).setMinWidth(miniWidth);
                }
            }
            if (!this.isBaseHeight()) {
                int miniHeight = this.getMinHeight((TextView) view);
                if (miniHeight > 0) {
                    miniHeight = this.scaleHeight(miniHeight);
                    ((TextView) view).setMinHeight(miniHeight);
                }
            }
        }
        if (!this.isBaseWidth()) {
            int minimumWidth = this.getMinimumWidth(view);
            if (minimumWidth > 0) {
                minimumWidth = this.scaleWidth(minimumWidth);
                view.setMinimumWidth(minimumWidth);
            }
        }
        if (!this.isBaseHeight()) {
            int minimumHeight = this.getMinimumHeight(view);
            if (minimumHeight > 0) {
                minimumHeight = this.scaleHeight(minimumHeight);
                view.setMinimumHeight(minimumHeight);
            }
        }
    }

    public final void scalePadding(View view) {
        if (view == null || this.isBaseSize()) {
            return;
        }
        int paddingLeft = view.getPaddingLeft();
        int paddingTop = view.getPaddingTop();
        int paddingRight = view.getPaddingRight();
        int paddingBottom = view.getPaddingBottom();

        if (!this.isBaseWidth()) {
            if (paddingLeft > 0) {
                paddingLeft = this.scaleWidth(paddingLeft);
            }
            if (paddingRight > 0) {
                paddingRight = this.scaleWidth(paddingRight);
            }
        }
        if (!this.isBaseHeight()) {
            if (paddingBottom > 0) {
                paddingBottom = this.scaleHeight(paddingBottom);
            }
            if (paddingTop > 0) {
                paddingTop = this.scaleHeight(paddingTop);
            }
        }

        view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    public final int scaleWidth(int pixel) {
        return Math.round(this.getBaseSize(pixel)
                * ScaleController.mCurScreenWidth
                / ScaleController.BASE_SCREEN_WIDTH);
    }

    public final int scaleHeight(int pixel) {
        return Math.round(this.getBaseSize(pixel)
                * ScaleController.mCurScreenHeight
                / ScaleController.BASE_SCREEN_HEIGHT);
    }

    /**
     * Get the base size in pixels.
     *
     * @param pixel
     * @return
     */
    private final int getBaseSize(float pixel) {
        return (int) (0.5F + pixel / ScaleController.mCurScreenDensity
                * ScaleController.BASE_SCREEN_DENSITY);
    }

    private final boolean isBaseSize() {
        return (this.isBaseHeight()) && (this.isBaseWidth());
    }

    private final boolean isBaseHeight() {
        return (ScaleController.mCurScreenHeight
                / ScaleController.BASE_SCREEN_HEIGHT == ScaleController.mCurScreenDensity
                / ScaleController.BASE_SCREEN_DENSITY);
    }

    private final boolean isBaseWidth() {
        return (ScaleController.mCurScreenWidth
                / ScaleController.BASE_SCREEN_WIDTH == ScaleController.mCurScreenDensity
                / ScaleController.BASE_SCREEN_DENSITY);
    }

    /**
     * Handle non-standard height size.
     *
     * @param hight
     * @return
     */
    private final int correctHeight(int hight) {
        if ((hight >= 672) && (hight <= 720)) {
            hight = 720;
        }
        return hight;
    }

    private final void getScreenSize(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(displayMetrics);
        ScaleController.mCurScreenDensity = displayMetrics.density;
        if (displayMetrics.widthPixels <= displayMetrics.heightPixels) {
            ScaleController.mCurScreenWidth = displayMetrics.heightPixels;
            ScaleController.mCurScreenHeight = this
                    .correctHeight(displayMetrics.widthPixels);
        } else {
            ScaleController.mCurScreenWidth = displayMetrics.widthPixels;
            ScaleController.mCurScreenHeight = this
                    .correctHeight(displayMetrics.heightPixels);
        }
    }

    /**
     * 控件的大小设置
     *
     * @param view              控件的id
     * @param wScreenProportion 宽度所占屏幕宽度的百分比(0.0 ~ 1.0)
     * @param wProportion       宽度的比例
     * @param hProportion       高度的比例
     * @return
     */
    public void scaleView(View view, float wScreenProportion, float wProportion, float hProportion) {
        if (view != null) {
            int width = (int) (getScreenWidth() * wScreenProportion);
            int height = (int) ((width / wProportion) * hProportion);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params == null) {
                params = new ViewGroup.LayoutParams(width, height);
            } else {
                params.width = width;
                params.height = height;
            }
            view.setLayoutParams(params);
        }
    }
}
