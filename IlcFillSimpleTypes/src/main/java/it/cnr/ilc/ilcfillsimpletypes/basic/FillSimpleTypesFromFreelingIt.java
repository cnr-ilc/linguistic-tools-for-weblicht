/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.ilcfillsimpletypes.basic;

import it.cnr.ilc.ilcfillsimpletypes.basic.i.FillSimpleTypes;
import it.cnr.ilc.ilcioutils.IlcIOUtils;
import it.cnr.ilc.ilcsimpletypes.IlcSimpleLemma;
import it.cnr.ilc.ilcsimpletypes.IlcSimpleSentence;
import it.cnr.ilc.ilcsimpletypes.IlcSimpleToken;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
    /**
     *
     * @param file
     * @return
     */
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

    public String[] splitLinesFromSep(String line, String Sep) {
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
    public void manageServiceOutput(List<String> lines) {
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

                token.setId(tid);
                token.setStart_offset(start_offset);
                token.setEnd_offset(end_offset);
                token.setTokenLength(tokenLenght);
                token.setTheToken(theToken);
                token.setWfid(tid);
                toks.add(token);

                // lemma
                lemma.setId(lid);
                lemma.setLemmaLength(theLemma.length());
                lemma.setThePos(pos);
                lemma.setTheLemma(theLemma);
                lemma.setType(getLemmaType(pos));
                // multiword??
                wfids = ifWmThenReturnIdSize(tid, theToken);
                if (wfids.length > 1) {
                    System.out.println("Token " + theToken+ " è MW with "+Arrays.toString(wfids));
                    tid=tid+wfids.length-1;
                } else {
                }

                lemma.setWfids(wfids);
                lems.add(lemma);
                prev_start_offset = start_offset;
                if (start_offset == prev_end_offset) {
                    theSentence = theSentence + theToken;
                    //System.out.println("IF token " + theToken+ " so "+start_offset+ " peo "+prev_end_offset);
                } else {
                    theSentence = theSentence + " " + theToken;
                    //System.out.println("ELSE token " + theToken+ " so "+start_offset+ " peo "+prev_end_offset);
                }
                prev_end_offset = end_offset;
                //System.out.println("word " + theSentence);
            } else {
                last_sent_end_offset = end_offset;
                sent.setId(sid);
                sent.setTheSentence(theSentence);
                sent.setSentenceLength(theSentence.length());
                sent.setEnd_offset(last_sent_end_offset);
                sent.setStart_offset(sent.getEnd_offset() - sent.getSentenceLength() + 1);
                //System.out.println("EMPTY " + theSentence);
                theSentence = "";
                System.err.println("AAAA " + getSents().toString());
                sent.setTokens(toks);
                getSents().add(sent);

                sid++;
                tokens.addAll(toks);
                lemmas.addAll(lems);
                toks = new ArrayList<>();
            }
            tid++;
            lid++;
        }
    }

    @Override
    public List<IlcSimpleSentence> createListOfSentences() {
        return getSents();
    }

    /**
     * @return the tokens
     */
    public ArrayList<IlcSimpleToken> getTokens() {
        return tokens;
    }

    /**
     * @return the sents
     */
    public ArrayList<IlcSimpleSentence> getSents() {
        return sents;
    }

    /**
     * @return the lemmas
     */
    public ArrayList<IlcSimpleLemma> getLemmas() {
        return lemmas;
    }

}
