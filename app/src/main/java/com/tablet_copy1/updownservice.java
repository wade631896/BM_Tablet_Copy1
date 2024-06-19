package com.tablet_copy1;

/**
 * Created by 521 on 2020/3/18.
 */

import android.util.Log;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.net.HttpRetryException;

public class updownservice {
    //Namespace of the Webservice - can be found in WSDL
    private static String NAMESPACE = "http://tempuri.org/";
    //Webservice URL - WSDL File location
    private static String URL = "http://www.shng.com.tw/ComputerCopy_Webservice/File_updownload.asmx?op=";
    //SOAP Action URI again Namespace + Web method name
//	private static String SOAP_ACTION = "http://footballpool.dataaccess.eu/TopGoalScorers";
    private static String SOAP_ACTION_URL = "http://tempuri.org/";
    public static String  FileUploadByBase64String( String webMethName,String fs, String fileName ) {
        String resTxt = null;
        URL=URL+webMethName;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters

        PropertyInfo sayHelloPI = new PropertyInfo();
        sayHelloPI.setName("fs");//檔案位元組
        sayHelloPI.setValue(fs);
        sayHelloPI.setType(String.class);
        request.addProperty(sayHelloPI);

        PropertyInfo sayHelloPI2 = new PropertyInfo();
        sayHelloPI2.setName("fileName");//檔案名稱
        sayHelloPI2.setValue(fileName);
        sayHelloPI2.setType(String.class);
        request.addProperty(sayHelloPI2);

        //request.addProperty(username, password);
        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        androidHttpTransport.debug = true;
        try {
            // Invoke web service
            androidHttpTransport.call(SOAP_ACTION_URL + webMethName, envelope);
        } catch (HttpRetryException e) {
            //Assign error message to resTxt
            resTxt = "Error occured1";
            e.printStackTrace();
        } catch (IOException e) {
            //Assign error message to resTxt
            resTxt = "Error occured2";
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            //Assign error message to resTxt
            resTxt = "Error occured3";
            e.printStackTrace();
        } catch (Exception e) {
            //Print error
            e.printStackTrace();
            //Assign error message to resTxt
            resTxt = "Error occured4";
        }
        SoapPrimitive response = null;
//        try {
//            resTxt=envelope.getResponse().toString();
//        } catch (SoapFault soapFault) {
//            soapFault.printStackTrace();
//        }
        return resTxt;
    }

    public static String  CreateDB( String webMethName ,String MDBfileName ) {
        String resTxt = null;
        URL=URL+webMethName;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters

        PropertyInfo sayHelloPI = new PropertyInfo();
        sayHelloPI.setName("MDBfileName");//檔案位元組
        sayHelloPI.setValue(MDBfileName);
        sayHelloPI.setType(String.class);
        request.addProperty(sayHelloPI);

        //request.addProperty(username, password);
        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        androidHttpTransport.debug = true;
        try {
            // Invoke web service
            androidHttpTransport.call(SOAP_ACTION_URL + webMethName, envelope);
        } catch (HttpRetryException e) {
            //Assign error message to resTxt
            resTxt = "Error occured1";
            e.printStackTrace();
        } catch (IOException e) {
            //Assign error message to resTxt
            resTxt = "Error occured2";
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            //Assign error message to resTxt
            resTxt = "Error occured3";
            e.printStackTrace();
        } catch (Exception e) {
            //Print error
            e.printStackTrace();
            //Assign error message to resTxt
            resTxt = "Error occured4";
        }
        SoapPrimitive response = null;
//        try {
//            resTxt=envelope.getResponse().toString();
//        } catch (SoapFault soapFault) {
//            soapFault.printStackTrace();
//        }
        return resTxt;
    }

    public static String  DataTrans( String webMethName,String SQLitefileName, String MDBfileName ) {
        String resTxt = null;
        URL=URL+webMethName;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters

        PropertyInfo sayHelloPI = new PropertyInfo();
        sayHelloPI.setName("SQLitefileName");//檔案位元組
        sayHelloPI.setValue(SQLitefileName);
        sayHelloPI.setType(String.class);
        request.addProperty(sayHelloPI);

        PropertyInfo sayHelloPI2 = new PropertyInfo();
        sayHelloPI2.setName("MDBfileName");//檔案名稱
        sayHelloPI2.setValue(MDBfileName);
        sayHelloPI2.setType(String.class);
        request.addProperty(sayHelloPI2);

        //request.addProperty(username, password);
        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        androidHttpTransport.debug = true;
        try {
            // Invoke web service
            androidHttpTransport.call(SOAP_ACTION_URL + webMethName, envelope);
        } catch (HttpRetryException e) {
            //Assign error message to resTxt
            resTxt = "Error occured1";
            e.printStackTrace();
        } catch (IOException e) {
            //Assign error message to resTxt
            resTxt = "Error occured2";
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            //Assign error message to resTxt
            resTxt = "Error occured3";
            e.printStackTrace();
        } catch (Exception e) {
            //Print error
            e.printStackTrace();
            //Assign error message to resTxt
            resTxt = "Error occured4";
        }
        SoapPrimitive response = null;
//        try {
//            resTxt=envelope.getResponse().toString();
//        } catch (SoapFault soapFault) {
//            soapFault.printStackTrace();
//        }
        return resTxt;
    }

