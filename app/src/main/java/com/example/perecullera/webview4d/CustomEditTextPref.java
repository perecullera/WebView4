package com.example.perecullera.webview4d;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by perecullera on 17/2/15.
 */
public class CustomEditTextPref extends EditTextPreference{
    public CustomEditTextPref(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomEditTextPref(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditTextPref(Context context) {
        super(context);
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);
        final Resources res = getContext().getResources();
        final Window window = getDialog().getWindow();
        // Title
        final int titleId = res.getIdentifier("alertTitle", "id", "android");
        final View title = window.findViewById(titleId);
        final int grey = res.getColor(android.R.color.white);
        //divider
        int divierId = window.getContext().getResources()
                .getIdentifier("android:id/titleDivider", null, null);
        View divider = window.findViewById(divierId);
        // background
        int titleback =window.getContext().getResources().getIdentifier("android:id/titleBackground", null, null);
        View back =window.findViewById(titleback);
        if (title != null) {
            ((TextView) title).setTextColor(grey);
        }if (divider != null){
            divider.setBackgroundColor(grey);
        }if (back !=null){
            back.setBackgroundColor(grey);
        }
    }
}
