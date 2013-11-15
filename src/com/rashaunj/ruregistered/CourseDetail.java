package com.rashaunj.ruregistered;



import java.io.File;

import java.io.RandomAccessFile;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import parse.Course;
import parse.Section;
import parse.TrackedCourse;
import com.actionbarsherlock.view.*;
import com.rashaunj.ruregistered.R;
import com.google.gson.Gson;
import com.google.gson.JsonElement;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;


import android.os.Bundle;


import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
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
		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        String index = in.index;
		String allCampuses = Section.getCampuses(in);
		String allTimes = Section.getMeetingTimes(in);				
        alert.setTitle("Full Course Description");
        alert.setMessage("Section: " +index + "\n" + "Credits: "+cred+"\n"+allCampuses +"\n" +allTimes +"\n");
        alert.setPositiveButton("Track Course", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
             String major = i.getString("major");
   			 String term = i.getString("term");
   			 String campus = i.getString("campus");
   			 String course = i.getString("course");
   			 String section = in.index;

   		     write(major,campus,term,course,section);
   		     Context context = getApplicationContext();
   		     CharSequence text = "1 section added to Course Tracker";
   		     int duration = Toast.LENGTH_SHORT;

   		     Toast toast = Toast.makeText(context, text, duration);
   		     toast.show();
   		     dialog.cancel();

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        alert.show(); 
        
	}
	

    @SuppressLint("WorldReadableFiles")
    /**
     * Writes selected course(s) to json.    	 
     */
	public void write(String major,String campus, String term, String course, String section) {
	   	 
		TrackedCourse in = new TrackedCourse(major,campus,term,course,section);

		Gson gson = new Gson();
		JsonElement json = gson.toJsonTree(in);



		try {
		 File file = new File(getFilesDir(), "RUTracker.json");
		// String contextA=getApplicationContext().getFilesDir().getAbsolutePath(); Testing file directory output
	     RandomAccessFile raf = new RandomAccessFile(new File(getApplicationContext().getFilesDir(),"RUTracker.json"), "rw");
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
