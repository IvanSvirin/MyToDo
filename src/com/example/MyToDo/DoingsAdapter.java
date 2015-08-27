package com.example.MyToDo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ivan on 20.07.2015.
 */
public class DoingsAdapter extends BaseAdapter{
    private ArrayList<Doing> doings;
    private Context c;
    public DoingsAdapter(ArrayList<Doing> doings, Context c) {
        this.doings = doings;
        this.c = c;
    }

    @Override
    public int getCount() {
        return doings.size();
    }

    @Override
    public Object getItem(int position) {
        return doings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View toDoView = convertView;
        if (toDoView == null) {
            toDoView = LayoutInflater.from(c).inflate(R.layout.listitem_doing, null);
        }
        RelativeLayout rlDoing = (RelativeLayout) toDoView.findViewById(R.id.doing);
        Doing doing = doings.get(position);
        TextView doingView = (TextView) toDoView.findViewById(R.id.doingView);
        doingView.setText(doing.getToDo());
        TextView dateView = (TextView) toDoView.findViewById(R.id.dateView);
        dateView.setText(doing.getDate());
        if (doing.isDone()) {
            doingView.setBackgroundColor(Color.argb(198, 149, 251, 108));
            dateView.setBackgroundColor(Color.argb(198, 149, 251, 108));
        }
        return toDoView;
    }
}
