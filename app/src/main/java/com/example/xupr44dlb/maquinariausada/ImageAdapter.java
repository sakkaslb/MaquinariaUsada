package com.example.xupr44dlb.maquinariausada;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Imagen> mImagenes;


    //constructor
    public ImageAdapter (Context c, ArrayList<Imagen> imagenes){
        mContext = c;
        mImagenes=imagenes;
    }

    @Override
    public int getCount() {
        return mImagenes.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView = new ImageView(mContext);
        new ImageLoadTask(mImagenes.get(i).getUrl(),imageView).execute();
        imageView.setLayoutParams(new Gallery.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        return imageView;
    }
}