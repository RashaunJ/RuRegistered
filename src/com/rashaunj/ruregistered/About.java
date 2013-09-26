package com.rashaunj.ruregistered;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.example.ruregistered.R;
import android.os.Bundle;


public class About extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
		bar.setTitle("About");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}



}
