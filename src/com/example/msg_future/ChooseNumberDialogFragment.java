package com.example.msg_future;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

@SuppressLint({ "NewApi", "ValidFragment" })
public class ChooseNumberDialogFragment extends DialogFragment{

	private String contactName;
	private EditText editNumber;
	private CharSequence[] phoneNumbers;
	
	public ChooseNumberDialogFragment(EditText numberArea, HashMap<String, String> numbers, String name){
		contactName = name;
		editNumber = numberArea;
		phoneNumbers = new String[numbers.size()];
		
		Set<String> keys = numbers.keySet();
        Iterator<String> i = keys.iterator();
        int index = 0;
        while(i.hasNext()){		// Make the radio buttons
        	String label = (String) i.next();
        	String number = (String) numbers.get(label);
        	phoneNumbers[index] = label+": "+number;
        	index++;
        }
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle(contactName);
	    
	    builder.setItems(phoneNumbers, new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int which) {
               // The 'which' argument contains the index position of the selected item
        	   setContact(phoneNumbers[which].toString());
           }
	    });
	    return builder.create();
	}
	
	private void setContact(String listItem){
		int colonIndex = listItem.indexOf(':')+2;
		String item = listItem.substring(colonIndex);
		editNumber.setText(item);
	}
}