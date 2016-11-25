/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.tokenizer;

import it.cnr.ilc.tokenizer.utils.RegexUtils;
import it.cnr.ilc.tokenizer.utils.Vars;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class TextFixer {

    private String lang;
    boolean debug = Vars.debug;
    private HashMap<String, Integer> prefixmap = new HashMap<String, Integer>();
    private final String START_QUOTES_REGEX = "“|‘|«|‹";
    private final String END_QUOTES_REGEX = "”|’|»|›";

    public TextFixer(String lang, HashMap<String, Integer> map) {
        this.lang = lang;
        this.prefixmap = map;
    }

    /**
     *
     * @param the_text
     * @return
     */
    public String fixText(String the_text) {
        String text;
        String retText = "";
        String[] words;//= new String[];
        int dim = 0;
        Pattern p;
        Matcher m;
        String pre = "";
        String quote = "";
        String post = "";
        String next_word = "";
        
        String message = "";
        // trimming
        text = the_text.trim();

        // fix some encoding
        text = fixEncoding(text);

        // splitting into pieces acco5rding to single space
        dim = text.split("\\s").length;
        words = new String[dim];
        words = text.split("\\s");

        // patterns
        String patternString1 = "^(\\S+)\\.(" + END_QUOTES_REGEX + ")(" + START_QUOTES_REGEX + "*\\p{IsUpper}\\S*)$";
        String patternString2 = "^(\\S+)\\.(" + END_QUOTES_REGEX + ")$";
        String patternString3 = "^" + START_QUOTES_REGEX + "*\\p{IsUpper}\\S*$";
        String patternString4 = "^(\\S+)\\.(\\S+)$";
        String patternString5 = "\\.";
        String patternString6 = "\\p{IsAlpha}";
        String patternString7 = "^[\\p{IsLower}]";
        String patternString8 = "^[0-9]+";
        String patternString9 = "^\\.(\\p{IsUpper}\\S+)$";
        String patterString10 = "^(\\S+)$";

        /* // check again
        dim=text.split("\\s+").length;
        words=new String[dim];
        words=text.split("\\s+");
         */
        // loop over words
        int i = 0;
        for (String word : words) {

            // fix first rule “bla bla bla.”Bla bla -> “bla bla bla”. Bla bla
            if (RegexUtils.hasSomeMatches(patternString1, word, 3)) { // F1R
                pre = RegexUtils.getResults()[0];
                quote = RegexUtils.getResults()[1];
                post = RegexUtils.getResults()[2];
                word = pre + quote + ". " + post;
                if (debug) {
                    message = "found1 " + "pre=" + pre + " quote=" + quote + " post=" + post + " word=" + word;
                    Logger.getLogger(TextFixer.class.getName()).log(Level.WARNING, message);
                }

            } else if (RegexUtils.hasSomeMatches(patternString2, word, 2)) { //S2E
                // fix second rule “bla bla bla.” Bla bla -> “bla bla bla”. Bla bla
                pre = RegexUtils.getResults()[0];
                quote = RegexUtils.getResults()[1];
                if (debug) {
                    message = "found2 " + "pre=" + pre + " quote=" + quote + " word=" + word;
                    Logger.getLogger(TextFixer.class.getName()).log(Level.WARNING, message);
                }

                if (i < words.length - 1) {
                    next_word = words[i + 1];
                }
                if ((i < words.length - 1) && RegexUtils.hasSomeMatches(patternString3, next_word, 0)) {
                    word = pre + quote + ".";
                    if (debug) {
                        message = "found3IF " + "pre=" + pre + " quote=" + quote + " word=" + word + " and netx_word=" + next_word;
                        Logger.getLogger(TextFixer.class.getName()).log(Level.WARNING, message);
                    }

                } else if (i == words.length - 1) {
                    word = pre + quote + ".";
                    if (debug) {
                        message = "found3ELSE " + "pre=" + pre + " post=" + post + " word=" + word;
                        Logger.getLogger(TextFixer.class.getName()).log(Level.WARNING, message);
                    }

                }

            } else if (RegexUtils.hasSomeMatches(patternString4, word, 2)) { // F4R
                pre = RegexUtils.getResults()[0];
                post = RegexUtils.getResults()[1];
                if (debug) {
                    message = "found4 " + "pre=" + pre + " post=" + post + " word=" + word;
                    Logger.getLogger(TextFixer.class.getName()).log(Level.WARNING, message);
                }

                // check haskey
                boolean hasKey = prefixmap.containsKey(pre);
                int keyval = -1;
                if (hasKey) {
                    keyval = prefixmap.get(pre);
                }
                if ((RegexUtils.hasSomeMatches(patternString5, pre, 0) && RegexUtils.hasSomeMatches(patternString6, pre, 0)) || (hasKey && keyval == 1) || RegexUtils.hasSomeMatches(patternString7, post, 0)) {

                    if (debug) {
                        message = "found4+5+6+7 NOCHANGE " + " hasKey=" + hasKey + " keyval=" + keyval
                                + " pre=" + pre + " post=" + post + " word=" + word;
                        Logger.getLogger(TextFixer.class.getName()).log(Level.WARNING, message);
                    }

                } else if ((hasKey && keyval == 2) && (RegexUtils.hasSomeMatches(patternString8, post, 0))) {
                    if (debug) {
                        message = "found4+5+6+7+8 NOCHANGE " + " hasKey=" + hasKey + " keyval=" + keyval
                                + " pre=" + pre + " post=" + post + " word=" + word;
                        Logger.getLogger(TextFixer.class.getName()).log(Level.WARNING, message);
                    }

                } else {
                    word = pre + ". " + post;
                    if (debug) {
                        message = "found4+5+6+7+8 CHANGE " + " hasKey=" + hasKey + " keyval=" + keyval
                                + " pre=" + pre + " post=" + post + " word=" + word;
                        Logger.getLogger(TextFixer.class.getName()).log(Level.WARNING, message);
                    }

                }

            } else if (RegexUtils.hasSomeMatches(patternString9, word, 1)) {
                post = RegexUtils.getResults()[0];
                if (debug) {
                    message = "found9 " + " post=" + post + " word=" + word;
                    Logger.getLogger(TextFixer.class.getName()).log(Level.WARNING, message);
                }
                //System.out.println("found9 " + " post=" + post + " word=" + word);
                if (i > 0 && RegexUtils.hasSomeMatches(patterString10, words[i - 1], 0)) {
                    word = ". " + post;
                    if (debug) {
                        message = "found10 " + " post=" + post + " word=" + word + " prev_word=" + words[i - 1] + "- ";
                        Logger.getLogger(TextFixer.class.getName()).log(Level.WARNING, message);
                    }

                }
            }

            i++;
            retText = retText + word + " ";
        }
        return retText;
    }

    public String fixEncoding(String text) {

        // ############################################ //
        text = text.replaceAll("â\u0080\u0099", "'");
        text = text.replaceAll("Ã\u0080", "À");
        text = text.replaceAll("\u0093", "“");
        text = text.replaceAll("\u0019", "");
        text = text.replaceAll("\u0013", "\"");

        // ############################################ //
        text = text.replaceAll("â€¦", "…"); // elipsis
        text = text.replaceAll("â\u0080¦", "…"); // elipsis
        text = text.replaceAll("â€“", "–"); // long hyphen
        text = text.replaceAll("â€™", "’"); //curly apostrophe
        text = text.replaceAll("â€œ", "“"); // curly open quote
        text = text.replaceAll("â€", "”"); // curly close quote
        text = text.replaceAll("Â»", "»");
        text = text.replaceAll("Â«", "«");

        // ############################################ //
        text = text.replaceAll("Ã¡", "á");
        text = text.replaceAll("Ã©", "é");
        text = text.replaceAll("Ã\\*", "í");
        text = text.replaceAll("Ã³", "ó");
        text = text.replaceAll("Ãº", "ú");

        // ############################################ //
        text = text.replaceAll("Ã\u0081", "Á");
        text = text.replaceAll("Ã‰", "É");
        text = text.replaceAll("Ã\u008D", "Í");
        text = text.replaceAll("Ã“", "Ó");
        text = text.replaceAll("Ãš", "Ú");

        // ############################################ //
        text = text.replaceAll("Ã±", "ñ");
        text = text.replaceAll("Ã§", "ç");
        text = text.replaceAll("Å“", "œ");

        text = text.replaceAll("Ã‘", "Ñ");
        text = text.replaceAll("Ã‡", "Ç");
        text = text.replaceAll("Å’", "Œ");

        // ############################################ //
        text = text.replaceAll("Â©", "©");
        text = text.replaceAll("Â®", "®");
        text = text.replaceAll("â„¢", "™");
        text = text.replaceAll("Ã˜", "Ø");
        text = text.replaceAll("Âª", "ª");

        // ############################################ //
        text = text.replaceAll("Ã¤", "ä");
        text = text.replaceAll("Ã«", "ë");
        text = text.replaceAll("Ã¯", "ï");
        text = text.replaceAll("Ã¶", "ö");
        text = text.replaceAll("Ã¼", "ü");

        text = text.replaceAll("Ã„", "Ä");
        text = text.replaceAll("Ã‹", "Ë");
        text = text.replaceAll("Ã\u008F ", "Ï");
        text = text.replaceAll("Ã– ", "Ö");
        text = text.replaceAll("Ãœ", "Ü");

        // ############################################ //
        text = text.replaceAll("Ã ", "à");
        text = text.replaceAll("Ã¨", "è");
        text = text.replaceAll("Ã¬", "ì");
        text = text.replaceAll("Ã²", "ò");
        text = text.replaceAll("Ã¹", "ù");

        text = text.replaceAll("Ã€", "À");
        text = text.replaceAll("Ãˆ", "È");
        text = text.replaceAll("ÃŒ", "Ì");
        text = text.replaceAll("Ã’", "Ò");
        text = text.replaceAll("Ã™", "Ù");

        // ############################################ //
        text = text.replaceAll("Ã¢", "â");
        text = text.replaceAll("Ãª", "ê");
        text = text.replaceAll("Ã®", "î");
        text = text.replaceAll("Ã´", "ô");
        text = text.replaceAll("Ã»", "û");

        text = text.replaceAll("Ã‚", "Â");
        text = text.replaceAll("ÃŠ", "Ê");
        text = text.replaceAll("ÃŽ", "Î");
        text = text.replaceAll("Ã”", "Ô");
        text = text.replaceAll("Ã›", "Û");
        // ############################################ //	
        //System.err.println(text);
        return text;
    }

}
