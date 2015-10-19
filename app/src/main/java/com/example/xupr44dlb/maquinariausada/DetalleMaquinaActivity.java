package com.example.xupr44dlb.maquinariausada;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;


public class DetalleMaquinaActivity extends Activity implements View.OnClickListener{
    static TextView mDotsText[];
    private int mDotsCount;
    private LinearLayout mDotsLayout;
    TextView txtPrecio, txtFamilia, txtModelo, txtUbicacion, txtDescripcion, txtSerie, txtAnio, txtHoras, txtGarantia,txtDetalleHeader;
    Button btnCotizar;
    ArrayList<Imagen> imagenes;
    Maquina maquina;
    ImageButton btnClose;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setUpWindow();
        Bundle bundle =this.getIntent().getExtras();

        btnClose=(ImageButton) findViewById(R.id.barra_btnClose);
        btnClose.setOnClickListener(this);
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

        if (bundle.getString("serie").length()>=15){
            txtSerie.setText(bundle.getString("serie").substring(1,15));
        } else {
            txtSerie.setText(bundle.getString("serie"));
        }
        txtAnio.setText(bundle.getString("anio"));
        txtHoras.setText(bundle.getString("horas"));
        txtGarantia.setText(bundle.getString("garantia").replace("null", "N/A"));
        txtPrecio.setText(bundle.getString("precio"));
        txtDetalleHeader.setText(bundle.getString("familia")+" - "+bundle.getString("modelo"));

        maquina=new Maquina();
        maquina.setT_anio(bundle.getString("anio"));
        maquina.setT_horas(bundle.getString("horas"));
        maquina.setModelo(bundle.getString("modelo"));
        maquina.setFamilia(bundle.getString("familia"));
        maquina.setDescripcion(bundle.getString("descripcion"));
        maquina.setT_precio(bundle.getString("precio"));
        maquina.setSerie(txtSerie.getText().toString());
        maquina.setPreciocredito(bundle.getFloat("preciolista"));

        btnCotizar=(Button) findViewById(R.id.btnCotizar);
        btnCotizar.setOnClickListener(this);

        //METODO PARA DESCARGAR LA IMAGEN DE HTTP

        imagenes=new ArrayList<Imagen>();
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

                final String nombre=txtFamilia.getText().toString().replace(" ","")+"_"+txtModelo.getText().toString().replace(" ","")+"_"+txtSerie.getText().toString().replace(" ","");
                Log.i("OJO","ENTRE A COTIZAR "+nombre);
                try {
                    Integer result=0;
                    new DownloadPDF(this,nombre, this,imagenes, maquina).execute();

                } catch (Exception e){
                    e.printStackTrace();
                }
                break;
            }
            case R.id.barra_btnClose:{
                this.finish();
                break;
            }
        }

    }
}

class DownloadPDF extends AsyncTask<Void, Integer, Integer>
{
    String nombre;
    ArrayList<Imagen> imagenes;
    Maquina maquina;
    Activity activity;
    ProgressDialog dialog;
    Context context;

