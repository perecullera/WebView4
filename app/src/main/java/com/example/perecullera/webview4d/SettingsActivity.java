package com.example.perecullera.webview4d;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


public class SettingsActivity extends PreferenceActivity {

    Button submit, exit;
    String server, port;
    EditText serverinput, portinput;
    SharedPreferences sh_Pref;
    SharedPreferences.Editor toEdit;
    SharedPreferences.OnSharedPreferenceChangeListener listener;
    MainActivity ma = new MainActivity();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        //getInit();
        ImageView logo = (ImageView) findViewById(android.R.id.home);
        logo.setVisibility(View.INVISIBLE);

        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {/*
                // Implementation
                System.out.println(key);
                //sharedPreferences();
                Toast.makeText(getApplicationContext(), "listener settings"+ prefs.toString(), Toast.LENGTH_SHORT).show();*/
                finish();

            }
        };
        sh_Pref = PreferenceManager.getDefaultSharedPreferences(this);
        sh_Pref.registerOnSharedPreferenceChangeListener(listener);
        Intent intent = getIntent();
        String message = intent.getStringExtra("message");
        if (message != null) {
            System.out.println(message);
            CustomDialogClass cdd = new CustomDialogClass(this, message);
            cdd.show(getFragmentManager(), message);
        }
    }




}
