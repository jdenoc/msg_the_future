package com.example.msg_future;


import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.EditText;

@SuppressLint({ "NewApi", "ValidFragment" })
public class ChooseNumberDialogFragment extends DialogFragment{

	private String contactName;
	private EditText editNumber;
	private CharSequence[] phoneNumbers;
	private long photo_thumb;
	
	public ChooseNumberDialogFragment(EditText numberArea, HashMap<String, String> numbers, String name, long photo_thumb){
		contactName = name;
		editNumber = numberArea;
		phoneNumbers = new String[numbers.size()];
		this.photo_thumb = photo_thumb;
		
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
	
	private Bitmap loadContactPhoto(ContentResolver cr) {
		
		Uri photoUri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, photo_thumb);
	    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, photoUri);
	    if (input == null) {
	        return null;
	    }
	    return BitmapFactory.decodeStream(input);
	}
	
}