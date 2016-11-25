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
public class SentenceSplitter {

    boolean debug = Vars.debug;
    private String lang;
    private HashMap<String, Integer> prefixmap = new HashMap<String, Integer>();

    /**
     *
     * @param lang the language
     * @param map the prefix map
     */
    public SentenceSplitter(String lang, HashMap<String, Integer> map) {
        this.lang = lang;
        this.prefixmap = map;
    }

    public String[] splitSentences(String input_text) {
        String[] sentences = new String[2];
        int s = 0;
        //System.err.println("\t****FT " + input_text);

        // some usefull patterns to avaid tokenization of HTML/XML tags
        String patternString1 = "^<.+>$";
        String patternString2 = "/^\\s*$/";
        //String patternString3 = "^\\s*$";

        String text = "";
        String message = "";
        input_text = input_text.trim();
        if (debug) {
            message = "Init split_sentences for text=-" + text + "- and input_text=-" + input_text + "-";
            Logger.getLogger(SentenceSplitter.class.getName()).log(Level.WARNING, message);
        }
        if (RegexUtils.hasSomeMatches(patternString1, input_text, 0)
                || RegexUtils.hasSomeMatches(patternString2, input_text, 0)) {
            if (debug) {
                message = "First check split_sentences for text=-" + text + "- and input_text=-" + input_text + "-";
                Logger.getLogger(SentenceSplitter.class.getName()).log(Level.WARNING, message);
            }
            text = text + doItFor(text, input_text);
            if (RegexUtils.hasSomeMatches(patternString2, input_text, 0) && !text.isEmpty()) {
                text = text + "<P>\n";
            }
        } else {
            text = text + input_text + " ";
            if (debug) {
                message = "Append the text, with a space=-" + text + "- and input_text=-" + input_text + "-";
                Logger.getLogger(SentenceSplitter.class.getName()).log(Level.WARNING, message);
            }
        }
        String m = doItFor(text, input_text);
        s = m.split("\n").length;
        sentences = new String[s];
        sentences = m.split("\n");

        /*
        sub split_sentences {

	my $input_text = shift(@_);
	chomp($input_text);
	my $text = "";
	if (/^<.+>$/ || /^\s*$/) {
		#time to process this block, we've hit a blank or <p>
		#&do_it_for($text,$input_text);
		#print "<P>\n" if (/^\s*$/ && $text); ##if we have text followed by <P>
		#$text = "";
		$text .= &do_it_for($text,$input_text);
		$text .= "<P>\n" if (/^\s*$/ && $text); ##if we have text followed by <P>
	}
	else {
		#append the text, with a space
		$text .= $input_text. " ";
	}
	$text = &do_it_for($text,$input_text);
	return split("\n", $text);
}

         */
        return sentences;
    }

    private String doItFor(String text, String input_text) {
        String ret = "";
        String message = "";
        if (debug) {
            message = "Before preprocess doItFor text=-" + text + "-";
            Logger.getLogger(SentenceSplitter.class.getName()).log(Level.WARNING, message);
        }
        if (!(text.isEmpty())) {
            ret = preProcess(text);
        }
        if (debug) {
            message = "After preprocess doItFor text=-" + text + "-";
            Logger.getLogger(SentenceSplitter.class.getName()).log(Level.WARNING, message);
        }
        return ret;
    }

