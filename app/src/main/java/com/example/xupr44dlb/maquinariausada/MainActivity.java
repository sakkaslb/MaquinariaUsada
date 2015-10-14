package com.example.xupr44dlb.maquinariausada;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;


public class MainActivity extends Activity implements View.OnClickListener{
    EditText txtUsuario, txtContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);
        txtUsuario=(EditText)findViewById(R.id.txtUsuario);
        txtContrasena=(EditText)findViewById(R.id.txtPassword);
        Button btnLogin=(Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
       // TextView lblRegistrate=(TextView) findViewById(R.id.lblRegistrate);
        //lblRegistrate.setOnClickListener(this);

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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:{
                Boolean resultado=false;
                try {
                    resultado=new Login(this,this,txtUsuario.getText().toString(),txtContrasena.getText().toString()).execute().get();
                } catch (InterruptedException e) {
                    Log.e("ERROR LOGIN","ERROR EN TODO");
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    Log.e("ERROR LOGIN","ERROR EN TODO PARTE 2");
                }
                  SharedPreferences prefs=getSharedPreferences("loginUsuarios", Context.MODE_PRIVATE);
               // Boolean res=prefs.getBoolean("session",false);

                if (resultado){
                    Toast.makeText(getApplicationContext(), "Acceso exitoso", Toast.LENGTH_LONG).show();
                    new DownloadInfo(this,this).execute();
                    Intent vintent=new Intent(MainActivity.this, MenuActivity.class);
                    Bundle b=new Bundle();
                    b.putBoolean("descargaInfo",false); //para pasar varios campos
                    vintent.putExtras(b);

                    //Actualizo la preferencia de ultima fecha de sincronizacion
                    Calendar c = Calendar.getInstance();
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("ultimoacceso", c.getTime().toString());
                    editor.commit();

                    startActivity(vintent);

                }else Toast.makeText(getApplicationContext(), resultado.toString(), Toast.LENGTH_LONG).show();

                break;
            }
            /*case R.id.lblRegistrate:{
                Intent vintent=new Intent(MainActivity.this,RegistroActivity.class);
                vintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(vintent);
                break;
            }*/

        }
    }


}class Login extends AsyncTask<Void, Integer, Boolean>
{
    Context context;
    Activity activity;
    String username, password, XUId, SalesRepNo, SalesRepId,vendedor ;
    ProgressDialog dialog;
    String URL="http://grupoiiasa.com:84/WSMobile/Login/login.svc/?user=";

    public Login(Context context, Activity actividad, String usuario, String clave) {
        this.context=context;
        this.activity=actividad;
        this.username=usuario;
        this.password=clave;
        dialog=new ProgressDialog(context);
    }

    @Override
    protected Boolean doInBackground(Void... params) {

            URL+=username+"&pass="+password.replace(" ","")+"";
            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(URL);
            try{
                HttpResponse response = client.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if(statusCode == 200){
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while((line = reader.readLine()) != null){
                        builder.append(line);
                    }
                } else {
                    Log.e(MainActivity.class.toString(),"Failed to get JSON object");
                }
            }catch(ClientProtocolException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }


            if (builder.toString().contains("false")) {

                SharedPreferences prefs=context.getSharedPreferences("loginUsuarios",0);
                prefs.edit().remove("usuario").commit();
                prefs.edit().remove("vendedor").commit();
                prefs.edit().remove("salesrepid").commit();
                prefs.edit().remove("salesrepno").commit();
                prefs.edit().remove("session").commit();
                return false;
            }
                else {

                String myJSONString = builder.toString().replace("]","");
                try {
                    JSONObject jObj = new JSONObject(myJSONString.replace("[",""));
                    username = jObj.getString("usuario");
                    vendedor=jObj.getString("vendedor");
                    XUId=jObj.getString("XUId");
                    SalesRepNo=jObj.getString("salesrepno");
                    SalesRepId=jObj.getString("SalesRepId");

                    Log.i("OJO",username);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return true;

            }


    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Procesando");
        dialog.setCancelable(false);
        dialog.show();


    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        if (result) {

            SharedPreferences prefs = context.getSharedPreferences("loginUsuarios", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("usuario", username);
            editor.putString("vendedor",vendedor);
            editor.putString("salesrepid",SalesRepId);
            editor.putString("salesrepno",SalesRepNo);
            editor.putBoolean("session", true);
            editor.commit();
            Log.i("MAIN LOGIN","HICE LOGIN Y GUARDE LAS PREFERENCIAS");
            //activity.startActivity(new Intent(activity, MenuActivity.class));
        }
        dialog.dismiss();
    }
}

class DownloadInfo extends AsyncTask<Void, Integer, Boolean>
{
    Context context;
    Activity activity;
    ProgressDialog dialog;
   // String URL="https://dl.dropboxusercontent.com/u/51632131/prueba.html";

    public DownloadInfo(Context context, Activity actividad) {
        this.context=context;
        this.activity=actividad;
        dialog=new ProgressDialog(context);

    }

    @Override
    protected Boolean doInBackground(Void... params) {

        //Leyendo las preferencias para ver la ultima fecha de modificacion
        SharedPreferences prefs=context.getSharedPreferences("loginUsuarios", Context.MODE_PRIVATE);
        String fechaini=prefs.getString("ultimoacceso","Thu Jan 2 00:00:00 GMT-00:00 2015");
        SimpleDateFormat formato=new SimpleDateFormat("EEE, MMM dd hh:mm:ss z yyyy");
        try {
            Date fecha=formato.parse(fechaini);
            Log.i("OJO",fecha.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i("ERROR","NO PUDE CONVERTIR"+fechaini);
        }
        String URL="http://grupoiiasa.com:84/WSMobile/ListadoMaquinariaUsada/tipos_listados.svc/maquinariasUsadas/?fechainicio=20150901&fechafin=20150930";
        //AQUI SE DEBE CONCATENAR LOS PARAMETROS CUANDO LOS TENGAS LISTOS PARA HACER EL REQUEST
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(URL);
        try{
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if(statusCode == 200){
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while((line = reader.readLine()) != null){
                    builder.append(line);
                }
            } else {
                Log.e(MainActivity.class.toString(),"Failed to get JSON object");
            }
        }catch(ClientProtocolException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        Log.i("OJO",builder.toString());
        String json=builder.toString().replace("<html>","");
        json=json.replace("<head>","");
        json=json.replace("</head>","");
        json=json.replace("<body>","");
        json=json.replace("</body>","");
        json=json.replace("</html>","");
        Log.i("OJO",json);

        try {

            JSONObject jsonRootObject=new JSONObject(json);
            JSONArray arr=jsonRootObject.optJSONArray("Maquina");
            for (int i=0; i<arr.length(); i++){
                Log.i("DOWNLOADINFO","INICIE DO IN BACKGROUND JSON NO"+i);
                JSONObject jsonProductObject = arr.getJSONObject(i);
                Integer id=jsonProductObject.getInt("id");
                new ValidateData(context,activity,id).execute();
                Maquina maquinaria=new Maquina();
                maquinaria.setId(id);
                maquinaria.setFamilia(jsonProductObject.getString("familia"));
                maquinaria.setLocalizacion(jsonProductObject.getString("localizacion"));
                maquinaria.setModelo(jsonProductObject.getString("modelo"));
                maquinaria.setSerie(jsonProductObject.getString("serie"));
                maquinaria.setHoras(jsonProductObject.getInt("horas"));
                maquinaria.setGarantia(jsonProductObject.getString("garantia"));
                maquinaria.setAnio(jsonProductObject.getInt("anio"));
                maquinaria.setPrecioSin((float) jsonProductObject.getDouble("precio_sin_acondicionador_sin_garantia"));
                maquinaria.setPrecioCertificado((float) jsonProductObject.getDouble("precio_cat_usado_certificado"));
                maquinaria.setPreciocredito((float) jsonProductObject.getDouble("precio_credito"));
                maquinaria.setDescripcion(jsonProductObject.getString("descripcion"));
                maquinaria.setFecha_modificacion(jsonProductObject.getString("fecha_modificacion"));
                maquinaria.setLink(jsonProductObject.getString("Link"));
                new InserData(context,activity, maquinaria).execute();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //loop through each object


        return true;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Descargando equipos nuevos...");
        dialog.show();

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        dialog.dismiss();
    }
}

class InserData extends AsyncTask<Void, Integer, Boolean>
{

    Context context;
    Activity activity;
    Maquina maquinaria;

    InserData(Context context, Activity activity, Maquina maquina) {
        this.context=context;
        this.activity=activity;
        this.maquinaria=maquina;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Boolean success=false;
        USQLiteHelper usuario=new USQLiteHelper(context,"DBUsada",null,1);
        SQLiteDatabase db=usuario.getWritableDatabase();
        ContentValues content= new ContentValues();
        content.put("id",maquinaria.getId());
        content.put("familia",maquinaria.getFamilia());
        content.put("localizacion", maquinaria.getLocalizacion());
        content.put("modelo",maquinaria.getModelo());
        content.put("serie",maquinaria.getSerie());
        content.put("horas",maquinaria.getHoras());
        content.put("garantia",maquinaria.getGarantia());
        content.put("anio",maquinaria.getAnio());
        content.put("precio_sin_acondicionar",maquinaria.getPrecioSin());
        content.put("precio_cat_usado_certificado",maquinaria.getPrecioCertificado());
        content.put("precio_credito",maquinaria.getPreciocredito());
        content.put("descripcion", maquinaria.getDescripcion());
        content.put("fecha_modificacion",maquinaria.getFecha_modificacion().toString());
        content.put("link",maquinaria.getLink());
        try {
            db.insert("Maquinaria",null,content);
            success=true;
        }
        catch (Exception ex)
        {
            success=false;

        }
        finally {
            db.close();

        }
        return success;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }
}

class ValidateData extends AsyncTask<Void, Integer, Boolean>
{
    Context context;
    Activity activity;
    Integer id;
    Boolean result=false;

    ValidateData(Context context, Activity activity, Integer id) {
       this.context=context;
       this.activity=activity;
       this.id=id;

    }

    @Override
    protected Boolean doInBackground(Void... params) {
       // Log.i("VALIDATE DATA","INICIE DO IN BACKGROUND");
        USQLiteHelper usuario=new USQLiteHelper(context,"DBUsada",null,1);
        SQLiteDatabase db=usuario.getWritableDatabase();
        Cursor c=db.rawQuery("SELECT id from Maquinaria where id="+id.toString(),null);
        if(c.getCount()<1){
            c.close();
            result=false;
        }
        if (c.moveToFirst()){
           // Log.i("VALIDATE DATA","ENTRE A MOVE TO FIRST");
            this.id=c.getInt(c.getColumnIndex("id"));
            try{
                db.delete("Maquinaria","id="+id.toString(),null);
            }catch (Exception e){
                Log.i("Exception",e.toString());
            }
            finally {
                c.close();

            }


            result=true;
        }
        db.close();
        return result;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

    }
}

