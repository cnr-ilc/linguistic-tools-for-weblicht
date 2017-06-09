/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.ilcfillsimpletypes.basic;

import it.cnr.ilc.ilcfillsimpletypes.basic.i.FillSimpleTypes;
import it.cnr.ilc.ilcioutils.IlcIOUtils;
import it.cnr.ilc.ilcsimpletypes.IlcSimpleSentence;
import it.cnr.ilc.ilcsimpletypes.IlcSimpleToken;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Read from Freeling_it output format
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class FillSimpleTypesFromFreelingIt implements FillSimpleTypes{
    private IlcSimpleToken token=new IlcSimpleToken();
    private IlcSimpleSentence sent = new IlcSimpleSentence();
    private ArrayList<IlcSimpleToken> tokens=new ArrayList<>();
    private ArrayList<IlcSimpleSentence> sents=new ArrayList<>();
    private String sep="\t";
    
    @Override
    /**
     * 
     * @param file
     * @return 
     */
    public List<String> getLinesFromFile(File file) {
        List<String> lines=null;
        IlcIOUtils.readFromFile(file);
        return lines;
    }

    @Override
    
    /**
     * 
     */
    public void createListOfTokens(List<String> list) {
        
    }

    @Override
    public void createListOfSentences(List<String> list) {
        
    }

    @Override
    public String[] splitLinesFromSep(String Sep) {
        return null;
    }
    
    

}
