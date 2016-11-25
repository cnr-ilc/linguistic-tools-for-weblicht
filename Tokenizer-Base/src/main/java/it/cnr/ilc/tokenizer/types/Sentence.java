/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.tokenizer.types;

import it.cnr.ilc.tokenizer.utils.Format;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class Sentence {

    private String theSentence;
    private int start_offset;
    private int end_offset;
    private int sentenceLength;
    private int id;
    private List<Token> tokens = new ArrayList<>();

    public Sentence(int id, String theSentence) {
        this.id = id;
        this.theSentence = theSentence;
    }

    public Sentence(int id, String theSentence, ArrayList<Token> tokens) {
        this.id = id;
        this.theSentence = theSentence;
        this.tokens = tokens;
    }

    public Sentence() {
    }

    /**
     * @return the theSentence
     */
    public String getTheSentence() {
        return theSentence;
    }

    /**
     * @param theSentence the theSentence to set
     */
    public void setTheSentence(String theSentence) {
        this.theSentence = theSentence;
    }

    /**
     * @return the start_offset
     */
    public int getStart_offset() {
        return start_offset;
    }

    /**
     * @param start_offset the start_offset to set
     */
    public void setStart_offset(int start_offset) {
        this.start_offset = start_offset;
    }

    /**
     * @return the end_offset
     */
    public int getEnd_offset() {
        return end_offset;
    }

    /**
     * @param end_offset the end_offset to set
     */
    public void setEnd_offset(int end_offset) {
        this.end_offset = end_offset;
    }

    /**
     * @return the sentenceLength
     */
    public int getSentenceLength() {
        return sentenceLength;
    }

    /**
     * @param sentenceLength the sentenceLength to set
     */
    public void setSentenceLength(int sentenceLength) {
        this.sentenceLength = sentenceLength;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the tokens
     */
    public List<Token> getTokens() {
        return tokens;
    }

    /**
     * @param tokens the tokens to set
     */
    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    @Override
    public String toString() {
        return "Sentence{" + "theSentence=" + theSentence + ", start_offset=" + start_offset + ", end_offset=" + end_offset + ", sentenceLength=" + sentenceLength + ", id=" + id + ", tokens=" + tokens + '}';
    }

    public String toKaf() {
        String ret="";
        
        for (Token t:getTokens()){
            //System.err.println("t "+t);
            ret=ret+t.toKaf().replaceAll("#S#", Integer.toString(id));
        }
        return ret;
    }

    public String toTab() {
        String ret = "";
        for (Token t : getTokens()) {
            ret = ret + id + Format.TAB + t.toTab() + "\n";
        }
        return ret;
    }
}
