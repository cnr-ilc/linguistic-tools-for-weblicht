/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.soapclient.i;

import org.soaplab.clients.SoaplabBaseClient;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public interface PanaceaService {
    
    /**
     * The endpoint where services are
     * TODO: move it to a property file
     */
    public static final String URL_ENDPOINT="http://langtech3.ilc.cnr.it:8080/soaplab2-axis/services";
    
    /**
     * run the specific service reading from a string
     */
    public void runService();
    
    
    
    /**
     * get the client from the soaplab endpoint
     * @param endpoint 
     * @return the soaplab client used to access the specific service
     */
    public SoaplabBaseClient getClient(String endpoint);
    
    /**
     * 
     * @return the status 
     */
    public int getStatus();
    
    
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
    
    
    
}
