/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.panacea.service.filler.impl;


import it.cnr.ilc.ilcioutils.IlcIOUtils;
import it.cnr.ilc.ilcsimpletypes.IlcSimpleLemma;
import it.cnr.ilc.ilcsimpletypes.IlcSimpleSentence;
import it.cnr.ilc.ilcsimpletypes.IlcSimpleToken;
import it.cnr.ilc.ilcutils.Format;
import it.cnr.ilc.panacea.service.filler.i.FillSimpleTypes;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Read from Freeling_it output format
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class FillSimpleTypesFromFreelingIt implements FillSimpleTypes {

    private IlcSimpleToken token;
    private IlcSimpleSentence sent;
    private IlcSimpleLemma lemma;

    private ArrayList<IlcSimpleToken> tokens = new ArrayList<>();
    private ArrayList<IlcSimpleSentence> sents = new ArrayList<>();
    private ArrayList<IlcSimpleLemma> lemmas = new ArrayList<>();
    private String sep = "\t";
    private String mw_sep = "_";

    @Override
    public List<String> getLinesFromFile(File file) {
        List<String> lines = null;
        lines = IlcIOUtils.readFromFile(file);
        return lines;
    }

    private String getLemmaType(String value) {
        String type = "closed";
        if (value.charAt(0) == 'N' || value.charAt(0) == 'V' || value.charAt(0) == 'A') {
            type = "open";
        }
        return type;
    }

    private int[] ifWmThenReturnIdSize(int tid, String value) {
        int[] ret;
        int l = value.split(mw_sep).length;

        ret = new int[l];
        for (int i = 0; i < l; i++) {
            //System.err.println("I "+i);
            ret[i] = (tid + i);
            //System.err.println("ret["+i+"] for i:"+i +" "+(tid + i)+ " size is: "+l);

        }
        return ret;
    }

    private String[] splitLinesFromSep(String line, String Sep) {
        String[] values;
        //values = new String[line.split(Sep).length];
        values = line.split(Sep);
        return values;
    }

    @Override
    public List<IlcSimpleToken> createListOfTokens() {
        return getTokens();
    }

    @Override
    public List<IlcSimpleLemma> createListOfLemmas() {
        return getLemmas();
    }

    @Override
    public void manageServiceOutput(List<String> lines, String serviceoutput) {
        //IlcSimpleSentence sent;// = new IlcSimpleSentence();
        int sid = 1;
        int tid = 1;
        int lid = 1;
        int[] wfids;
        int tokenLenght;
        int start_offset = 0, end_offset = 0;
        int prev_start_offset = -1, prev_end_offset = -1;
        int last_sent_end_offset;
        int size = lines.size();
        String theSentence = "";
        String theToken = "";
        String theLemma = "";
        String pos = "";
        String[] splitted;
        ArrayList<IlcSimpleToken> toks = new ArrayList<>();
        ArrayList<IlcSimpleLemma> lems = new ArrayList<>();
        if (serviceoutput.equals(Format.SERVICE_OUT_TAG)) { // tokens + offsets + splitted by sentences
            for (String line : lines) {
                if (!line.isEmpty()) {

                    splitted = splitLinesFromSep(line, sep);
                    sent = new IlcSimpleSentence();
                    token = new IlcSimpleToken();
                    lemma = new IlcSimpleLemma();

                    theToken = splitted[0];
                    theLemma = splitted[1];
                    pos = splitted[2];
                    start_offset = Integer.parseInt(splitted[4]);
                    end_offset = Integer.parseInt(splitted[5]);
                    tokenLenght = end_offset - start_offset;

//                    System.err.println("token in manage " + theToken);
//                    System.err.println("TAGGED");
//           
                    token.setTheToken(theToken);
                    token.setId(tid);
                    token.setWfid(tid);
                    token.setStart_offset(start_offset);
                    token.setEnd_offset(end_offset);
                    token.setTokenLength(tokenLenght);
                    token.setSid(sid);
                    token.setLemma(lemma);

                    // lemma and pos and additional info on token 
                    lemma.setId(lid);
                    lemma.setLemmaLength(theLemma.length());
                    lemma.setThePos(pos);
                    lemma.setTheLemma(theLemma);
                    lemma.setType(getLemmaType(pos));
                    // multiword??
                    wfids = ifWmThenReturnIdSize(tid, theToken);
                    if (wfids.length > 1) {
                        //System.out.println("Token " + theToken + " è MW with " + Arrays.toString(wfids));
                        tid = tid + wfids.length - 1;
                    }

                    lemma.setWfids(wfids);
                    lems.add(lemma);

                    toks.add(token);

                    prev_start_offset = start_offset;
                    if (start_offset == prev_end_offset) {
                        theSentence = theSentence + theToken;
                        //System.out.println("IF token " + theToken + " so " + start_offset + " peo " + prev_end_offset);
                    } else {
                        theSentence = theSentence + " " + theToken;
                        //System.out.println("ELSE token " + theToken + " so " + start_offset + " peo " + prev_end_offset);
                    }
                    prev_end_offset = end_offset;
                    //System.out.println("word " + theSentence);
                } else {
                    //System.err.println("FINISCO QUI?");
                    last_sent_end_offset = end_offset;
                    sent.setId(sid);
                    sent.setTheSentence(theSentence);
                    sent.setSentenceLength(theSentence.length());
                    sent.setEnd_offset(last_sent_end_offset);
                    sent.setStart_offset(sent.getEnd_offset() - sent.getSentenceLength() + 1);
                    //System.out.println("EMPTY " + theSentence);
                    theSentence = "";

                    sent.setTokens(toks);
                    getSents().add(sent);

                    sid++;
                    tokens.addAll(toks);
                    lemmas.addAll(lems);
                    toks = new ArrayList<>();
                    //System.err.println("AAAA " + getSents().toString());
                }
                tid++;
                lid++;
            }
        } else if (serviceoutput.equals(Format.SERVICE_OUT_SPLIT)) { // no lemma but empty line for sentence
            for (String line : lines) {
                if (!line.isEmpty()) {
                    splitted = splitLinesFromSep(line, sep);
                    sent = new IlcSimpleSentence();
                    token = new IlcSimpleToken();

                    theToken = splitted[0];

//                    System.err.println("token in splitted " + theToken);
//                    System.err.println("SPLITTED");
//           
                    token.setTheToken(theToken);
                    token.setId(tid);
                    token.setWfid(tid);
                    token.setStart_offset(start_offset);
                    token.setEnd_offset(end_offset);
                    token.setSid(sid);
                    toks.add(token);
                    theSentence = theSentence + " " + theToken;

                } else {
                    //System.err.println("FINISCO QUI SPLITTED?");
                    last_sent_end_offset = end_offset;
                    sent.setId(sid);
                    sent.setTheSentence(theSentence);
                    sent.setSentenceLength(theSentence.length());
                    sent.setEnd_offset(last_sent_end_offset);
                    sent.setStart_offset(sent.getEnd_offset() - sent.getSentenceLength() + 1);
                    //System.out.println("EMPTY " + theSentence);
                    theSentence = "";

                    sent.setTokens(toks);
                    getSents().add(sent);

                    sid++;
                    tokens.addAll(toks);

                    toks = new ArrayList<>();
                    //System.err.println("AAAA SPLITTED" + getSents().toString());
                }
                tid++;
                //System.err.println("line " + line);
            }

        } else if (serviceoutput.equals(Format.SERVICE_OUT_TOK)) { // a list of tokens in a single sentence
            for (String line : lines) {

                splitted = splitLinesFromSep(line, sep);
                sent = new IlcSimpleSentence();
                token = new IlcSimpleToken();

                theToken = splitted[0];

//                System.err.println("token in token " + theToken);
//                System.err.println("TOKEN");
//           
                token.setTheToken(theToken);
                token.setId(tid);
                token.setWfid(tid);
                token.setStart_offset(start_offset);
                token.setEnd_offset(end_offset);
                token.setSid(sid);
                toks.add(token);
                theSentence = theSentence + " " + theToken;
                //System.err.println("FINISCO QUI TOKEN? " + theSentence);
                last_sent_end_offset = end_offset;

                //System.out.println("EMPTY " + theSentence);
//                toks = new ArrayList<>();

                tid++;

                //System.err.println("line " + line);
            }
            sent.setTokens(toks);
            sent.setId(sid);
            sent.setTheSentence(theSentence);
            sent.setSentenceLength(theSentence.length());
            sent.setEnd_offset(theSentence.length());
            sent.setStart_offset(0);
            sent.setTokens(toks);
            getSents().add(sent);

            tokens.addAll(toks);
            //System.err.println("AAAA TOKEN" + getSents().toString());
        }

        //System.err.println("tokens " + toks.toString());
    }

    @Override
    public List<IlcSimpleSentence> createListOfSentences() {
        return getSents();
    }

    /**
     * @return the tokens
     */
    @Override
    public ArrayList<IlcSimpleToken> getTokens() {
        return tokens;
    }

    /**
     * @return the sents
     */
    @Override
    public ArrayList<IlcSimpleSentence> getSents() {
        return sents;
    }

    /**
     * @return the lemmas
     */
    @Override
    public ArrayList<IlcSimpleLemma> getLemmas() {
        return lemmas;
    }

}
