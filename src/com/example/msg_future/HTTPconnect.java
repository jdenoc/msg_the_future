package com.example.msg_future;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.util.Log;		// TESTING
import android.widget.Toast;

//Connect to the Internet
public class HTTPconnect extends AsyncTask<Object, Void, Boolean>{

	private final String TAG = "HTTPconnect";		// TESTING
	private Context mainThread;
	private int type;
	
	@Override
	protected Boolean doInBackground(Object... params) {
//	   	Runs processes in background
		mainThread = (Context) params[0];
		type = (Integer) params[1];
		
		
		if(type == 1){		// 1: send data to server
			return sendData(params);
		}else{				// 2: get data from server
			return sendSMS(getData(params));
		}
    }// END doInBackground()
	
	protected void onPostExecute(Boolean result){
		Log.d(TAG, "Finished Execution results...");		//	TESTING
		if(type == 1){		// 1: send data result
			if(result){
				Log.d(TAG, "Success!");		//	TESTING
				Toast.makeText(mainThread, "Message successfully scheduled! (^_^)", Toast.LENGTH_SHORT).show();
			} else {
				Log.d(TAG, "Failure!");		//	TESTING
				Toast.makeText(mainThread, "Message NOT scheduled (;_;)", Toast.LENGTH_SHORT).show();
			}
		} else {			// 2: get data result
			if(result){
				Log.d(TAG, "Success!");		//	TESTING
				Toast.makeText(mainThread, "Message successfully sent! (^_^)", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private String processSendDate(String date, String time, String timezone){
		return date+" "+time.substring(0, 2)+":"+time.substring(5)+" "+timezone;
	}
	
	private boolean sendData(Object[] data){
		// Context data[0]
		// int data[1]		Type		(1: send data value)
		// String data[2]	URL
		// String data[3]	msg
		// String data[4]	phone
		// String data[5]	from		(device phone number)
		// String data[6]	send date
		// String data[7]	send time
		// String data[8]	timezone 	(device's current timezone)
		
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost((String) data[2]);
			Log.d(TAG, "Site Accessed");		//	TESTING
			
			JSONObject obj = new JSONObject();
			obj.put("msg", (String) data[3]);
			obj.put("phone", (String) data[4]);
			obj.put("from", (String) data[5]);
			obj.put("send", processSendDate((String) data[6], (String) data[7], (String) data[8]));
		    httppost.setEntity(new StringEntity(obj.toString(), "UTF-8"));
		    
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			String responceValue = EntityUtils.toString(entity);
			Log.d(TAG, "Response: "+responceValue);
			if (responceValue.equals("1")){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			Log.e(TAG, "Error in http connection: "+e.toString());		//	TESTING
			return false;
		}
	}
	
	private String getData(Object[] data){
		// Context data[0]
		// int data[1]		Type	(2: get data value)
		// String data[2]	URL
		// String data[3]	device phone number
		
		String json = "";
		
		try{
			Log.d(TAG, "URL: "+(String) data[2]+"?phone="+(String) data[3]);
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httppost = new HttpGet((String) data[2]+"?phone="+(String) data[3]);
			Log.d(TAG, "Site Accessed");		//	TESTING
			
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			json = EntityUtils.toString(entity);
		    
			Log.d(TAG, "Response: "+json);
			return json;
			
		}catch(Exception e){
			Log.e(TAG, "Error in http connection: "+e.toString());		//	TESTING
			return "";
		}
	}
	
	private boolean sendSMS(String jsonData){
//		(`msg`, `phone`, `from`, `send`)
		JSONObject msgData;
		String phoneNumber, msg;
		try {
			msgData = new JSONObject(jsonData);
			phoneNumber= msgData.getString("phone");
			msg = msgData.getString("msg");
		} catch (JSONException e) {
			e.printStackTrace();
			Log.e(TAG, "JSON ERROR: "+e.toString());
			return false;
		}
		
	   SmsManager sms = SmsManager.getDefault();
       sms.sendTextMessage(phoneNumber, null, msg, null, null);
    
       return true;
	}

}