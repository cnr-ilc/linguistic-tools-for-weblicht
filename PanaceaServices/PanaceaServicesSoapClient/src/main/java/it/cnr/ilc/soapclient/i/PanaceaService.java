/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.soapclient.i;

import java.util.Map;
import org.soaplab.clients.SoaplabBaseClient;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public interface PanaceaService {

    /**
     * The endpoint where services are TODO: move it to a property file
     */
    public static final String URL_ENDPOINT = "http://langtech3.ilc.cnr.it:8080/soaplab2-axis/services";

    

    /**
     * get the client from the soaplab endpoint
     *
     * @param endpoint the endpoint of the services
     * @return the soaplab client used to access the specific service
     */
    public SoaplabBaseClient getClient(String endpoint);

    /**
     *
     * @return the status
     */
    public int getStatus();

    /**
     * Set the input type and the language.
     *
     * @param inputs the map with input parameters
     * @param inputType Either the string to analyze or the URL
     * where the data are.
     * @param fromUrl if true the input is read from URL
     */
    public void runService(String inputType, Map inputs, boolean fromUrl);

    /**
     *
     * @return the output url where the result is
     */
    public String getOutputUrl();

    /**
     *
     * @return the output url where the result is
     */
    public String getOutputStream();

    public Map getInputs();

    public void setInputs(Map inputs);

    public String getSERVICE_NAME();

}
