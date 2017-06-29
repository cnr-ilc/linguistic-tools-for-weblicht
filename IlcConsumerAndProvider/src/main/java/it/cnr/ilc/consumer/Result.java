/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.consumer;

import it.cnr.ilc.ilcsimpletypes.IlcSimpleSentence;
import it.cnr.ilc.producer.tokaf.LinguisticProcessor;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class Result {
    private List<IlcSimpleSentence> sentences = new ArrayList<>();
    private LinguisticProcessor linguisticProcessor;
    private String message = "";
    
}
