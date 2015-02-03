package com.example.perecullera.webview4d;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SettingsActivity extends PreferenceActivity {

    Button submit, exit;
    String server, port;
    EditText serverinput, portinput;
    SharedPreferences sh_Pref;
    SharedPreferences.Editor toEdit;
    SharedPreferences.OnSharedPreferenceChangeListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        //getInit();

        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                // Implementation
                System.out.println(key);
                //sharedPreferences();
                Toast.makeText(getApplicationContext(), "listener settings"+ prefs.toString(), Toast.LENGTH_SHORT).show();

            }
        };
        sh_Pref = PreferenceManager.getDefaultSharedPreferences(this);
        sh_Pref.registerOnSharedPreferenceChangeListener(listener);

    }

    private void sharedPreferences() {

        toEdit = sh_Pref.edit();
        toEdit.putString("server", server);
        toEdit.putString("port", port);
        toEdit.commit();
    }


}
