/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.tokenizer.utils;

import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class RegexUtils {

    /**
     * @return the results
     */
    public static String[] getResults() {
        return results;
    }

    /**
     * @param aResults the results to set
     */
    public static void setResults(String[] aResults) {
        results = aResults;
    }

    private final String pre = "";
    private final String quote = "";
    private final String post = "";

    private static String[] results;

    /**
     *
     * @param regex the regular expression to compile the pattern
     * @param word the word to match on
     * @param numgroups the number of groups from matches
     * @return true if found false otherwise
     */
    public static boolean hasSomeMatches(String regex, String word, int numgroups) {
        boolean found = false;
        Pattern p;
        Matcher m;
        p = Pattern.compile(regex);
        m = p.matcher(word);

        found = m.find();
        if (found) {
            if (numgroups > 0) {
                String[] temp = new String[numgroups];
                for (int i = 0; i < numgroups; i++) {
                    temp[i] = m.group(i + 1);
                    //System.err.println("CCCC "+i+" "+m.group(i+1));
                }
                setResults(temp);
            }
        }
        return found;
    }

}