    public DownloadPDF(Context pcontext, String pnombre,Activity pactivity, ArrayList<Imagen> pimagenes,  Maquina pmaquina) {
        this.nombre=pnombre;
        this.activity=pactivity;
        this.imagenes=pimagenes;
        this.maquina=pmaquina;
        this.context=pcontext;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        Integer result=0;
        try {
            File pdfFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/maquinariausada");
            if (!pdfFolder.exists()) {
                pdfFolder.mkdirs();
                Log.i("OJO", "Pdf Directory created");
            }
            File myFile = new File(pdfFolder+"/"+nombre+ ".pdf");
            FileOutputStream output = new FileOutputStream(myFile);
            Document document = new Document();
            PdfWriter.getInstance(document, output);
            try {

                document.open();
                document.setPageSize(PageSize.A4);
                document.add(createFirstTable(imagenes, maquina));
                if (imagenes.size()>1)
                {
                    document.add(createImageTable(imagenes));
                }

                result=1;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                document.close();

            }

            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
                result=0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            result=0;

        }
        return result;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage("Descargando PDF. Por favor espere...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();

    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        dialog.dismiss();
        promptForNextAction(nombre);

    }
    public PdfPTable createFirstTable(ArrayList<Imagen> imagenes, Maquina maquina) throws BadElementException, IOException {

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(90);
        table.setSpacingBefore(0f);
        table.setSpacingAfter(0f);

        table.getDefaultCell().setBorder(0);

        PdfPCell cell;
        // we add a cell with colspan 3
        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
        cell = new PdfPCell(new Phrase("COTIZACIÓN DE MAQUINARIA USADA",boldFont));
        cell.setColspan(3);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(0);
        table.addCell(cell);
        //AGREGANDO IMAGEN DE LOGO
        Drawable d = context.getResources().getDrawable(R.drawable.ic_iiasa);
        BitmapDrawable bitDw = ((BitmapDrawable) d);
        Bitmap bmp = bitDw.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 80, stream);
        Image image = Image.getInstance(stream.toByteArray());
        image.scaleAbsolute(130,35);
        cell = new PdfPCell(image);
        cell.setColspan(2);
        cell.setRowspan(2);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(0);
        table.addCell(cell);
        // FIN DE AGREGAR IMAGEN LOGO
        table.addCell("    ");
        table.addCell("    ");
        table.addCell("    ");
        table.addCell("    ");
        table.addCell("    ");
        if (imagenes.size()<=0) {
            //IMAGEN INICIAL DE LA MAQUINA
            d = context.getResources().getDrawable(R.drawable.maquinanoencontrada);
            bitDw = ((BitmapDrawable) d);
            bmp = bitDw.getBitmap();
            stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 80, stream);
            image = Image.getInstance(stream.toByteArray());

        }
        else {
            String imageUrl = imagenes.get(0).getUrl().toString();
            Log.i("IMAGEN PDF",imageUrl);
            image = Image.getInstance(new URL(imageUrl));

        }
        image.scaleAbsolute(250, 210);
        cell = new PdfPCell(image);
        cell.setRowspan(14);
        cell.setColspan(3);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(0);
        table.addCell(cell);

        //DATOS PRINCIPALES
        Font fontbold = FontFactory.getFont("Times-Roman", 10, Font.BOLD);
        table.addCell(new Paragraph("Fecha de emisión:",fontbold));
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String fecha= dateFormat.format(Calendar.getInstance().getTime());
        table.addCell(fecha);
        table.addCell(new Paragraph("Validez:",fontbold));
        table.addCell("10 días");
        table.addCell("       ");
        table.addCell("       ");

        Font fontheader = FontFactory.getFont("Times-Roman", 14, Font.BOLD);
        Font fontdetallesgrandes = FontFactory.getFont("Times-Roman", 9);
        cell = new PdfPCell(new Paragraph("Especificaciones del Equipo", fontheader));
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(0);
        table.addCell(cell);
        table.addCell("  ");
        table.addCell("  ");
        table.addCell(new Paragraph("Familia:",fontbold));
        table.addCell(new Paragraph(maquina.getFamilia(),fontdetallesgrandes));

        table.addCell(new Paragraph("Modelo:",fontbold));
        table.addCell(new Paragraph(maquina.getModelo(),fontdetallesgrandes));
        table.addCell(new Paragraph("No. Serie:",fontbold));
        table.addCell(new Paragraph(maquina.getSerie(),fontdetallesgrandes));

        table.addCell(new Paragraph("Año:",fontbold));
        table.addCell(new Paragraph(maquina.getT_anio(),fontdetallesgrandes));
        table.addCell(new Paragraph("Horas:",fontbold));
        table.addCell(new Paragraph(maquina.getT_horas(),fontdetallesgrandes));
        table.addCell("   ");
        table.addCell("   ");


        cell=new PdfPCell(new Paragraph("Descripción del Equipo",fontheader));
        cell.setColspan(2);
        cell.setBorder(0);
        table.addCell(cell);
        table.addCell("       ");
        table.addCell("       ");
        cell=new PdfPCell(new Paragraph(maquina.getDescripcion().replace("null","No hay información disponible")));
        cell.setColspan(2);
        cell.setBorder(0);
        table.addCell(cell);



        cell=new PdfPCell(new Paragraph("  "));
        cell.setColspan(5);
        cell.setBorder(0);
        table.addCell(cell);

