package com.example.perecullera.webview4d;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by perecullera on 12/2/15.
 */
public class CustomDialogClass extends Dialog implements View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes;
    public String message;
    public TextView txtMessage;

    public CustomDialogClass(Activity a, String msg) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.message = msg;
    }

   @Override
   protected void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
       requestWindowFeature(Window.FEATURE_NO_TITLE);
       setContentView(R.layout.custom_dialog);
       yes = (Button) findViewById(R.id.btn_yes);
       yes.setOnClickListener(this);
       txtMessage = (TextView)findViewById(R.id.txt_message);
       txtMessage.setText(message);
   }

    @Override
    public void onClick(View view) {
        dismiss();
    }
}
