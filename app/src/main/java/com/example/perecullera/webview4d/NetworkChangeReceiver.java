package com.example.perecullera.webview4d;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by perecullera on 13/2/15.
 */
public class NetworkChangeReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(final Context context, final Intent intent) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE );
        if ( activeNetInfo != null )
        {
            String msg = context.getString(R.string.activeNetStr);
            Toast.makeText( context, msg + activeNetInfo.getTypeName(), Toast.LENGTH_SHORT ).show();
        }
        if( mobNetInfo != null )
        {
            String msg = context.getString(R.string.mobNetStr);
            Toast.makeText( context, msg + mobNetInfo.getTypeName(), Toast.LENGTH_SHORT ).show();
        }if (activeNetInfo == null){
            NetworkUtil nt = new NetworkUtil();
            nt.showNoConnectionDialog(context);
        }
    }
}
