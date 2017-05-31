/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.soapclient.app;

import it.cnr.ilc.soapclient.impl.FreelingIt;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class SimpleClient {

    public static final String CLASS_NAME = SimpleClient.class.getName();

    public static void main(String[] args) {
        String message;
        FreelingIt freelingIt = new FreelingIt();
        Map inputs = new HashMap();
        inputs.put("output_format", "token"); 
        freelingIt.setInputs(inputs);
        message = "Calling service Freeling_It";
        Logger.getLogger(CLASS_NAME).log(Level.INFO, message);
        freelingIt.runServiceFromUrl();
        System.err.println("Hi");

    }

}
