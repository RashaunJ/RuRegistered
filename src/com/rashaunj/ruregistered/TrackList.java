package com.rashaunj.ruregistered;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import parse.Course;
import parse.CourseList;
import parse.Section;
import parse.TrackedCourse;


import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.rashaunj.ruregistered.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;

public class TrackList extends SherlockActivity {
	public ArrayList<TrackedCourse> open = new ArrayList<TrackedCourse>();
	public ArrayList<TrackedCourse> closed = new ArrayList<TrackedCourse>();
	Hashtable<String,ArrayList<TrackedCourse>> in = new Hashtable<String,ArrayList<TrackedCourse>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_track_list);
		LoadData task = new LoadData();
		task.execute();
	}


	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case android.R.id.home:
	    	finish();
	        break;

	    }
	    return false;
	}
	 private class LoadData extends AsyncTask<Void,Void,Void> {
			ProgressDialog pg;
			TrackedCourseListAdapter adapter;
			ListView listview = (ListView) findViewById(R.id.tracklistview);;


		    protected void onPreExecute()
		    {
		        pg= ProgressDialog.show(TrackList.this, "Please wait...","Loading List", true);            
		    }; 
	     protected void onPostExecute(Void result) {
	    	 adapter = new TrackedCourseListAdapter(TrackList.this,
		                R.layout.tracked_item, closed);
	    	 	listview.setAdapter(adapter);
			    adapter.notifyDataSetChanged();
			    ActionBar bar = getSupportActionBar();
				bar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
				bar.setTitle("Currently Tracked Courses");
				getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		        pg.dismiss();
				listview.setOnItemLongClickListener(new OnItemLongClickListener() {
				     @Override
				     public boolean onItemLongClick(final AdapterView<?> parent, View v, final int position, long id) {
				    	 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				 				TrackList.this);

				 			alertDialogBuilder.setTitle("Tracker");
				  
				 			alertDialogBuilder
				 				.setMessage("Would you like to untrack this course")
				 				.setCancelable(false)
				 				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
				 					public void onClick(DialogInterface dialog,int id) {
				 						closed.remove(position);
				 						try {
											update(closed);
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
				 						adapter.notifyDataSetChanged();
				 						

				 					}
				 				  })
				 				.setNegativeButton("No",new DialogInterface.OnClickListener() {
				 					public void onClick(DialogInterface dialog,int id) {

				 						dialog.cancel();
				 					}
				 				});
				 				// create alert dialog
				 				AlertDialog alertDialog = alertDialogBuilder.create();
				  
				 				// show it
				 				alertDialog.show();
				    	 return true;
		
				     }
				 });
	     }

		@Override
		protected Void doInBackground(Void... params) {
		try {
			create(in);
			Set<String> keySet = in.keySet();
			for(String key: keySet){
				getClosed(in.get(key),closed);
			}
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		}
	 }
	 public Hashtable<String,ArrayList<TrackedCourse>> create(Hashtable<String,ArrayList<TrackedCourse>> in) throws IOException{
			Gson gson = new Gson();    	
		        File file = new File(getFilesDir(), "RUTracker.json");
		    	FileInputStream stream = new FileInputStream(file);
		    	if( file.exists()){
				BufferedReader br = new BufferedReader(
						new InputStreamReader(stream));
				
		      	StringBuilder jsonText = new StringBuilder();
		      	String curr = null;
		      	while ((curr = br.readLine()) != null){	
		      	jsonText.append(curr);
		      	}
		      	br.close();
		      	InputStream jsonStream = new ByteArrayInputStream(jsonText.toString().getBytes());
		      	//Json source file
		      	JsonReader reader = new JsonReader(new InputStreamReader(jsonStream));//Converts String to type InputStream
		      	JsonParser parser = new JsonParser();           
		      	JsonArray userarray= parser.parse(reader).getAsJsonArray();
		      	
		      	
		      	for(JsonElement singleClass: userarray){
		      		TrackedCourse singleCourse = gson.fromJson(singleClass, TrackedCourse.class);
		      		if(in.containsKey(singleCourse.major)){
		      			in.get(singleCourse.major).add(singleCourse);
		      		}
		      		else{
		      			ArrayList<TrackedCourse> push = new ArrayList<TrackedCourse>();
		      			push.add(singleCourse);
		      			in.put(singleCourse.major, push);
		      		}
		      	}
		    }
		      	
		    
		    return in;
		    

		}

		public ArrayList<TrackedCourse> getClosed(ArrayList<TrackedCourse> in, ArrayList<TrackedCourse> closed) throws Exception{
			ArrayList<Course> curr = new ArrayList<Course>();
			CourseList.create(curr,in.get(0).major, in.get(0).term, in.get(0).campus);
			for(TrackedCourse k: in){
			for(Course i: curr){
					if(i.title.equals(k.course)){
					Section[] check =  i.sections;
					for(Section j: check){
						if(j.index.equals(k.section)){
							closed.add(k);
							
					}
					}
				}

			}
			}
		return closed;
	}
		public void update(ArrayList<TrackedCourse> in ) throws IOException{
			Gson gson = new Gson();
		    	//Properly format json table
		      File file = new File(getApplicationContext().getFilesDir() , "RUTracker.json");
		      	RandomAccessFile raf = new RandomAccessFile(file,"rw");
		      	raf.setLength(0);//Clears RUTracker.json
		      	for(int i =0;i<in.size();i++){
		      		JsonElement json = gson.toJsonTree(in.get(i));
			      	if(raf.length()==0){
			      		raf.writeBytes("["+json.toString()+"]");
			      	}
			      	else{
			          	raf.setLength(file.length()-1);
			          	raf.seek(file.length());
			          	raf.writeBytes(","+json.toString()+"]");
			      	}
			      	raf.close();
		      	}

		      	//closed.clear();
		    
		}


}
