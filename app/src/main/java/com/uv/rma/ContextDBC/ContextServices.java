package com.uv.rma.ContextDBC;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by Manzanares on 20/05/15.
 */
public class ContextServices {

    /*String NAMESPACE = "http://tempuri.org/";
    String URL="http://192.168.0.105:49995/Contexto.asmx";
    String METHOD_NAME = "Demo";
    String SOAP_ACTION = "http://tempuri.org/Demo";
    */
    private static final String metodo = "GetCitiesByCountry";
    private static final String namespace = "http://www.webserviceX.NET";
    // namespace + metodo
    private static final String accionSoap = "http://www.webserviceX.NET/GetCitiesByCountry";
    // url del servicio WEB
    private static final String url = "http://www.webservicex.net/globalweather.asmx";

    public String getResultado(String parametro) throws Exception {
        try {
            //Modelado de la peticion
            SoapObject peticion = new SoapObject(namespace, metodo);
            // Paso parametros al WS
            peticion.addProperty("CountryName", parametro);
            // Modelado del Sobre
            SoapSerializationEnvelope sobre = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            sobre.dotNet = true;
            sobre.setOutputSoapObject(peticion);
            // Modelo el transporte
            HttpTransportSE transporte = new HttpTransportSE(url);
            // Llamada
            transporte.call(accionSoap, sobre);
            // Resultado
            SoapPrimitive resultado = (SoapPrimitive) sobre.getResponse();
            return resultado.toString() ;
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }
        return "";
    }
}