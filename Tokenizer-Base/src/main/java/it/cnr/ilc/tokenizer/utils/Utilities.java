/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.tokenizer.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class Utilities {

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
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, message);
            //System.exit(-1);
        } finally {
            if (targetStream != null) {
                try {
                    targetStream.close();
                } catch (IOException e) {
                    message = "IOException in closing the stream for " + filepath + " " + e.getMessage();
                    Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, message);
                    //System.exit(-1);
                }

            }

        }
        //System.err.println(theString);
        return theString;
    }

    /**
     *
     * @param is
     * @return
     */
    public String convertInputStreamToString(InputStream is) {
        StringWriter writer = new StringWriter();
        String encoding = Vars.encoding;
        String message = "";
        String theString = "";
        try {
            IOUtils.copy(is, writer, encoding);
            theString = writer.toString();
        } catch (Exception e) {
            message = "IOException in coverting the stream into a string " + e.getMessage();
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, message);
        }

        return theString;

    }

}
