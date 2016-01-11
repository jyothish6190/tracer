package com.exalture.tracer.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.exalture.tracer.R;

/**
 * Created by Jyothish Jose on 08-01-2016.
 */
public class SettingsAdapter extends BaseAdapter {
    private static final String TAG = "SettingsAdapter";

    private static String[] settings;

    private static Activity activity;

    private static LayoutInflater inflater;

    private static TextView itemText;

    public SettingsAdapter(Activity activity, String[] settings){
        this.activity = activity;
        this.settings = settings;

        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return settings.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = inflater.inflate(R.layout.settings_item, null);
        }

        itemText = (TextView) view.findViewById(R.id.edit_settings_name);

        itemText.setText(settings[position]);

        return view;
    }
}
