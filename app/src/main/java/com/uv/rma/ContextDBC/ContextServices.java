package com.uv.rma.ContextDBC;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

   /*String NAMESPACE = "http://tempuri.org/";
    String URL="http://192.168.0.105:49995/Contexto.asmx";
    String METHOD_NAME = "Demo";
    String SOAP_ACTION = "http://tempuri.org/Demo";
    */

/**
 * Created by Manzanares on 20/05/15.
 */
public class ContextServices {
    String NAMESPACE = "http://tempuri.org/";
    String URL="http://192.168.0.105:1234/Contexto.asmx";
    String METHOD_NAME = "WriteContact";
    String SOAP_ACTION = "http://tempuri.org/";//WriteContact";
    String ACTION="http://tempuri.org/";
    int idActual;
    CObject dataContext;


    public void SelectAction(int idAnterior,Context contextSup){
        METHOD_NAME="ListenSentence";
        ACTION = ACTION.concat(METHOD_NAME);
        Log.d("Manzanares",ACTION);
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        String respuesta=null;

        HttpTransportSE trans = new HttpTransportSE(URL);

        trans.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        trans.debug=true;
        try {
            trans.call(ACTION, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            respuesta = response.toString();
          //  Log.d("RESULTADO", "respuesta = " + respuesta.toString());
            Gson gson = new Gson();
            instruccionHelper newMyObj = gson.fromJson(respuesta, instruccionHelper.class);
            //Log.d("RESULTADO","final = "+ newMyObj.id+"  "+newMyObj.instruccion+" "+newMyObj.tipo+" "+newMyObj.estado);
            Log.d("RESULTADO","peticion actual"+newMyObj.id+"consulta"+newMyObj.instruccion);
            idActual = Integer.parseInt(newMyObj.id);
            if(Integer.parseInt(newMyObj.id )== idAnterior){
                Log.d("RESULTADO", "MISMA QUE LA ANTERIOR");
                idActual = Integer.parseInt(newMyObj.id);
            }else{
                Log.d("TABLETA", "CONSULTA BD");
                openData(contextSup);
                CObject.Respuesta res = resultadoSentence(newMyObj.instruccion,newMyObj.tipo);
                closeData();
                CallsendRequest call = new CallsendRequest();
                Log.d("Servicio Publicacion", "voy a llamar al servicio"+res.datos);
                call.execute(res.tables,res.datos,newMyObj.id,newMyObj.tipo);

            }
        } catch (IOException e) {
            Log.d("manzanares", "error");
        } catch (XmlPullParserException e) {
            Log.d("manzanares", "error");
        }
//        return respuesta;

    }

    public void openData(Context contextSup){
        dataContext = new CObject();
        Log.d("RESULTADO", "abri data");
        dataContext.openData(contextSup);
    }

    public void closeData(){
        dataContext.cerrarData();
    }

    public CObject.Respuesta resultadoSentence(String consulta,String tipo){
        CObject.Respuesta res = dataContext.checkSentence(consulta,tipo);
        return res;
    }


    public class instruccionHelper{
        String id;
        String estado;
        String instruccion;
        String tipo;
    }




    public class  CallsendRequest extends AsyncTask<String,Void,String>
    {


        /* @Override
                 protected String doInBackground(String... params) {
                     ContextServices x = new ContextServices();
                     return  x.saludar("hola");
                 }*/
        @Override /// Escuchamos el servidor
        protected String doInBackground(String... params) {
            sendRequest(params[0],params[1],params[2],params[3]);
            return  "";
        }
    }

    public void sendRequest (String tables,String datos,String id,String tipo){
        METHOD_NAME="sendResquest";
        ACTION = "http://tempuri.org/sendResquest";
        Log.d("Manzanares",ACTION);
        SoapObject request = new SoapObject(NAMESPACE,"sendResquest");
        PropertyInfo x = new PropertyInfo();
       // x.setName("tables");
       // x.setValue(tables);
       // x.setType(String.class);
        request.addProperty("tables",tables);
        request.addProperty("datos",datos);
        request.addProperty("id",id);
        request.addProperty("tipo",tipo);

        Log.d("SERVICIO RES ENVIADO", tables);
        Log.d("SERVICIO RES ENVIADO", datos);
        Log.d("SERVICIO RES ENVIADO", id);
        Log.d("SERVICIO RES ENVIADO", tipo);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        String respuesta=null;

        HttpTransportSE trans = new HttpTransportSE(URL);

        //trans.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        trans.debug=true;

        try {
            trans.call(ACTION, envelope);
            envelope.getResponse();
            Log.d("SERVICIO PUBLICACION", "Confirmado envio");


        } catch (IOException e) {
            Log.d("SERVICIO PUBLICACION", e.toString());
        } catch (XmlPullParserException e) {
            Log.d("SERVICIO PUBLICACION", e.toString());
        }

    }

}
