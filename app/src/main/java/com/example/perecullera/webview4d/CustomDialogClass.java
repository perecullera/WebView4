package com.example.perecullera.webview4d;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by perecullera on 12/2/15.
 */
public class CustomDialogClass extends DialogFragment implements View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes;
    public String message;
    public TextView txtMessage;

    public CustomDialogClass(Activity a, String msg) {
        super();
        // TODO Auto-generated constructor stub
        this.c = a;
        this.message = msg;
    }

   @Override
   public Dialog onCreateDialog(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
//       requestWindowFeature(Window.FEATURE_NO_TITLE);
       // Use the Builder class for convenient dialog construction
       AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
       builder.setMessage(message)
               .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // FIRE ZE MISSILES!

                   }
               });
       return builder.create();
   }

    @Override
    public void onClick(View view) {
        dismiss();
    }

}
