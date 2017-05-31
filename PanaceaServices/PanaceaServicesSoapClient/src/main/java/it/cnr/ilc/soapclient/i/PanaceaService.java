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
    
    public static final String URL_ENDPOINT="http://langtech3.ilc.cnr.it:8080/soaplab2-axis/services";
    
    /**
     * run the specific service
     */
    public void runService();
    
    /**
     * run the specific service reading from url
     */
    public void runServiceFromUrl();
    
    /**
     * get the client from the soaplab endpoint
     * @param endpoint 
     * @return the soaplab client
     */
    public SoaplabBaseClient getClient(String endpoint);
    
}
