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

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class Tokenizer {

    private String lang;

    private HashMap<String, Integer> prefixmap = new HashMap<String, Integer>();

    // some variables
    private String SUBSTITUTE = "####";

    /**
     *
     * @param lang
     * @param map
     */
    public Tokenizer(String lang, HashMap<String, Integer> map) {
        this.lang = lang;
        this.prefixmap = map;
    }

    public String tokenize(String text) {

        String patternString1 = "DOTMULTI\\.";
        String patternString2 = "^(\\S+)\\.$";
        String patternString3 = "\\.";
        String patternString4 = "\\p{IsAlpha}";
        String patternString5 = "^[\\p{IsLower}]";
        String patternString6 = "^[0-9]+";
        //String patternString7 = "\\p{IsAlpha}";

        String[] words;
        int i = 0;
        String message = "";

        text = text.trim();

        /*
        #tokenize the dashes of the beginning of the lines
	$text =~ s/^\-([^ ])/\- $1/g;
         */
        text = text.replaceAll("^\\-([^ ])", "\\- $1");

        /*
        # turn 0092 into '
	$text =~ s/Ž/\'/g;
         */
        text = text.replaceAll("Ž", "\\'");

        // add single spaces to begin and end of the text
        text = " " + text + " ";
        /*
        # seperate out all "other" special characters
	$text =~ s/([^\p{IsAlnum}\s\.\'\`\,\-\’])/ $1 /g;
         */
        text = text.replaceAll("([^\\p{IsAlnum}\\s\\.\\'\\`\\,\\-\\’])", " $1 ");
        /*
        #multi-dots stay together
	$text =~ s/\.([\.]+)/ DOTMULTI$1/g;
         */
        text = text.replaceAll("\\.([\\.]+)", " DOTMULTI$1");
        /*
        while($text =~ /DOTMULTI\./) {
		$text =~ s/DOTMULTI\.([^\.])/DOTDOTMULTI $1/g;
		$text =~ s/DOTMULTI\./DOTDOTMULTI/g;
	}
         */

        while (RegexUtils.hasSomeMatches(patternString1, text, 0)) {
            text = text.replaceAll("DOTMULTI\\.([^\\.])", "DOTDOTMULTI $1");
            text = text.replaceAll("DOTMULTI\\.", "DOTDOTMULTI");
        }
        //System.err.println("ORIG_TEXT 2 "+text);
        /*
        # seperate out "," except if within numbers (5,300)
	$text =~ s/([^\p{IsN}])[,]([^\p{IsN}])/$1 , $2/g;
         */
        text = text.replaceAll("([^\\p{IsN}])[,]([^\\p{IsN}])", "$1 , $2");
        /*
        # separate , pre and post number
	$text =~ s/([\p{IsN}])[,]([^\p{IsN}])/$1 , $2/g;
	$text =~ s/([^\p{IsN}])[,]([\p{IsN}])/$1 , $2/g;
         */
        text = text.replaceAll("([\\p{IsN}])[,]([^\\p{IsN}])", "$1 , $2");
        text = text.replaceAll("([^\\p{IsN}])[,]([\\p{IsN}])", "$1 , $2");

        /*
        # turn `into '
	$text =~ s/\`/\'$SUBSTITUTE/g;

	#turn '' into "
	$text =~ s/\'\'/ \"$SUBSTITUTE /g;
         */
        text = text.replaceAll("\\`", "\\'" + SUBSTITUTE);
        text = text.replaceAll("\\'\\'", " \\\"" + SUBSTITUTE + " ");
        /*
        #tokenize the words like '05-'06
	$text =~ s/(['|’])([0-9][0-9])\-(['|’])([0-9][0-9])/$1$2 - $3$4/g;
	#replace the ' with ### to don't tokenize words like '90
	$text =~ s/ ['|’]([0-9][0-9])/ ###$1/g;
         */
        text = text.replaceAll("(['|’])([0-9][0-9])\\-(['|’])([0-9][0-9])", "$1$2 - $3$4");
        text = text.replaceAll(" ['|’]([0-9][0-9])", " ###$1");

        /*
        Italian ONLY at this revision
        if ($LANGUAGE eq "it") {
		#split contractions left
		$text =~ s/([^\p{IsAlpha}])(['|’])([^\p{IsAlpha}])/$1 $2 $3/g;
		$text =~ s/([^\p{IsAlpha}])(['|’])([\p{IsAlpha}])/$1 $2 $3/g;
		$text =~ s/([\p{IsAlpha}])(['|’])([^\p{IsAlpha}])/$1 $2 $3/g;
		$text =~ s/([\p{IsAlpha}])(['|’])([\p{IsAlpha}])/$1$2 $3/g;
		$text =~ s/([^\p{IsAlpha}\p{IsN}]po) (['|’])([^\p{IsAlpha}])/$1$2 $3/g; # rule for "po'"
	} else {
		$text =~ s/\'/ \' /g;
	}
         */
        if (lang.equalsIgnoreCase(Vars.EN)) {
            /*
            $text =~ s/([^\p{IsAlpha}])(['|’])([^\p{IsAlpha}])/$1 $2 $3/g;
		$text =~ s/([^\p{IsAlpha}\p{IsN}])(['|’])([\p{IsAlpha}])/$1 $2 $3/g;
		$text =~ s/([\p{IsAlpha}])(['|’])([^\p{IsAlpha}])/$1 $2 $3/g;
		$text =~ s/([\p{IsAlpha}])(['|’])([\p{IsAlpha}])/$1 $2$3/g;
		#special case for "1990's"
		$text =~ s/([\p{IsN}])(['|’])([s])/$1 $2$3/g;
             */
            text = text.replaceAll("([^\\p{IsAlpha}])(['|’])([^\\p{IsAlpha}])", "$1 $2 $3");
            text = text.replaceAll("([^\\p{IsAlpha}\\p{IsN}])(['|’])([\\p{IsAlpha}])", "$1 $2 $3");
            text = text.replaceAll("([\\p{IsAlpha}])(['|’])([^\\p{IsAlpha}])", "$1 $2 $3");
            text = text.replaceAll("([\\p{IsAlpha}])(['|’])([\\p{IsAlpha}])", "$1 $2$3");
            // special case for "1990's"
            text = text.replaceAll("([\\p{IsN}])(['|’])([s])", "$1 $2$3");
        } else if (lang.equalsIgnoreCase(Vars.IT)) {
            text = text.replaceAll("([^\\p{IsAlpha}])(['|’])([^\\p{IsAlpha}])", "$1 $2 $3");
            text = text.replaceAll("([^\\p{IsAlpha}])(['|’])([\\p{IsAlpha}])", "$1 $2 $3");
            text = text.replaceAll("([\\p{IsAlpha}])(['|’])([^\\p{IsAlpha}])", "$1 $2 $3");
            text = text.replaceAll("([\\p{IsAlpha}])(['|’])([\\p{IsAlpha}])", "$1$2 $3");
            text = text.replaceAll("([^\\p{IsAlpha}\\p{IsN}]po) (['|’])([^\\p{IsAlpha}])", "$1$2 $3"); // rule for po'

        } else if (lang.equalsIgnoreCase(Vars.FR)) {
            /*
            #split contractions left
		$text =~ s/([^\p{IsAlpha}])(['|’])([^\p{IsAlpha}])/$1 $2 $3/g;
		$text =~ s/([^\p{IsAlpha}])(['|’])([\p{IsAlpha}])/$1 $2 $3/g;
		$text =~ s/([\p{IsAlpha}])(['|’])([^\p{IsAlpha}])/$1 $2 $3/g;
		$text =~ s/([\p{IsAlpha}])(['|’])([\p{IsAlpha}])/$1$2 $3/g;
             */
            text = text.replaceAll("([^\\p{IsAlpha}])(['|’])([^\\p{IsAlpha}])","$1 $2 $3");
            text = text.replaceAll("([^\\p{IsAlpha}])(['|’])([\\p{IsAlpha}])","$1 $2 $3");
            text = text.replaceAll("([\\p{IsAlpha}])(['|’])([^\\p{IsAlpha}])","$1 $2 $3");
            text = text.replaceAll("([\\p{IsAlpha}])(['|’])([\\p{IsAlpha}])","$1$2 $3");
        } else {
            text = text.replaceAll("/\\'", " \\' ");
        }

        /*
        #replace the ### with ' to tokenize words like '90
	$text =~ s/ ###([0-9][0-9])/ '$1/g;
         */
        text = text.replaceAll(" ###([0-9][0-9])", " '$1");

        /*
        #word token method
	my @words = split(/\s/,$text);
	$text = "";
	for (my $i=0;$i<(scalar(@words));$i++) {
		my $word = $words[$i];
		if ( $word =~ /^(\S+)\.$/) {
			my $pre = $1;
			if (($pre =~ /\./ && $pre =~ /\p{IsAlpha}/) || ($NONBREAKING_PREFIX{$pre} && $NONBREAKING_PREFIX{$pre}==1) || ($i<scalar(@words)-1 && ($words[$i+1] =~ /^[\p{IsLower}]/))) {
				#no change
			} elsif (($NONBREAKING_PREFIX{$pre} && $NONBREAKING_PREFIX{$pre}==2) && ($i<scalar(@words)-1 && ($words[$i+1] =~ /^[0-9]+/))) {
				#no change
			} else {
				$word = $pre." .";
			}
		}
		$text .= $word." ";
	}
         */
        //words = new String[text.split("\\s").length];
        words = text.split("\\s");
        text = "";
        String pre = "";

        for (String word : words) {
            if (RegexUtils.hasSomeMatches(patternString2, word, 1)) {
                pre = RegexUtils.getResults()[0];
                // check haskey
                boolean hasKey = prefixmap.containsKey(pre);
                int keyval = -1;
                if (hasKey) {
                    keyval = prefixmap.get(pre);
                }
                if ((RegexUtils.hasSomeMatches(patternString3, pre, 0) && RegexUtils.hasSomeMatches(patternString4, pre, 0))
                        || (hasKey && keyval == 1) || (i < words.length - 1 && RegexUtils.hasSomeMatches(patternString5, words[i + 1], 0))) {
                    if (Vars.debug) {
                        message = "found3+4+5 NOCHANGE " + " hasKey=" + hasKey + " keyval=" + keyval
                                + " pre=" + pre + " word=" + word + " next_word=" + words[i + 1];
                        Logger.getLogger(TextFixer.class.getName()).log(Level.WARNING, message);
                    }
                } else if ((hasKey && keyval == 1) || (i < words.length - 1 && RegexUtils.hasSomeMatches(patternString6, words[i + 1], 0))) {
                    if (Vars.debug) {
                        message = "found3+4+5 NOCHANGEELSEIF " + " hasKey=" + hasKey + " keyval=" + keyval
                                + " pre=" + pre + " word=" + word + " next_word=" + words[i + 1];
                        Logger.getLogger(TextFixer.class.getName()).log(Level.WARNING, message);
                    }
                } else {
                    word = pre + " .";
                    if (Vars.debug) {
                        message = "found3+4+5 *CHANGE* " + " hasKey=" + hasKey + " keyval=" + keyval
                                + " pre=" + pre + " word=" + word;//+ " next_word=" + words[i + 1];
                        Logger.getLogger(TextFixer.class.getName()).log(Level.WARNING, message);
                    }
                }
            }
            i++;
            text = text + word + " ";
            //System.err.println("\t\tNT -" + text + "-");
        }
        /*
        # clean up extraneous spaces
	$text =~ s/ +/ /g;
	$text =~ s/^ //g;
	$text =~ s/ $//g;

	#restore multi-dots
	while($text =~ /DOTDOTMULTI/) {
		$text =~ s/DOTDOTMULTI/DOTMULTI./g;
	}
	$text =~ s/DOTMULTI/./g;
         */
        text = text.replaceAll(" +", " ");
        text = text.replaceAll("^ ", "");
        text = text.replaceAll(" $", "");
        /*
        #restore multi-dots
	while($text =~ /DOTDOTMULTI/) {
		$text =~ s/DOTDOTMULTI/DOTMULTI./g;
	}
	$text =~ s/DOTMULTI/./g;
         */
        //System.err.println("\t\tNT -" + text + "-");
        while (RegexUtils.hasSomeMatches("DOTDOTMULTI", text, 0)) {
            //System.err.println("\t\t\tNT -" + text + "-");
            text = text.replaceAll("DOTDOTMULTI", "DOTMULTI.");
            //System.err.println("\t\t\t\tNT -" + text + "-");
        }
        text = text.replaceAll("DOTMULTI", ".");
        if (Vars.debug) {
            message = "Returning text=-" + text + "-";
            Logger.getLogger(TextFixer.class.getName()).log(Level.WARNING, message);
        }
// TODO URL
        return text;
    }

}
