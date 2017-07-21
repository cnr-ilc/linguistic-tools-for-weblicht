/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.ilcsimpletypes;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class IlcSimpleSentence {

    private String theSentence;
    private int start_offset;
    private int end_offset;
    private int sentenceLength;
    private int id;
    private List<IlcSimpleToken> tokens = new ArrayList<>();

    public IlcSimpleSentence(int id, String theSentence) {
        this.id = id;
        this.theSentence = theSentence;
    }

    public IlcSimpleSentence(int id, String theSentence, ArrayList<IlcSimpleToken> tokens) {
        this.id = id;
        this.theSentence = theSentence;
        this.tokens = tokens;
    }

    public IlcSimpleSentence() {
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
    public List<IlcSimpleToken> getTokens() {
        return tokens;
    }

    /**
     * @param tokens the tokens to set
     */
    public void setTokens(List<IlcSimpleToken> tokens) {
        this.tokens = tokens;
    }

    @Override
    public String toString() {
        return "Sentence{" + "theSentence=" + theSentence + ", start_offset=" + start_offset + ", end_offset=" + end_offset + ", sentenceLength=" + sentenceLength + ", id=" + id + ", tokens=" + tokens + '}';
    }

}
