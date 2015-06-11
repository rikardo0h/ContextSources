package com.uv.rma.ContextDBC;

/**
 * Created by Manzanares on 17/05/15.
 */

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import java.util.*;
import com.google.gson.Gson;

import java.util.List;

public class DataContext extends SQLiteOpenHelper {


    //Sentencia SQL para crear la tabla de Usuarios
    String sqlCreate = "CREATE TABLE pato (codigo INTEGER, nombre TEXT)";

    public DataContext(Context contexto, String nombre,
                                CursorFactory factory, int version) {
        super(contexto, "/mnt/sdcard/" + nombre + ".db", factory, version);
        // "/mnt/sdcard/"
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL("DROP TABLE IF EXISTS orientacion");
        db.execSQL("DROP TABLE IF EXISTS acelerometro");
        db.execSQL("DROP TABLE IF EXISTS magnometro");
        db.execSQL("DROP TABLE IF EXISTS luz");
        db.execSQL("DROP TABLE IF EXISTS temperatura");
        db.execSQL("DROP TABLE IF EXISTS ubicacion");
        //db.execSQL(sqlCreate);
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
        sqlCreate= "CREATE TABLE IF NOT EXISTS ubicacion (_id INTEGER PRIMARY KEY ,longitud FLOAT, altitud FLOAT, tiempo TIMESTAMP)";
        db.execSQL(sqlCreate);
        sqlCreate= "CREATE TABLE IF NOT EXISTS sensores (_id INTEGER PRIMARY KEY ,sensor varchar(50), estado boolean, tiempo TIMESTAMP)";
        db.execSQL(sqlCreate);
        sqlCreate= "delete from sensores";
        db.execSQL(sqlCreate);
        for(Sensor sensor: listaSensores) {
            switch(sensor.getType()) {

                case Sensor.TYPE_ORIENTATION:            //orientacion
                    sqlCreate= "CREATE TABLE IF NOT EXISTS orientacion (_id INTEGER PRIMARY KEY ,v1 FLOAT, v2 FLOAT,v3 FLOAT, tiempo TIMESTAMP)";
                    db.execSQL(sqlCreate);
                    break;
                case Sensor.TYPE_ACCELEROMETER:
                    sqlCreate= "CREATE TABLE IF NOT EXISTS acelerometro (_id INTEGER PRIMARY KEY ,v1 FLOAT, v2 FLOAT,v3 FLOAT, tiempo TIMESTAMP)";
                    db.execSQL(sqlCreate);
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    sqlCreate= "CREATE TABLE IF NOT EXISTS magnometro (_id INTEGER PRIMARY KEY ,v1 FLOAT, v2 FLOAT,v3 FLOAT, tiempo TIMESTAMP)";
                    db.execSQL(sqlCreate);
                    break;
                case Sensor.TYPE_LIGHT:
                    sqlCreate= "CREATE TABLE IF NOT EXISTS luz (_id INTEGER PRIMARY KEY ,v1 FLOAT, tiempo TIMESTAMP)";
                    db.execSQL(sqlCreate);
                    break;
                case Sensor.TYPE_TEMPERATURE:
                    sqlCreate= "CREATE TABLE IF NOT EXISTS temperatura (_id INTEGER PRIMARY KEY ,v1 FLOAT, tiempo TIMESTAMP)";
                    db.execSQL(sqlCreate);
                    break;
                /*default: //case Sensor.TYPE_LIGHT:
                    sqlCreate= "CREATE TABLE IF NOT EXISTS ubicacion (_id Integer ,v1 FLOAT, tiempo TIMESTAMP)";
                    db.execSQL(sqlCreate);
                */
            }
        }
    }

    public void clean(SQLiteDatabase db){
        db.execSQL("Delete FROM orientacion WHERE tiempo < '2015-05-19 00:00:00'");
        db.execSQL("Delete FROM acelerometro WHERE tiempo < '2015-05-19 00:00:00'");
        db.execSQL("Delete FROM magnometro WHERE tiempo < '2015-05-19 00:00:00'");
        db.execSQL("Delete FROM luz WHERE tiempo < '2015-05-19 00:00:00'");

        db.execSQL("delete FROM ubicacion WHERE " +
                    "date('now') < datetime(tiempo,'-1 minute') ");
    }


    public Cursor selectAll(SQLiteDatabase db, String sensor){
        //String sql = "SELECT * FROM "+sensor +" where MAX(_id)==_id LIMIT 1;";//+sensor;
        String sql ="SELECT * FROM "+sensor+" ORDER BY _id DESC LIMIT 1;";
        Cursor x = db.rawQuery(sql, null);
        Log.d("REGISTRO OK", db.toString());
        return x;
    }

    public Cursor selectSensor(SQLiteDatabase db){
        String sql ="SELECT sensor FROM sensores ;";
        Cursor y = db.rawQuery(sql,null);
        return y;
    }

    public String nivelIluminacion(SQLiteDatabase db){
        String sql ="DROP VIEW IF EXISTS ILUMINACION";
        db.execSQL(sql);
        sql = "Create view iluminacion as SELECT AVG(v1) AS promedio from luz";
        db.execSQL(sql);
        sql= "select promedio from iluminacion";
        Cursor y =db.rawQuery(sql, null);
        y.moveToFirst();
        String promedio = y.getString(y.getColumnIndex("promedio"));
        Float prom = Float.parseFloat(promedio);
        Log.d("Promedio",promedio);
        if(prom<100) return "Obscuro";
        if(prom>100&&prom<400) return "Normal";

        return "Muy claro";
    }

    public String getDistance(SQLiteDatabase db,double lat1, double lon1){//}, double lat2, double lon2)
        String sql ="SELECT * FROM ubicacion ORDER BY _id DESC LIMIT 1;";
        Cursor x = db.rawQuery(sql, null);
        x.moveToFirst();
        String longitud = x.getString(x.getColumnIndex("longitud"));
        String latitud = x.getString(x.getColumnIndex("altitud"));
        Float lat2 = Float.parseFloat(latitud);
        Float lon2 = Float.parseFloat(longitud);

        Log.d("Distancia_cerca",lat1+" "+lon1+" "+lat2+" "+lon2);
        double latA = Math.toRadians(lat1);
        double lonA = Math.toRadians(lon1);
        double latB = Math.toRadians(lat2);
        double lonB = Math.toRadians(lon2);
        double cosAng = (Math.cos(latA) * Math.cos(latB) * Math.cos(lonB-lonA)) + (Math.sin(latA) * Math.sin(latB));
        double ang = Math.acos(cosAng);
        double dist = ang *6371;
        return String.valueOf(dist);
    }




}
