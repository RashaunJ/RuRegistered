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


import parse.Course;
import parse.CourseWriter;
import parse.Email;
import parse.Section;
import parse.TrackedCourse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

public class Tracker extends Service {
	public ArrayList<TrackedCourse> open = new ArrayList<TrackedCourse>();
	public ArrayList<TrackedCourse> closed = new ArrayList<TrackedCourse>();
	public ArrayList<TrackedCourse> in = new ArrayList<TrackedCourse>();
	public String email;
    @Override
    public void onCreate() {
		Context context = getApplicationContext();
		CharSequence text = "Attempting to start Course Tracker...";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
       	final String PREFS_NAME = "MyPrefsFile";
       	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
       	settings.getString(email,"");
    }

	  @Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
		Runnable r = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {

					create(in);
					for(int i = 0;i<in.size();i++){
						checkOpen(in.get(i));
					}
					if(!open.isEmpty()){
						Email.deploy(open);
						open.clear();
						in.clear();
						update(closed);
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			
		};
		Thread t = new Thread(r);
		t.start();
		return Service.START_STICKY;
	  }
		
	public static ArrayList<TrackedCourse> create(ArrayList<TrackedCourse> in) throws IOException{
		String storageState = Environment.getExternalStorageState();
		Gson gson = new Gson();

	    if (storageState.equals(Environment.MEDIA_MOUNTED)) {
	    	//File dir = Environment.getExternalStorageDirectory();
	    	
	        File file = new File(Environment.getExternalStorageDirectory()+"/Android/data/com.example.ruregistered/files/RUTracker.json");
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

	      		in.add(singleCourse);

	      	}
	    }
	      	}
	    
	    return in;
	    

	}


	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	public void update(ArrayList<TrackedCourse> in ) throws IOException{
	    String storageState = Environment.getExternalStorageState();
		Gson gson = new Gson();

	    if (storageState.equals(Environment.MEDIA_MOUNTED)) {
	    	//Properly format json table
	      File file = new File(getExternalFilesDir(null), "RUTracker.json");
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

	      	closed.clear();

	    }
	}
	
	public void checkOpen(TrackedCourse in) throws Exception{
		ArrayList<Course> curr = CourseWriter.create(in.major, in.term, in.campus);
			for(int i= 0;i<curr.size();i++){
				if(curr.get(i).title.equals(in.course)){//Oh god this is so inefficient I'm sorry
				Section check[] =  curr.get(i).sections;
				for(int j = 0;j<check.length;j++){
					if(check[j].index.equals(in.section)){
						if(check[j].openStatus==true)
						open.add(in);
						else{
							closed.add(in);
						}
					}
				}
				}
			}


	
}

}