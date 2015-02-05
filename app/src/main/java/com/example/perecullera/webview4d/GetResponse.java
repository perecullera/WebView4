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
            conexion.setConnectTimeout(3000);
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
        return respons;
    }

    @Override
    protected void onPostExecute(Integer result) {
        dialog.dismiss();
    }

/*
    public boolean is404(String string) {
        boolean bo;

        try {

            url = new URL(string);
            conexion = (HttpURLConnection) url.openConnection();
            conexion.setConnectTimeout(3000);
            Log.i("connection", "opened");
            conexion.connect();
            conexion.setRequestMethod("HEAD");
            int respons = conexion.getResponseCode();
            Log.i("connection", "getresponse code");
            bo = (respons == HttpURLConnection.HTTP_OK);
            if (bo) {
                return true;

            } else {

                Log.i("MyActivity", "Error :" + respons);
                return false;

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            //TODO
        } catch (IOException e) {
            e.printStackTrace();
            //TODO
        }
        return false;
    }
*/
}
   /* //carrega un html d'error guardat a els arxius de l'app
    private void carregaHtml() {
        //TODO
        webview.loadUrl("file:///android_asset/error.html");

    }

    public void carregaWeb(){

        fullurl = valueOf(Uri.parse(fullurl));
        webview.loadUrl(fullurl);
    }
}*/
