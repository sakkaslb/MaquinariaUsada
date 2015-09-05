package com.example.xupr44dlb.maquinariausada;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener, AdapterViewCompat.OnItemSelectedListener{
    private Toolbar toolbar;
    Button btnFiltrar;
    ToggleButton btnVerFiltros;
    Spinner cmbFamilias, cmbOrden, cmbPrecio, cmbRegion;
    EditText txtModelo, txtBusquedaGeneral;
    Intent vintent;
    LinearLayout filtrosLayout;
    RelativeLayout filtrospadreLayout;
    GridView grid;

    public MenuActivity() {
    }

    public ArrayList<Maquina> getListadoMaquinas() {
        return listadoMaquinas;
    }

    public void setListadoMaquinas(ArrayList<Maquina> listadoMaquinas) {
        this.listadoMaquinas = listadoMaquinas;
    }

    public  ArrayList<Maquina> listadoMaquinas=new ArrayList<Maquina>();

    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_menu);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        txtBusquedaGeneral=(EditText) findViewById(R.id.txtBusquedaGeneral);

        //DESCARGA DE INFORMACION


        Boolean downloadInfo=true;
        Bundle bundle =this.getIntent().getExtras();
        downloadInfo=bundle.getBoolean("descargaInfo");
        if (downloadInfo)
        {
            Log.i("MENU","ENTRE A DESCARGAR INFORMACION");
            ProgressDialog progreso=new ProgressDialog(this);
            progreso.setMessage("Descargando maquinas...");
            progreso.show();
            new DownloadInfo(this,this).execute();
            progreso.dismiss();
            //GUARDAR ULTIMA FECHA DE DESCARGA
            SharedPreferences prefs=getSharedPreferences("loginUsuarios", Context.MODE_PRIVATE);
            Calendar c = Calendar.getInstance();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("ultimoacceso", c.getTime().toString());
            editor.commit();

        }


        //BOTONES
        btnVerFiltros=(ToggleButton) findViewById(R.id.btnVerFiltros);
        btnVerFiltros.setOnClickListener(this);

        btnFiltrar=(Button) findViewById(R.id.btnFiltrar);
        btnFiltrar.setOnClickListener(this);

        //LAYOUT

        filtrosLayout=(LinearLayout) findViewById(R.id.LayoutFiltros);
        filtrospadreLayout=(RelativeLayout) findViewById(R.id.linearLayout2);

        //Modelos

        txtModelo=(EditText) findViewById(R.id.txtModelo);

        ArrayAdapter<CharSequence> adaptador;
        //Cargando combo de familias
        cmbFamilias=(Spinner) findViewById(R.id.cmbFamilia);
        adaptador=ArrayAdapter.createFromResource(this,R.array.Familias,android.R.layout.simple_spinner_item);
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

        //Cargando combo de region

        cmbRegion=(Spinner) findViewById(R.id.cmbRegion);
        adaptador=ArrayAdapter.createFromResource(this, R.array.Regiones, android.R.layout.simple_spinner_item);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cmbRegion.setAdapter(adaptador);


        //Gridview
        grid=(GridView) findViewById(R.id.gridView1);
        try {  listadoMaquinas=new ObtenerListado(this,this,cmbOrden.getSelectedItem().toString(),cmbFamilias.getSelectedItem().toString(),txtModelo.getText().toString(),cmbRegion.getSelectedItem().toString(),cmbPrecio.getSelectedItem().toString(), txtBusquedaGeneral.getText().toString()).execute().get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.i("OBSERVACION","SI EJECUTE OBTENER LISTADO");

        grid.setAdapter(new CustomAdapter(this,listadoMaquinas));

       // grid.setAdapter(new CustomAdapter(this,prgmNameList,prgmImages));
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
            SharedPreferences prefs=getSharedPreferences("loginUsuarios",0);
            prefs.edit().putBoolean("session",false).commit();
            prefs.edit().remove("usuario").commit();
            prefs.edit().remove("vendedor").commit();
            prefs.edit().remove("salesrepid").commit();
            prefs.edit().remove("salesrepno").commit();
            Toast.makeText(getApplicationContext(), "Cerrando sesión", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(this, MainActivity.class);
            startActivity(i);
            return true;
        }
        if(id==R.id.action_vercotizaciones){
            Intent i=new Intent(this, ListadoArchivosActivity.class);
            startActivity(i);
        }
         if(id==R.id.action_search){
             try {
                 Log.i("OBSERVACION","ANTES DE EJECUTAR OBTENER LISTADO X BUSQUEDA");
                 listadoMaquinas=new ObtenerListado(this,this,"","","","","",txtBusquedaGeneral.getText().toString()).execute().get();

             } catch (InterruptedException e) {
                 e.printStackTrace();
             } catch (ExecutionException e) {
                 e.printStackTrace();
             }
             Log.i("OBSERVACION","DESPUES DE EJECUTAR OBTENER LISTADO X BUSQUEDA");

             grid.setAdapter(new CustomAdapter(this, listadoMaquinas));
         }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnVerFiltros:
            {
               if(btnVerFiltros.isChecked())
               {
                   TranslateAnimation anim = new TranslateAnimation(-100f, 0f, 0f, 0f);
                   anim.setDuration(500);
                   filtrospadreLayout.setAnimation(anim);
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

                try {

                    listadoMaquinas=new ObtenerListado(this,this,cmbOrden.getSelectedItem().toString(),cmbFamilias.getSelectedItem().toString(),txtModelo.getText().toString(),cmbRegion.getSelectedItem().toString(),cmbPrecio.getSelectedItem().toString(),txtBusquedaGeneral.getText().toString()).execute().get();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                grid.setAdapter(new CustomAdapter(this,listadoMaquinas));

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

class ObtenerListado extends AsyncTask<Void,Integer,ArrayList<Maquina>>{

   Context context;
   Activity activity;
   String orden, familia, modelo, localizacion, precio, busqueda;
   ProgressDialog progreso;

    public ObtenerListado(Context ctx, Activity activity,String p_orden, String p_familia, String p_modelo, String p_localizacion, String p_precio, String p_busqueda) {
        this.context=ctx;
        this.activity=activity;
        this.orden=p_orden;
        this.familia=p_familia;
        this.modelo=p_modelo;
        this.localizacion=p_localizacion;
        this.precio=p_precio;
        this.busqueda=p_busqueda;
        progreso=new ProgressDialog(context);
    }


    @Override
    protected ArrayList<Maquina> doInBackground(Void... params) {

        ArrayList<Maquina> resultado=new ArrayList<Maquina>();
        USQLiteHelper usuario=new USQLiteHelper(context,"DBUsada",null,1);
        SQLiteDatabase db=usuario.getWritableDatabase();
        String query="";
        Log.i("OJO",busqueda);
        if (modelo==null){
            modelo="";
        }
        if(familia==null){
            familia="";
        }
        if(localizacion==null)
        {
            localizacion="";
        }
        if(busqueda.isEmpty()){

        query="SELECT familia, localizacion, modelo, serie, horas, garantia, precio_sin_acondicionar, precio_cat_usado_certificado,"+
                      " precio_credito, descripcion, link,id, anio from Maquinaria where familia like '%"+familia+"%' and modelo like '%"+modelo+"%' and localizacion like '%"+localizacion+"%' ";

        if(precio.contains("Menor a 35.000")){
             query+=" and precio_sin_acondicionar<35000 ";
        }else if(precio.contains("35.000 a 50.000")){
            query+=" and precio_sin_acondicionar>=35000 and precio_sin_acondicionar<=50000 ";
        } else if(precio.contains("50.000 a 100.000")){
            query+=" and precio_sin_acondicionar>50000 and precio_sin_acondicionar<=100000 ";
        } else if(precio.contains("100.000 en adelante")){
            query+=" and precio_sin_acondicionar>100000 ";
        }else {
            query+=" and precio_sin_acondicionar>0 ";
        }

        if(orden.contains("Más recientes")) {
            query += " ORDER BY fecha_modificacion DESC ";
        }
        else if (orden.contains("Precio: Menor a Mayor")){
            query += " ORDER BY precio_sin_acondicionar ";

        } else if(orden.contains("Precio: Mayor a Menor")){
            query += " ORDER BY precio_sin_acondicionar DESC";

        } else {
            query += " ORDER BY modelo ";
        }
        } else {
            query="SELECT familia, localizacion, modelo, serie, horas, garantia, precio_sin_acondicionar, precio_cat_usado_certificado,"+
                    " precio_credito, descripcion, link,id, anio from Maquinaria where familia like '%"+busqueda+"%' or modelo like '%"+busqueda+"%' or localizacion like '%"+busqueda+"%' or serie like '%"+busqueda+"%'";

        }

        Log.i("CONSULTA LISTADO",query);

        Cursor c=db.rawQuery(query,null);
        if (c.moveToFirst()){
            do{
                Log.i("CONSULTA LISTADO","ENTRE POR EL ID"+c.getInt(11));
                Maquina maquinaria=new Maquina();
                maquinaria.setFamilia(c.getString(0));
                maquinaria.setLocalizacion(c.getString(1));
                maquinaria.setModelo(c.getString(2));
                maquinaria.setSerie(c.getString(3));
                maquinaria.setHoras(c.getInt(4));
                maquinaria.setGarantia(c.getString(5));
                maquinaria.setPrecioSin(c.getFloat(6));
                maquinaria.setPrecioCertificado(c.getFloat(7));
                maquinaria.setPreciocredito(c.getFloat(8));
                maquinaria.setDescripcion(c.getString(9));
                maquinaria.setLink(c.getString(10));
                maquinaria.setId(c.getInt(11));
                maquinaria.setAnio(c.getInt(12));
                resultado.add(maquinaria);

            }
            while (c.moveToNext());
            c.close();
        }
        Log.i("Resultado","AUXILIO"+resultado.size());
        db.close();
        return resultado;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progreso.setMessage("Cargando equipos...");
        progreso.show();
    }

    @Override
    protected void onPostExecute(ArrayList<Maquina> maquinas) {
        super.onPostExecute(maquinas);
        progreso.dismiss();
    }
}
