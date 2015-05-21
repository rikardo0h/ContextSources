package com.uv.rma.ContextDBC;

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

    String NAMESPACE = "http://www.w3schools.com/webservices/";
    String URL="http://www.w3schools.com/webservices/tempconvert.asmx";
    String METHOD_NAME = "CelsiusToFahrenheit";
    String SOAP_ACTION = "http://www.w3schools.com/webservices/CelsiusToFahrenheit";


    public String ejectContextServices(){
        String re;
        try {

            // Modelo el request
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("Celsius", "40"); // Paso parametros al WS

            // Modelo el Sobre
            SoapSerializationEnvelope sobre = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            sobre.dotNet = true;
            sobre.setOutputSoapObject(request);

            // Modelo el transporte
            HttpTransportSE transporte = new HttpTransportSE(URL);

            // Llamada
            transporte.call(SOAP_ACTION, sobre);

            // Resultado
            SoapPrimitive resultado = (SoapPrimitive) sobre.getResponse();

            re=resultado.toString();

        } catch (Exception e) {
            re=e.getMessage();
        }

    return re;
    }
}