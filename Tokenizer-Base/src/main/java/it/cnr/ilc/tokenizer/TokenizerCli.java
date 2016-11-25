/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.tokenizer;

import it.cnr.ilc.tokenizer.types.LinguisticProcessor;
import it.cnr.ilc.tokenizer.types.Result;
import it.cnr.ilc.tokenizer.types.Sentence;
import it.cnr.ilc.tokenizer.types.Token;
import it.cnr.ilc.tokenizer.utils.RegexUtils;
import it.cnr.ilc.tokenizer.utils.Vars;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class TokenizerCli {

    private static boolean debug = Vars.debug;
    private static String[] sentences;
    // some variables
    private static String SUBSTITUTE = "####";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Timestamp timestamp;// = new Timestamp(System.currentTimeMillis());

   

    public Result run(String lang, String text) {
        /*
        For the result
         */
        Result obj = new Result();
        List<Sentence> theSentences = new ArrayList<>();
        Sentence theSentence;
        List<Token> theTokens;
        Token theToken;
        String message = "";
        String fixedText = "";
        int index = 0;
        int last_index = 0;
        int last_offset = -1;
        int j = 0;
        int sent = 1;
        int para = 1;
        int counter = 0;
        int charcount = 0;
        int offset = 0;
        String[] tokens;
        int wfid = 1;
        LinguisticProcessor lp = new LinguisticProcessor();
        String ts="";
        lp.setLayer("text");
//        String text = "Mi chiamo Riccardo. Abito a cascina. Art.1 - â Ã  -// Ã* â€¦ Ã Ã± -";
//        text = "“bla bla F1R.”F1R bla “bla bla SR.”  A.B “bla bla TR.”";
//        text = "“bla bla F1R.”F1R  OpeNER is amazing .OpeNER is cool bla “bla bla SR.” ABB.A <P>SS</P>";
//        text = "Mi chiamo Riccardo. Mr. Del Gratta..... Dove abiti? Abito a Cascina! Voi di dove siete? ....Di Padova: c.c.p. e che cavolo!";
//        text = "Mr. Del Gratta..... Abito al 5,5, e sono nato nel '71. Anche '05-'06";
//        text = "Mr. Mr. Del Gratta..... ”Abito al '71........” E quindi? 120 Kg peso..... La mia email è riccardo.delgratta@ilc.cnr.it";
        //text="Mr.. Del Gratta";

        // some usefull patterns to avaid tokenization of HTML/XML tags
        String patternString1 = "^<.+>$";
        String patternString2 = "/^\\s*$/";
        if (debug) {
            message = "Initializing " + LoadPrefixes.class.getName() + " for " + lang;
            Logger.getLogger(TokenizerCli.class.getName()).log(Level.INFO, message);
        }
        LoadPrefixes loadprefixes = new LoadPrefixes(lang);
        if (debug) {
            message = "Loading prefixes " + LoadPrefixes.class.getName() + " for " + lang;
            Logger.getLogger(TokenizerCli.class.getName()).log(Level.INFO, message);
        }
        loadprefixes.readPrefixesAsStream(lang);

        if (debug) {
            message = "Initializing " + TextFixer.class.getName() + " for " + lang;
            Logger.getLogger(TokenizerCli.class.getName()).log(Level.INFO, message);
        }
        TextFixer textFixer = new TextFixer(lang, loadprefixes.getNONBREAKING_PREFIX());
        if (RegexUtils.hasSomeMatches(patternString1, text, 0) || RegexUtils.hasSomeMatches(patternString2, text, 0)) {
            if (debug) {
                message = "Containing Tags " + TokenizerCli.class.getName() + " for " + lang;
                Logger.getLogger(TokenizerCli.class.getName()).log(Level.INFO, message);
            }
        } else {
            if (debug) {
                message = "Fixing Text " + TextFixer.class.getName() + " for " + lang;
                Logger.getLogger(TokenizerCli.class.getName()).log(Level.INFO, message);
            }
            fixedText = textFixer.fixText(text);
            if (debug) {
                message = "Original Text\t" + text;
                Logger.getLogger(TokenizerCli.class.getName()).log(Level.WARNING, message);
                message = "Fixed Text\t" + fixedText;
                Logger.getLogger(TokenizerCli.class.getName()).log(Level.WARNING, message);
            }
            if (debug) {
                message = "Initializing " + SentenceSplitter.class.getName() + " for " + lang;
                Logger.getLogger(TokenizerCli.class.getName()).log(Level.INFO, message);
            }
            timestamp = new Timestamp(System.currentTimeMillis());
            ts=SentenceSplitter.class.getName()+"-"+lang+"#"+timestamp.toString().replaceAll(" ", "T")+"Z";
            SentenceSplitter sentenceSplitter = new SentenceSplitter(lang, loadprefixes.getNONBREAKING_PREFIX());
            lp.getLps().add(ts);

            if (debug) {
                message = "Initializing " + Tokenizer.class.getName() + " for " + lang;
                Logger.getLogger(TokenizerCli.class.getName()).log(Level.INFO, message);
            }
            Tokenizer tokenizer = new Tokenizer(lang, loadprefixes.getNONBREAKING_PREFIX());

            sentences = sentenceSplitter.splitSentences(fixedText);
            timestamp = new Timestamp(System.currentTimeMillis());
            ts=Tokenizer.class.getName()+"-"+lang+"#"+timestamp.toString().replaceAll(" ", "T")+"Z";
            
            lp.getLps().add(ts);

            //
            if (debug) {
                message = "Starting tokenization in sentences " + Tokenizer.class.getName() + " for " + lang;
                Logger.getLogger(TokenizerCli.class.getName()).log(Level.INFO, message);
            }
            for (String s : sentences) {
                // new sentence
                theSentence = new Sentence();
                theSentence.setStart_offset(last_offset + 1);

                // new list of tokens
                theTokens = new ArrayList<>();
                String tok = "";
                if (debug) {
                    message = "Tokenizing  sentence=-" + s + "-";
                    Logger.getLogger(TokenizerCli.class.getName()).log(Level.INFO, message);
                }
                tok = tokenizer.tokenize(s);
                // fix some codes
                tok = tok.replaceAll("([\u0093|\u0094|\u0085|\u0097|\u0096|«|»])([^ ])", "$1 $2");
                tok = tok.replaceAll("([^ ])([\u0093|\u0094|\u0085|\u0097|\u0096|«|»])", "$1 $2");
                tok = tok.replaceAll(" @ ", "@");
                tok = tok.replaceAll("([DLNO]) '", "$1'");

                //System.err.println("TOKS " + tok);
                /*
                $tok =~ s/o( )?'( )?clock/o'clock/g;
      $tok =~ s/ ' ([0-9][0-9]s)/ '$1/g;
      #detokenize some time formats
      $tok =~ s/([0-9][0-9]*) ' ([0-9][0-9]*) "/$1'$2"/g;
      $tok =~ s/([0-9][0-9]*) : ([0-9][0-9])/$1:$2/g;
      #detokenize some height formats
      $tok =~ s/([0-9][0-9]*) ' ([0-9][0-9])/$1'$2/g;
      #tokenize two dashes
      $tok =~ s/\-\-/ \-\-/g;
      #correct ºC tokenization
      $tok =~ s/([0-9])( )?º( )?C/$1 ºC/g;
      $tok =~ s/ +/ /g;
                 */
                tok = tok.replaceAll("o( )?'( )?clock", "o'clock'");
                tok = tok.replaceAll(" ' ([0-9][0-9]s)", " '$1");
                tok = tok.replaceAll("([0-9][0-9]*) ' ([0-9][0-9]*) \"", "$1'$2");
                tok = tok.replaceAll("([0-9][0-9]*) : ([0-9][0-9])", "$1:$2");
                tok = tok.replaceAll("([0-9][0-9]*) ' ([0-9][0-9])", "$1'$2");

                tok = tok.replaceAll("\\-\\-", " \\-\\-");
                tok = tok.replaceAll("([0-9])( )?º( )?C", "$1 ºC");
                tok = tok.replaceAll(" +", " ");

                tokens = tok.split(" ");
                int i = 0;
                for (String token : tokens) {
                    String from = "from=-";
                    //System.err.println("TOKEN " + token);
                    //index = tok.indexOf(token, last_index);
                    /*
                    #if token was substituted at tokenization, be careful
                    if ( $token eq "\"".$SUBSTITUTE ) {
                      $index = index($_, "'", $last_index);
                      $token = "\"";
                    }
                    elsif ( $token eq "\'".$SUBSTITUTE ) {
                      $index = index($_, "`", $last_index);
                      $token = "\'";
                     }
                     */
                    // new token
                    theToken = new Token();
                    if (token.equals("\"" + SUBSTITUTE)) {
                        index = text.indexOf("'", last_index);
                        token = "\"";
                        if (debug) {
                            from = from + "1- ";
                            message = "Index 1=" + index + " for token=-" + token + "- in -" + tok + " with li=-" + last_index + "- and offset=-" + offset + "- and charcount=-" + charcount + "- with text=-" + text + "-";
                            Logger.getLogger(TokenizerCli.class.getName()).log(Level.INFO, message);
                        }

                    } else if (token.equals("\"" + SUBSTITUTE)) {
                        index = text.indexOf("`", last_index);
                        token = "\'";
                        if (debug) {
                            from = from + "2- ";
                            message = "Index 2=" + index + " for token=-" + token + "- in -" + tok + " with li=-" + last_index + "- and offset=-" + offset + "- and charcount=-" + charcount + "- with text=-" + text + "-";
                            Logger.getLogger(TokenizerCli.class.getName()).log(Level.INFO, message);
                        }

                    }
                    offset = charcount + index;

                    /*if input text has been preprocesed and tokens has been moved ( 'hello.' => 'hello'.),
         offset of the "." char is at the left of "'" char not at the right
                     */
                    if (index == -1) {
                        index = text.indexOf(token, last_index - 2);
                        offset = charcount + index;
                        if (debug) {
                            from = from + "3- ";
                            message = "Index 3=" + index + " for token=-" + token + "- in -" + tok + " with li=-" + last_index + "- and offset=-" + offset + "- and charcount=-" + charcount + "- with text=-" + text + "-";
                            Logger.getLogger(TokenizerCli.class.getName()).log(Level.INFO, message);
                        }

                    } else if (i < tokens.length - 1) {/*make sure that found offset is not an offset of the same char at other position so,
	# find offset of the next token and compare
	# next token is at the same sentence
                         */
                        if (debug) {
                            from = from + "4- ";
                            message = "Index 4=" + index + " for token=-" + token + "- in -" + tok + " with li=-" + last_index + "- and offset=-" + offset + "- and charcount=-" + charcount + "- with text=-" + text + "-";
                            Logger.getLogger(TokenizerCli.class.getName()).log(Level.INFO, message);
                        }

                        int next_token_index = charcount + text.indexOf(tokens[i + 1], token.length() + last_index - 1);
                        //System.err.println("\n\n\nXXXX -"+tok+"- XXXX \n\n\n");

                        if (debug) {
                            from = from + "4.9- ";
                            message = "Index 4.9=" + index + " for token=-" + token + "- in -" + tok + " and next_token=-" + tokens[i + 1] + "- and next_token_index=-" + next_token_index + "- with li=-" + last_index + "- and offset=-" + offset + "- and charcount=-" + charcount + "- with text=-" + text + "-";
                            Logger.getLogger(TokenizerCli.class.getName()).log(Level.INFO, message);
                        }
                        if (index == -1 || (next_token_index > -1 && offset > next_token_index + 1)) {

                            if (debug) {
                                from = from + "5- ";
                                message = "Index 5=" + index + " for token=-" + token + "- in -" + tok + " and next_token_index=-" + next_token_index + "-  with li=-" + last_index + "- and offset=-" + offset + "- and charcount=-" + charcount + "- with text=-" + text + "-";
                                Logger.getLogger(TokenizerCli.class.getName()).log(Level.INFO, message);
                            }
                            index = text.indexOf(token, last_index - 2);
                            offset = charcount + index;

                        }
                    } else if (sent < sentences.length) {
                        /*  make sure that found offset is not an offset of the same char at other position so,
                            # find offset of the next token and compare
                            # next token is at next sentence
                         */

                        if (debug) {
                            from = from + "6- ";
                            message = "Index 6=" + index + " for token=-" + token + "- in -" + tok + " with li=-" + last_index + "- and offset=-" + offset + "- and charcount=-" + charcount + "- with text=-" + text + "-";
                            Logger.getLogger(TokenizerCli.class.getName()).log(Level.INFO, message);
                        }

                        String next_sentence = sentences[j + 1];
                        next_sentence = tokenizer.tokenize(next_sentence);
                        String[] next_tokens = next_sentence.split(" ");
                        String next_token = next_tokens[0];
                        int next_token_index = charcount + next_sentence.indexOf(next_token, token.length() + last_index - 1);
                        if ((next_token_index > -1 && offset > next_token_index + 1)) {
                            if (debug) {
                                from = from + "7- ";
                                message = "Index 7=" + index + " for token=-" + token + "- in -" + tok + " with li=-" + last_index + "- and offset=-" + offset + "- and charcount=-" + charcount + "- with text=-" + text + "-";
                                Logger.getLogger(TokenizerCli.class.getName()).log(Level.INFO, message);
                            }
                            index = next_sentence.indexOf(token, last_index - 2);
                            offset = charcount + index;

                        }
                    }

                    last_index = index + token.length();
                    //
                    last_offset = offset;
                    i++;
                    index = text.indexOf(token, last_index);
                    if (debug) {
                        from = from + "7- ";
                        message = "End index=" + index + " " + from + "for token=-" + token + "- in -" + tok + " with li=-" + last_index + "- and offset=-" + offset + "- and charcount=-" + charcount + "- with text=-" + text + "-\n";
                        Logger.getLogger(TokenizerCli.class.getName()).log(Level.INFO, message);
                    }

                    theToken.setId(i);
                    theToken.setTokenLength(token.length());
                    theToken.setTheToken(token);
                    theToken.setStart_offset(offset);
                    theToken.setEnd_offset(offset + token.length() - 1);
                    theToken.setWfid(wfid);
                    theTokens.add(theToken);
                    wfid++;

                } //rof tokens
                theSentence.setTheSentence(s);
                theSentence.setTokens(theTokens);
                theSentence.setId(sent);
                theSentence.setEnd_offset(theSentence.getStart_offset() + s.length() - 1);
                theSentence.setSentenceLength(s.length());

                j++;
                sent++;
                theSentences.add(theSentence);
                
            } // rof sentences
            if (fixedText.length() == 0) {
                charcount = charcount + 1;
            } else {
                charcount = charcount + fixedText.length();
            }
        }
        para++;
        obj.setSentences(theSentences);
        obj.setLinguisticProcessor(lp);

        //textFixer.fixEncoding("- â Ã  -// Ã* â€¦ Ã Ã± -");
        return obj;
    }

}
