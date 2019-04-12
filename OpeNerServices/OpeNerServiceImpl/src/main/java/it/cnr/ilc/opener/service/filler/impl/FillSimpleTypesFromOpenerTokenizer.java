/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.opener.service.filler.impl;

import eu.kyotoproject.kaf.KafSaxParser;
import eu.kyotoproject.kaf.KafWordForm;
import eu.kyotoproject.kaf.KafWordformSaxParser;
import it.cnr.ilc.ilcioutils.IlcIOUtils;
import it.cnr.ilc.ilcsimpletypes.IlcSimpleLemma;
import it.cnr.ilc.ilcsimpletypes.IlcSimpleSentence;
import it.cnr.ilc.ilcsimpletypes.IlcSimpleToken;
import it.cnr.ilc.opener.service.filler.i.FillSimpleTypes;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Read from tokenizer output format
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class FillSimpleTypesFromOpenerTokenizer implements FillSimpleTypes {

    private IlcSimpleToken token;
    private IlcSimpleSentence sent;
    private IlcSimpleLemma lemma;

    private ArrayList<IlcSimpleToken> tokens = new ArrayList<>();
    private ArrayList<IlcSimpleSentence> sents = new ArrayList<>();
    private ArrayList<IlcSimpleLemma> lemmas = new ArrayList<>();
    private String sep = "\t";
    private String mw_sep = "_";

    /**
     * Read the file each line at time
     * @param file the file
     * @return the list of lines
     */
    @Override
    public List<String> getLinesFromFile(File file) {
        List<String> lines = null;
        lines = IlcIOUtils.readFromFile(file);
        return lines;
    }

    /**
     * 
     * @param value
     * @return the lemma type
     */
    private String getLemmaType(String value) {
        String type = "closed";
        if (value.charAt(0) == 'N' || value.charAt(0) == 'V' || value.charAt(0) == 'A') {
            type = "open";
        }
        return type;
    }

    /**
     * 
     * @param tid the token id
     * @param value the multi word
     * @return an array of token ids
     */
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

    /**
     * 
     * @param line the line to split
     * @param Sep the separator
     * @return an array of token from sep
     */
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

    /**
     * Read the file and create a list of sentences, tokens and lemmas
     * @param file the file to read
     */
    @Override
    public void manageServiceOutput(File file) {
        int tokenLenght;
        int start_offset = 0, end_offset = 0;
//        int prev_start_offset = -1, prev_end_offset = -1;
//        int last_sent_end_offset;
        int sentId = -1;

        String theSentence = "";
        String theToken = "";
        String theLemma = "";
        String pos = "";
        int tid=1;
        ArrayList<IlcSimpleToken> toks = new ArrayList<>();
        KafSaxParser parser = new KafSaxParser();
        KafWordformSaxParser kwfsp = new KafWordformSaxParser();
        parser.parseFile(file);
        kwfsp.parseContent(parser.getXML());

        for (KafWordForm wf : kwfsp.kafWordFormList) {

            token = new IlcSimpleToken();

            theToken = wf.getWf();
            tokenLenght = theToken.length();
            end_offset = start_offset + tokenLenght - 1;
            if (sentId != Integer.parseInt(wf.getSent())) {
                tid=1;
                toks = new ArrayList<IlcSimpleToken>();
                
                //System.out.println("it.cnr.ilc.opener.service.filler.impl.FillSimpleTypesFromOpenerTokenizer.manageServiceOutput() prev sent "+theSentence);
                if(theSentence.length() > 0){
                    sent.setTheSentence(theSentence);
                    sent.setEnd_offset(start_offset);
                    sent.setSentenceLength(theSentence.length());
                    getSents().add(sent);
                }
                sent = new IlcSimpleSentence();
                sent.setStart_offset(start_offset);
                
                theSentence = theToken;
                sentId = Integer.parseInt(wf.getSent());
                sent.setId(sentId);

            } else {
                theSentence=theSentence+" "+theToken;
                //System.out.println("it.cnr.ilc.opener.service.filler.impl.FillSimpleTypesFromOpenerTokenizer.manageServiceOutput() sent "+theSentence);
                
            }

            
            token.setTheToken(theToken);
            token.setTokenLength(tokenLenght);
            token.setId(tid); //Integer.parseInt(wf.getWid().substring(1)));
            token.setWfid(Integer.parseInt(wf.getWid().substring(1)));
            token.setStart_offset(start_offset);
            token.setEnd_offset(end_offset);
            token.setSid(Integer.parseInt(wf.getSent()));
            
            toks.add(token);
            sent.setTokens(toks);
//            theSentence = theSentence + " " + theToken;
//            //System.err.println("FINISCO QUI TOKEN? " + theSentence);
//            last_sent_end_offset = end_offset;
//
//            //System.out.println("EMPTY " + theSentence);
////                toks = new ArrayList<>();
//            tid++;
//
//            //System.err.println("line " + line);
//            sent.setTokens(toks);
//            sent.setId(sid);
//            sent.setTheSentence(theSentence);
//            sent.setSentenceLength(theSentence.length());
//            sent.setEnd_offset(theSentence.length());
//            sent.setStart_offset(0);
//            sent.setTokens(toks);
//            getSents().add(sent);
//
//            tokens.addAll(toks);
            //System.err.println("AAAA TOKEN" + getSents().toString());
            start_offset = start_offset + theToken.length() + 1;
            tid++;
        } // end for on wordform add last tokens and sentence
        sent.setTheSentence(theSentence);
        sent.setEnd_offset(end_offset);
        sent.setSentenceLength(theSentence.length());
        getSents().add(sent);
       
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

    @Override
    public void manageServiceOutput(InputStream input) {

    }

}