    public static String  CreateDB3( String webMethName ,String DB3fileName ) {
        String resTxt = null;
        URL=URL+webMethName;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters

        PropertyInfo sayHelloPI = new PropertyInfo();
        sayHelloPI.setName("DB3fileName");//檔案位元組
        sayHelloPI.setValue(DB3fileName);
        sayHelloPI.setType(String.class);
        request.addProperty(sayHelloPI);

        //request.addProperty(username, password);
        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        androidHttpTransport.debug = true;
        try {
            // Invoke web service
            androidHttpTransport.call(SOAP_ACTION_URL + webMethName, envelope);
        } catch (HttpRetryException e) {
            //Assign error message to resTxt
            resTxt = "Error occured1";
            e.printStackTrace();
        } catch (IOException e) {
            //Assign error message to resTxt
            resTxt = "Error occured2";
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            //Assign error message to resTxt
            resTxt = "Error occured3";
            e.printStackTrace();
        } catch (Exception e) {
            //Print error
            e.printStackTrace();
            //Assign error message to resTxt
            resTxt = "Error occured4";
        }
        SoapPrimitive response = null;
//        try {
//            resTxt=envelope.getResponse().toString();
//        } catch (SoapFault soapFault) {
//            soapFault.printStackTrace();
//        }
        return resTxt;
    }

    public static String  DataTransDB3( String webMethName ) {
        String resTxt = null;
        URL=URL+webMethName;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters

        //request.addProperty(username, password);
        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        androidHttpTransport.debug = true;
        try {
            // Invoke web service
            androidHttpTransport.call(SOAP_ACTION_URL + webMethName, envelope);
        } catch (HttpRetryException e) {
            //Assign error message to resTxt
            resTxt = "Error occured1";
            e.printStackTrace();
        } catch (IOException e) {
            //Assign error message to resTxt
            resTxt = "Error occured2";
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            //Assign error message to resTxt
            resTxt = "Error occured3";
            e.printStackTrace();
        } catch (Exception e) {
            //Print error
            e.printStackTrace();
            //Assign error message to resTxt
            resTxt = "Error occured4";
        }
        SoapPrimitive response = null;
//        try {
//            resTxt=envelope.getResponse().toString();
//        } catch (SoapFault soapFault) {
//            soapFault.printStackTrace();
//        }
        return resTxt;
    }

    public static String  uploadImages( String webMethName,String s,String picname) {
        String resTxt = null;
        URL=URL+webMethName;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters

        PropertyInfo sayHelloPI = new PropertyInfo();
        sayHelloPI.setName("s");//檔案位元組
        sayHelloPI.setValue(s);
        sayHelloPI.setType(String.class);
        request.addProperty(sayHelloPI);

        PropertyInfo sayHelloPI2 = new PropertyInfo();
        sayHelloPI2.setName("picname");//檔案位元組
        sayHelloPI2.setValue(picname);
        sayHelloPI2.setType(String.class);
        request.addProperty(sayHelloPI2);

        //request.addProperty(username, password);
        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        androidHttpTransport.debug = true;
        try {
            // Invoke web service
            androidHttpTransport.call(SOAP_ACTION_URL + webMethName, envelope);
        } catch (HttpRetryException e) {
            //Assign error message to resTxt
            resTxt = "Error occured1";
            e.printStackTrace();
        } catch (IOException e) {
            //Assign error message to resTxt
            resTxt = "Error occured2";
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            //Assign error message to resTxt
            resTxt = "Error occured3";
            e.printStackTrace();
        } catch (Exception e) {
            //Print error
            e.printStackTrace();
            //Assign error message to resTxt
            resTxt = "Error occured4";
        }
        SoapPrimitive response = null;
//        try {
//            resTxt=envelope.getResponse().toString();
//        } catch (SoapFault soapFault) {
//            soapFault.printStackTrace();
//        }
        return resTxt;
    }
}
