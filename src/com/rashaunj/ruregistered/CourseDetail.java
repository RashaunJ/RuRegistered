package com.rashaunj.ruregistered;



import java.io.File;
import java.io.FileOutputStream;

import java.io.RandomAccessFile;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import parse.Course;
import parse.Section;
import parse.TrackedCourse;
import com.actionbarsherlock.view.*;
import com.example.ruregistered.R;
import com.google.gson.Gson;
import com.google.gson.JsonElement;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;


import android.os.Bundle;
import android.os.Environment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;

public class CourseDetail extends SherlockFragmentActivity {
	Bundle i;
	 Tracker push = new Tracker();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_detail);
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
		bar.setDisplayHomeAsUpEnabled(true);
		bar.setTitle("Course Description");
		Bundle extras = getIntent().getExtras();
		i=getIntent().getExtras();
		final Course selected = List.mcourse.get(extras.getInt("SecID"));
		final ListView sections = (ListView) findViewById(R.id.sectionlist);
		CustomAdapter adapter;
		TextView Name = (TextView) findViewById(R.id.CName);
		TextView Prereqs = (TextView) findViewById(R.id.section_prereq_full);
		Prereqs.setMovementMethod(new ScrollingMovementMethod());// Scrolling textfield
		Name.setText(selected.title);
		Section allSections[] = List.mcourse.get(extras.getInt("SecID")).sections;
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Adds back button functionality
		sections.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String credits = selected.credits;
				Section in = selected.sections[position];
			    Popup(in, credits);
			}
		});

		if(selected.preReqNotes!=null){
		Prereqs.setText(selected.preReqNotes.replaceAll("\\<.*?>",""));//Removes leftover html tagging
		}
		else{
			Prereqs.setText("None");
		}
		adapter = new CustomAdapter(this,
                R.layout.section_item, allSections);
        //adapter = new CustomAdapter(CourseDetail.this,R.id.sectionlist,allSections);
		sections.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    getSupportMenuInflater().inflate(R.menu.activity_course_detail, menu);
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case android.R.id.home:
	    	finish();
	        //NavUtils.navigateUpFromSameTask(this);
	        break;

	    }
	    return false;
	}
	private void Popup(final Section in, String cred) {
		 String index = in.index;
		 String c = cred;
		 String allCampuses = Section.getCampuses(in);
		 String allTimes = Section.getMeetingTimes(in);
		 AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
		 helpBuilder.setTitle("Full Course Description");
		 helpBuilder.setMessage("Section: " +index);
		 

		 LayoutInflater inflater = getLayoutInflater();
		 View checkboxLayout = inflater.inflate(R.layout.course_popup, null);
		 helpBuilder.setView(checkboxLayout);
		 final AlertDialog helpDialog = helpBuilder.create();
		 helpDialog.show();
		 TextView credits = (TextView) checkboxLayout.findViewById(R.id.detailCredits);
		 TextView campus = (TextView) checkboxLayout.findViewById(R.id.detailCampus);
		 TextView meetingTimes = (TextView) checkboxLayout.findViewById(R.id.detailMeetLabel);
		 credits.setText("Credits: " +c);
		 campus.setText(allCampuses);
		 meetingTimes.setText(allTimes);
		 Button cancel = (Button)checkboxLayout.findViewById(R.id.cancel);
		 cancel.setOnClickListener(new View.OnClickListener() {
		 @Override
		 public void onClick(View v) {
		     // TODO Auto-generated method stub
		     helpDialog.cancel();
		     
		 }});
		 
		 cancel.setOnClickListener(new View.OnClickListener() {
		 @Override
		 public void onClick(View v) {
		     // TODO Auto-generated method stub
		     helpDialog.cancel();
		     
		 }});
		 Button track = (Button)checkboxLayout.findViewById(R.id.track);
		 track.setOnClickListener(new View.OnClickListener() {
		 @Override		 
		 public void onClick(View v) {
			 String major = i.getString("major");
			 String term = i.getString("term");
			 String campus = i.getString("campus");
			 String level = "U";
			 String course = i.getString("course");
			 String section = in.index;

		     write(major,campus,term,level,course,section);
		     Context context = getApplicationContext();
		     CharSequence text = "1 section added to Course Tracker";
		     int duration = Toast.LENGTH_SHORT;

		     Toast toast = Toast.makeText(context, text, duration);
		     toast.show();
		     helpDialog.cancel();
		     
		 }});

	}
	

    @SuppressLint("WorldReadableFiles")
    /**
     * Writes selected course(s) to json.    	 
     */
	public void write(String major,String campus, String term, String level, String course, String section) {
	   	 
		TrackedCourse in = new TrackedCourse(major,campus,term,course,section);

		Gson gson = new Gson();
		JsonElement json = gson.toJsonTree(in);



		try {
		 File file = new File(getFilesDir(), "RUTracker.json");
		 String contextA=getApplicationContext().getFilesDir().getAbsolutePath();
	     RandomAccessFile raf = new RandomAccessFile(new File(getApplicationContext().getFilesDir(),"RUtracker.json"), "rw");
	      	if(raf.length()==0){
	      		raf.writeBytes("["+json.toString()+"]");
	      	}
	      	else{
	          	raf.setLength(file.length()-1);
	          	raf.seek(file.length());
	          	raf.writeBytes(","+json.toString()+"]");
	      	}
	     raf.close();

	    /*
	     
	     FileOutputStream fos= openFileOutput("tracker.json",
	            Context.MODE_APPEND | Context.MODE_WORLD_READABLE);
	   
	    fos.write(json.toString().getBytes());
	    fos.close();
	    */
	    

	/* Writes to sd card. Not usable on all devices due to varying file structures.
	    String storageState = Environment.getExternalStorageState();

	    if (storageState.equals(Environment.MEDIA_MOUNTED)) {
	    	//Properly format json table
	      File file = new File(getExternalFilesDir(null), "RUTracker.json");
	      	RandomAccessFile raf = new RandomAccessFile(file,"rw");
	      	raf.close();


	    }
	    */

	} catch (Exception e) {

	    e.printStackTrace();

	}
		}
}
