/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.consumer;

import it.cnr.ilc.ilcsimpletypes.IlcSimpleLemma;
import it.cnr.ilc.ilcsimpletypes.IlcSimpleSentence;
import it.cnr.ilc.producer.LinguisticProcessor;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class Result {
    private List<IlcSimpleSentence> sentences = new ArrayList<>();
    private List<IlcSimpleLemma> lemmas = new ArrayList<>();
    private LinguisticProcessor linguisticProcessor;
    private String message = "";

    /**
     * @return the sentences
     */
    public List<IlcSimpleSentence> getSentences() {
        return sentences;
    }

    /**
     * @param sentences the sentences to set
     */
    public void setSentences(List<IlcSimpleSentence> sentences) {
        this.sentences = sentences;
    }

    /**
     * @return the linguisticProcessor
     */
    public LinguisticProcessor getLinguisticProcessor() {
        return linguisticProcessor;
    }

    /**
     * @param linguisticProcessor the linguisticProcessor to set
     */
    public void setLinguisticProcessor(LinguisticProcessor linguisticProcessor) {
        this.linguisticProcessor = linguisticProcessor;
    }

    /**
     * @return the lemmas
     */
    public List<IlcSimpleLemma> getLemmas() {
        return lemmas;
    }

    /**
     * @param lemmas the lemmas to set
     */
    public void setLemmas(List<IlcSimpleLemma> lemmas) {
        this.lemmas = lemmas;
    }
    
}
