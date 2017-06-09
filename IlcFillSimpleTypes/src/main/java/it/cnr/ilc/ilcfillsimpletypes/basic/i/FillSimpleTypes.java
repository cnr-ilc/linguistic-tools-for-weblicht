/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.ilcfillsimpletypes.basic.i;

import java.io.File;
import java.util.List;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public interface FillSimpleTypes {
    
    
    public List<String> getLinesFromFile(File file); 
    
    public void createListOfTokens(List<String> list);
    
    public void createListOfSentences(List<String> list);
    
    public String[] splitLinesFromSep(String Sep); 
    
    
}
