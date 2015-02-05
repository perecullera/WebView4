package com.example.perecullera.webview4d;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.net.HttpURLConnection;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static java.lang.String.valueOf;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    //views
    private DrawerLayout mDrawer;
    private ListView mDrawerOptions;
    static WebView webView;

    //drawer labels
    String settings ;
    String refresh ;
    String exit ;
    private String[] values;

    //Sharedprefrences and listener
    SharedPreferences sPref;
    SharedPreferences.OnSharedPreferenceChangeListener listener;
    String server;
    String port;

    //urls
    String BaseUrl = "http://";
    String fullurl ;

    //progressdialog
    ProgressDialog pd;
    //progressdialog text
    String pdmessage ;

    //HTML messages
    String invalidUrl ;
    String messServer1;
    String messServer2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //instantiate variables
        settings = getString(R.string.drawer_settings);
        refresh = getString(R.string.drawer_refresh);
        exit =getString(R.string.drawer_exit);
        values = new String[]{settings, refresh, exit};

        pdmessage = getString(R.string.progreesdiag_mess);

        invalidUrl = getString(R.string.messages_invalid);
        messServer1 = getString(R.string.message_server1);
        messServer2 = getString(R.string.message_server2);



        pd =  ProgressDialog.show(this, "", pdmessage, true);

        setContentView(R.layout.activity_web_view);

        //webview
        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                pd.show();
            }
            @Override
            public void onPageFinished (WebView view, String url){
                if (pd.isShowing()) {
                    pd.dismiss();
                }
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){
                @Override
                public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                pd.setProgress(progress * 1000);
                }
        });
        webView.getSettings().setJavaScriptEnabled(true);

        //drawer
        mDrawerOptions = (ListView) findViewById(R.id.left_drawer);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerOptions.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values));
        mDrawerOptions.setOnItemClickListener(this);

        //carreguem preferencies
        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        loadSerPort();


        //és una URL valida?
        if (validUrl()){
            //carreguem web
            carregaWeb();
        }else{
            //carreguem alert i html error
            //TODO
            carregaHtml(invalidUrl);
        }

        //instanciem listener dels canvis a les preferències
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                loadSerPort();
                //és una URL valida?
                if (validUrl()){
                    carregaWeb();
                }else{
                    //carreguem alert i html error
                    //TODO
                    carregaHtml(invalidUrl);
                }
            }
        };
        sPref.registerOnSharedPreferenceChangeListener(listener);

        // amaga actionBar
        getSupportActionBar().hide();

        //debug
        Map<String,?> keys = sPref.getAll();
        for(Map.Entry<String,?> entry : keys.entrySet()){
            Log.d("map values", entry.getKey() + ": " +
                    entry.getValue().toString());
        }
    }
    //carrega preferències a les variables de la classe, si no en troba posa "" com a default
    private void loadSerPort() {
        //TODO
        server = sPref.getString("server", "");
        port = sPref.getString("port", "");

    }

    //carrega un html d'error guardat a els arxius de l'app
    private void carregaHtml(String message) {
        //TODO
        String summary = "<html><body><b>ERROR</b> ";
        String tale = "</body></html>";
        webView.loadData(summary + message + tale, "text/html", null);
        if (pd.isShowing()) {
            pd.dismiss();
        }

    }

    //comprova que el server i port, en cas q n'hi hagi, siguin una URL valida
    // en cas q sigui vàlida la carrega a la variable fullUrl
    public boolean validUrl() {
        String url;
        //TODO
        //cas server i port estiguin buits
        if (port.equals("")&& server.equals("")){
            return false;
        //cas no s'ha introduit port
        }else if (port.equals("")){
            url = BaseUrl + server;
            if (Patterns.WEB_URL.matcher(url).matches()){
                fullurl = url;
                return true;
            }else{
                return false;
            }
        //cas en q s'ha introduit port
        }else if (!port.equals("")&&!server.equals("")) {
            url = BaseUrl + server + ":" + port;
            if (Patterns.WEB_URL.matcher(url).matches()){
                fullurl = url;
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    public void carregaWeb(){

        //debug
        System.out.println("preferencias "+ sPref.toString());
        System.out.println("server "+ server);
        System.out.println("port "+ port);
        //Toast.makeText(this, "preferencias" + sPref.toString(), Toast.LENGTH_SHORT).show();

        fullurl = valueOf(Uri.parse(fullurl));

        if (getResponse(fullurl)){
            webView.loadUrl(fullurl);
            //Toast.makeText(this, "obrint webview amb url: " + fullurl, Toast.LENGTH_SHORT).show();
        }
    }

    public boolean getResponse (String url){
        int response;
        GetResponse getResponse = new GetResponse(this);
        try {
            response = getResponse.execute(url).get();
            Log.i("response : " , String.valueOf(response));
            if (response == HttpURLConnection.HTTP_OK){
                Log.i("returning true  : " + response , url);
                return true;
            }if (response == 0){
                // no hi ha conexió amb el server
                carregaHtml(messServer1);
                return false;
            }
            else {
                Log.i("returning false  : "+ response , url);
                carregaHtml(String.valueOf(response));
                return false;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            carregaHtml(String.valueOf(e));
        } catch (ExecutionException e) {
            e.printStackTrace();
            carregaHtml(String.valueOf(e));
        }
        carregaHtml(messServer2);
        return false;
    }

   /* @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("waiting 5 minutes..");
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                return mProgressDialog;
            default:
                return null;
        }
    }*/

//    @Override
//    public void onPause(){
//
//    }

//    @Override
//    public void onResume(){
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.web_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //Toast.makeText(this, "Pulsado " + values[i], Toast.LENGTH_SHORT).show();
        if (values[i].equals(settings)){
            Intent settingsInt = new Intent(this, SettingsActivity.class);
            settingsInt.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
            startActivity(settingsInt);
        }else if (values[i].equals(refresh)){
            webView.loadUrl(webView.getUrl());
        }else if (values[i].equals(exit)){
            //TODO
        }
        mDrawer.closeDrawers();
    }
}
