/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.restclient.app;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class ReadExternalPropFiles {

    String name;
    static String[] result;
    static InputStream inputStream;
    static Properties prop;

    /**
     * This function allows to configure the server from an external file
     *
     * @param fileprop the external file
     * @return the property file
     * @throws IOException
     *
     */
    public static Properties getPropertyFile(String fileprop) throws IOException {
        prop = new Properties();
        try {
            //inputStream = ReadExternalPropFiles.class.getResourceAsStream(fileprop);
            //inputStream = new FileInputStream(fileprop);// ReadExternalPropFiles.class.getResourceAsStream(fileprop);
            inputStream=ReadExternalPropFiles.class.getClassLoader().getResourceAsStream(fileprop);
            prop.load(inputStream);

        } catch (IOException ioe) {

            throw new IOException("Property File " + fileprop + " - " + ioe.getMessage());

        }

        return prop;
    }

}
