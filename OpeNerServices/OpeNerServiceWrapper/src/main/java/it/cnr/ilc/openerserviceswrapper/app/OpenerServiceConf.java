/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.openerserviceswrapper.app;

import io.dropwizard.Configuration;
import it.cnr.ilc.restclient.app.ReadExternalPropFiles;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class OpenerServiceConf extends Configuration{
    Properties prop;
    public Properties getProp(){
    try {
             prop = ReadExternalPropFiles.getPropertyFile("opener.properties");
             //System.out.println("it.cnr.ilc.openerserviceswrapper.app.OpenerServiceConf.getProp() -"+prop.getProperty("transport")+"-");
        } catch (IOException e) {

            e.printStackTrace();
        }
    return prop;
    }
    
}
