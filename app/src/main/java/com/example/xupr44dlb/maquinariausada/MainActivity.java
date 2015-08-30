package com.example.xupr44dlb.maquinariausada;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;



public class MainActivity extends Activity implements View.OnClickListener{
    EditText txtUsuario, txtContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtUsuario=(EditText)findViewById(R.id.txtUsuario);
        txtContrasena=(EditText)findViewById(R.id.txtPassword);
        Button btnLogin=(Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        TextView lblRegistrate=(TextView) findViewById(R.id.lblRegistrate);
        lblRegistrate.setOnClickListener(this);

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
                new Login(this,this,txtUsuario.getText().toString(),txtContrasena.getText().toString()).execute();
                SharedPreferences prefs=getSharedPreferences("loginUsuario", Context.MODE_PRIVATE);
                Boolean res=prefs.getBoolean("session",false);
                if (res){
                    Toast.makeText(getApplicationContext(), "Acceso exitoso", Toast.LENGTH_LONG).show();
                    Intent vintent=new Intent(MainActivity.this, MenuActivity.class);
                    startActivity(vintent);
                }else Toast.makeText(getApplicationContext(), "Acceso fallido", Toast.LENGTH_LONG).show();

                break;
            }
            case R.id.lblRegistrate:{
                Intent vintent=new Intent(MainActivity.this,RegistroActivity.class);
                vintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(vintent);
                break;
            }

        }
    }


}class Login extends AsyncTask<Void, Integer, Boolean>
{
    Context context;
    Activity activity;
    String username, password;
    ProgressDialog dialog;
    private static String URL="http://grupoiiasa.com:84/WSMobile/Login/login.svc/?user=xupr44azd&pass=Ingres0";

    public Login(Context context, Activity actividad, String usuario, String clave) {
        this.context=context;
        this.activity=actividad;
        this.username=usuario;
        this.password=clave;
        dialog=new ProgressDialog(context);

    }

    @Override
    protected Boolean doInBackground(Void... params) {
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
        String myJSONString = builder.toString().replace("]","");
        try {
            JSONObject jObj = new JSONObject(myJSONString.replace("[",""));
            String u = jObj.getString("usuario");
            Log.i("OJO",u);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Procesando");
        dialog.show();


    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        dialog.dismiss();
        if (result) {

            SharedPreferences prefs = context.getSharedPreferences("loginUsuarios", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("usuario", username);
            editor.putBoolean("session", true);
            editor.commit();
            activity.startActivity(new Intent(activity, MenuActivity.class));
        }
    }
}
