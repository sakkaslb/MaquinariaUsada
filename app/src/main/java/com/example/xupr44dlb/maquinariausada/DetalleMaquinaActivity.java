package com.example.xupr44dlb.maquinariausada;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class DetalleMaquinaActivity extends Activity implements View.OnClickListener{
    static TextView mDotsText[];
    private int mDotsCount;
    private LinearLayout mDotsLayout;
    TextView txtPrecio, txtFamilia, txtModelo, txtUbicacion, txtDescripcion, txtSerie, txtAnio, txtHoras, txtGarantia,txtDetalleHeader;
    Button btnCotizar;
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
        txtUbicacion.setText(bundle.getString("ubicacion").replace("null","No hay datos disponibles"));
        txtDescripcion.setText(bundle.getString("descripcion").replace("null","No hay datos disponibles"));
        txtSerie.setText(bundle.getString("serie"));
        txtAnio.setText(bundle.getString("anio"));
        txtHoras.setText(bundle.getString("horas"));
        txtGarantia.setText(bundle.getString("garantia"));
        txtPrecio.setText(bundle.getString("precio"));
        txtDetalleHeader.setText(bundle.getString("familia")+" - "+bundle.getString("modelo"));

        btnCotizar=(Button) findViewById(R.id.btnCotizar);
        btnCotizar.setOnClickListener(this);

        //METODO PARA DESCARGAR LA IMAGEN DE HTTP
        ArrayList<Imagen> imagenes=new ArrayList<Imagen>();
        try {
            imagenes=new DownloadImages(this,this,bundle.getInt("id")).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Gallery gallery = (Gallery)findViewById(R.id.gallery);
        gallery.setAdapter(new ImageAdapter(this,imagenes));

        mDotsLayout = (LinearLayout)findViewById(R.id.image_count);
        //here we count the number of images we have to know how many dots we need
        mDotsCount = imagenes.size();

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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
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
        switch (v.getId()){
            case R.id.btnCotizar:{

                String nombre=txtFamilia.getText().toString().replace(" ","")+"_"+txtModelo.getText().toString().replace(" ","")+"_"+txtSerie.getText().toString().replace(" ","");
                Log.i("OJO","ENTRE A COTIZAR "+nombre);
                try {
                    createPdf(nombre);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

    }
    private void createPdf(String nombre) throws FileNotFoundException, DocumentException{

        File pdfFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/maquinariausada");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdirs();
            Log.i("OJO", "Pdf Directory created");
        }
        File myFile = new File(pdfFolder+"/"+nombre+ ".pdf");
        Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
        Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 12);
        FileOutputStream output = new FileOutputStream(myFile);
        Document document = new Document();
        DecimalFormat df=new DecimalFormat("0.00");
        PdfWriter.getInstance(document, output);
        try {

            document.open();
            document.setPageSize(PageSize.A4);
            document.add(new Paragraph("Cotización de Maquinaria Usada"));

            float[] columnWidths={1.5f, 2f, 5f, 2f};
            PdfPTable table=new PdfPTable(columnWidths);
            table.setWidthPercentage(90f);

            insertCell(table,"Atributo", Element.ALIGN_CENTER,1,bfBold12);
            insertCell(table,"Descripción", Element.ALIGN_LEFT,1,bfBold12);
            table.setHeaderRows(1);
            Paragraph parrafo=new Paragraph("_");
            parrafo.add(table);
            document.add(parrafo);
          /*  Drawable d = getResources().getDrawable(R.drawable.repuestos);
            BitmapDrawable bitDw = ((BitmapDrawable) d);
            Bitmap bmp = bitDw.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 200, stream);
            Image image = Image.getInstance(stream.toByteArray());
            document.add(image);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
                document.close();
        try {
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        promptForNextAction(nombre);


    }
    private void insertCell(PdfPTable table, String text, int align, int colspan, Font font){

        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        //in case there is no text and you wan to create an empty row
        if(text.trim().equalsIgnoreCase("")){
            cell.setMinimumHeight(10f);
        }
        //add the call to the table
        table.addCell(cell);

    }
    public void viewPdf(String nombre){
        File pdfFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/maquinariausada");
        File myFile = new File(pdfFolder+"/"+nombre+ ".pdf");

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
    private void emailNote(String nombre)
    {
        File pdfFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/maquinariausada");
        File myFile = new File(pdfFolder+"/"+nombre+ ".pdf");
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_SUBJECT,nombre);
        email.putExtra(Intent.EXTRA_TEXT,"Por favor contactarnos");
        Uri uri = Uri.parse(myFile.getAbsolutePath());
        email.putExtra(Intent.EXTRA_STREAM, uri);
        email.setType("message/rfc822");
        Intent intent = Intent.createChooser(email, "Abrir el archivo");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Instale alguna aplicacion para enviar emails", Toast.LENGTH_LONG).show();
        }
    }
    public void promptForNextAction(String nombre)
    {
        final String archivo=nombre;
        final String[] options = { getString(R.string.label_email), getString(R.string.label_preview),
                getString(R.string.label_cancel) };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Se guardó el PDF con éxito.");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals(getString(R.string.label_email))){
                    emailNote(archivo);
                }else if (options[which].equals(getString(R.string.label_preview))){
                    viewPdf(archivo);
                }else if (options[which].equals(getString(R.string.label_cancel))){
                    dialog.dismiss();
                }
            }
        });

        builder.show();

    }
}
 class DownloadImages extends AsyncTask<Void, Integer, ArrayList<Imagen>>
 {
     Context context;
     Activity activity;
     Integer mId;
     ProgressDialog dialog;
     public DownloadImages( Context context, Activity activity, int id) {
         mId=id;
         this.context=context;
         this.activity=activity;
         dialog=new ProgressDialog(context);
     }

     @Override
     protected ArrayList<Imagen> doInBackground(Void... params) {
         ArrayList<Imagen> imagens=new ArrayList<Imagen>();
         String URL="http://grupoiiasa.com:84/WSMobile/ListadoMaquinariaUsada/tipos_listados.svc/maquinariasUsadasImagenesxId/?idmaquina="+mId.toString();
         Log.i("OJO",URL);
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
         String json=builder.toString();
         if (json.contains("No hay datos para mostrar")){ Log.i("ERROR","NO HAY IMAGENES");}
         else{
         try {
             JSONObject jsonRootObject=new JSONObject(json);
             JSONArray arr=jsonRootObject.optJSONArray("Maquina");
             for (int i=0; i<arr.length();i++){
                 JSONObject jsonProductObject = arr.getJSONObject(i);
                 Integer id=jsonProductObject.getInt("id");
                 Imagen imagen=new Imagen();
                 imagen.setId(jsonProductObject.getInt("idmaquinaria"));
                 imagen.setDescripcion(jsonProductObject.getString("descripcion"));
                 imagen.setUrl(jsonProductObject.getString("Link"));
                 imagens.add(imagen);
             }
         } catch (JSONException e){
             e.printStackTrace();
         }}
         return imagens;
     }

     @Override
     protected void onPreExecute() {
         super.onPreExecute();
         dialog.setMessage("Descargando imágenes...");
         dialog.show();

     }

     @Override
     protected void onPostExecute(ArrayList<Imagen> imagens) {
         super.onPostExecute(imagens);
         dialog.dismiss();
     }
 }

