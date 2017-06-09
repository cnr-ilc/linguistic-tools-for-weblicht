/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.tokenizer.utils;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class Vars {
    public static boolean debug=false;
    public static String IT="ita";
    public static String EN="eng";
    public static String FR="fra";
    public static String ES="esp";
    public static String DE="deu";
    public static String NL="nld";
    public static String version ="1.0.1";
    public static List<String> langs = Arrays.asList(Vars.DE, Vars.EN, Vars.ES, Vars.FR, Vars.IT, Vars.NL);
    public static String encoding="UTF-8";

} 
