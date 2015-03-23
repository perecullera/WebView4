package com.example.perecullera.webview4d;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.net.HttpURLConnection;
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

    NetworkUtil nt;
    private final NetworkChangeReceiver mybroadcast = new NetworkChangeReceiver();

    private boolean isReceiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.getWindow().getDecorView().setSystemUiVisibility(getSystemUiFlags());
        //carrega layout
        setContentView(R.layout.activity_web_view);



        //instantiate variables
        settings = getString(R.string.drawer_settings);
        refresh = getString(R.string.drawer_refresh);
//        exit =getString(R.string.drawer_exit);
        values = new String[]{settings, refresh};

        pdmessage = getString(R.string.progreesdiag_mess);

        invalidUrl = getString(R.string.messages_invalid);
        messServer1 = getString(R.string.message_server1);
        messServer2 = getString(R.string.message_server2);



        pd =  ProgressDialog.show(this, "", pdmessage, true);
        pd.setCancelable(true);


        // amaga actionBar
       getSupportActionBar().hide();

        nt = new NetworkUtil();

        //amaga navigationBar
        UiChangeListener();


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
        /*webView.setWebChromeClient(new WebChromeClient(){
                @Override
                public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                pd.setProgress(progress * 1000);
                }
        });*/
        WebSettings settings = webView.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);

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
            goToPref(invalidUrl);
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
                    goToPref(invalidUrl);
                }
            }
        };
        sPref.registerOnSharedPreferenceChangeListener(listener);

    }
    //carrega preferències a les variables de la classe, si no en troba posa "" com a default
    public void loadSerPort() {
        //TODO
        server = sPref.getString("server", "");
        port = sPref.getString("port", "");

    }

    //carrega un html d'error guardat a els arxius de l'app
     public void goToPref( String message) {
        //TODO

        Intent settingsInt = new Intent(this , SettingsActivity.class);
        settingsInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        settingsInt.putExtra("message", message);
        startActivity(settingsInt);

    }

    //comprova que el server i port, en cas q n'hi hagi, siguin una URL valida
    // en cas q sigui vàlida la carrega a la variable fullUrl
    public  boolean validUrl() {
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
        if (fullurl == null){
            Intent settingsInt = new Intent(this, SettingsActivity.class);
            settingsInt.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
            startActivity(settingsInt);
        }

        fullurl = valueOf(Uri.parse(fullurl));
        if (checkConnectivity()!= 0) {
            if (getResponse(fullurl)) {
                webView.loadUrl(fullurl);
                //Toast.makeText(this, "obrint webview amb url: " + fullurl, Toast.LENGTH_SHORT).show();
            }
        } else {
            nt.showNoConnectionDialog(this);
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
                goToPref(messServer1);
                return false;
            }
            else {
                Log.i("returning false  : "+ response , url);
                //TODO
                goToPref(messServer1);
                return false;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            goToPref(messServer1);
        } catch (ExecutionException e) {
            e.printStackTrace();
            goToPref(messServer1);
        }
        goToPref(messServer2);
        return false;
    }

    public int checkConnectivity(){
        return nt.getConnectivityStatus(this);
    }
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
            carregaWeb();
        }else if (values[i].equals(exit)){
            //TODO
        }
        mDrawer.closeDrawers();
    }
    @Override
    public void onPause(){
        super.onPause();
        if (isReceiverRegistered) {
            try {
                unregisterReceiver(mybroadcast);
            } catch (Exception e) {
                e.printStackTrace();
            }
            isReceiverRegistered = false;
        }

    }

    @Override
    public void onResume(){
        super.onResume();

        if (!isReceiverRegistered) {
            registerReceiver(
                    mybroadcast,
                    new IntentFilter(
                            ConnectivityManager.CONNECTIVITY_ACTION));
            isReceiverRegistered = true;
        }
        this.getWindow().getDecorView().setSystemUiVisibility(getSystemUiFlags());
    }
    @Override
    public void onStart(){
        super.onStart();
        this.getWindow().getDecorView().setSystemUiVisibility(getSystemUiFlags());
    }
    public void UiChangeListener()
    {
        final View decorView = this.getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener (new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                }
            }
        });
    }
    private static int getSystemUiFlags() {
        return View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
    }

}
