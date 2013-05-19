package com.example.msg_future;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

@SuppressLint({ "NewApi", "ValidFragment" })
public class DatePickerFragment extends DialogFragment implements OnDateSetListener {

	private TextView dateDisplay;
	String timeText;
	
	public DatePickerFragment(TextView timeText, TextView dateText){
		dateDisplay = dateText;
		this.timeText = timeText.getText().toString();
	}
		
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of TimePickerDialog and return it
        return new DatePickerDialog(getActivity(), (OnDateSetListener) this, year, month, day);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    	String min, hour;
    	if(minute >= 10){
    		min = String.valueOf(minute);
    	}else{
    		min = "0"+String.valueOf(minute);
    	}
    	
    	if(hourOfDay >= 10){
    		hour = String.valueOf(hourOfDay);
    	}else{
    		hour = "0"+String.valueOf(hourOfDay);
    	}
    	
        dateDisplay.setText(hour+" : "+min);
    }

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		
		if(futureCheck(year, monthOfYear, dayOfMonth)){
			String month, day;
	    	if(monthOfYear >= 10){
	    		month = String.valueOf(monthOfYear);
	    	}else{
	    		month = "0"+String.valueOf(monthOfYear+1);
	    	}
	    	
	    	if(dayOfMonth >= 10){
	    		day = String.valueOf(dayOfMonth);
	    	}else{
	    		day = "0"+String.valueOf(dayOfMonth);
	    	}
	    	
	        dateDisplay.setText(String.valueOf(year)+"-"+month+"-"+day);
		} else {
			Toast.makeText(getActivity(), R.string.invalid_date_time, Toast.LENGTH_SHORT).show();
		}
	}
	
	private boolean futureCheck(int y, int m, int d){
		boolean isTimePast = true;
		boolean isDatePast = true;
//		int colonIndex = timeText.indexOf(" : ");
		final Calendar c = Calendar.getInstance();
		
		if(y < c.get(Calendar.YEAR)){
			isDatePast = true;
		}else if(y == c.get(Calendar.YEAR) && m < c.get(Calendar.MONTH)){
			isDatePast = true;
		}else if(y == c.get(Calendar.YEAR) && m == c.get(Calendar.MONTH) && d < c.get(Calendar.DAY_OF_MONTH)){
			isDatePast = true;
		}else{
			isDatePast = false;
		}
		
		
		if(!isDatePast){
			if(timeText.equals(getResources().getString(R.string.time_default))){
				isTimePast = false;
			}else{
				int hour = Integer.parseInt(timeText.substring(0, 2));
				int min = Integer.parseInt(timeText.substring(5));
				
		        int currentHour = c.get(Calendar.HOUR_OF_DAY);
		        int currentMinute = c.get(Calendar.MINUTE);
				
		        if(currentHour < hour){
		        	isTimePast = true;
		        }else if(currentHour == hour && currentMinute > min){
		        	isTimePast = true;
		        }else{
		        	isTimePast = false;
		        }
			}
		}
		
		return !isTimePast;
	}
    
}