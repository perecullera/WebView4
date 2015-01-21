package com.example.perecullera.webview4d;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements
        OnClickListener {

    Button submit, exit;
    String server, port;
    EditText serverinput, portinput;
    SharedPreferences sh_Pref;
    SharedPreferences.Editor toEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getInit();

    }
    public void getInit() {

        submit = (Button) findViewById(R.id.submit);
        exit = (Button) findViewById(R.id.exit);
        serverinput = (EditText) findViewById(R.id.serverinput);
        portinput = (EditText) findViewById(R.id.portinput);
        submit.setOnClickListener(this);
        exit.setOnClickListener(this); }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

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
    public void onClick(View currentButton) {
        switch (currentButton.getId()) {
            case R.id.submit:
                server = serverinput.getText().toString();
                port = portinput.getText().toString();
                sharedPreferences();
                Toast.makeText(this, "Details are saved", 20).show();
                Intent launchBrowser;
                launchBrowser = new Intent(this, WebViewActivity.class);
                launchBrowser.putExtra("server",server);
                launchBrowser.putExtra("port", port);
                //launchBrowser.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                startActivity(launchBrowser);
                break;
            case R.id.exit:
                finish();
        }
    }

    private void sharedPreferences() {
        sh_Pref = getSharedPreferences("Login Credentials", MODE_PRIVATE);
        toEdit = sh_Pref.edit();
        toEdit.putString("Server", server);
        toEdit.putString("Port", port);
        toEdit.commit();
    }


}