    private String preProcess(String text) {
        //String retText = "";
        String[] words;
        int num = 0;
        String prefix = "";
        String message = "";
        String next_word = "";
        String starting_punct = "";
        String patternString1 = "([\\p{IsAlnum}\\.\\-]*)([\\'\\\"\\)\\]\\%\\p{IsPf}]*)(\\.+)$";
        String patternString2 = "(\\.)[\\p{IsUpper}\\-]+(\\.+)$";
        String patternString3 = "^([ ]*[\\'\\\"\\(\\[\\¿\\¡\\p{IsPi}]*[ ]*[\\p{IsUpper}0-9])";

        String patternString4 = "^[0-9]+";
        String patternString5 = "\\n$";

//clean up spaces at head and tail of each line as well as any double-spacing
        /*
    original
    $text =~ s/ +/ /g;
            $text =~ s/\n /\n/g;
            $text =~ s/ \n/\n/g;
            $text =~ s/^ //g;
    $text =~ s/ $//g;
         */
        if (debug) {
            message = "Init preprocess doItFor text=-" + text + "-";
            Logger.getLogger(SentenceSplitter.class.getName()).log(Level.WARNING, message);
        }
        text = text.replaceAll(" +", " ");
        text = text.replaceAll("\n ", "\n");
        text = text.replaceAll(" \n", "\n");
        text = text.replaceAll("^ ", "");
        text = text.replaceAll(" $", "");
        if (debug) {
            message = "Preprocess step 1 doItFor text=-" + text + "-\n";
            Logger.getLogger(SentenceSplitter.class.getName()).log(Level.WARNING, message);
        }
        /*
        #####add sentence breaks as needed#####
	#non-period end of sentence markers (?!) followed by sentence starters.
        
	$text =~ s/([?!]) +([\'\"\(\[\¿\¡\p{IsPi}]*[\w])/$1\n$2/g;
         */
        text = text.replaceAll("([?!]) +([\\'\\\"\\(\\[\\¿\\¡\\p{IsPi}]*[\\w])", "$1\n$2");
        if (debug) {
            message = "Preprocess step 2 doItFor text=-" + text + "-\n";
            Logger.getLogger(SentenceSplitter.class.getName()).log(Level.WARNING, message);
        }

        /*
        #multi-dots followed by sentence starters
        $text =~ s/(\.[\.]+) +([\'\"\(\[\¿\¡\p{IsPi}]*[\p{IsUpper}])/$1\n$2/g;
         */
        text = text.replaceAll("(\\.[\\.]+) +([\\'\\\"\\(\\[\\¿\\¡\\p{IsPi}]*[\\p{IsUpper}])", "$1\n$2");
        if (debug) {
            message = "Preprocess step 3 doItFor text=-" + text + "-\n";
            Logger.getLogger(SentenceSplitter.class.getName()).log(Level.WARNING, message);
        }
        /*
        # add breaks for sentences that end with some sort of punctuation inside a quote or parenthetical and are followed by a possible sentence starter punctuation and upper case
        $text =~ s/([?!\.][\ ]*[\'\"\)\]\p{IsPf}]+) +([\'\"\(\[\¿\¡\p{IsPi}]*[\ ]*[\p{IsUpper}])/$1\n$2/g;
         */
        text = text.replaceAll("([?!\\.][\\ ]*[\\'\\\"\\)\\]\\p{IsPf}]+) +([\\'\\\"\\(\\[\\¿\\¡\\p{IsPi}]*[\\ ]*[\\p{IsUpper}])", "$1\n$2");
        if (debug) {
            message = "Preprocess step 4 doItFor text=-" + text + "-\n";
            Logger.getLogger(SentenceSplitter.class.getName()).log(Level.WARNING, message);
        }
        /*
        # add breaks for sentences that end with some sort of punctuation are followed by a sentence starter punctuation and upper case
        $text =~ s/([?!\.]) +([\'\"\(\[\¿\¡\p{IsPi}]+[\ ]*[\p{IsUpper}])/$1\n$2/g;
         */
        text = text.replaceAll("([?!\\.]) +([\\'\\\"\\(\\[\\¿\\¡\\p{IsPi}]+[\\ ]*[\\p{IsUpper}])", "$1\n$2");
        if (debug) {
            message = "Preprocess step 5 doItFor text=-" + text + "-\n";
            Logger.getLogger(SentenceSplitter.class.getName()).log(Level.WARNING, message);
        }
        // manage words
        num = text.split(" ").length;
        words = new String[num];
        int i = 0;
        words = text.split(" ");
        text = "";
        if (debug) {
            message = "Preprocess step 6 before loop doItFor text=-" + text + "-\n";
            Logger.getLogger(SentenceSplitter.class.getName()).log(Level.WARNING, message);
        }
        for (String word : words) {
            if (i < words.length - 1) {
                next_word = words[i + 1];
            } else {
                next_word = "";
            }

            if (RegexUtils.hasSomeMatches(patternString1, word, 3)) {
                // check if $1 is a known honorific and $2 is empty, never break
                prefix = RegexUtils.getResults()[0];
                starting_punct = RegexUtils.getResults()[1];
                // check haskey
                if (debug) {
                    message = "Preprocess (check if $1 is a known honorific and $2 is empty, never break) in loop for word=-" + word + "- "
                            + "and text=-" + text + "- and prefix=-" + prefix + "- and punct=-" + starting_punct + "-\n";
                    Logger.getLogger(SentenceSplitter.class.getName()).log(Level.WARNING, message);
                }

                boolean hasKey = prefixmap.containsKey(prefix);
                int keyval = -1;
                if (hasKey) {
                    keyval = prefixmap.get(prefix);
                }

                if (!prefix.isEmpty() && keyval == 1 && starting_punct.isEmpty()) {
                    if (debug) {
                        message = "Preprocess (not breaking 1) in loop for word=-" + word + "- and text=-" + text + "- and prefix=-" + prefix + "- and punct=-" + starting_punct + "- and hasKey=-" + hasKey + "- and keyval=" + keyval;
                        Logger.getLogger(SentenceSplitter.class.getName()).log(Level.WARNING, message);
                    }

                } else if (RegexUtils.hasSomeMatches(patternString2, word, 0)) {
                    if (debug) {
                        message = "Preprocess (not breaking 2 - upper case acronym) in loop for word=-" + word + "- and text=-" + text + "- and prefix=-" + prefix + "- and punct=-" + starting_punct + "- and hasKey=-" + hasKey + "- and keyval=" + keyval;
                        Logger.getLogger(SentenceSplitter.class.getName()).log(Level.WARNING, message);
                    }
                } else if (RegexUtils.hasSomeMatches(patternString3, next_word, 1)) {
                    if (debug) {
                        message = "Preprocess (Breaking 1 - (the next word has a bunch of initial quotes, maybe a space, then either upper case or a number) in loop for word=-" + word + "- and text=-" + text + "- and prefix=-" + prefix + "- and punct=-" + starting_punct 
                                + "- and hasKey=-" + hasKey + "- and keyval=" + keyval+"- and next_word=-"+next_word+"-";
                        Logger.getLogger(SentenceSplitter.class.getName()).log(Level.WARNING, message);
                    }
                    // check last word

                    /*
                    unless ($prefix && $NONBREAKING_PREFIX{$prefix} && $NONBREAKING_PREFIX{$prefix} == 2 
                    && !$starting_punct && ($words[$i+1] =~ /^[0-9]+/));
                     */
                    if (!(!prefix.isEmpty() && hasKey && keyval == 2 && starting_punct.isEmpty()
                            && (RegexUtils.hasSomeMatches(patternString4, words[i + 1], 0)))) {
                         if (debug) {
                        message = "Preprocess (Breaking 2 - (we always add a return for these unless we have a numeric non-breaker and a number start) in loop for word=-" + word + "- and text=-" + text + "- and prefix=-" + prefix + "- and punct=-" + starting_punct 
                                + "- and hasKey=-" + hasKey + "- and keyval=" + keyval+"- and next_word=-"+next_word+"-";
                        Logger.getLogger(SentenceSplitter.class.getName()).log(Level.WARNING, message);
                    }
                        word = word + "\n";
                    }

                } else {
                    //System.err.println("ELSE RegexUtils.hasSomeMatches(patternString3, next_word, 1)" + text + "-");
                }
            } else {
                //System.err.println("ELSE RegexUtils.hasSomeMatches(patternString1, word, 3)" + text + "-");
            }
            text = text + word + " ";

            /* 
            for ($i=0;$i<(scalar(@words)-1);$i++) {
		if ($words[$i] =~ /([\p{IsAlnum}\.\-]*)([\'\"\)\]\%\p{IsPf}]*)(\.+)$/) {
			#check if $1 is a known honorific and $2 is empty, never break
			my $prefix = $1;
			my $starting_punct = $2;
			if($prefix && $NONBREAKING_PREFIX{$prefix} && $NONBREAKING_PREFIX{$prefix} == 1 && !$starting_punct) {

				#not breaking;
			} elsif ($words[$i] =~ /(\.)[\p{IsUpper}\-]+(\.+)$/) {
				#not breaking - upper case acronym	
			} elsif($words[$i+1] =~ /^([ ]*[\'\"\(\[\¿\¡\p{IsPi}]*[ ]*[\p{IsUpper}0-9])/) {
				#the next word has a bunch of initial quotes, maybe a space, then either upper case or a number
				$words[$i] = $words[$i]."\n" unless ($prefix && $NONBREAKING_PREFIX{$prefix} && $NONBREAKING_PREFIX{$prefix} == 2 && !$starting_punct && ($words[$i+1] =~ /^[0-9]+/));
				#we always add a return for these unless we have a numeric non-breaker and a number start
			}
			
		}
		$text = $text.$words[$i]." ";
}
             */
            i++;
        }
        text = text.replaceAll(" +", " ");
        text = text.replaceAll("\n ", "\n");
        text = text.replaceAll(" \n", "\n");
        text = text.replaceAll("^ ", "");
        text = text.replaceAll("^ ", "");
        text = text.replaceAll(" $", "");

        if (!(RegexUtils.hasSomeMatches(patternString5, text, 0))) {
            //System.err.println("VBVBVBVBVBVBBBVB "+text);
            text = text + "\n";

        }
        if (debug) {
            message = "End preprocess doItFor text=-" + text + "-";
            Logger.getLogger(SentenceSplitter.class.getName()).log(Level.WARNING, message);
        }

        return text;
    }

}
