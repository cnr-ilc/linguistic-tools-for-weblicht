/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.tokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class LoadPrefixes {

    private HashMap<String, Integer> __NONBREAKING_PREFIX = new HashMap<String, Integer>();

    private String lang = "";

    private boolean debug = false;

    public static final String PREFIX_PATH = "/prefixes/";
    public static String PREFIX_FILE_NAME = "nonbreaking_prefix";
    
    /**
     * void constructor
     * need to loadPrefixes
     */
    public LoadPrefixes() {
    }
    /**
     * Constructor with lang as parameter
     * 
     * @param lang the language
     */
    public LoadPrefixes(String lang) {
        this.lang = lang;
       
        
        
    }
    

    /**
     * read the prefix files 
     * @param lang 
     */
    public void readPrefixesAsStream(String lang) {
        BufferedReader reader = null;
        HashMap<String, Integer> prefixmap = new HashMap<String, Integer>();
        String prefix_file_name = PREFIX_PATH + lang + "/" + PREFIX_FILE_NAME;
        if (debug) {
            Logger.getLogger(LoadPrefixes.class.getName()).log(Level.INFO, "Reading " + prefix_file_name);
        }

        //System.err.println("-- "+PREFIX_FILE_NAME);
        try {
            reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(prefix_file_name), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0) {
                    // check if the line start with #
                    if (!line.startsWith("#")) {
                        if (debug) {
                            Logger.getLogger(LoadPrefixes.class.getName()).log(Level.INFO, "Reading valid line " + line);
                        }
                        // check numeral only
                        if (line.contains("#")) {
                            // split line
                            String[] items= new String[2];
                            items=line.split(" ");
                            if (debug) {
                                Logger.getLogger(LoadPrefixes.class.getName()).log(Level.INFO, "Reading numeric only line " + items[0]);
                            }
                            prefixmap.put(items[0], 2);
                        } else {
                            prefixmap.put(line, 1);
                        }
                        
                    }  

                   

                }
            }
        } catch (Exception ex) {
            Logger.getLogger(LoadPrefixes.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(LoadPrefixes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        setNONBREAKING_PREFIX(prefixmap);
    }

    /**
     * @return the __NONBREAKING_PREFIX__
     */
    public HashMap<String, Integer> getNONBREAKING_PREFIX() {
        return __NONBREAKING_PREFIX;
    }

    /**
     * @param prefixmap prefixmap to set
     */
    public void setNONBREAKING_PREFIX(HashMap<String, Integer> prefixmap) {
        this.__NONBREAKING_PREFIX = prefixmap;
    }

    /**
     * @return the lang
     */
    public String getLang() {
        return lang;
    }

    /**
     * @param lang the lang to set
     */
    public void setLang(String lang) {
        this.lang = lang;
    }

}
