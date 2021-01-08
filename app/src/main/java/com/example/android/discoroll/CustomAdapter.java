package com.example.android.discoroll;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import pl.droidsonroids.gif.GifImageView;

public class CustomAdapter extends BaseAdapter {
    Context context;
    int characters[];
    LayoutInflater inflter;
    public CustomAdapter(Context applicationContext, int[] characters) {
        this.context = applicationContext;
        this.characters = characters;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return characters.length;
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
        view = inflter.inflate(R.layout.gridview, null); // inflate the layout
        GifImageView icon = (GifImageView) view.findViewById(R.id.character); // get the reference of ImageView
        icon.setImageResource(characters[i]); // set logo images
        return view;
    }
}


