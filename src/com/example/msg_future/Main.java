package com.example.msg_future;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;

import android.telephony.TelephonyManager;
//import android.util.Log;		// TESTING
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity implements OnClickListener {

	private Button sendButton, setDate, setTime, getContact;
	private EditText contacts, msg;
	private TextView dateDisplay, timeDisplay;
	
	private String msgText, contactsText, dateText, timeText;
	private HashMap<String, String> numbers = new HashMap<String, String>();
	
	private static final int PICK_CONTACT = 1;
//	private static final String TAG = "Main";		// TESTING
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		dateDisplay = (TextView) findViewById(R.id.dateDisplay);
		setDate = (Button) findViewById(R.id.setDate);
		timeDisplay = (TextView) findViewById(R.id.timeDisplay);
		setTime = (Button) findViewById(R.id.setTime);
		contacts = (EditText) findViewById(R.id.txtPhoneNo);
		getContact = (Button) findViewById(R.id.getContact);
		msg = (EditText) findViewById(R.id.txtMessage);
		sendButton = (Button) findViewById(R.id.btnSendSMS);
		
		setDate.setOnClickListener(this);
		setTime.setOnClickListener(this);
		getContact.setOnClickListener(this);
		sendButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v){
		switch(v.getId()){
			case R.id.btnSendSMS:
				contactsText = contacts.getText().toString();
				msgText = msg.getText().toString();
				dateText = dateDisplay.getText().toString();
				timeText = timeDisplay.getText().toString();
				
				if(dateText.equals(getResources().getString(R.string.date_default))){
					Toast.makeText(this, "You haven't selected a date to send the message", Toast.LENGTH_SHORT).show();
					break;
				}
				
				if(timeText.equals(getResources().getString(R.string.time_default))){
					Toast.makeText(this, "You haven't selected a time to send the message", Toast.LENGTH_SHORT).show();
					break;
				}
				
				if(contactsText.equals("")){
					Toast.makeText(this, "You didn't set a phone number...", Toast.LENGTH_SHORT).show();
					break;
				}
				
				if (msgText.equals("")){
					Toast.makeText(this, "You don't have any message text...", Toast.LENGTH_SHORT).show();
					break;
				}
				
				new HTTPconnect().execute(		// AsyncTask<Object, Void, Boolean>
					getApplicationContext(),	// Activity Context
					1,							// Type
					"http://ec2-54-241-47-117.us-west-1.compute.amazonaws.com/schedule_msg.php",
					msgText,					// msg		
					contactsText,				// phone			(message to text to)
					getMyPhoneNumber(),			// from 			(devices phone number)
					dateText,					// send date		(date to sent message)
					timeText,					// send time		(time to send message)
					getDeviceTimezone()			// timezone			(current timezone of device)
					
				);
				
				break;
				
			case R.id.setDate:
				showDatePickerDialog(v);
				break;
				
			case R.id.setTime:
				showTimePickerDialog(v);
				break;
				
			case R.id.getContact:
				Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(intent, PICK_CONTACT);
				break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.		TODO
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@SuppressLint("NewApi")
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
		case R.id.menu_settings:
			Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
			FragmentManager fm = getFragmentManager();
            SettingsDialogFragment settings = new SettingsDialogFragment(this);
            settings.setRetainInstance(true);
            settings.show(fm, "settings");
//            if(settings.getResponce()){
//            	Toast.makeText(this, "START service", Toast.LENGTH_LONG).show();
//            }else{
//            	Toast.makeText(this, "STOP service", Toast.LENGTH_LONG).show();
//            }
            	
			break;
		}
		return false;
	}// END onOptionsItemSelected()
	
	@SuppressLint("NewApi")
	public void showTimePickerDialog(View v) {
	    TimePickerFragment newFragment = new TimePickerFragment(timeDisplay, dateDisplay);
	    newFragment.show(getFragmentManager(), "timePicker");
	}
	
	
	@SuppressLint("NewApi")
	public void showDatePickerDialog(View v) {
	    DatePickerFragment newFragment = new DatePickerFragment(timeDisplay, dateDisplay);
	    newFragment.show(getFragmentManager(), "dateDisplay");
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
	    super.onActivityResult(reqCode, resultCode, data);
	    
	    switch(reqCode){
	       case (PICK_CONTACT):
	         if (resultCode == Activity.RESULT_OK){
	             String id, name;
	        	 Uri contactData = data.getData();

//	             *** DEPRICATED (but works) ***
	             Cursor c = managedQuery(contactData, null, null, null, null);

	             if (c.moveToFirst()){
//	                  other data is available for the Contact.  I have decided to only get the name of the Contact.
	            	 id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
	                 name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
	                 if(c.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER) > 0){
	                // Query phone here. Covered next
	                     Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
	                     while (phones.moveToNext()) {
	                    	 String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	                    	 String type = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
	                    	 String label = (String) Phone.getTypeLabel(this.getResources(), Integer.parseInt(type), "");
	                    	 if(label.equals("Custom")){
	                    		 label = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL));
	                    	 }
	                    	 numbers.put(label, number);
                         } 
	                     phones.close(); 
	                 }
	                 
	                 FragmentManager fm = getFragmentManager();
	                 ChooseNumberDialogFragment cndf = new ChooseNumberDialogFragment(contacts, numbers, name);
	                 cndf.setRetainInstance(true);
	                 cndf.show(fm, "chooseNumber");
	             }
//	             *** END - DEPRICATED ***
	         }
	    }
	}
	
	private String getMyPhoneNumber(){
	    return ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getLine1Number();
	}
	
	private String getDeviceTimezone(){
//			Obtains the devices current time zone setting
		    Calendar now = Calendar.getInstance();	// get Calendar instance
		    TimeZone timeZone = now.getTimeZone();	// get current TimeZone using getTimeZone method of Calendar class

//		    display current TimeZone using getDisplayName() method of TimeZone class
		    return timeZone.getDisplayName(timeZone.inDaylightTime(new Date()), TimeZone.SHORT);
	}
}
