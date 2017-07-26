/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.panacea.service.filler.i;

import it.cnr.ilc.ilcsimpletypes.IlcSimpleLemma;
import it.cnr.ilc.ilcsimpletypes.IlcSimpleSentence;
import it.cnr.ilc.ilcsimpletypes.IlcSimpleToken;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public interface FillSimpleTypes {

    /**
     * reads the lines from the input file
     * @param file the input file
     * @return the list of lines
     */
    public List<String> getLinesFromFile(File file);

    
    /**
     * create a list of tokens
     * @return a list of tokens
     */
    public List<IlcSimpleToken> createListOfTokens();

    /**
     * create a list of sentences
     * @return a list of sentences
     */
    public List<IlcSimpleSentence> createListOfSentences();

    
    /**
     * create a list of lemmas
     * @return a list of lemmas
     */
    public List<IlcSimpleLemma> createListOfLemmas();

    /**
     * reads the output of the service and creates tokens, lemmas, sentences according to @param serviceoutput
     * @param lines lines to read
     * @param serviceoutput the output format of the service
     */
    public void manageServiceOutput(List<String> lines, String serviceoutput);

    /**
     * 
     * @return the tokens 
     */
    public ArrayList<IlcSimpleToken> getTokens();

    /**
     * 
     * @return the sentences
     */
    public ArrayList<IlcSimpleSentence> getSents();
    
    /**
     * 
     * @return the lemmas
     */
    public ArrayList<IlcSimpleLemma> getLemmas();

}
