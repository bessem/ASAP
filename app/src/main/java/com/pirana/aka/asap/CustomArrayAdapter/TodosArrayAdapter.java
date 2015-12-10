package com.pirana.aka.asap.CustomArrayAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pirana.aka.asap.DAO.Todo;
import com.pirana.aka.asap.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aka on 12/3/15.
 */
public class TodosArrayAdapter extends ArrayAdapter<Todo> {
    public Context mContext;
    public int resources;
    public List<Todo> todos = new ArrayList<Todo>();
    public TodosArrayAdapter(Context context, int resource, List<Todo> todos) {
        super(context, resource, todos);
        this.mContext = context;
        this.resources = resource;
        this.todos = todos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        if(convertView==null){
            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(resources, parent, false);
        }
        Todo todo = todos.get(position);
        TextView textViewItem = (TextView) convertView.findViewById(R.id.firstLine);
        textViewItem.setText(todo.getAbout());
        return convertView;
    }
}
