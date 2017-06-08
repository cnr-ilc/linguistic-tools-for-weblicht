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
import org.apache.commons.validator.routines.UrlValidator;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class SimpleClient {

    public static final String CLASS_NAME = SimpleClient.class.getName();

    public static void main(String[] args) {
        String message;
        String inputType = "Hai ragione. Bisognerebbe scappare!!";
        //String inputType = "https://raw.githubusercontent.com/clarin-eric/LRS-Hackathon/master/samples/resources/txt/hermes-it.txt";

        inputType = "Ti prego, che bel fiore: pensi di meritartelo? se sì, coglimelo. Avervi a cena è bello, non puoi schifarmi";
        boolean fromUrl = false;
        FreelingIt freelingIt = new FreelingIt();

        // Get an UrlValidator
        UrlValidator defaultValidator = new UrlValidator(); // default schemes
        if (defaultValidator.isValid(inputType)) {
            fromUrl = true;
            message = String.format("The inputType supplied -%s- requires to be execued reading from a URL. So fromUrl is set to %s", inputType, fromUrl);
            Logger.getLogger(CLASS_NAME).log(Level.INFO, message);

        } else {
            fromUrl = false;
            message = String.format("The inputType supplied -%s- requires to be execued reading from a String. So fromUrl is set to %s", inputType, fromUrl);
            Logger.getLogger(CLASS_NAME).log(Level.INFO, message);
        }

        Map inputs = new HashMap();
        inputs.put("output_format", "tagged");
        freelingIt.setInputs(inputs);
        message = String.format("Calling service Freeling_It with fromUrl %s", fromUrl);
        Logger.getLogger(CLASS_NAME).log(Level.INFO, message);

        freelingIt.runService(inputType, fromUrl);
        if (freelingIt.getStatus() == 0 && !freelingIt.getOutputUrl().isEmpty()) {
            System.err.println("Hi well done: ");
        }
        System.err.println("\t Status: " + freelingIt.getStatus());
        System.err.println("\t outputUrl: " + freelingIt.getOutputUrl());
        System.err.println("\t Stream: " + freelingIt.getOutputStream());

    }

}
