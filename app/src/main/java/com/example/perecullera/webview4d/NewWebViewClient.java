package com.example.perecullera.webview4d;

import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

/**
 * Created by perecullera on 3/2/15.
 */
public class NewWebViewClient extends WebViewClient {

    private boolean error;
    String file = "file:///android_asset/error.html";

    /*@Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

        //debug
        Log.i("WEB_VIEW_TEST", "error code:" + errorCode);
        //Toast.makeText(Context.getContext(), "WEB_VIEW_TEST"+ "error code:" + errorCode, Toast.LENGTH_SHORT).show();
        System.out.println("WEB_VIEW_TEST" + "error code:" + errorCode);

        error = true;

        *//*String file = "file:///android_asset/error.html";
        WebView webview = MainActivity.webView;
        webview.loadUrl(file);*//*
    }
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        if (error) {

            //WebView webview = MainActivity.webView;
            view.loadUrl(file);
            System.out.println("loading :" + file);
        }else{
            error = false;

        }

    }*/

    @Override
    public void onLoadResource (WebView view, String urlConnection){
        boolean response;
        GetResponse getResponse = new GetResponse();
        try {
            response = getResponse.execute(urlConnection).get();
            Log.i("response : " , String.valueOf(response));
            if (response){
                Log.i("open webview  : " , urlConnection);
                super.onLoadResource(view, urlConnection);
            }else if (!response){
                Log.i("open default : " , file);
                view.loadUrl(file);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            //TODO
        } catch (ExecutionException e) {
            e.printStackTrace();
            //TODO
        }
    }

    public String convertToString(InputStream inputStream){
        StringBuffer string = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                string.append(line + "\n");
            }
        } catch (IOException e) {}
        return string.toString();
    }


    }


