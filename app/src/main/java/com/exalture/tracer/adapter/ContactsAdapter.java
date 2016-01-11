package com.exalture.tracer.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.exalture.tracer.R;

/**
 * Created by Jyothish Jose on 11-01-2016.
 */
public class ContactsAdapter extends BaseAdapter {
    private static final String TAG = "ContactsAdapter";

    private Activity activity;
    private String[] names;
    private String[] numbers;

    private int primaryPosition;
    private int type;

    private TextView nameText;
    private TextView numberText;
    private ImageView contactImage;

    private static LayoutInflater inflater;

    public ContactsAdapter(Activity activity, String[] names, String[] numbers, int primaryPosition, int type) {
        this.activity = activity;
        this.names = names;
        this.numbers = numbers;

        this.primaryPosition = primaryPosition;
        this.type = type;

        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return numbers.length;
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
        contactImage = (ImageView) view.findViewById(R.id.contact_action_image);

        nameText.setText(names[position]);
        numberText.setText(numbers[position]);

        if(primaryPosition == position){
            view.setBackgroundColor(Color.LTGRAY);
        }

        if(type == 1){
            contactImage.setImageResource(R.drawable.trash);
        } else{
            contactImage.setImageResource(android.R.drawable.ic_menu_share);
        }

        return view;
    }
}
