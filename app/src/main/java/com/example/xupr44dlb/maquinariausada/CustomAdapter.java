package com.example.xupr44dlb.maquinariausada;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    /*String[] result;
    int[] imageId;*/
    ArrayList<Maquina> maquinas;
    Context context;

    private static LayoutInflater inflater = null;

   // public CustomAdapter(Activity menuActivity, String[] prgmNameList, int[] prgmImages) {
   public CustomAdapter(Activity menuActivity, ArrayList<Maquina> maquinaria) {
        // TODO Auto-generated constructor stub
        //result = prgmNameList;
       // imageId = prgmImages;

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
        holder.precio=(TextView) rowView.findViewById(R.id.lblAnuncioHoras);
        holder.horas=(TextView) rowView.findViewById(R.id.lblAnuncioHoras);
        holder.garantia=(TextView) rowView.findViewById(R.id.lblAnuncioGarantia);
        holder.img=(ImageView) rowView.findViewById(R.id.imgAnuncioFoto);

        // holder.tv.setText(result[position]);
        // holder.img.setImageResource(imageId[position]);
        holder.tv.setText((CharSequence) maquinas.get(position).getModelo());
       // holder.precio.setText((int) maquinas.get(position).getPrecioCertificado());
   //     holder.horas.setText(maquinas.get(position).getHoras());
        holder.garantia.setText(maquinas.get(position).getGarantia());
        new ImageLoadTask(maquinas.get(position).getLink(), holder.img).execute();

        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "Hiciste click en " + maquinas.get(position).getModelo(), Toast.LENGTH_LONG).show();
                Intent vintent=new Intent(context,DetalleMaquinaActivity.class);
                context.startActivity(vintent);

            }
        });

        return rowView;
    }

}
class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

    private String url;
    private ImageView imageView;

    public ImageLoadTask(String url, ImageView imageView) {
        this.url = url;
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        imageView.setImageBitmap(bitmap);
    }
}
