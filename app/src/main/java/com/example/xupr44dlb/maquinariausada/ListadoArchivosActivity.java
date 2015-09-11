package com.example.xupr44dlb.maquinariausada;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class ListadoArchivosActivity extends Activity {

    public ListadoArchivosActivity(){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listadoarchivos);
        final ListView listado = (ListView) findViewById(R.id.list_documentos);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setLogo(R.drawable.ic_iiasa);
        File pdfFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/maquinariausada");
        ArrayList<String> archivos = new ArrayList<String>();
        if (pdfFolder.exists()) {
            File[] filenames = pdfFolder.listFiles();
            String archivo = "";
            for (int i = 0; i < filenames.length; i++) {
                archivo = filenames[i].getName().toString();
                archivos.add(archivo);
            }

        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, archivos
        );
        listado.setAdapter(arrayAdapter);
        listado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = (String) listado.getItemAtPosition(position);
                viewPdf(selectedValue);
            }
        });
    }
    public void viewPdf(String nombre){
        File pdfFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/maquinariausada");
        File myFile = new File(pdfFolder+"/"+nombre);

        Intent target = new Intent(Intent.ACTION_VIEW);

        target.setDataAndType(Uri.fromFile(myFile), "application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        Intent intent = Intent.createChooser(target, "Abrir el archivo");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Instale alguna aplicacion para ver PDFs", Toast.LENGTH_LONG).show();
        }
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                   finish();


        }
        return true;
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        ListadoArchivosActivity.this.finish();
    }
}
