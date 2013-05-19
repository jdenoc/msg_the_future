package com.example.msg_future;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.text.format.DateFormat;
//import android.util.Log;		// TESTING
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

@SuppressLint({ "NewApi", "ValidFragment" })
public class TimePickerFragment extends DialogFragment implements OnTimeSetListener {

	private TextView timeDisplay;
	private String dateText;
//	private final String TAG = "TimePickerFragment";		// TESTING
	
	public TimePickerFragment(TextView timeText, TextView dateText){
		timeDisplay = timeText;
		this.dateText = dateText.getText().toString();
	}
		
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    	if(futureCheck(hourOfDay, minute)){
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
	    	
	        timeDisplay.setText(hour+" : "+min);
		} else {
			Toast.makeText(getActivity(), R.string.invalid_date_time, Toast.LENGTH_SHORT).show();
		}	
    }
    
    private boolean futureCheck(int h, int m){
		boolean isTimePast = true;
		boolean isDatePast = true;
		final Calendar c = Calendar.getInstance();
		
		if(h < c.get(Calendar.HOUR_OF_DAY)){
			isTimePast = true;
		} else if(h == c.get(Calendar.HOUR_OF_DAY) && m < c.get(Calendar.MINUTE)+5){
			isTimePast = true;
		} else {
			isTimePast = false;
		}
		
		if(!isTimePast){
			if(dateText.equals(getResources().getString(R.string.date_default))){
				isDatePast = false;
			}else{
				int year = Integer.parseInt(dateText.substring(0, 4));
				int month = Integer.parseInt(dateText.substring(5, 7));
				int day = Integer.parseInt(dateText.substring(8));
				
		        int currentYear = c.get(Calendar.YEAR);
		        int currentMonth = c.get(Calendar.MONTH)+1;
		        int currentDay = c.get(Calendar.DAY_OF_MONTH);
				
		        if(currentYear < year){
		        	isDatePast = true;
		        } else if(currentYear == year && currentMonth > month){
		        	isDatePast = true;
		        } else if(currentYear == year && currentMonth == month && currentDay > day){
		        	isDatePast = true;
		        }else{
		        	isDatePast = false;
		        }
			}
		}
		
		return !isDatePast;
	}
    
}