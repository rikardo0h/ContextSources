package com.uv.rma.ContextDBC;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import com.uv.rma.ContextDBC.ContextServices;
import com.uv.rma.ContextDBC.DataContext;

import java.util.List;

public class WrapperService extends Service implements SensorEventListener {
    SensorManager sensorManager;
    Sensor orientationSensor;
    Sensor acelerometerSensor;
    Sensor magneticSensor;
    Sensor temperatureSensor;
    Sensor ligthSensor;

    DataContext usdbh;
    SQLiteDatabase db;
    ContentValues nuevoRegistro = new ContentValues();//;
    Runnable runnable = new Runnable() {
        public void run() {
            limpiar();
            handler.postDelayed(runnable, 10000);
        }
    };


    final Handler handler = new Handler();


    public WrapperService() {
    }

    @Override
    public IBinder onBind(Intent intent)  {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    //Codigo del servicio
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
         sensorManager = (SensorManager)
                getSystemService(SENSOR_SERVICE);
        List<Sensor> listaSensores = sensorManager.
                getSensorList(Sensor.TYPE_ALL);
        crearData();
        usdbh.makeContext(db, listaSensores);

        for(Sensor sensor: listaSensores) {
            Toast.makeText(getApplicationContext(), sensor.getName(),
                    Toast.LENGTH_LONG).show();
            nuevoRegistro.clear();
            nuevoRegistro.put("sensor",getRealType(sensor.getName()));
            nuevoRegistro.put("estado", 1);
            nuevoRegistro.put("tiempo", (DateFormat.format("dd-MM-yyyy hh:mm:ss"
                    , new java.util.Date()).toString()));
            saveD(nuevoRegistro, "sensores");
        }




        Log.d("TAG","EAQUI VAMOS");

        //////
        listaSensores = sensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
        if (!listaSensores.isEmpty()) {
             orientationSensor = listaSensores.get(0);
            sensorManager.registerListener(this, orientationSensor,
                    50000000);}
        listaSensores = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (!listaSensores.isEmpty()) {
             acelerometerSensor = listaSensores.get(0);
            sensorManager.registerListener(this, acelerometerSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);}
        listaSensores = sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
        if (!listaSensores.isEmpty()) {
             magneticSensor = listaSensores.get(0);
            sensorManager.registerListener(this, magneticSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);}
        listaSensores = sensorManager.getSensorList(Sensor.TYPE_TEMPERATURE);
        if (!listaSensores.isEmpty()) {
             temperatureSensor = listaSensores.get(0);
            sensorManager.registerListener(this, temperatureSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);}
        listaSensores = sensorManager.getSensorList(Sensor.TYPE_LIGHT);
        if (!listaSensores.isEmpty()) {
             ligthSensor = listaSensores.get(0);
            sensorManager.registerListener(this, ligthSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);}

        ////
        runnable.run();
//        setServicio();


        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        //sensorManager.unregisterListener(this);
        sensorManager.unregisterListener(this,orientationSensor);
        sensorManager.unregisterListener(this, orientationSensor);
        sensorManager.unregisterListener(this, ligthSensor);
        sensorManager.unregisterListener(this, temperatureSensor);
        sensorManager.unregisterListener(this, magneticSensor);
        sensorManager.unregisterListener(this, acelerometerSensor);
                //(orientationSensor,Sensor.TYPE_ORIENTATION);
                //(this,orientationSensor);
        //cerrarData();
        handler.removeCallbacks(runnable);
        super.onDestroy();
        //Toast.makeText(this, "Servicio destruido", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int precision) {}

    @Override
    public void onSensorChanged(SensorEvent evento) {
        synchronized (this) {
            switch(evento.sensor.getType()) {

                case Sensor.TYPE_ORIENTATION:
                    nuevoRegistro.clear();
                    /*for (int i=0 ; i<3 ; i++) {
                        //Toast.makeText(getApplicationContext(), ("Orientación "+i+": "+evento.values[i]),
                          //      Toast.LENGTH_LONG).show();
                        nuevoRegistro.put("v"+(i+1), evento.values[i]);
                    }*/
                    nuevoRegistro.put("v1", evento.values[0]);
                    nuevoRegistro.put("v2", evento.values[1]);
                    nuevoRegistro.put("v3", evento.values[2]);
                   // nuevoRegistro.put("_id","1");
                    nuevoRegistro.put("tiempo", (DateFormat.format("dd-MM-yyyy hh:mm:ss"
                            , new java.util.Date()).toString()));
                   // db.insert("orientacion", null, nuevoRegistro);
                    saveD(nuevoRegistro,"orientacion");
                    break;
                case Sensor.TYPE_ACCELEROMETER:
                    nuevoRegistro.clear();
                    nuevoRegistro.put("v1", evento.values[0]);
                    nuevoRegistro.put("v2", evento.values[1]);
                    nuevoRegistro.put("v3", evento.values[2]);
                    //nuevoRegistro.put("_id","1");
                    nuevoRegistro.put("tiempo", (DateFormat.format("dd-MM-yyyy hh:mm:ss"
                            , new java.util.Date()).toString()));
                    //db.insert("acelerometro", null, nuevoRegistro);
                    saveD(nuevoRegistro,"acelerometro");
                    /*for (int i=0 ; i<3 ; i++) {
                        Toast.makeText(getApplicationContext(), ("Acelerómetro "+i+": "+evento.values[i]),
                                Toast.LENGTH_LONG).show();
                    }*/
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    nuevoRegistro.clear();
                    nuevoRegistro.put("v1", evento.values[0]);
                    nuevoRegistro.put("v2", evento.values[1]);
                    nuevoRegistro.put("v3", evento.values[2]);
                   // nuevoRegistro.put("_id","1");
                    nuevoRegistro.put("tiempo", (DateFormat.format("dd-MM-yyyy hh:mm:ss"
                            , new java.util.Date()).toString()));
                    //db.insert("magnometro", null, nuevoRegistro);
                    saveD(nuevoRegistro,"magnometro");
                    /*for (int i=0 ; i<3 ; i++) {
                        Toast.makeText(getApplicationContext(), ("Magenisto "+i+": "+evento.values[i]),
                                Toast.LENGTH_LONG).show();
                    }*/

                    break;
                case Sensor.TYPE_LIGHT:
                    nuevoRegistro.clear();
                    nuevoRegistro.put("v1", evento.values[0]);
                   // nuevoRegistro.put("_id","1");
                    nuevoRegistro.put("tiempo", (DateFormat.format("dd-MM-yyyy hh:mm:ss"
                            , new java.util.Date()).toString()));
                    //db.insert("luz", null, nuevoRegistro);
                    saveD(nuevoRegistro,"luz");
                     /*   Toast.makeText(getApplicationContext(), ("lUZ "+": "+evento.values[0]),
                                Toast.LENGTH_LONG).show();*/
                    break;
                case Sensor.TYPE_TEMPERATURE:
                    nuevoRegistro.clear();
                    nuevoRegistro.put("v1", evento.values[0]);
                   // nuevoRegistro.put("_id","1");
                    nuevoRegistro.put("tiempo", (DateFormat.format("dd-MM-yyyy hh:mm:ss"
                            , new java.util.Date()).toString()));
                    //db.insert("temperatura", null, nuevoRegistro);
                    saveD(nuevoRegistro,"temperatura");
                    /*Toast.makeText(getApplicationContext(), ("temperatura "+": "+evento.values[0]),
                            Toast.LENGTH_LONG).show();*/
            }
        }
    }

    public void lib() {
        // Be sure to unregister the sensor when the activity pauses.

        sensorManager .unregisterListener(this);
    }


    @Override
    public boolean stopService(Intent name) {
        // TODO Auto-generated method stub
        return super.stopService(name);

    }

    public void crearData(){
       usdbh = new DataContext(this, "BDcontext", null, 1);
        db = usdbh.getWritableDatabase();
    }

    public void cerrarData(){
        if(db != null) {
            db.close();
        }
    }

    public void saveD(ContentValues x,String tabla){
        if(db != null) {
            db.insert(tabla, null, x);
        }
    }

    public void imprimirAlgo(){
        Log.d("Ejemplo", "Hola");
    }

    public void limpiar(){
        usdbh.clean(db);
    }

//    public void setServicio(){
        /*ContextServices x = new ContextServices();
        Toast.makeText(getApplicationContext(), ("RESULTADO "+x.ejectContextServices()),
        Toast.LENGTH_LONG).show();*/
    //}


    public String getRealType(String tipo){
        if (tipo.indexOf("Acceleration")!=-1) {
            return "acelerometro";
        }
        if (tipo.indexOf("Orientation")!=-1) {
            return "orientacion";
        }
        if (tipo.indexOf("Magnetic")!=-1) {
            return "magnetismo";
        }
        if (tipo.indexOf("Light")!=-1) {
            return "luz";
        }
        return "error";
    }

}
