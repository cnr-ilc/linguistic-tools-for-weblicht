/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.ilcutils;

import java.util.Arrays;
import java.util.List;

/**
 * Variables. TODO: read properties from file
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class Vars {

    public static String version = "1.0";
    public static boolean debug = false;
    public static String OF_KAF = "kaf";
    public static String OF_TCF = "tcf";
    public static String OF_JSON = "json";
    public static String OF_TAB = "tab";

    public static String IF_KAF = "kaf";
    public static String IF_RAW = "raw";

    public static String ITA = "ita";
    public static String ENG = "eng";
    public static String FRA = "fra";
    public static String ESP = "esp";
    public static String DEU = "deu";
    public static String NLD = "nld";
    public static String IT = "it";
    public static String EN = "en";
    public static String FR = "fr";
    public static String ES = "es";
    public static String DE = "de";
    public static String NL = "nl";

    public static String SERVICE_DESR = "desr";
    public static String SERVICE_FR2CONLL_IT = "fc_freeling_text_2_conll_it";
    public static String FREELING_IT = "freeling_it";

    public static String OF_DEF = "tabbed";
    public static List<String> panaceaServices = Arrays.asList(Vars.SERVICE_DESR, Vars.SERVICE_FR2CONLL_IT, Vars.FREELING_IT);
    public static List<String> panaceaFormats = Arrays.asList(Vars.OF_DEF, Vars.OF_TCF, Vars.OF_KAF);
    public static List<String> openerFormats = Arrays.asList(Vars.OF_TCF, Vars.OF_KAF, Vars.OF_JSON);

    public static List<String> iFormats = Arrays.asList(Vars.IF_KAF, Vars.IF_RAW);
    public static String encoding = "UTF-8";
}
