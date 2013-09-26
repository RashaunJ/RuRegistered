package com.rashaunj.ruregistered;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.example.ruregistered.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;

public class Launcher extends SherlockActivity implements OnItemClickListener {

  // Array of strings storing country names
    String[] EntryText = new String[] {
            "Add New Course",
            "View Tracked Courses",
            "Settings",
            "About"
            
    };

    // Array of integers points to images stored in /res/drawable-ldpi/
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
       
   	final String PREFS_NAME = "MyPrefsFile";
   	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean firstTime = settings.getBoolean("firstTime", true);
        if (firstTime) { 
            final SharedPreferences.Editor editor = settings.edit();
            editor.putInt("interval", 300000);
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
			intent = new Intent(this,TrackerHome.class);
			startActivity(intent);
		}
		else if(arg2==3){
			intent = new Intent(this,About.class);
			startActivity(intent);
		}

	}
}