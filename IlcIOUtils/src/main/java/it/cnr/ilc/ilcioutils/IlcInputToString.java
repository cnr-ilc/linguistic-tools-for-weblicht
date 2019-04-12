/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.ilcioutils;

import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

/**
 * Some utilities to read from an inputstream and write a string
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class IlcInputToString {
    /**
     * logme
     */
     public static final String CLASS_NAME = IlcInputToString.class.getName();

    /**
     * Convert an inputstream into a string
     *
     * @param is the inputstream
     * @return the string from the input
     */
    public static String convertInputStreamToString(InputStream is) {
        StringWriter writer = new StringWriter();
        String encoding = "UTF-8";
        String message = "";
        String theString = "";
        try {
            IOUtils.copy(is, writer, encoding);
            theString = writer.toString();
        } catch (Exception e) {
            message = "IOException in coverting the stream into a string " + e.getMessage();
            Logger.getLogger(CLASS_NAME).log(Level.SEVERE, message);
        }

        //System.err.println("DDDD " + theString);
        IOUtils.closeQuietly(is);
        IOUtils.closeQuietly(writer);
        return theString;
    }

    /**
     * Convert an inputstream into a string
     *
     * @param theUrl the url to get the context from
     * @return the string from the input
     */
    public static String convertInputStreamFromUrlToString(String theUrl) {
        StringWriter writer = new StringWriter();
        InputStream is = null;
        String encoding = "UTF-8";
        String message = "";
        String theString = "";
        try {
            is = new URL(theUrl).openStream();
            IOUtils.copy(is, writer, encoding);
            theString = writer.toString();
        } catch (Exception e) {
            message = "IOException in converting the stream into a string " + e.getMessage();
            Logger.getLogger(CLASS_NAME).log(Level.SEVERE, message);
        }

        //System.err.println("DDDD " + theString);
        IOUtils.closeQuietly(is);
        IOUtils.closeQuietly(writer);
        return theString;
    }

    
    
}
