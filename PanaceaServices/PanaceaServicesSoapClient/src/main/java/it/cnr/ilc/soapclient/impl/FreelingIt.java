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
 * method @see runService firstly reads from an existing input map, then adds the mandatory fields. 
 * <br>
 * From the invoker you might:
 * <ul>
 * <li>set input("ner", &lt;value>&gt;) values: basic, none (default none)</li>
 * <li>set input("multiword", &lt;value>&gt;) values: true, false (default false)</li>
 * <li><li>set input("output_format", &lt;value>&gt;) values: token, tagged, splitted, (default tagged)</li>
 * </ul>
 * 
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class FreelingIt implements PanaceaService {

    public static final String CLASS_NAME = FreelingIt.class.getName();

    public static final String SERVICE_NAME = "freeling_it";

    public static final String SERVICE_CATEGORY = "panacea";

    public static final String SERVICE_INPUT_DIRECT_DATA = "input_direct_data";

    public static final String SERVICE_INPUT_URL_DATA = "input_url";

    public static final String SERVICE_LANGUAGE = "language";

    public static final String SERVICE_LANGUAGE_VAL = "it.cfg";

    public static final String SERVICE_OUTPUT_STREAM = "std_output";

    public static final String SERVICE_ENDPOINT = URL_ENDPOINT + "/" + SERVICE_CATEGORY + "." + SERVICE_NAME;

    private Map inputs = new HashMap();

    @Override
    public void runService() {
        SoaplabBaseClient client = getClient(SERVICE_ENDPOINT);
        Map local_inputs = getInputs();
        local_inputs.put(SERVICE_INPUT_DIRECT_DATA, "Mi chiamo Riccardo. Abito a Pisa in una cascina, mi piace il tacchino come a Giunone. Mia suocera ha una sedia a rotelle.");
        local_inputs.put(SERVICE_LANGUAGE, SERVICE_LANGUAGE_VAL);
        //local_inputs.put("output_format", "token");

        try {
            SoaplabMap results = client.runAndWaitFor(SoaplabMap
                    .fromMap(getInputs()));
            Map outputs = SoaplabMap.toMap(results);

            System.out.println("Result:\n " + outputs.get(SERVICE_OUTPUT_STREAM));
            for (Object k : outputs.keySet()) {
                System.err.println("object for k " + k + ": " + outputs.get(k));

            }
        } catch (Exception e) {
            e.printStackTrace();
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

            System.exit(-1);
        }
        return null;
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
    public void runServiceFromUrl() {
        SoaplabBaseClient client = getClient(SERVICE_ENDPOINT);
        Map local_inputs = getInputs();
        local_inputs.put(SERVICE_INPUT_URL_DATA, "https://raw.githubusercontent.com/clarin-eric/LRS-Hackathon/master/samples/resources/txt/hermes-it.txt");
        local_inputs.put(SERVICE_LANGUAGE, SERVICE_LANGUAGE_VAL);
        //local_inputs.put("output_format", "token");

        try {
            SoaplabMap results = client.runAndWaitFor(SoaplabMap
                    .fromMap(getInputs()));
            Map outputs = SoaplabMap.toMap(results);

            System.out.println("Result:\n " + outputs.get(SERVICE_OUTPUT_STREAM));
            for (Object k : outputs.keySet()) {
                System.err.println("object for k " + k + ": " + outputs.get(k));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
