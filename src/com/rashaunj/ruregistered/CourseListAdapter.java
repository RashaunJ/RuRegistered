package com.rashaunj.ruregistered;

import java.util.ArrayList;

import com.example.ruregistered.R;

import parse.Course;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CourseListAdapter extends ArrayAdapter<Course>{

    Context context;
    int layoutResourceId;   
    ArrayList<Course> data = null;
   
    public CourseListAdapter(Context context, int layoutResourceId, ArrayList<Course> data) {
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
            row = inflater.inflate(R.layout.courselist_item,null);
            holder = new SectionHolder();
            holder.coursename = (TextView)row.findViewById(R.id.coursename);
            holder.numberopen = (TextView)row.findViewById(R.id.numberopen);
           
            row.setTag(holder);
        }
        else
        {
            holder = (SectionHolder)row.getTag();
        }
       
        Course curr = data.get(position);
        holder.coursename.setText(curr.courseNumber+": " + curr.title);
        holder.numberopen.setText(curr.openSections+" sections open of " + curr.sections.length);
        return row;
    }
   
    static class SectionHolder
    {
        TextView coursename;
        TextView numberopen;
    }
}