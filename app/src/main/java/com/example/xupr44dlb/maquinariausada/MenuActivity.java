package com.example.xupr44dlb.maquinariausada;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.ToggleButton;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener, AdapterViewCompat.OnItemSelectedListener{
    private Toolbar toolbar;
    Button btnFiltrar;
    ToggleButton btnVerFiltros;
    EditText txtVerPromos, txtModelo;
    Spinner cmbFamilias, cmbOrden, cmbPrecio, cmbRegion, cmbUbicacion;
    String [] datosFamilias, datosOrden, datosPrecio, datosRegion, datosUbicacion;
    Intent vintent;
    LinearLayout filtrosLayout;
    RelativeLayout filtrospadreLayout;
    GridView grid;


    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        //BOTONES
        btnVerFiltros=(ToggleButton) findViewById(R.id.btnVerFiltros);
        btnVerFiltros.setOnClickListener(this);

        btnFiltrar=(Button) findViewById(R.id.btnFiltrar);
        btnFiltrar.setOnClickListener(this);

        //LAYOUT

        filtrosLayout=(LinearLayout) findViewById(R.id.LayoutFiltros);
        filtrospadreLayout=(RelativeLayout) findViewById(R.id.linearLayout2);


        //Cargando combo de familias
        cmbFamilias=(Spinner) findViewById(R.id.cmbFamilia);
        ArrayAdapter<CharSequence> adaptador=
                ArrayAdapter.createFromResource(this, R.array.Familias,
                        android.R.layout.simple_spinner_item);

        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cmbFamilias.setAdapter(adaptador);

        //Cargando combo de orden

        cmbOrden=(Spinner)findViewById(R.id.cmbOrden);
        adaptador=ArrayAdapter.createFromResource(this, R.array.orden, android.R.layout.simple_spinner_item);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cmbOrden.setAdapter(adaptador);

        //Cargando combo de precios

        cmbPrecio=(Spinner) findViewById(R.id.cmbPrecio);
        adaptador=ArrayAdapter.createFromResource(this, R.array.Precios, android.R.layout.simple_spinner_item);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cmbPrecio.setAdapter(adaptador);

        //Cargando combo de ubicacion
        cmbUbicacion=(Spinner) findViewById(R.id.cmbUbicacion);
        adaptador=ArrayAdapter.createFromResource(this, R.array.Ubicacion, android.R.layout.simple_spinner_item);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cmbUbicacion.setAdapter(adaptador);

        //Cargando combo de region

        cmbRegion=(Spinner) findViewById(R.id.cmbRegion);
        adaptador=ArrayAdapter.createFromResource(this, R.array.Regiones, android.R.layout.simple_spinner_item);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cmbRegion.setAdapter(adaptador);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnVerFiltros:
            {
               if(btnVerFiltros.isChecked())
               {
                   filtrosLayout.setVisibility(View.VISIBLE);
                   filtrospadreLayout.setVisibility(View.VISIBLE);

               }
                else
               {
                   filtrosLayout.setVisibility(View.GONE);
                   filtrospadreLayout.setVisibility(View.GONE);
               }
                break;
            }
            case R.id.btnFiltrar:
            {
                vintent=new Intent(this,MainActivity.class);
                startActivity(vintent);
                break;
            }

        }
    }


    @Override
    public void onItemSelected(AdapterViewCompat<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterViewCompat<?> parent) {

    }

}

