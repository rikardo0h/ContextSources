package com.uv.rma.ContextDBC;

/**
 * Created by Manzanares on 17/05/15.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.Sensor;

import java.util.List;

public class DataContext extends SQLiteOpenHelper {


    //Sentencia SQL para crear la tabla de Usuarios
    String sqlCreate = "CREATE TABLE demo (codigo INTEGER, nombre TEXT)";

    public DataContext(Context contexto, String nombre,
                                CursorFactory factory, int version) {
        super(contexto, "/mnt/sdcard/"+nombre+".db", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        //db.execSQL("DROP TABLE IF EXISTS orientacion");
        db.execSQL("DROP TABLE IF EXISTS acelerometro");
        db.execSQL("DROP TABLE IF EXISTS magnometro");
        db.execSQL("DROP TABLE IF EXISTS luz");
        db.execSQL("DROP TABLE IF EXISTS temperatura");
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {

        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS demo");

        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
    }

    public void makeContext(SQLiteDatabase db,List<Sensor> listaSensores) {
        String sqlCreate="";
        for(Sensor sensor: listaSensores) {
            switch(sensor.getType()) {

                case Sensor.TYPE_ORIENTATION:            //orientacion
                    sqlCreate= "CREATE TABLE IF NOT EXISTS orientacion (_id Integer ,v1 FLOAT, v2 FLOAT,v3 FLOAT, tiempo TIMESTAMP)";
                    db.execSQL(sqlCreate);
                    break;
                case Sensor.TYPE_ACCELEROMETER:
                    sqlCreate= "CREATE TABLE IF NOT EXISTS acelerometro (_id Integer ,v1 FLOAT, v2 FLOAT,v3 FLOAT, tiempo TIMESTAMP)";
                    db.execSQL(sqlCreate);
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    sqlCreate= "CREATE TABLE IF NOT EXISTS magnometro (_id Integer ,v1 FLOAT, v2 FLOAT,v3 FLOAT, tiempo TIMESTAMP)";
                    db.execSQL(sqlCreate);
                    break;
                case Sensor.TYPE_LIGHT:
                    sqlCreate= "CREATE TABLE IF NOT EXISTS luz (_id Integer ,v1 FLOAT, tiempo TIMESTAMP)";
                    db.execSQL(sqlCreate);
                    break;
                case Sensor.TYPE_TEMPERATURE:
                    sqlCreate= "CREATE TABLE IF NOT EXISTS temperatura (_id Integer ,v1 FLOAT, tiempo TIMESTAMP)";
                    db.execSQL(sqlCreate);
                    break;
                default: //case Sensor.TYPE_LIGHT:
                    sqlCreate= "CREATE TABLE IF NOT EXISTS ubicacion (_id Integer ,v1 FLOAT, tiempo TIMESTAMP)";
                    db.execSQL(sqlCreate);

            }
        }
    }

    public void clean(SQLiteDatabase db){
        db.execSQL("Delete FROM orientacion WHERE tiempo < '2015-05-19 00:00:00'");
        db.execSQL("Delete FROM acelerometro WHERE tiempo < '2015-05-19 00:00:00'");
        db.execSQL("Delete FROM magnometro WHERE tiempo < '2015-05-19 00:00:00'");
        db.execSQL("Delete FROM luz WHERE tiempo < '2015-05-19 00:00:00'");

        db.execSQL("SELECT * FROM orientacion WHERE " +
                    "date('now') < datetime(tiempo,'-1 minute') ");

    }

}
