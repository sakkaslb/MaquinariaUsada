package com.example.xupr44dlb.maquinariausada;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    /*String[] result;
    int[] imageId;*/
    ArrayList<Maquina> maquinas;
    Context context;

    private static LayoutInflater inflater = null;


   public CustomAdapter(Activity menuActivity, ArrayList<Maquina> maquinaria) {
        // TODO Auto-generated constructor stub


        context = menuActivity;
        maquinas= maquinaria;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
       // return result.length;
        return maquinas.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv, precio, horas, garantia;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.activity_anuncio, null);
        holder.tv=(TextView) rowView.findViewById(R.id.lblAnuncioHeader);
        holder.precio=(TextView) rowView.findViewById(R.id.lblAnuncioPrecio);
        holder.horas=(TextView) rowView.findViewById(R.id.lblAnuncioHoras);
        holder.garantia=(TextView) rowView.findViewById(R.id.lblAnuncioGarantia);
        holder.img=(ImageView) rowView.findViewById(R.id.imgAnuncioFoto);

        // holder.tv.setText(result[position]);
        // holder.img.setImageResource(imageId[position]);
        holder.tv.setText(maquinas.get(position).getModelo()+" ");
        NumberFormat fmt = NumberFormat.getCurrencyInstance();
        final String precio=fmt.format(maquinas.get(position).getPrecioSin()).toString();

        holder.precio.setText(precio);
        String hora=String.valueOf(maquinas.get(position).getHoras());
        holder.horas.setText("Horas: "+hora);
        holder.garantia.setText("Garantia: "+maquinas.get(position).getGarantia().replace("null","N/A"));

        //NUEVA MANERA DE CARGAR IMAGENES
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);

        // END - UNIVERSAL IMAGE LOADER SETUP

        if (maquinas.get(position).getLink().contains("http://"))
        {
            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .showImageForEmptyUri(R.drawable.ic_iiasa)
                    .showImageOnFail(R.drawable.ic_iiasa)
                    .showImageOnLoading(R.drawable.ic_iiasa).build();

            imageLoader.displayImage(maquinas.get(position).getLink(),holder.img, options);

        }


        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "Hiciste click en " + maquinas.get(position).getModelo(), Toast.LENGTH_SHORT).show();
                Intent vintent=new Intent(context,DetalleMaquinaActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("id",maquinas.get(position).getId());
                bundle.putString("familia", maquinas.get(position).getFamilia());
                bundle.putString("modelo", maquinas.get(position).getModelo());
                bundle.putString("ubicacion",maquinas.get(position).getLocalizacion());
                bundle.putString("serie",maquinas.get(position).getSerie());
                bundle.putString("descripcion", maquinas.get(position).getDescripcion());
                bundle.putString("precio",precio);
                bundle.putString("horas",String.valueOf(maquinas.get(position).getHoras()));
                bundle.putString("garantia",maquinas.get(position).getGarantia());
                bundle.putString("anio",String.valueOf(maquinas.get(position).getAnio()));
                bundle.putString("link", maquinas.get(position).getLink());
                bundle.putFloat("preciolista", maquinas.get(position).getPrecioSin());
                vintent.putExtras(bundle);

                context.startActivity(vintent);

            }
        });

        return rowView;
    }

}
