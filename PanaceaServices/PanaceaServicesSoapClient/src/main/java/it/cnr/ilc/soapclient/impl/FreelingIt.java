/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.soapclient.impl;

import it.cnr.ilc.soapclient.i.PanaceaService;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.soaplab.clients.ClientConfig;
import org.soaplab.clients.ServiceLocator;
import org.soaplab.clients.SoaplabBaseClient;
import org.soaplab.share.SoaplabException;
import org.soaplab.share.SoaplabMap;


/**
 * This class implements the freeling_it soaplab service.
 * <br>
 * The following parameters are mandatory:
 * <ul>
 * <li>Either @see SERVICE_INPUT_DIRECT_DATA or @see SERVICE_INPUT_URL_DATA</li>
 * <li>The language @see SERVICE_LANGUAGE_VAL with the only value it.cfg</li>
 * </ul>
 * Optionally you can set other input values as well as the output_format. The
 * method @see runService firstly reads from an existing input map, then adds
 * the mandatory fields.
 * <br>
 * From the invoker you might:
 * <ul>
 * <li>set input("ner", &lt;value>&gt;) values: basic, none (default none)</li>
 * <li>set input("multiword", &lt;value>&gt;) values: true, false (default
 * false)</li>
 * <li><li>set input("output_format", &lt;value>&gt;) values: token, tagged,
 * splitted, (default tagged)</li>
 * </ul>
 *
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class FreelingIt implements PanaceaService {

    /**
     * class name used for logging purposes
     */
    public static final String CLASS_NAME = FreelingIt.class.getName();

    /**
     * The name of the service
     */
    public static final String SERVICE_NAME = "freeling_it";

    /**
     * The category of services. Services are category.servicename
     */
    public static final String SERVICE_CATEGORY = "panacea";

    /**
     * A parameter to set if the data are passed as string
     */
    public static final String SERVICE_INPUT_DIRECT_DATA = "input_direct_data";

    /**
     * A parameter to set if the data are passed as url
     */
    public static final String SERVICE_INPUT_URL_DATA = "input_url";

    /**
     * A mandatory parameter to set the language parameter
     */
    public static final String SERVICE_LANGUAGE = "language";

    /**
     * Managed value: it.cfg
     */
    public static final String SERVICE_LANGUAGE_VAL = "it.cfg";

    /**
     * The standard output. Get the result from stream instead that from the url
     * (which is default)
     */
    public static final String SERVICE_OUTPUT_STREAM = "std_output";

    /**
     * The endpoint to invoke the service from
     */
    public static final String SERVICE_ENDPOINT = URL_ENDPOINT + "/" + SERVICE_CATEGORY + "." + SERVICE_NAME;

    /**
     * A map containing values for input parameters
     */
    private Map inputs = new HashMap();

    /**
     * The status: 0 ok, 1 ko
     */
    private int status = 1;

    /**
     * The url where the result is
     */
    private String outputUrl = "";

    /**
     * The stream where the result is
     */
    private String outputFromStream = "";

    
    /**
     * Set the input type and the language.
     * @param inputType Either the string to analyze or the URL where the data are.
     * @param fromUrl if true the input is read from URL
     */
    public void runService(String inputType, boolean fromUrl) {
        System.err.println("string "+inputType);
        
        getInputs().put(SERVICE_LANGUAGE, SERVICE_LANGUAGE_VAL);
        if (fromUrl){
            getInputs().put(SERVICE_INPUT_URL_DATA, inputType);
        } else {
            getInputs().put(SERVICE_INPUT_DIRECT_DATA, inputType);
        }
        runService();
    }

    @Override
    public void runService() {
        
        SoaplabBaseClient client = getClient(SERVICE_ENDPOINT);
        

        String message = "";
        try {
            SoaplabMap results = client.runAndWaitFor(SoaplabMap
                    .fromMap(getInputs()));
            Map outputs = SoaplabMap.toMap(results);

            int i = 0;
            // status is at position 0
            // url is at position 1
            // summary is at position 2
            // stream is at position 3
            for (Object k : outputs.keySet()) {
                if (i == 0) {
                    status = (int) Integer.parseInt((String) outputs.get(k));
                    //System.err.println("object for k " + k + " at position i=" + i + ": " + outputs.get(k));
                    setStatus(status);
                }
                if (i == 1) {
                    outputUrl = (String) outputs.get(k);
                    setOutputUrl(outputUrl);

                }

                if (i == 3) {
                    outputFromStream = (String) outputs.get(k);
                    setOutputFromStream(outputFromStream);
                    System.err.println("object for k " + k + " at position i=" + i + ": " + outputs.get(k));

                }
//                if (i > 1) {
//                    System.err.println("object for k " + k + " at position i=" + i + ": " + outputs.get(k));
//                }
                i++;
            }
        } catch (Exception e) {
            message = e.getMessage();
            Logger.getLogger(CLASS_NAME).log(Level.SEVERE, message);
            setStatus(1);
            setOutputUrl("");
            setOutputFromStream("");
        }
    }

    @Override
    public SoaplabBaseClient getClient(String endpoint) {
        String message;
        ServiceLocator locator = new ServiceLocator();
        locator.setProtocol(ClientConfig.PROTOCOL_AXIS1);
        locator.setServiceEndpoint(endpoint);
        
        try {
            return new SoaplabBaseClient(locator);
        } catch (SoaplabException e) {
            message = "SoaplabException " + e.getMessage();
            Logger.getLogger(CLASS_NAME).log(Level.SEVERE, message);
            setStatus(1);
            //System.exit(-1);
        }
        return null;
    }



    @Override
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String getOutputUrl() {
        return outputUrl;
    }

    /**
     * @param outputUrl the outputUrl to set
     */
    public void setOutputUrl(String outputUrl) {
        this.outputUrl = outputUrl;
    }

    /**
     * @return the inputs
     */
    public Map getInputs() {
        return inputs;
    }

    /**
     * @param inputs the inputs to set
     */
    public void setInputs(Map inputs) {
        this.inputs = inputs;
    }

    @Override
    public String getOutputStream() {
       return outputFromStream;
    }

    /**
     * @param outputFromStream the outputFromStream to set
     */
    public void setOutputFromStream(String outputFromStream) {
        this.outputFromStream = outputFromStream;
    }

}
