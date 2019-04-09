/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.opener.service.i;

import java.util.Map;
import java.util.Properties;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public interface OpenerService {

    /**
     * The endpoint where services are
     */
    public String URL_ENDPOINT = "";

    /**
     * The transport
     */
    public String TRANSPORT = "";

    public String getSERVICE_NAME();

    public void setSERVICE_NAME(String service);

    public Properties getProp();

    public void setProp(Properties prop);

    public void runService(String inputType, Map inputs, boolean fromUrl);

    /**
     *
     * @return the response
     */
    public String getOutputStream();
    
    public void setOutputStream(String output);

}
