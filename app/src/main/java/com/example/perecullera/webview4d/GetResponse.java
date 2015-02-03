package com.example.perecullera.webview4d;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by perecullera on 3/2/15.
 */
public class GetResponse extends AsyncTask<String, Void, Boolean> {
    @Override
    protected Boolean doInBackground(String... strings) {
        // here you will use the url to access the headers.
        // in this case, the Content-Length one
        URL url;
        HttpURLConnection conexion;
        boolean bo;

        try {

            url = new URL(strings[0]);
            conexion = (HttpURLConnection)url.openConnection();
            conexion.setConnectTimeout(3000);
            Log.i("connection", "opened");
            conexion.connect();
            conexion.setRequestMethod("HEAD");
            int respons = conexion.getResponseCode();
            Log.i("connection", "getresponse code");
            bo = ( respons == HttpURLConnection.HTTP_OK);
            if (bo) {
                return true;

            }else {

                Log.i("MyActivity", "Error :" + respons);
                return false;

            }
        }catch (MalformedURLException e) {
            e.printStackTrace();
            //TODO
        } catch (IOException e) {
            e.printStackTrace();
            //TODO
        }
        return false;
    }
}
