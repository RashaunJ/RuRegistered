package com.rashaunj.ruregistered;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.rashaunj.ruregistered.R;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class Settings extends SherlockPreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar bar = getSupportActionBar();
		addPreferencesFromResource(R.xml.preferences);
		bar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
		bar.setTitle("Settings");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	   	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		EditTextPreference email = (EditTextPreference) findPreference("email");	
		email.setDefaultValue(settings.getString("email", "youremail@here.com"));
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


}
