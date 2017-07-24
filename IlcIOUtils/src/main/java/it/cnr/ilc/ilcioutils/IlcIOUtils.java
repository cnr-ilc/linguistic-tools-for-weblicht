/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.ilcioutils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * Set of methods to read to and from strings anf diles
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class IlcIOUtils {

    /**
     * logme
     */
    public static final String CLASS_NAME = IlcIOUtils.class.getName();

    /**
     * This method uses FileUtils to quietly delete a file and checks if the
     * file exists and, if so, deletes it again.
     *
     * @param file the file to delete
     *
     */
    public static void deleteFile(File file) {
        FileUtils.deleteQuietly(file);
        if (file.exists()) {
            file.delete();
        }

    }

    /**
     * Read the content from file
     *
     * @param file the file to read from
     * @return a list of lines
     */
    public static List<String> readFromFile(File file) {
        List<String> lines = new ArrayList<>();
        String message;
        String encoding = "UTF-8";
        try {
            lines = FileUtils.readLines(file, encoding);

        } catch (Exception e) {
            message = String.format("IOException in reading the file %s %s" + file.getName(), e.getMessage());
            Logger.getLogger(CLASS_NAME).log(Level.SEVERE, message);
        }

        return lines;
    }
    
    

    /**
     * Read content from URL and creates a list of lines. use this method when
     * the result of the service are accessed via URL
     *
     * @param theUrl the url where the content is
     * @return an ArrayList from the content
     */
    public static ArrayList<String> readStreamAndCreateAListOfStringFromUrl(String theUrl) {

        ArrayList<String> lines = new ArrayList<>();
        String message;
        String encoding = "UTF-8";

        BufferedReader in = null;
        try {
            URL input = new URL(theUrl);
            in = new BufferedReader(
                    new InputStreamReader(input.openStream(), encoding));
            if (in != null) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    lines.add(inputLine);
                }
            }

        } catch (Exception e) {
            message = String.format("IOException in reading the stream from the url %s %s" + theUrl, e.getMessage());
            Logger.getLogger(CLASS_NAME).log(Level.SEVERE, message);
        }
        IOUtils.closeQuietly(in);

        return lines;
    }

    /**
     * Creates an inputstream from an URL
     *
     * @param theUrl the url where the content is
     * @return the input stream from the URL
     */
    public static InputStream readStreamFromUrl(String theUrl) {

        InputStream is = null;
        String encoding = "UTF-8";
        String message = "";

        try {
            is = new URL(theUrl).openStream();

        } catch (Exception e) {
            message = "IOException in coverting the stream into a string " + e.getMessage();
            Logger.getLogger(CLASS_NAME).log(Level.SEVERE, message);
        }

        //System.err.println("DDDD " + theString);
        IOUtils.closeQuietly(is);

        return is;
    }
    
    /**
     * read the content from file
     * @param filepath a path where the file to read the content from is
     * @return the content of the file
     * @throws IOException 
     */
    public static String readFileContent(String filepath) throws IOException {
        String message = "";
        File initialFile;
        InputStream targetStream = null;
        String theString = "";

        try {
            initialFile = new File(filepath);
            targetStream = FileUtils.openInputStream(initialFile);
            theString = IOUtils.toString(targetStream, "UTF-8");
        } catch (IOException e) {

            message = "IOaaException in reading the stream for " + filepath + " " + e.getMessage();
            Logger.getLogger(CLASS_NAME).log(Level.SEVERE, message);
            //System.exit(-1);
        } finally {
            if (targetStream != null) {
                try {
                    targetStream.close();
                } catch (IOException e) {
                    message = "IOException in closing the stream for " + filepath + " " + e.getMessage();
                    Logger.getLogger(CLASS_NAME).log(Level.SEVERE, message);
                    //System.exit(-1);
                }

            }

        }
        //System.err.println(theString);
        return theString;
    }

}
