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
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.rashaunj.ruregistered.R;

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
		Spinner campusspin = (Spinner) findViewById(R.id.campus);
		final Spinner termspin = (Spinner) findViewById(R.id.term);
		final Spinner majorspin = (Spinner) findViewById(R.id.majors);
		Button submit = (Button) findViewById(R.id.submit);
		final EditText manual = (EditText) findViewById(R.id.coursecode);
		ArrayList<String> majorList = new ArrayList<String>();
		majorList = getAssetText(0);
		
	    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

		final String campus[] = {"New Brunswick","Newark","Camden"};
		String terms[] = {"Winter 2013","Spring 2014"};
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
            		term = "02014";
            	}
            	else if(termcode==1){
            		term = "12014";            	
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
    	    	extras.putString("campus","NB");
    	    }
    	    else if(arg2 ==1){
    	    	fileName = "nw.txt";
    	    	extras.putString("campus","NK");
    	    }
    	    else if(arg2==2){
    	    	fileName = "ca.txt";
    	    	extras.putString("campus","CM");

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
      	ArrayAdapter<String> majorAdapter = new ArrayAdapter<String>(TrackerHome.this,
   		android.R.layout.simple_spinner_item, majorList);
          majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		} catch (IOException e) {
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


