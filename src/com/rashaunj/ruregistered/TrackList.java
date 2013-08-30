package com.rashaunj.ruregistered;

import java.io.IOException;
import java.util.ArrayList;

import parse.Section;
import parse.TrackedCourse;


import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.example.ruregistered.R;
import com.example.ruregistered.R.drawable;
import com.example.ruregistered.R.id;
import com.example.ruregistered.R.layout;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class TrackList extends SherlockActivity {
	ArrayList<TrackedCourse> list = new ArrayList<TrackedCourse>();

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
		                R.layout.tracked_item, list);
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
				     	final int pos = position;

				 			alertDialogBuilder.setTitle("Tracker");
				  
				 			alertDialogBuilder
				 				.setMessage("Would you like to untrack this course")
				 				.setCancelable(false)
				 				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
				 					Bundle in = getIntent().getExtras();
				 					public void onClick(DialogInterface dialog,int id) {
				 						Tracker track = new Tracker();
				 						list.remove(position);
				 						try {
											track.update(list);
											adapter.notifyDataSetChanged();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
				 						

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
			/*
			try {
				Tracker.create(list);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			return null;
		}
	 }

}
