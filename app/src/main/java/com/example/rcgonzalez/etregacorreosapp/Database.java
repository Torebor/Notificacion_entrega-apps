package com.example.rcgonzalez.etregacorreosapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pmeneses on 13/05/2016.
 */
public class Database extends SQLiteOpenHelper {

    //Sentencia SQL para crear la tabla de contacto
    String sqlCreate = "CREATE TABLE empresa (ID INTEGER PRIMARY KEY,Nombre TEXT, Notificacion TEXT, Aviso INTEGER)";
    String sqlCreatemodem = "CREATE TABLE modem (ID INTEGER PRIMARY KEY,nombre TEXT, numero TEXT)";

    public Database(Context contexto, String nombre,
                     CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(sqlCreate);
        db.execSQL(sqlCreatemodem);
        //db.execSQL(sqlCreatesms);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        //NOTA: Por simplicidad del ejemplo aquí utilizamos directamente la opción de
        //      eliminar la tabla anterior y crearla de nuevo vacía con el nuevo formato.
        //      Sin embargo lo normal será que haya que migrar datos de la tabla antigua
        //      a la nueva, por lo que este método debería ser más elaborado.

        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS contacto");
        db.execSQL("DROP TABLE IF EXISTS modem");

        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
        db.execSQL(sqlCreatemodem);
       //db.execSQL(sqlCreatesms);
    }

}
