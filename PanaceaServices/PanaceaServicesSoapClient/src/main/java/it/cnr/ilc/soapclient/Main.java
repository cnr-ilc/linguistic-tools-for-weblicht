/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.soapclient;

import it.cnr.ilc.soapclient.app.ReadExternalPropFiles;
import it.cnr.ilc.soapclient.app.SimpleSoapClient;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class Main {

    public static final String CLASS_NAME = Main.class.getName();

    public static void main(String[] args) {

        boolean goahead = true;
        String message, routine = "main";
        message = String.format("Initialize class %s in routine %s", CLASS_NAME, routine);
        //message = "IOException in closing the stream " + e.getMessage();
        Logger.getLogger(CLASS_NAME).log(Level.INFO, message);
        Properties prop=null;

        try {
             prop = ReadExternalPropFiles.getPropertyFile("panacea.properties");
        } catch (IOException e) {

            e.printStackTrace();
        }

        SimpleSoapClient sc = new SimpleSoapClient(prop);

        if (sc.checkArgsForHelp(args)) {
            sc.printTheHelp();
            System.exit(0);
        }
        message = String.format("Checking arguments %s in routine %s", CLASS_NAME, routine);
        //message = "IOException in closing the stream " + e.getMessage();
        Logger.getLogger(CLASS_NAME).log(Level.INFO, message);
        goahead = sc.checkArgs(args);

        message = String.format("Starting execution %s in routine %s", CLASS_NAME, routine);
        //message = "IOException in closing the stream " + e.getMessage();
        Logger.getLogger(CLASS_NAME).log(Level.INFO, message);
        sc.init(goahead);
        message = String.format("Ending execution %s in routine %s", CLASS_NAME, routine);
        //message = "IOException in closing the stream " + e.getMessage();
        Logger.getLogger(CLASS_NAME).log(Level.INFO, message);
        System.exit(0);

    }

}
