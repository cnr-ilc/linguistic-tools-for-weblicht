/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.soapclient.app;

import it.cnr.ilc.ilcfillsimpletypes.basic.FillSimpleTypesFromFreelingIt;
import it.cnr.ilc.ilcioutils.IlcIOUtils;
import it.cnr.ilc.ilcioutils.IlcInputToFile;
import it.cnr.ilc.ilcsimpletypes.IlcSimpleLemma;
import it.cnr.ilc.soapclient.i.PanaceaService;
import it.cnr.ilc.soapclient.impl.FreelingIt;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
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
    private String output_format = Format.OUT_TAB;
    private String find_mw = Format.FIND_MW;
    private String find_ner = Format.FIND_NER;

    private String service = "";
    private String iFile = "";
    private String oFile = "";

    public Theservice theservice = new Theservice();

    public void init(boolean goahead) {
        PrintStream ps = System.out;
        BufferedReader br = null;
        boolean str = true;
        String input = "";
        String message = "";
        ServiceFactory factory = new ServiceFactory();
        boolean fromUrl = false;

        Map inputs = new HashMap();

        if (goahead) {
            if (getiFile().isEmpty()) {
                try {
                    br = new BufferedReader(new InputStreamReader(System.in));

                    while (str) {

                        //System.out.print("Enter something : ");
                        input = br.readLine();
                        str = false;

                    }

                } catch (IOException e) {
                    //tokenizerCli.printHelp();
                    message = "IOException in reading the stream " + e.getMessage();
                    Logger
                            .getLogger(CLASS_NAME).log(Level.SEVERE, message);

                    System.exit(-1);

                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            // tokenizerCli.printHelp();

                            message = "IOException in closing the stream " + e.getMessage();
                            Logger
                                    .getLogger(CLASS_NAME).log(Level.SEVERE, message);

                            System.exit(-1);

                        }
                    }
                }

            } else {// read the input file
                try {
                    input = IlcIOUtils.readFileContent(getiFile());
                } catch (IOException e) {
                    theservice.printHelp();
                    System.exit(-1);
                }

            }

            System.err.println("input " + input);

            // actual code from here
            PanaceaService s = factory.getService(getService());

            //theservice.setService(s);
            theservice.setService(s, input, inputs);
            theservice.run();

            inputs = s.getInputs();

            //s.setInputForService("false", "basic", "tagged");
            inputs = s.getInputs();

            s.setInputs(inputs);
            inputs = s.getInputs();

            //FillSimpleTypesFromFreelingIt fillSimpleTypesFromFreelingIt = new FillSimpleTypesFromFreelingIt();
            // Get an UrlValidator
            UrlValidator defaultValidator = new UrlValidator(); // default schemes
            if (defaultValidator.isValid(input)) {
                fromUrl = true;
                message = String.format("The inputType supplied -%s- requires to be execued reading from a URL. So fromUrl is set to %s", input, fromUrl);
                Logger.getLogger(CLASS_NAME).log(Level.INFO, message);

            } else {
                fromUrl = false;
                message = String.format("The inputType supplied -%s- requires to be execued reading from a String. So fromUrl is set to %s", input, fromUrl);
                Logger.getLogger(CLASS_NAME).log(Level.INFO, message);
            }

            // Map inputs = new HashMap();
            //inputs.put("output_format", "tagged");
            //freelingIt.setInputs(inputs);
            message = String.format("Calling service Freeling_It with fromUrl %s", fromUrl);
            Logger.getLogger(CLASS_NAME).log(Level.INFO, message);

            s.runService(input, fromUrl);
            if (s.getStatus() == 0 && !s.getOutputUrl().isEmpty()) {
                System.err.println("Hi well done: ");
            }
            System.err.println("\t Status: " + s.getStatus());
            System.err.println("\t outputUrl: " + s.getOutputUrl());

        } else {

            theservice.printHelp();
            //System.err.println("EXIT");
            System.exit(0);
        }

    }

    public static void main(String[] args) {

        boolean goahead = true;

        SimpleClient sc = new SimpleClient();

        if (sc.checkArgsForHelp(args)) {
            sc.printTheHelp();
            System.exit(0);
        }
        goahead = sc.checkArgs(args);
        sc.init(goahead);
        System.exit(0);

//        //m.init(goahead);
//        String message;
////        String inputType = "Hai ragione. Bisognerebbe scappare!!";
////        //String inputType = "https://raw.githubusercontent.com/clarin-eric/LRS-Hackathon/master/samples/resources/txt/hermes-it.txt";
////
////        inputType = "Ti prego, che bel fiore: pensi di meritartelo? se sì, coglimelo. Amabilmente lavoro con il Primo Ministro per promulgare delle buone leggi per le persone sulle sedie a rotelle. Avervi a cena è bello, non puoi schifarmi. E che diavolo!";
////        inputType = "Avere a cena il Primo Ministro vale una promozione. E che diavolo.";
//        boolean fromUrl = false;
//        FreelingIt freelingIt = new FreelingIt();
//        Map inputs = new HashMap();
//        inputs = freelingIt.getInputs();
//
//        freelingIt.setInputForService("false", "basic", "token");
//        inputs = freelingIt.getInputs();
//
//        freelingIt.setInputs(inputs);
//        inputs = freelingIt.getInputs();
//
//        //FillSimpleTypesFromFreelingIt fillSimpleTypesFromFreelingIt = new FillSimpleTypesFromFreelingIt();
//        // Get an UrlValidator
//        UrlValidator defaultValidator = new UrlValidator(); // default schemes
//        if (defaultValidator.isValid(inputType)) {
//            fromUrl = true;
//            message = String.format("The inputType supplied -%s- requires to be execued reading from a URL. So fromUrl is set to %s", inputType, fromUrl);
//            Logger.getLogger(CLASS_NAME).log(Level.INFO, message);
//
//        } else {
//            fromUrl = false;
//            message = String.format("The inputType supplied -%s- requires to be execued reading from a String. So fromUrl is set to %s", inputType, fromUrl);
//            Logger.getLogger(CLASS_NAME).log(Level.INFO, message);
//        }
//
//        // Map inputs = new HashMap();
//        //inputs.put("output_format", "tagged");
//        //freelingIt.setInputs(inputs);
//        message = String.format("Calling service Freeling_It with fromUrl %s", fromUrl);
//        Logger.getLogger(CLASS_NAME).log(Level.INFO, message);
//
//        freelingIt.runService(inputType, fromUrl);
//        if (freelingIt.getStatus() == 0 && !freelingIt.getOutputUrl().isEmpty()) {
//            System.err.println("Hi well done: ");
//        }
//        System.err.println("\t Status: " + freelingIt.getStatus());
//        System.err.println("\t outputUrl: " + freelingIt.getOutputUrl());
//        // System.err.println("\t Stream: " + freelingIt.getOutputStream());
//
////        String x = InputToString.convertInputStreamFromUrlToString(freelingIt.getOutputUrl());
////        try {
////            InputToString.readStramFromUrl(freelingIt.getOutputUrl());
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        for (String line : InputToString.readStreamAndCreateAstringFromUrl(freelingIt.getOutputUrl())) {
////            System.err.println("line "+line);
////        }
////        
////        File file = IlcInputToFile.createAndWriteTempFileFromString(freelingIt.getOutputStream());
////
//////        for (String line : IlcIOUtils.readFromFile(file)) {
//////            System.err.println("line "+line);
//////        }
//////        for (String line : fillSimpleTypesFromFreelingIt.getLinesFromFile(file)) {
//////            System.err.println("line "+line);
//////        }
////        fillSimpleTypesFromFreelingIt.manageServiceOutput(fillSimpleTypesFromFreelingIt.getLinesFromFile(file));
////        //System.err.println("Tokens "+fillSimpleTypesFromFreelingIt.getTokens().toString());
////        //System.err.println("lemmas " + fillSimpleTypesFromFreelingIt.getLemmas());
////        for (IlcSimpleLemma lemma : fillSimpleTypesFromFreelingIt.getLemmas()) {
////            System.err.println(lemma.toKaf());
////        }
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
        //String inputType = "Hai ragione. Bisognerebbe scappare!!";
        String inputType = "https://raw.githubusercontent.com/clarin-eric/LRS-Hackathon/master/samples/resources/txt/hermes-it.txt";

//        inputType = "Ti prego, che bel fiore: pensi di meritartelo? se sì, coglimelo. Amabilmente lavoro con il Primo Ministro per promulgare delle buone leggi per le persone sulle sedie a rotelle. Avervi a cena è bello, non puoi schifarmi. E che diavolo!";
//        inputType = "Avere a cena il Primo Ministro vale una promozione. E che diavolo.";
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
//        inputs.put("output_format", "token");
//        inputs.put("multiword", "false");
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

    public void printTheHelp() {
        //tokenizerCli.printHelp();
        System.err.println("PRINTHELP");
        theservice.printHelp();
    }

    private boolean checkServices(String service) {

        return Vars.services.contains(service);

    }

    private boolean checkArgs(String[] args) {
        boolean ret = true;
        int i = 0;
        if ((args.length % 2) != 0) {
            return false;
        }
        for (String arg : args) {
            switch (arg) {
                case "-s":

                    if (checkServices(args[i + 1])) {
                        setService(args[i + 1]);
                        break;
                    } else {
                        return false;
                    }
                case "-i":
                    setiFile(args[i + 1]);
                    break;
                case "-o":
                    setoFile(args[i + 1]);
                    break;
                case "-f":
                    //setFormat(args[i + 1]);
                    break;
                case "-of":
                    break;

                case "-sf":
                    break;

            }
            //System.err.println("arg at " + i + "-" + arg + "-");
            i++;
        }

        return true;
    }

    /**
     * @return the service
     */
    public String getService() {
        return service;
    }

    /**
     * @param service the service to set
     */
    public void setService(String service) {
        this.service = service;
    }

    /**
     * @return the iFile
     */
    public String getiFile() {
        return iFile;
    }

    /**
     * @param iFile the iFile to set
     */
    public void setiFile(String iFile) {
        this.iFile = iFile;
    }

    /**
     * @return the oFile
     */
    public String getoFile() {
        return oFile;
    }

    /**
     * @param oFile the oFile to set
     */
    public void setoFile(String oFile) {
        this.oFile = oFile;
    }
}
