package com.example.xupr44dlb.maquinariausada;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class DetalleMaquinaActivity extends Activity implements View.OnClickListener{
    static TextView mDotsText[];
    private int mDotsCount;
    private LinearLayout mDotsLayout;
    TextView txtPrecio, txtFamilia, txtModelo, txtUbicacion, txtDescripcion, txtSerie, txtAnio, txtHoras, txtGarantia,txtDetalleHeader;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setUpWindow();
        Bundle bundle =this.getIntent().getExtras();
        txtModelo=(TextView) findViewById(R.id.txtDetalleModelo);
        txtFamilia=(TextView) findViewById(R.id.txtDetalleFamilia);
        txtUbicacion=(TextView) findViewById(R.id.txtDetalleLocalizacion);
        txtDescripcion=(TextView)findViewById(R.id.txtDetalleDescripcion);
        txtSerie=(TextView) findViewById(R.id.txtDetalleNoSerie);
        txtAnio=(TextView) findViewById(R.id.txtDetalleAnio);
        txtHoras=(TextView)findViewById(R.id.txtDetalleHoras);
        txtGarantia=(TextView) findViewById(R.id.txtDetalleGarantia);
        txtPrecio=(TextView)findViewById(R.id.txtDetallePrecio);
        txtDetalleHeader=(TextView)findViewById(R.id.txtDetalleHeader);

        txtModelo.setText(bundle.getString("modelo"));
        txtFamilia.setText(bundle.getString("familia"));
        txtUbicacion.setText(bundle.getString("ubicacion"));
        txtDescripcion.setText(bundle.getString("descripcion"));
        txtSerie.setText(bundle.getString("serie"));
        txtAnio.setText(bundle.getString("anio"));
        txtHoras.setText(bundle.getString("horas"));
        txtGarantia.setText(bundle.getString("garantia"));
        txtPrecio.setText(bundle.getString("precio"));
        txtDetalleHeader.setText(bundle.getString("familia")+" - "+bundle.getString("modelo"));


        Gallery gallery = (Gallery)findViewById(R.id.gallery);
        gallery.setAdapter(new ImageAdapter(this));
        mDotsLayout = (LinearLayout)findViewById(R.id.image_count);
        //here we count the number of images we have to know how many dots we need
        mDotsCount = gallery.getAdapter().getCount();

        //here we create the dots
        //as you can see the dots are nothing but "."  of large size
        mDotsText = new TextView[mDotsCount];

        //here we set the dots
        for (int i = 0; i < mDotsCount; i++) {
            mDotsText[i] = new TextView(this);
            mDotsText[i].setText(".");
            mDotsText[i].setTextSize(45);
            mDotsText[i].setTypeface(null, Typeface.BOLD);
            mDotsText[i].setTextColor(android.graphics.Color.GRAY);
            mDotsLayout.addView(mDotsText[i]);
        }
        //when we scroll the images we have to set the dot that corresponds to the image to White and the others
        //will be Gray
        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapterView, View view, int pos, long l) {

                for (int i = 0; i < mDotsCount; i++) {
                    DetalleMaquinaActivity.mDotsText[i]
                            .setTextColor(Color.GRAY);
                }

                DetalleMaquinaActivity.mDotsText[pos]
                        .setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView adapterView) {

            }
        });

    }
    public void setUpWindow() {

        // Creates the layout for the window and the look of it

        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_detallemaquina);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        // Params for the window.
        // You can easily set the alpha and the dim behind the window from here
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 1.0f;    // lower than one makes it more transparent
        params.dimAmount = 0.7f;  // set it higher if you want to dim behind the window
        getWindow().setAttributes(params);

        // Gets the display size so that you can set the window to a percent of that
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        // You could also easily used an integer value from the shared preferences to set the percent
        if (height > width) {
            getWindow().setLayout((int) (width * .9), (int) (height * .7));
        } else {
            getWindow().setLayout((int) (width * .9), (int) (height * .9));
        }


    }

    @Override
    public void onClick(View v) {

    }
}
 class DownloadImages extends AsyncTask<Void, Integer, ArrayList<Imagen>>
 {

     @Override
     protected ArrayList<Imagen> doInBackground(Void... params) {
         return null;
     }

     @Override
     protected void onPreExecute() {
         super.onPreExecute();
     }

     @Override
     protected void onPostExecute(ArrayList<Imagen> imagens) {
         super.onPostExecute(imagens);
     }
 }

