package com.polinc.movieappex.ui.prods;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.polinc.movieappex.R;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<Bitmap> logos =new ArrayList<Bitmap>();
    LayoutInflater inflter;
    public CustomAdapter(Context applicationContext ) {
        this.context = applicationContext;
        inflter = (LayoutInflater.from(applicationContext));
    }

    public void addItem(Bitmap newImage){
        logos.add(newImage);
        notifyDataSetChanged();
        System.out.println("addItem="+newImage);
    }
    public void addItems(ArrayList<Bitmap> newImage){
        System.out.println("addItems="+newImage);
        logos.addAll(newImage);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return logos.size();
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
        view = inflter.inflate(R.layout.prod_gridview, null); // inflate the layout
        ImageView icon = (ImageView) view.findViewById(R.id.icon); // get the reference of ImageView
        icon.setImageBitmap(logos.get(i)); // set logo images

        return view;
    }
}