        //PRECIO
        cell=new PdfPCell(new Paragraph("Precio Unitario de Venta en Almacén",fontbold));
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        Font fontprecio=FontFactory.getFont("Times-Roman", 16, Font.ITALIC);
        cell=new PdfPCell(new Paragraph(maquina.getT_precio(),fontprecio));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        table.addCell("   ");
        table.addCell("   ");

        //IVA
        cell=new PdfPCell(new Paragraph("12% IVA",fontbold));
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        NumberFormat fmt = NumberFormat.getCurrencyInstance();
        String iva=fmt.format(maquina.getPreciocredito()*0.12).toString();
        cell=new PdfPCell(new Paragraph(iva,fontprecio));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        table.addCell("   ");
        table.addCell("   ");


        //TOTAL MAS IVA
        cell=new PdfPCell(new Paragraph("Precio total incluido IVA",fontbold));
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        String totaliva=fmt.format(maquina.getPreciocredito()*1.12).toString();
        cell=new PdfPCell(new Paragraph(totaliva,fontprecio));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        table.addCell("  ");
        table.addCell("  ");

        cell=new PdfPCell(new Paragraph("   "));
        cell.setColspan(5);
        cell.setBorder(0);
        table.addCell(cell);

        cell=new PdfPCell(new Paragraph("Nota: Precio unitario basado en la compra de UNA máquina.",fontbold));
        cell.setColspan(5);
        cell.setBorder(0);
        table.addCell(cell);

        cell=new PdfPCell(new Paragraph("   "));
        cell.setColspan(5);
        cell.setBorder(0);
        table.addCell(cell);

        cell=new PdfPCell(new Paragraph("“Los precios, especificaciones y disponibilidad están sujetos a cambio sin previo aviso. Además estos precios no incluyen: seguros y transporte o cualquier variación que hubiere el Impuesto al Valor Agregado y/o tributos al comercio exterior.“",fontbold));
        cell.setColspan(5);
        cell.setBorder(0);
        table.addCell(cell);

        cell=new PdfPCell(new Paragraph("   "));
        cell.setColspan(5);
        cell.setBorder(0);
        table.addCell(cell);

        cell=new PdfPCell(new Paragraph("Plazo de Entrega",fontbold));
        cell.setColspan(2);
        cell.setBorder(0);
        table.addCell(cell);
        table.addCell("Inmediata");
        table.addCell("  ");
        table.addCell("  ");

        cell=new PdfPCell(new Paragraph("Forma de Pago",fontbold));
        cell.setColspan(2);
        cell.setBorder(0);
        table.addCell(cell);
        table.addCell("Contado");
        table.addCell("  ");
        table.addCell("  ");

        cell=new PdfPCell(new Paragraph("   "));
        cell.setColspan(5);
        cell.setBorder(0);
        table.addCell(cell);

        cell=new PdfPCell(new Paragraph("   "));
        cell.setColspan(5);
        cell.setBorder(0);
        table.addCell(cell);

        cell=new PdfPCell(new Paragraph("Esta cotización está sujeta al artículo No. 148 del Código de Comercio.",fontbold));
        cell.setColspan(5);
        cell.setBorder(0);
        table.addCell(cell);

        cell=new PdfPCell(new Paragraph("   "));
        cell.setColspan(5);
        cell.setBorder(0);
        table.addCell(cell);
        cell=new PdfPCell(new Paragraph("   "));
        cell.setColspan(5);
        cell.setBorder(0);
        table.addCell(cell);
        cell=new PdfPCell(new Paragraph("   "));
        cell.setColspan(5);
        cell.setBorder(0);
        table.addCell(cell);


        cell=new PdfPCell(new Paragraph("   "));
        cell.setColspan(5);
        cell.setBorder(0);
        table.addCell(cell);
        cell=new PdfPCell(new Paragraph("   "));
        cell.setColspan(5);
        cell.setBorder(0);
        table.addCell(cell);
        cell=new PdfPCell(new Paragraph("   "));
        cell.setColspan(5);
        cell.setBorder(0);
        table.addCell(cell);

