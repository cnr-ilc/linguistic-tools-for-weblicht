/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.ilcfillsimpletypes.basic.i;

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

    public List<String> getLinesFromFile(File file);

    public List<IlcSimpleToken> createListOfTokens();

    public List<IlcSimpleSentence> createListOfSentences();

    public List<IlcSimpleLemma> createListOfLemmas();

    public void manageServiceOutput(List<String> lines, String servceoutput);

    public ArrayList<IlcSimpleToken> getTokens();

    public ArrayList<IlcSimpleSentence> getSents();
    
    public ArrayList<IlcSimpleLemma> getLemmas();

}
