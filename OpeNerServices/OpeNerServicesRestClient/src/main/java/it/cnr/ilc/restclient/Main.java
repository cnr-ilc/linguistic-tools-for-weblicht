/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.restclient;

import it.cnr.ilc.restclient.app.ReadExternalPropFiles;
import it.cnr.ilc.restclient.app.SimpleRestClient;

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
        
        
        Properties prop=null;

        try {
             prop = ReadExternalPropFiles.getPropertyFile("opener.properties");
        } catch (IOException e) {

            e.printStackTrace();
        }
       
        SimpleRestClient src = new SimpleRestClient(prop);
        
       
        
        
        boolean goahead = true;
        String message, routine = "main";
        message = String.format("Initialize class %s in routine %s", CLASS_NAME, routine);
        //message = "IOException in closing the stream " + e.getMessage();
        Logger.getLogger(CLASS_NAME).log(Level.INFO, message);

       // SimpleRestClient sc = new SimpleRestClient();

        if (src.checkArgsForHelp(args)) {
            src.printTheHelp();
            System.exit(0);
        }
        
        message = String.format("Checking arguments %s in routine %s", CLASS_NAME, routine);
        //message = "IOException in closing the stream " + e.getMessage();
        Logger.getLogger(CLASS_NAME).log(Level.INFO, message);
        goahead = src.checkArgs(args);
        //System.out.println("it.cnr.ilc.restclient.Main.main() goahead="+goahead);

        message = String.format("Starting execution %s in routine %s", CLASS_NAME, routine);
        //message = "IOException in closing the stream " + e.getMessage();
        Logger.getLogger(CLASS_NAME).log(Level.INFO, message);
        src.init(goahead);
        message = String.format("Ending execution %s in routine %s", CLASS_NAME, routine);
        //message = "IOException in closing the stream " + e.getMessage();
        Logger.getLogger(CLASS_NAME).log(Level.INFO, message);
        System.exit(0);

    }

}
