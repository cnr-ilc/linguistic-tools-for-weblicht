/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.soapclient.app;

import it.cnr.ilc.ilcfillsimpletypes.basic.FillSimpleTypesFromFreelingIt;
import it.cnr.ilc.ilcioutils.IlcInputToFile;
import it.cnr.ilc.ilcsimpletypes.IlcSimpleLemma;
import it.cnr.ilc.soapclient.impl.FreelingIt;
import java.io.File;
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
    public String output_format = Format.OUT_TAB;
    public String find_mw = Format.FIND_MW;
    public String find_ner = Format.FIND_NER;

    private boolean checkArgsForHelp(String[] args) {

        for (String arg : args) {
            switch (arg) {
                case "-h":

                    return true;

            }
            //System.err.println("arg at " + i + "-" + arg + "-");

        }

        return false;
    }

    public static void main(String[] args) {

        boolean goahead = true;

        SimpleClient m = new SimpleClient();

        if (m.checkArgsForHelp(args)) {
            m.printTheHelp();
            System.exit(0);
        }
        goahead = m.checkArgs(args);

        //m.init(goahead);
        String message;
        String inputType = "Hai ragione. Bisognerebbe scappare!!";
        //String inputType = "https://raw.githubusercontent.com/clarin-eric/LRS-Hackathon/master/samples/resources/txt/hermes-it.txt";

        inputType = "Ti prego, che bel fiore: pensi di meritartelo? se sì, coglimelo. Amabilmente lavoro con il Primo Ministro per promulgare delle buone leggi per le persone sulle sedie a rotelle. Avervi a cena è bello, non puoi schifarmi. E che diavolo!";
        inputType = "Avere a cena il Primo Ministro vale una promozione. E che diavolo.";
        boolean fromUrl = false;
        FreelingIt freelingIt = new FreelingIt();
        Map inputs = new HashMap();
        inputs = freelingIt.getInputs();

        freelingIt.setInputForService("false", "basic", "token");
        inputs = freelingIt.getInputs();

        freelingIt.setInputs(inputs);
        inputs = freelingIt.getInputs();

        //FillSimpleTypesFromFreelingIt fillSimpleTypesFromFreelingIt = new FillSimpleTypesFromFreelingIt();
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

        // Map inputs = new HashMap();
        //inputs.put("output_format", "tagged");
        //freelingIt.setInputs(inputs);
        message = String.format("Calling service Freeling_It with fromUrl %s", fromUrl);
        Logger.getLogger(CLASS_NAME).log(Level.INFO, message);

        freelingIt.runService(inputType, fromUrl);
        if (freelingIt.getStatus() == 0 && !freelingIt.getOutputUrl().isEmpty()) {
            System.err.println("Hi well done: ");
        }
        System.err.println("\t Status: " + freelingIt.getStatus());
        System.err.println("\t outputUrl: " + freelingIt.getOutputUrl());
        // System.err.println("\t Stream: " + freelingIt.getOutputStream());

//        String x = InputToString.convertInputStreamFromUrlToString(freelingIt.getOutputUrl());
//        try {
//            InputToString.readStramFromUrl(freelingIt.getOutputUrl());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        for (String line : InputToString.readStreamAndCreateAstringFromUrl(freelingIt.getOutputUrl())) {
//            System.err.println("line "+line);
//        }
//        
//        File file = IlcInputToFile.createAndWriteTempFileFromString(freelingIt.getOutputStream());
//
////        for (String line : IlcIOUtils.readFromFile(file)) {
////            System.err.println("line "+line);
////        }
////        for (String line : fillSimpleTypesFromFreelingIt.getLinesFromFile(file)) {
////            System.err.println("line "+line);
////        }
//        fillSimpleTypesFromFreelingIt.manageServiceOutput(fillSimpleTypesFromFreelingIt.getLinesFromFile(file));
//        //System.err.println("Tokens "+fillSimpleTypesFromFreelingIt.getTokens().toString());
//        //System.err.println("lemmas " + fillSimpleTypesFromFreelingIt.getLemmas());
//        for (IlcSimpleLemma lemma : fillSimpleTypesFromFreelingIt.getLemmas()) {
//            System.err.println(lemma.toKaf());
//        }
    }

    public static void main1(String[] args) {
        /*
        String message;
        String inputType = "Hai ragione. Bisognerebbe scappare!!";
        //String inputType = "https://raw.githubusercontent.com/clarin-eric/LRS-Hackathon/master/samples/resources/txt/hermes-it.txt";

        inputType = "Ti prego, che bel fiore: pensi di meritartelo? se sì, coglimelo. Amabilmente lavoro con il Primo Ministro per promulgare delle buone leggi per le persone sulle sedie a rotelle. Avervi a cena è bello, non puoi schifarmi. E che diavolo!";
        inputType = "Avere a cena il Primo Ministro vale una promozione. E che diavolo.";
        boolean fromUrl = false;
        FreelingIt freelingIt = new FreelingIt();
        Map inputs = new HashMap();
        inputs = freelingIt.getInputs();
        
        freelingIt.setInputForService("false", "basic", "tagged");
        inputs = freelingIt.getInputs();
        
        freelingIt.setInputs(inputs);
        inputs = freelingIt.getInputs();
         */
        String message;
        String inputType = "Hai ragione. Bisognerebbe scappare!!";
        //String inputType = "https://raw.githubusercontent.com/clarin-eric/LRS-Hackathon/master/samples/resources/txt/hermes-it.txt";

        inputType = "Ti prego, che bel fiore: pensi di meritartelo? se sì, coglimelo. Amabilmente lavoro con il Primo Ministro per promulgare delle buone leggi per le persone sulle sedie a rotelle. Avervi a cena è bello, non puoi schifarmi. E che diavolo!";
        inputType = "Avere a cena il Primo Ministro vale una promozione. E che diavolo.";
        boolean fromUrl = false;
        FreelingIt freelingIt = new FreelingIt();

        FillSimpleTypesFromFreelingIt fillSimpleTypesFromFreelingIt = new FillSimpleTypesFromFreelingIt();

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
        inputs.put("output_format", "token");
        inputs.put("multiword", "false");
        freelingIt.setInputs(inputs);
        message = String.format("Calling service Freeling_It with fromUrl %s", fromUrl);
        Logger.getLogger(CLASS_NAME).log(Level.INFO, message);

        freelingIt.runService(inputType, fromUrl);
        if (freelingIt.getStatus() == 0 && !freelingIt.getOutputUrl().isEmpty()) {
            System.err.println("Hi well done: ");
        }
        System.err.println("\t Status: " + freelingIt.getStatus());
        System.err.println("\t outputUrl: " + freelingIt.getOutputUrl());
        // System.err.println("\t Stream: " + freelingIt.getOutputStream());

//        String x = InputToString.convertInputStreamFromUrlToString(freelingIt.getOutputUrl());
//        try {
//            InputToString.readStramFromUrl(freelingIt.getOutputUrl());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        for (String line : InputToString.readStreamAndCreateAstringFromUrl(freelingIt.getOutputUrl())) {
//            System.err.println("line "+line);
//        }
//        
        File file = IlcInputToFile.createAndWriteTempFileFromString(freelingIt.getOutputStream());

//        for (String line : IlcIOUtils.readFromFile(file)) {
//            System.err.println("line "+line);
//        }
//        for (String line : fillSimpleTypesFromFreelingIt.getLinesFromFile(file)) {
//            System.err.println("line "+line);
//        }
        fillSimpleTypesFromFreelingIt.manageServiceOutput(fillSimpleTypesFromFreelingIt.getLinesFromFile(file));
        //System.err.println("Tokens "+fillSimpleTypesFromFreelingIt.getTokens().toString());
        //System.err.println("lemmas " + fillSimpleTypesFromFreelingIt.getLemmas());
        for (IlcSimpleLemma lemma : fillSimpleTypesFromFreelingIt.getLemmas()) {
            System.err.println(lemma.toKaf());
        }

    }
}
