package com.aks.calarifaiapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aks.calarifaiapp.R;

import java.util.List;

import clarifai2.dto.prediction.Concept;

/**
 * Created by Awais on 11/5/2016.
 */

public class MyAdapter extends BaseAdapter {


    List<Concept>  data;
    Context context;
    LayoutInflater layoutInflater;


    public MyAdapter(List<Concept>  data, Context context) {
        super();
        this.data = data;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {

        return data.size();
    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        convertView= layoutInflater.inflate(R.layout.item_row, null);

        TextView label=(TextView)convertView.findViewById(R.id.label);
        TextView prob=(TextView)convertView.findViewById(R.id.probability);


        label.setText(data.get(position).name() != null ? data.get(position).name() : data.get(position).id());
        prob.setText(String.valueOf((data.get(position).value())));


        return convertView;
    }

}
