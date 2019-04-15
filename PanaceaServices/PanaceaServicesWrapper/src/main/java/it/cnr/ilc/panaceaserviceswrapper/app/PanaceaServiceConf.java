/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.panaceaserviceswrapper.app;

import io.dropwizard.Configuration;
import it.cnr.ilc.soapclient.app.ReadExternalPropFiles;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class PanaceaServiceConf extends Configuration{
    Properties prop;
    public Properties getProp(){
    try {
             prop = ReadExternalPropFiles.getPropertyFile("panacea.properties");
             //System.out.println("it.cnr.ilc.openerserviceswrapper.app.OpenerServiceConf.getProp() -"+prop.getProperty("transport")+"-");
        } catch (IOException e) {

            e.printStackTrace();
        }
    return prop;
    }
}
