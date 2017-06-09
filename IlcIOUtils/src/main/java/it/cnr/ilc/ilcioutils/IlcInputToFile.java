/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.ilcioutils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class IlcInputToFile {

    
    private static final String TEMP_FILE_PREFIX = "out_from_service_";
    private static final String TEMP_FILE_SUFFIX = ".txt";
    
    /**
     * logme
     */
     public static final String CLASS_NAME = IlcInputToFile.class.getName();

    

   

    /**
     * Creates a file with the content read from the URL
     * @param theUrl the URL where the content is
     * @return a File with the content from the URL
     */
     public static File createAndWriteTempFileFromUrl(String theUrl) {
        File tempOutputFile=null;
        String message;
        try {
          tempOutputFile   = File.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX);
         
          FileUtils.copyURLToFile(new URL(theUrl), tempOutputFile);
        } catch (IOException e) {
            message = String.format("Error in accessing URL %s %s", theUrl, e.getMessage());
            Logger.getLogger(CLASS_NAME).log(Level.SEVERE, message);
        }
        return tempOutputFile;
       
    }
    
    /**
     * Creates a file with the content read from the URL
     * @param string the String with the content is
     * @return a File with the content from the URL
     */
     public static File createAndWriteTempFileFromString(String string) {
        File tempOutputFile=null;
        String encoding="UTF-8";
        String message;
        try {
          tempOutputFile   = File.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX);
          FileUtils.writeStringToFile(tempOutputFile, string, encoding);
          
        } catch (IOException e) {
            message = String.format("Error in accessing String %s %s", string, e.getMessage());
            Logger.getLogger(CLASS_NAME).log(Level.SEVERE, message);
        }
        return tempOutputFile;
       
    }
    
    

}
