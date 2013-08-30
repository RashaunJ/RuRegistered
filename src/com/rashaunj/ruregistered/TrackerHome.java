package com.rashaunj.ruregistered;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;


import parse.Majors;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.example.ruregistered.R;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class TrackerHome extends SherlockActivity implements OnItemSelectedListener {
	ArrayList<String> majorList;
    Bundle extras = new Bundle();

	@Override

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);	
		Calendar calender = Calendar.getInstance();
	       calender.setTimeInMillis(System.currentTimeMillis());
	       calender.add(Calendar.SECOND, 9000);
	       Log.d("Testing", "Calender Set time:"+calender.getTime());
			Intent intent = new Intent(TrackerHome.this, Tracker.class);
			PendingIntent pintent = PendingIntent.getService(getBaseContext(), 0, intent, 0);
			AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			alarm.setRepeating(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), 9000*1000, pintent);		
		Spinner campusspin = (Spinner) findViewById(R.id.campus);
		final Spinner termspin = (Spinner) findViewById(R.id.term);
		final Spinner majorspin = (Spinner) findViewById(R.id.majors);
		Button submit = (Button) findViewById(R.id.submit);
		final EditText manual = (EditText) findViewById(R.id.coursecode);
		ArrayList<String> majorList = new ArrayList<String>();
		majorList = getAssetText(0);
		final String campus[] = {"New Brunswick","Newark","Camden"};
		String terms[] = {"Spring 2013","Summer 2013","Fall 2013"};
		ArrayAdapter<String> campusAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, campus);
			campusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			campusspin.setAdapter(campusAdapter);
			campusspin.setOnItemSelectedListener(this);
		ArrayAdapter<String> termAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, terms);
			termAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			termspin.setAdapter(termAdapter);
		final ArrayAdapter<String> majorAdapter = new ArrayAdapter<String>(TrackerHome.this,
             	android.R.layout.simple_spinner_item, majorList);
                majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				majorspin.setAdapter(majorAdapter);							
			ActionBar bar = getSupportActionBar();
			bar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            submit.setOnClickListener(new View.OnClickListener() { 
            @Override
            public void onClick(View v) {
            	String coursecode = manual.getText().toString();
            	String majorselection;
            	int termcode = termspin.getSelectedItemPosition();
            	String term = null;
        		Intent intent = new Intent(TrackerHome.this,List.class);
            	
        		if (coursecode.matches("")) {         	    
                	majorselection= majorspin.getSelectedItem().toString();
                	majorselection = majorselection.replaceAll("[\\D]", "");
            	}        		
            	else{
            		majorselection = coursecode;
            	}
        		
            	if(termcode==0){
            		term = "12013";
            	}
            	else if(termcode==1){
            		term = "72013";            	
            	}
            	else if(termcode==2){
            		term = "92013";
            	}
            	extras.putString("term", term);
        		extras.putString("major",majorselection);
        		intent.putExtras(extras);
            	majorAdapter.notifyDataSetChanged();
        		startActivity(intent);

            }
          });
	}



	@Override
	

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.activity_home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case android.R.id.home:
	    	finish();
	        break;

	    }
	    return false;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
        
        String line = "";
        ArrayList<String> incoming = new ArrayList<String>();
        AssetManager assetManager = getAssets();
        String fileName = null;
        try {
        	if(arg2 == 0){
    	    	fileName = "nb.txt";
    	    	extras.putString("campusCode","NB");
    	    }
    	    else if(arg2 ==1){
    	    	fileName = "nw.txt";
    	    	extras.putString("campusCode","NK");
    	    }
    	    else if(arg2==2){
    	    	fileName = "ca.txt";
    	    	extras.putString("campusCode","CM");

    	    }
            InputStream in = assetManager.open(fileName);
            if (in != null) {
                InputStreamReader input = new InputStreamReader(in);
                BufferedReader buffreader = new BufferedReader(input);
                while ((line = buffreader.readLine()) != null) {
                    if (line.contains("<OPTION VALUE")) {
        				line = line.trim();
        				line=line.replaceAll("<(.|\n)*?>","");//Remove html tagging
        				incoming.add(line);
                    }
                }
                in.close();
            } else {
                Log.e("Input Stream Problem",
                        "Input stream of text file is null");
            }
        } catch (Exception e) {
            Log.e("0003:Error in get stream", e.getMessage());
        }
        majorList = incoming;
		Spinner majorspin = (Spinner) findViewById(R.id.majors);
        ArrayAdapter<String> majorAdapter = new ArrayAdapter<String>(TrackerHome.this,
             	android.R.layout.simple_spinner_item, majorList);
                majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				majorspin.setAdapter(majorAdapter);

    }
	



	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
        try {
        	
			ArrayList<String> majorList = Majors.localParse(0);
      	//majorspin = (Spinner) findViewById(R.id.majors);
      	ArrayAdapter<String> majorAdapter = new ArrayAdapter<String>(TrackerHome.this,
   		android.R.layout.simple_spinner_item, majorList);
          majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			//majorspin.setAdapter(majorAdapter);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private ArrayList<String> getAssetText(int lineNumber) {
        String line = "";
        ArrayList<String> incoming = new ArrayList<String>();
        AssetManager assetManager = getAssets();
        String fileName;
        try {
        	if(lineNumber == 0){
    	    	fileName = "nb.txt";    	    }
    	    else if(lineNumber ==1){
    	    	fileName = "nw.txt";    	    }
    	    else{
    	    	fileName = "ca.txt";
    	    }
            InputStream in = assetManager.open(fileName);
            if (in != null) {
                InputStreamReader input = new InputStreamReader(in);
                BufferedReader buffreader = new BufferedReader(input);
                while ((line = buffreader.readLine()) != null) {
                    if (line.contains("<OPTION VALUE")) {
        				line = line.trim();
        				line=line.replaceAll("<(.|\n)*?>","");//Remove html tagging
        				incoming.add(line);
                    }
                }
                in.close();
            } else {
                Log.e("Input Stream Problem",
                        "Input stream of text file is null");
            }
        } catch (Exception e) {
            Log.e("0003:Error in get stream", e.getMessage());
        }
      return incoming;
    }
}
	

