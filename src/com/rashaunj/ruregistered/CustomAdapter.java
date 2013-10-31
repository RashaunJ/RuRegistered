package com.rashaunj.ruregistered;

import com.rashaunj.ruregistered.R;

import parse.Section;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<Section>{

    Context context;
    int layoutResourceId;   
    Section data[] = null;
   
    public CustomAdapter(Context context, int layoutResourceId, Section[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        SectionHolder holder = null;
       
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
           
            holder = new SectionHolder();
            holder.index = (TextView)row.findViewById(R.id.index);
            holder.openStatus = (TextView)row.findViewById(R.id.openStatus);
            row.setTag(holder);
        }
        else
        {
            holder = (SectionHolder)row.getTag();
        }
       
        Section curr = data[position];
        holder.index.setText(curr.index);
        if(curr.openStatus!=false){
        holder.openStatus.setText("Open");
        holder.openStatus.setTextColor(Color.parseColor("#00C000"));
        }
        else{
        holder.openStatus.setText("Closed");
        holder.openStatus.setTextColor(Color.parseColor("#FF0000"));
        }
        
        
       
        return row;
    }
   
    static class SectionHolder
    {
        TextView openStatus;
        TextView index;
        TextView campus;
    }
}