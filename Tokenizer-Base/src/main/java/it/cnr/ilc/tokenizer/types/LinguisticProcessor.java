/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.tokenizer.types;

import it.cnr.ilc.tokenizer.utils.Vars;
import java.util.ArrayList;
import java.util.List;

/**
 *
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

    public String toKaf() {
        String ret = "";

        
        ret = "\t\t<linguisticProcessors layer=\"" + getLayer() + "\">\n";
        for (String lp: getLps()) {
            ret=ret+"\t\t\t"+"<lp name=\""+lp.split("#")[0]+"\" version=\""+Vars.version+"\" timestamp=\""+lp.split("#")[1]+"\"/>\n";
        }
        ret=ret+"\t\t</linguisticProcessors>\n";

        return ret;

    }
}
