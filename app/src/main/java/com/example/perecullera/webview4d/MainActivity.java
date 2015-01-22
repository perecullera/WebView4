package com.example.perecullera.webview4d;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.net.URI;
import java.util.Map;

import static java.lang.String.valueOf;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private DrawerLayout mDrawer;
    private ListView mDrawerOptions;
    private static final String[] values = {"Settings", "Refresh", "Drawer 3"};
    WebView webView;
    SharedPreferences sPref;
    SharedPreferences.OnSharedPreferenceChangeListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        //drawer
        mDrawerOptions = (ListView) findViewById(R.id.left_drawer);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        mDrawerOptions.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values));
        mDrawerOptions.setOnItemClickListener(this);


        carregaWeb();
        

        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                carregaWeb();
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
    public void carregaWeb(){
        //Intent intent = getIntent();
        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        String server = sPref.getString("Server", "DEFAULT");
        String port = sPref.getString("Port", "DEFAULT");

        //debug
        System.out.println("preferencias "+ sPref.toString());
        System.out.println("server "+ server);
        System.out.println("port "+ port);
        Toast.makeText(this, "preferencias" + sPref.toString(), Toast.LENGTH_SHORT).show();



        String BaseUrl = "http://";
        String fullurl = BaseUrl + server +":"+ port;
        fullurl = valueOf(URI.create(fullurl));

        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
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
        }
        mDrawer.closeDrawers();
    }
}
