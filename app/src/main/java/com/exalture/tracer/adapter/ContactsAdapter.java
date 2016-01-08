package com.exalture.tracer.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.exalture.tracer.R;

/**
 * Created by Jyothish Jose on 05-01-2016.
 */
public class ContactsAdapter extends BaseAdapter {
    private static final String TAG = "ContactsAdapter";

    private int type = 1;

    private String[] names;
    private String[] numbers;

//    ui reference
    private Activity activity;
    private TextView nameText;
    private TextView numberText;
    private ImageView actionImage;

    private static LayoutInflater inflater;

    public ContactsAdapter(Activity activity, String[] names, String[] numbers, int type) {
        this.activity = activity;
        this.names = names;
        this.numbers = numbers;
        this.type = type;

        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return names.length;
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
            view = inflater.inflate(R.layout.list_item, null);
        }

        nameText = (TextView) view.findViewById(R.id.edit_contact_name);
        numberText = (TextView) view.findViewById(R.id.edit_contact_number);
        actionImage = (ImageView) view.findViewById(R.id.contact_action_image);

        nameText.setText(names[position]);
        numberText.setText(numbers[position]);

        if(type == 2){
            actionImage.setImageResource(android.R.drawable.ic_menu_share);
        } else{
            actionImage.setImageResource(R.drawable.trash);
        }

        return view;
    }
}
