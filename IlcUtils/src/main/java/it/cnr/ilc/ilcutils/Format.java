/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.ilcutils;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class Format {
    public static String OUT_KAF="kaf";
    public static String OUT_TCF="tcf";
    public static String OUT_TAB="tab";
    public static String SEP="\t";
    
   /**
     * formats for output as TAB which is the default
     */
    public static String SERVICE_OUT_TAG = "tagged"; // 
    
    /**
     * formats for output as SPLIT which is the default
     */
    public static String SERVICE_OUT_SPLIT = "splitted"; // 
    
    /**
     * formats for output as TOKEN which is the default
     */
    public static String SERVICE_OUT_TOK = "token"; // 
  
   
    
    /**
     * formats for output as TOKEN which is the default
     */
    public static String FIND_NER = "none"; // 
    
    /**
     * formats for output as TOKEN which is the default
     */
    public static String FIND_MW = "false"; // 
    
    public static List<String> serviceFormats = Arrays.asList(SERVICE_OUT_SPLIT, SERVICE_OUT_TOK, SERVICE_OUT_TAG);
    
}
