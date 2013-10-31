package com.rashaunj.ruregistered;


import com.rashaunj.ruregistered.R;

import parse.Section;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SectionAdapter extends ArrayAdapter<Section>{
   private Section[] entries;
   private Activity activity;

   public SectionAdapter(Activity a, int textViewResourceId, Section entries[]) {
       super(a, textViewResourceId, entries);
       this.entries = entries;
       this.activity = a;
   }

   public static class ViewHolder{
       public TextView index;
       public TextView openStatus;
   }

   @Override
   public View getView(int position, View convertView, ViewGroup parent) {
       View v = convertView;
       ViewHolder holder;
       if (v == null) {
           LayoutInflater vi =
               (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           v = vi.inflate(R.layout.section_item, null);
           holder = new ViewHolder();
           holder.index = (TextView) v.findViewById(R.id.index);
           holder.openStatus = (TextView) v.findViewById(R.id.openStatus);
           v.setTag(holder);
       }
       else
           holder=(ViewHolder)v.getTag();

       final Section custom = entries[position];
       if (custom != null) {

           if(custom.openStatus==false){
        	   holder.index.setText(entries[position].index);
        	   holder.openStatus.setText("Closed");
           }
           else{
        	   holder.index.setText(entries[position].index);
        	   holder.openStatus.setText("Open");
           }
           
       }
       return v;
   }

}