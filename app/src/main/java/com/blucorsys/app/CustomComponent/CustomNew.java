package com.blucorsys.app.CustomComponent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blucorsys.app.labourcontractorapp.R;

import java.util.ArrayList;
import java.util.List;

public class CustomNew extends BaseAdapter {
    Context context;
    int images[];

    private List<String> categoryEng;
    LayoutInflater inflter;

    public CustomNew(Context applicationContext, List<String> category ) {
        this.context = applicationContext;

        categoryEng =   category;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return categoryEng.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner_items, null);
        //ImageView icon = (ImageView) view.findViewById(R.id.imageViewc);
        TextView names = (TextView) view.findViewById(R.id.textViewc);
        //icon.setImageResource(images[i]);

        names.setText(categoryEng.get(i));

        return view;
    }
}
