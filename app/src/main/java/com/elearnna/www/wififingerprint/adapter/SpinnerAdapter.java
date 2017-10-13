package com.elearnna.www.wififingerprint.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Ahmed on 10/12/2017.
 */

public class SpinnerAdapter extends ArrayAdapter<String> {
    Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),
            "fonts/Quicksand-Regular.otf");

    public SpinnerAdapter(Context context, int resource, String[] items) {
        super(context, resource, items);
    }

    // Affects default (closed) state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getView(position, convertView, parent);
        textView.setTypeface(typeface);
        return textView;
    }

    // Affects opened state of the spinner
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
        textView.setTypeface(typeface);
        return textView;
    }
}
