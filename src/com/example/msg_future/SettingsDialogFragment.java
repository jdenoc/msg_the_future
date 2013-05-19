package com.example.msg_future;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

@SuppressLint({ "NewApi", "ValidFragment" })
public class SettingsDialogFragment extends DialogFragment{

	private Context mainActivity;
	
	public SettingsDialogFragment(Context main){
		mainActivity = main;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle("Settings");
	    builder.setMessage("Would you like to start the \"Future\" Messaging service?\n\nNote: it is likely to effect you're battery life.\n");
	    builder.setPositiveButton("Start Service", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   mainActivity.startService(new Intent(mainActivity, WebService.class));

	           }
	       });
	    builder.setNegativeButton("Stop Service", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int id) {
        	   mainActivity.stopService(new Intent(mainActivity, WebService.class));
           }
       });
	    
	   return builder.create();
	}
}