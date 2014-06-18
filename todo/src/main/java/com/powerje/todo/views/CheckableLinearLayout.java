package com.powerje.todo.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;

/**
 * Created by jep on 6/15/14.
 */
public class CheckableLinearLayout extends LinearLayout implements Checkable {
    private boolean checked = false;

    public CheckableLinearLayout(Context context) {
        super(context);
    }

    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckableLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
        if (checked) {
            setBackgroundColor(Color.parseColor("#bdc3c7"));
        } else {
            setBackgroundColor(Color.parseColor("#00000000"));
        }
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        checked = !checked;
    }
}
