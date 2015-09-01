package com.example.xupr44dlb.maquinariausada;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class USQLiteHelper extends SQLiteOpenHelper{
    public USQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreate="CREATE TABLE Maquinaria (" +
                "id INTEGER,"+
                "familia TEXT,"+
                "status TEXT,"+
                "localizacion TEXT,"+
                "modelo TEXT,"+
                "serie TEXT,"+
                "horas INTEGER,"+
                "garantia TEXT,"+
                "anio INTEGER,"+
                "precio_sin_acondicionar FLOAT,"+
                "precio_cat_usado_certificado FLOAT,"+
                "precio_credito FLOAT,"+
                "descripcion TEXT,"+
                "status_ubic TEXT,"+
                "fecha_modificacion TEXT,"+
                "link TEXT)";

        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
