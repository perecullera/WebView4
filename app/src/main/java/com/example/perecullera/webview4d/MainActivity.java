package com.example.perecullera.webview4d;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Map;

import static java.lang.String.valueOf;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private DrawerLayout mDrawer;
    private ListView mDrawerOptions;
    private static final String[] values = {"Settings", "Refresh", "Exit"};
    WebView webView;
    SharedPreferences sPref;
    SharedPreferences.OnSharedPreferenceChangeListener listener;
    String server;
    String port;
    String BaseUrl = "http://";
    String fullurl ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        //webview
        webView = (WebView) findViewById(R.id.webview);

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
            carregaHtml();
        }

        //instanciem listener dels canvis a les preferències
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                loadSerPort();
                //és una URL valida?
                if (validUrl()){
                    //carreguem web
                    carregaWeb();
                }else{
                    //carreguem alert i html error
                    //TODO
                    carregaHtml();
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
    private void carregaHtml() {
        //TODO
        webView.loadUrl("file:///android_asset/error.html");

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
        //Intent intent = getIntent();


        //debug
        System.out.println("preferencias "+ sPref.toString());
        System.out.println("server "+ server);
        System.out.println("port "+ port);
        Toast.makeText(this, "preferencias" + sPref.toString(), Toast.LENGTH_SHORT).show();





        //nou mètode valida() ping
        //comprova conexió
        fullurl = valueOf(Uri.parse(fullurl));


        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(fullurl);
        Toast.makeText(this, "obrint webview amb url: " + fullurl, Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this, "Pulsado " + values[i], Toast.LENGTH_SHORT).show();
        if (values[i].equals("Settings")){
            Intent settingsInt = new Intent(this, SettingsActivity.class);
            settingsInt.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
            startActivity(settingsInt);
        }else if (values[i].equals("Refresh")){
            webView.loadUrl(webView.getUrl());
        }else if (values[i].equals("Exit")){

        }
        mDrawer.closeDrawers();
    }
}
