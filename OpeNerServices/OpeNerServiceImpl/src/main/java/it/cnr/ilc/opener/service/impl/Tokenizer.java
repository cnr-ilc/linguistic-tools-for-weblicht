/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.opener.service.impl;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import it.cnr.ilc.opener.service.i.OpenerService;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

/**
 * This class implements the tokenizer opener service.
 * <br>
 * The following parameters are mandatory:
 * <ul>
 * <li>input as string</li>
 * <li>The language </li>
 * <li>The input format </li>
 * </ul>
 * Input values are extracted from the input json data.
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class Tokenizer implements OpenerService {

    //Properties
    Properties prop;

    /**
     * class name used for logging purposes
     */
    public static final String CLASS_NAME = Tokenizer.class.getName();

    /**
     * The name of the service
     */
    private static final String SERVICE_NAME = "tokenizer";

    /**
     * The category of services. Services are category.servicename
     */
    // for future uses? public static final String SERVICE_CATEGORY = "opener";
    public static final String SERVICE_CATEGORY = ""; // it is empty, but leave it for future uses

    /**
     * transport
     */
    public String TRANSPORT = ""; //prop.getProperty("transport");
    public String URL_ENDPOINT = "";
    private String OUTPUT = "";

   
    public Tokenizer() {

    }

    /**
     * The endpoint to invoke the service from
     */
    public String SERVICE_ENDPOINT = ""; //

    /**
     * @return the SERVICE_NAME
     */
    @Override
    public String getSERVICE_NAME() {
        return SERVICE_NAME;
    }

    @Override
    public void setSERVICE_NAME(String service) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Properties getProp() {
        return prop;
    }

    @Override
    public void setProp(Properties prop) {
        this.prop = prop;
    }

    /**
     * 
     * @param inputType the input string
     * @param inputs the map containing input values
     * @param fromUrl true is the input is read from URL
     */
    @Override
    public void runService(String inputType, Map inputs, boolean fromUrl) {
        // variables;
      //  URL url;
        WebResource webResource;
        Client client;
        ClientResponse response;
        MultivaluedMap formData = new MultivaluedMapImpl();
        String output;

        setVariablesFromProp();

        inputs.put("input", inputType);
        StringBuilder sb = new StringBuilder();
        Iterator<Entry<String, String>> iter = inputs.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<String, String> entry = iter.next();
            formData.add(entry.getKey(), entry.getValue());
//            sb.append(entry.getKey());
//            sb.append('=').append('"');
//            sb.append(entry.getValue());
//            sb.append('"');
//            if (iter.hasNext()) {
//                sb.append(',').append(' ');
//            }
        }

//        System.out.println("it.cnr.ilc.opener.service.impl.Tokenizer.runService() here I am " + sb.toString());
//        System.out.println("it.cnr.ilc.opener.service.impl.Tokenizer.runService() prop " + URL_ENDPOINT);
        // actual code from here
        try {

            client = Client.create();
            webResource = client.resource(URL_ENDPOINT);
            response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);

            output = response.getEntity(String.class);
            setOutputStream(output);

            //InputStream s = new InputStream
            //System.out.println(output);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Ancillary method to set values from properties
     */
    private void setVariablesFromProp() {

        TRANSPORT = getProp().getProperty("transport");
        URL_ENDPOINT = getProp().getProperty("serviceUrl") + "/" + SERVICE_CATEGORY + getSERVICE_NAME();
        URL_ENDPOINT = TRANSPORT + "://" + URL_ENDPOINT;
    }

    @Override
    public String getOutputStream() {
        return OUTPUT;
    }

    @Override
    public void setOutputStream(String output) {
        OUTPUT = output;
    }

}
