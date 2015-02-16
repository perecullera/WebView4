package com.example.perecullera.webview4d;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by perecullera on 13/2/15.
 */
public class NetworkChangeReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(final Context context, final Intent intent) {

        String status = NetworkUtil.getConnectivityStatusString(context);
        Toast.makeText(context, status, Toast.LENGTH_LONG).show();

        if (status == "Not connected to Internet") {
            NetworkUtil nt = new NetworkUtil();
            nt.showNoConnectionDialog(context);
        }

    }
}