        //AGREGAR FOOTER
        Drawable f = context.getResources().getDrawable(R.drawable.repuestos);
        BitmapDrawable bitDwf = ((BitmapDrawable) f);
        Bitmap bmpf = bitDwf.getBitmap();
        ByteArrayOutputStream streamf = new ByteArrayOutputStream();
        bmpf.compress(Bitmap.CompressFormat.PNG, 80, streamf);
        Image footer=Image.getInstance(streamf.toByteArray());
        footer.scaleAbsolute(445,115);
        cell = new PdfPCell(footer);
        cell.setColspan(5);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(0);
        table.addCell(cell);

        cell=new PdfPCell(new Paragraph("   "));
        cell.setColspan(5);
        cell.setBorder(0);
        table.addCell(cell);

        cell=new PdfPCell(new Paragraph("   "));
        cell.setColspan(5);
        cell.setBorder(0);
        table.addCell(cell);

        cell=new PdfPCell(new Paragraph("   "));
        cell.setColspan(5);
        cell.setBorder(0);
        table.addCell(cell);


        return table;
    }
    public PdfPTable createImageTable (ArrayList<Imagen> imagenes) throws BadElementException, IOException {

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(90);
        table.setSpacingBefore(0f);
        table.setSpacingAfter(0f);
        PdfPCell cell;
        // AÑADIR TITULO
        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
        cell = new PdfPCell(new Phrase("Imágenes adicionales del Equipo",boldFont));
        cell.setColspan(4);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(0);
        table.addCell(cell);
        //AGREGANDO IMAGEN DE LOGO
        Drawable d = context.getResources().getDrawable(R.drawable.ic_iiasa);
        BitmapDrawable bitDw = ((BitmapDrawable) d);
        Bitmap bmp = bitDw.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 80, stream);
        Image image = Image.getInstance(stream.toByteArray());
        image.scaleAbsolute(130,35);
        cell = new PdfPCell(image);
        cell.setColspan(2);
        cell.setRowspan(2);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(0);
        table.addCell(cell);


        cell=new PdfPCell(new Paragraph("   "));
        cell.setColspan(6);
        cell.setBorder(0);
        table.addCell(cell);


        cell=new PdfPCell(new Paragraph("   "));
        cell.setColspan(6);
        cell.setBorder(0);
        table.addCell(cell);


        if (imagenes.size()>0) {

            int h=imagenes.size();
            if (h>6) {
                h=6;
            }
            for (int i=1; i<h ;i++)
            {
                String imageUrl = imagenes.get(i).getUrl().toString();
                image = Image.getInstance(new URL(imageUrl));
                image.scaleAbsolute(250, 210);
                cell = new PdfPCell(image);
                cell.setRowspan(14);
                cell.setColspan(3);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                table.addCell(cell);


            }

        }

        return table;
    }
    public void viewPdf(String nombre){
        File pdfFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/maquinariausada");
        File myFile = new File(pdfFolder+"/"+nombre+ ".pdf");

        Intent target = new Intent(Intent.ACTION_VIEW);

        target.setDataAndType(Uri.fromFile(myFile), "application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        Intent intent = Intent.createChooser(target, "Abrir el archivo");
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context.getApplicationContext(), "Instale alguna aplicacion para ver PDFs", Toast.LENGTH_LONG).show();
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
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context.getApplicationContext(), "Instale alguna aplicacion para enviar emails", Toast.LENGTH_LONG).show();
        }
    }
    public void promptForNextAction(String nombre)
    {
        final String archivo=nombre;
        final String[] options = { context.getString(R.string.label_email), context.getString(R.string.label_preview),
                context.getString(R.string.label_cancel) };

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Se guardó el PDF con éxito.");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals(context.getString(R.string.label_email))){
                    emailNote(archivo);
                }else if (options[which].equals(context.getString(R.string.label_preview))){
                    viewPdf(archivo);
                }else if (options[which].equals(context.getString(R.string.label_cancel))){
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
        ArrayList<Imagen> imagens=new ArrayList<>();
        String URL="http://grupoiiasa.com:84/WSMobile/ListadoMaquinariaUsada/tipos_listados.svc/maquinariasUsadasImagenesxId/?idmaquina="+mId.toString()+"&pais=ecu";
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
        if (json.contains("No hay datos para mostrar"))
        {
            Imagen imagen=new Imagen();
            imagen.setId(1);
            imagen.setUrl("http://www.iiasanews.com/MaquinariaUsada/nodisponible.jpg");
            imagens.add(imagen);
        }
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
