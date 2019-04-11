/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.ilcioutils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 *Some utilities to read from an inputstream and write a file
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class IlcInputToFile {

    
    private static final String TEMP_FILE_PREFIX = "out_from_service_";
    private static final String TEMP_FILE_IS_PREFIX = "out_from_is_";
    private static final String TEMP_FILE_SUFFIX = ".txt";
    
    /**
     * logmessage
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
     * Creates a file with the content read from string
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
     /**
     * Creates a file with the content read from string
     * @param input the InputStream containing the content
     * @return a File with the content from the URL
     */
     public static File createAndWriteTempFileFromInputStream(InputStream input) {
        File tempOutputFile=null;
        String encoding="UTF-8";
        String string; 
        String message;
        try {
          string = IOUtils.toString(input, "UTF-8"); 
          tempOutputFile   = File.createTempFile(TEMP_FILE_IS_PREFIX, TEMP_FILE_SUFFIX);
          FileUtils.writeStringToFile(tempOutputFile, string, encoding);
           
          
          
        } catch (IOException e) {
            message = String.format("Error in accessing InputStream %s %s", input.toString(), e.getMessage());
            Logger.getLogger(CLASS_NAME).log(Level.SEVERE, message);
        }
        return tempOutputFile;
       
    }
    
    

}
