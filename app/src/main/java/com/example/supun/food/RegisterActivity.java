package com.example.supun.food;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Objects;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity {
    EditText mUsernameView;
    EditText mPhoneNumberView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username);


        mPhoneNumberView = (EditText) findViewById(R.id.phone_number);


        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });


    }

    private void register(){
       if(isValidInputs()){
           //save registration details
           SharedPreferences sharedPref =  PreferenceManager.getDefaultSharedPreferences(getBaseContext());
           SharedPreferences.Editor editor = sharedPref.edit();
           editor.putString(getString(R.string.username_key), mUsernameView.getText().toString());
           editor.putString(getString(R.string.phone_number_key), mPhoneNumberView.getText().toString());
           editor.apply();

           GlobalState.setCurrentUsername(mUsernameView.getText().toString());
           GlobalState.setPhoneNumber(mPhoneNumberView.getText().toString());

           //set reminder
           Intent myIntent = new Intent(this , NotifyService.class);
           AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

           PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);

           Calendar calendar = Calendar.getInstance();
           TimePicker timePicker1;
           timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
           int hour = timePicker1.getCurrentHour()-calendar.getTime().getHours();
           int min = timePicker1.getCurrentMinute()-calendar.getTime().getMinutes();


//           calendar.set(Calendar.SECOND, 0);
//           calendar.set(Calendar.MINUTE, 13);
//           calendar.set(Calendar.HOUR, 7);
//           calendar.set(Calendar.AM_PM, Calendar.PM);
//           calendar.add(Calendar.DAY_OF_MONTH, 1);
           Long time =  (calendar.getTimeInMillis())+1000*60*min+1000*60*60*hour;
           Log.d("selectTime","hr:"+hour+" min:"+min+" t_now:"+calendar.getTimeInMillis()+" t_new:"+time);
           alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,time, 1000*60*60*24 , pendingIntent);

//           Calendar cal = Calendar.getInstance();
//           Intent intent = new Intent(Intent.ACTION_EDIT);
//           intent.setType("vnd.android.cursor.item/event");
//           intent.putExtra("beginTime", cal.getTimeInMillis());
//           intent.putExtra("allDay", false);
//           intent.putExtra("rrule", "FREQ=DAILY");
//           intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
//           intent.putExtra("title", "A Test Event from android app");
//           startActivity(intent);
// get calendar
//           Calendar cal = Calendar.getInstance();
//           Uri EVENTS_URI = Uri.parse(getCalendarUriBase(this) + "events");
//           ContentResolver cr = getContentResolver();
//
//// event insert
//           ContentValues values = new ContentValues();
//           values.put("calendar_id", 1);
//           values.put("title", "Reminder Title");
//           values.put("allDay", 0);
//           values.put("dtstart", cal.getTimeInMillis() + 11*60*1000); // event starts at 11 minutes from now
//           values.put("dtend", cal.getTimeInMillis()+60*60*1000); // ends 60 minutes from now
//           values.put("description", "Reminder description");
////           values.put("visibility", 0);
//           values.put("hasAlarm", 1);
//           values.put("eventTimezone", String.valueOf(cal.getTimeZone()));
//           Uri event = cr.insert(EVENTS_URI, values);
//
//// reminder insert
//           Uri REMINDERS_URI = Uri.parse(getCalendarUriBase(this) + "reminders");
//           values = new ContentValues();
//           values.put( "event_id", Long.parseLong(event.getLastPathSegment()));
//           values.put( "method", 1 );
//           values.put( "minutes", 10 );
//           cr.insert( REMINDERS_URI, values );

           //start activate
           Intent myintent = new Intent(getBaseContext(), OrderActivity.class);
           startActivity(myintent);
           finish();
       }
    }
    private String getCalendarUriBase(Activity act) {

        String calendarUriBase = null;
        Uri calendars = Uri.parse("content://calendar/calendars");
        Cursor managedCursor = null;
        try {
            managedCursor = act.managedQuery(calendars, null, null, null, null);
        } catch (Exception e) {
        }
        if (managedCursor != null) {
            calendarUriBase = "content://calendar/";
        } else {
            calendars = Uri.parse("content://com.android.calendar/calendars");
            try {
                managedCursor = act.managedQuery(calendars, null, null, null, null);
            } catch (Exception e) {
            }
            if (managedCursor != null) {
                calendarUriBase = "content://com.android.calendar/";
            }
        }
        return calendarUriBase;
    }
    private boolean isValidInputs() {
        if(Objects.equals(mUsernameView.getText().toString(), "")){
            mUsernameView.setError("Please enter your name");
            return false;

        }
        if(Objects.equals(mPhoneNumberView.getText().toString(), "")){
            mPhoneNumberView.setError("Please enter your phone number");
            return false;
        }
            return true;

    }




}

