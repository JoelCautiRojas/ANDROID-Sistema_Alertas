package com.example.joel_64.apppnp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Joel-64 on 21/04/2017.
 */

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {
    String cadenaSQL = "CREATE TABLE usuario (id INTEGER PRIMARY KEY, idbd VARCHAR(11) ,nombres VARCHAR(50), apellidos VARCHAR(50), dni INTEGER(8), correo VARCHAR(50), clave VARCHAR(20), direccion TEXT, telefono VARCHAR(50), estado VARCHAR(3) )";
    public AdminSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(cadenaSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS usuario");
        db.execSQL(cadenaSQL);

    }
}