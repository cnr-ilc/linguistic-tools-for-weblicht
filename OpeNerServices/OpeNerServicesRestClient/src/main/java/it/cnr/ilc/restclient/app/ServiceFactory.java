/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.restclient.app;


import it.cnr.ilc.ilcutils.Vars;
import it.cnr.ilc.opener.service.filler.i.FillSimpleTypes;
import it.cnr.ilc.opener.service.filler.impl.FillSimpleTypesFromOpenerTokenizer;
import it.cnr.ilc.opener.service.i.OpenerService;
import it.cnr.ilc.opener.service.impl.Tokenizer;




/**
 * Factory to instantiate the correct service according to the input
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class ServiceFactory {

    /**
     * Returns the panacea service
     * @param service the name of the service
     * @return the instantiated service
     */
    public OpenerService getService(String service) {
        if (service == null) {
            return null;
        }
        if (service.equalsIgnoreCase("tokenizer")) {
            return new Tokenizer();
        }
        return null;
    }
    
    /**
     * Returns the class to read th input and fill basic types
     * @param service the name of the service
     * @return the instantiated class to read the service output 
     */
    public FillSimpleTypes getFillSimpleType(String service){
    if (service == null) {
            return null;
        }
        if (service.equalsIgnoreCase("tokenizer")) {
            return new FillSimpleTypesFromOpenerTokenizer();
        }
        return null;
    }

}
