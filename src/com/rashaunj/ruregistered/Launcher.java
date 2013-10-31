package com.rashaunj.ruregistered;

import java.util.ArrayList;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.rashaunj.ruregistered.R;
import com.rashaunj.ruregistered.List.LoadData;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;

public class Launcher extends SherlockActivity implements OnItemClickListener {

    String[] EntryText = new String[] {
            "Add New Course",
            "View Tracked Courses",
            "Settings",
            "About"
            
    };
    AlarmManager alarm;
    int[] EntryImage = new int[]{
                R.drawable.add,
                R.drawable.track,
                R.drawable.settings,
                R.drawable.about
               
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_launcher);
	   alarm= (AlarmManager)getSystemService(Context.ALARM_SERVICE);
      	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        boolean firstTime = settings.getBoolean("firstTime", true);
        if (firstTime) { 
            final SharedPreferences.Editor editor = settings.edit();
            editor.putString("interval", "300000");
            editor.putBoolean("trackerstate",true);
            editor.putBoolean("firstTime", false);
            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            final EditText input = new EditText(this);
            alert.setView(input);
            alert.setTitle("Welcome");
            alert.setMessage("This appears to be your first run. I'll need your email address.");
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String value = input.getText().toString().trim();
                    editor.putString("email", value);
                    editor.commit();
                    
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.cancel();
                }
            });
            alert.show();               
        }
        
        ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
        for(int i=0;i<EntryImage.length;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("txt", EntryText[i]);            
            hm.put("item", Integer.toString(EntryImage[i]) );
            aList.add(hm);
        }

        String[] from = { "item","txt"};


        int[] to = { R.id.grid_item_image,R.id.grid_item_text};

        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.grid_item, from, to);        
        
        // Getting a reference to gridview of MainActivity
        GridView gridView = (GridView) findViewById(R.id.launch_menu);
        
        // Setting an adapter containing images to the gridview
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(Launcher.this);

    }
	@Override
	public void onResume(){
	    super.onResume();
	   	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
	   	Boolean check = settings.getBoolean("trackerstate", false);
	   	if(check==true){
			startTracker();
	   	}
	   	else{
	   		Intent intent = new Intent(Launcher.this, Tracker.class);
			PendingIntent pintent = PendingIntent.getService(getBaseContext(), 0, intent, 0);
	   		alarm.cancel(pintent);
	   	}
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent;
		if(arg2==0){
			intent = new Intent(this,TrackerHome.class);
			startActivity(intent);
		}
		else if(arg2==1){
			intent = new Intent(this,TrackList.class);
			startActivity(intent);
		}
		else if(arg2==2){
			intent = new Intent(this,Settings.class);
			startActivity(intent);
		}
		else if(arg2==3){
			intent = new Intent(this,About.class);
			startActivity(intent);
		}

	}
	public void startTracker(){
	   	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		Calendar calender = Calendar.getInstance();
	    calender.setTimeInMillis(System.currentTimeMillis());
	    calender.add(Calendar.SECOND, 30);
	    Log.d("Testing", "Calender Set time:"+calender.getTime());
		Intent intent = new Intent(Launcher.this, Tracker.class);
		PendingIntent pintent = PendingIntent.getService(getBaseContext(), 0, intent, 0);
	    //alarm= (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		//Necessary since Android ListPreference does not parse integer-arrays in XML
		int interval = Integer.parseInt(settings.getString("interval", "300000"));		
		alarm.setRepeating(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), interval, pintent);
	}
}
	
