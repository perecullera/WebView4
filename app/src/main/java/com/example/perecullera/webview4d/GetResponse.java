package com.example.perecullera.webview4d;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by perecullera on 3/2/15.
 */
public class GetResponse extends AsyncTask<String, Integer, Integer> {
    URL url;
    HttpURLConnection conexion;
    MainActivity activity;
    ProgressDialog dialog;

    String port;
    String server;
    String fullurl;

    private Context context;

    WebView webview = activity.webView;

    public GetResponse(MainActivity activity) {
        this.activity = activity;
        context = activity;
        dialog = new ProgressDialog(context);

        port = activity.port;
        server = activity.server;
        fullurl = activity.fullurl;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Loading...");
        dialog.show();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        dialog.setProgress(progress[0]);
    }

    @Override
    protected Integer doInBackground(String... strings) {
        boolean bo;
        int respons = 0;
        try {

            url = new URL(strings[0]);
            conexion = (HttpURLConnection) url.openConnection();
            conexion.setConnectTimeout(10000);
            Log.i("connection", "opened");
            //conexion.connect();
            conexion.setRequestMethod("HEAD");
            respons = conexion.getResponseCode();
            Log.i("connection", "getresponse code");
            bo = (respons == HttpURLConnection.HTTP_OK);
            if (bo) {
                return respons;

            } else {

                Log.i("MyActivity", "Error :" + respons);
                return respons;

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println(e);
            //TODO
        } catch (IOException e) {
            e.printStackTrace();
            //TODO
            System.out.println(e);
        }
        //close hhttpurlconnection to fix buf of recicling closed socket
        finally {
            conexion.disconnect();
        }
        return respons;
    }

    @Override
    protected void onPostExecute(Integer result) {
        dialog.dismiss();
    }
}
