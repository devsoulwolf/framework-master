/*******************************************************************************
 * Copyright 2013-2014 Toaker XiaoDaoW
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.toaker.framework.core.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.toaker.framework.R;
import com.toaker.framework.core.view.ComplexActionLayout;


/**
 * Decorator for XiaoDaoW
 *
 * author Toaker [Toaker](ToakerQin@gmail.com)
 *         [Toaker](http://www.toaker.com)
 * Time Create by 2015/4/24 11:22
 */
public class TitleBar extends FrameLayout {

    private RelativeLayout mTitleGroup;

    private ComplexActionLayout mActionLeft;

    private ComplexActionLayout    mActionRight;

    private ComplexActionLayout    mRightCenter;

    private TextView       mTitleText;

    private View           mCustomView;

    private int            mDefaultColor = 0xFF4DD487;

    private RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private void initialize() {
        LayoutInflater.from(getContext()).inflate(R.layout.custom_title_bar,this);
        mTitleGroup = (RelativeLayout) findViewById(R.id.xi_title_bar_group);
        mActionLeft = (ComplexActionLayout) findViewById(R.id.xi_title_bar_left_action);
        mActionRight = (ComplexActionLayout) findViewById(R.id.xi_title_bar_right_action);
        mTitleText = (TextView) findViewById(R.id.xi_title_bar_middle_text);
        mRightCenter = (ComplexActionLayout) findViewById(R.id.xi_title_bar_right_action2);
    }

    public void setActionRightCenterResource(int resId){
        mRightCenter.setIconImageResource(resId);
    }

    public void setActionLeftResource(int resId){
        mActionLeft.setIconImageResource(resId);
    }

    public void setActionRightResource(int resId){
        mActionRight.setIconImageResource(resId);
    }

    public void setActionRightCenterVisibility(int visibility) {
        mRightCenter.setVisibility(visibility);
    }
    public void setActionLeftVisibility(int visibility) {
        mActionLeft.setVisibility(visibility);
    }

    public void setActionRightVisibility(int visibility) {
        mActionRight.setVisibility(visibility);
    }

    public void setTitleVisibility(int visibility) {
        mTitleText.setVisibility(visibility);
    }

    public void setActionLeftClickListener(OnClickListener clickListener){
        mActionLeft.setOnClickListener(clickListener);
    }

    public void setActionRightCenterClickListener(OnClickListener clickListener){
        mRightCenter.setOnClickListener(clickListener);
    }

    public void setActionRightClickListener(OnClickListener clickListener){
        mActionRight.setOnClickListener(clickListener);
    }

    public void reset(){
        mActionRight.setIconImageResource(0);
        mActionLeft.setIconImageResource(0);
        mRightCenter.setOnClickListener(null);
        mRightCenter.setIconImageResource(0);
        mActionLeft.setOnClickListener(null);
        mActionRight.setOnClickListener(null);
        if(this.mCustomView != null && this.mCustomView.getParent() != null){
            mTitleGroup.removeView(mCustomView);
        }
    }

    public void resetColor(){
        setBackgroundColor(mDefaultColor);
    }

    public void setTitle(CharSequence title){
        mTitleText.setText(title);
    }

    public void setTitle(int resId){
        mTitleText.setText(resId);
    }

    public void setCustomView(View view){
        if(this.mCustomView != null && this.mCustomView.getParent() != null){
            mTitleGroup.removeView(mCustomView);
        }
        this.mCustomView = view;
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mTitleGroup.addView(this.mCustomView,params);
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        if(mTitleGroup != null){
            mTitleGroup.setBackgroundColor(color);
        }
    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
        if(mTitleGroup != null){
            mTitleGroup.setBackgroundResource(resid);
        }
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        super.setBackgroundDrawable(background);
        if(mTitleGroup != null){
            mTitleGroup.setBackgroundDrawable(background);
        }
    }

    public void setLeftText(CharSequence text){
        mActionLeft.setText(text);
    }

    public void setRightText(CharSequence text){
        mActionRight.setText(text);
    }

    public void setRightCenterText(CharSequence text){
        mRightCenter.setText(text);
    }

    public void show(){
        if(getVisibility() != VISIBLE){
            setVisibility(VISIBLE);
        }
    }

    public void hide(){
        if(getVisibility() != GONE){
            setVisibility(GONE);
        }
    }

    public RelativeLayout getTitleGroup() {
        return mTitleGroup;
    }

    public ComplexActionLayout getActionLeft() {
        return mActionLeft;
    }

    public ComplexActionLayout getActionRight() {
        return mActionRight;
    }

    public ComplexActionLayout getRightCenter() {
        return mRightCenter;
    }

    public TextView getTitleText() {
        return mTitleText;
    }
}
