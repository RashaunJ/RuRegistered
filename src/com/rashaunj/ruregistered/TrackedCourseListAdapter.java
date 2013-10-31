
package com.rashaunj.ruregistered;

import java.util.ArrayList;

import com.rashaunj.ruregistered.R;

import parse.TrackedCourse;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TrackedCourseListAdapter extends ArrayAdapter<TrackedCourse>{

    Context context;
    int layoutResourceId;   
    ArrayList<TrackedCourse> data = null;
   
    public TrackedCourseListAdapter(Context context, int layoutResourceId, ArrayList<TrackedCourse> data) {
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
            //row = inflater.inflate(layoutResourceId, parent, false);
            row = inflater.inflate(R.layout.tracked_item,null);
            holder = new SectionHolder();
            holder.coursename = (TextView)row.findViewById(R.id.tcoursename);
            holder.section = (TextView)row.findViewById(R.id.tsection);
           
            row.setTag(holder);
        }
        else
        {
            holder = (SectionHolder)row.getTag();
        }
       
        TrackedCourse curr = data.get(position);
        holder.coursename.setText(curr.major+":" + curr.course);
        holder.section.setText("Section " + curr.section);
        return row;
    }
   
    static class SectionHolder
    {
        TextView coursename;
        TextView section;
    }
}
