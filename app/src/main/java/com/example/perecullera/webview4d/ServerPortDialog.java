package com.example.perecullera.webview4d;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by perecullera on 29/1/15.
 */
public class ServerPortDialog extends DialogPreference {

    EditText server;
    EditText port;

    public ServerPortDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPersistent(false);
        setDialogLayoutResource(R.layout.server_port_dialog_layout);
    }
    @Override
    public void onBindDialogView(View view){
        super.onBindDialogView(view);

        server = (EditText)view.findViewById(R.id.ser_dialog);
        port = (EditText)view.findViewById(R.id.port_dialog);

        SharedPreferences sharedPreferences = getSharedPreferences();
        server.setText(sharedPreferences.getString("server", "default"), TextView.BufferType.EDITABLE);
        port.setText(sharedPreferences.getString("port", "default"), TextView.BufferType.EDITABLE);



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
    @Override
    public void onDialogClosed(boolean positiveResult){
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            SharedPreferences.Editor editor = getEditor();
            editor.putString("server", server.getText().toString());
            editor.putString("port", port.getText().toString());
            editor.commit();
        }
    }
}
