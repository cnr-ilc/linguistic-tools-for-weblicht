/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.producer;


import java.util.ArrayList;
import java.util.List;

/**
 * A linguistic processor is needed for KAF
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class LinguisticProcessor {

    
    private String layer;
    private List<String> lps = new ArrayList<>();
    
    /**
     * @return the layer
     */
    public String getLayer() {
        return layer;
    }
 
    /**
     * @param layer the layer to set
     */
    public void setLayer(String layer) {
        this.layer = layer;
    }

    /**
     * @return the lps
     */
    public List<String> getLps() {
        return lps;
    }

    /**
     * @param lps the lps to set
     */
    public void setLps(List<String> lps) {
        this.lps = lps;
    }

    @Override
    public String toString() {
        return "LnguisticProcessors{" + "layer=" + layer + ", lps=" + lps + '}';
    }

    
    
}
