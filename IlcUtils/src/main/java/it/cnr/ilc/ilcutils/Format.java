/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.ilcutils;

import java.util.Arrays;
import java.util.List;

/**
 * Some properties. TODO: read properties from file
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class Format {

    public static String SEP = "\t";

// Properties for for panacea services //
    // output formats //
    public static String OUT_KAF = "kaf";
    public static String OUT_TCF = "tcf";
    public static String OUT_TAB = "tab";

    // Freeling specific output formats. These are the output of the services. No connection with the output
    /**
     * formats for output as TAB which is the default
     */
    public static String PANACEA_SERVICE_OUT_TAG = "tagged"; // 

    /**
     * format for output as SPLIT
     */
    public static String PANACEA_SERVICE_OUT_SPLIT = "splitted"; // 

    /**
     * format for output as TOKEN
     */
    public static String PANACEA_SERVICE_OUT_TOK = "token"; // 
    
    /**
     * list of freeling panacea service outputs 
     */
    public static List<String> serviceFormats = Arrays.asList(PANACEA_SERVICE_OUT_SPLIT, PANACEA_SERVICE_OUT_TOK, PANACEA_SERVICE_OUT_TAG);

    // other properties 
    public static String PANACEA_FIND_NER = "none"; //
    public static String PANACEA_FIND_MW = "false"; //

    // Properties for OpeNer
    public static String OPENER_SERVICE_OUT_TAG = "kaf"; // 
    public static String OPENER_SERVICE_IN_TAG = "kaf"; // 

    /**
     * formats for output as TOKEN which is the default
     */
    
    public static List<String> iFormats = Arrays.asList(Vars.IF_KAF, Vars.IF_RAW);

}
