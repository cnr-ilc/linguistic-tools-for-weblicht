/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.ilcsimpletypes;

import java.util.Arrays;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class IlcSimpleLemma {

    private String theLemma;

    private int lemmaLength;
    private int id;

    private String type;

    /**
     * the referenced tokenid
     */
    private int[] wfids;
    private String thePos;

    public IlcSimpleLemma(String theLemma, String thePos) {
        this.theLemma = theLemma;
        this.thePos = thePos;
    }

    public IlcSimpleLemma() {
    }

    /**
     * @return the theLemma
     */
    public String getTheLemma() {
        return theLemma;
    }

    /**
     * @param theLemma the theLemma to set
     */
    public void setTheLemma(String theLemma) {
        this.theLemma = theLemma;
    }

    /**
     * @return the lemmaLength
     */
    public int getLemmaLength() {
        return lemmaLength;
    }

    /**
     * @param lemmaLength the lemmaLength to set
     */
    public void setLemmaLength(int lemmaLength) {
        this.lemmaLength = lemmaLength;
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
     * @return the thePos
     */
    public String getThePos() {
        return thePos;
    }

    /**
     * @param thePos the thePos to set
     */
    public void setThePos(String thePos) {
        this.thePos = thePos;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the wfids
     */
    public int[] getWfids() {
        return wfids;
    }

    /**
     * @param wfids the wfids to set
     */
    public void setWfids(int[] wfids) {
        this.wfids = wfids;
    }

    @Override
    public String toString() {
        return "Lemma{" + "theLemma=" + theLemma + ", thePos=" + thePos + ", theType=" + type + ", lemmaLength=" + lemmaLength + ", id=" + id + ", wfid=" + Arrays.toString(wfids) + '}';
    }

    public String wfids2String(int[] values) {
        String ret="";
        for (int value : values) {
            ret=ret+", "+value;
        }
        return ret;
    }
    
    public String toKaf() {
        String ret="";
        ret="\t\t\t<term tid=\"t"+id+"\" type=\""+type+"\"  lemma=\""+theLemma+"\" pos=\""+thePos+"\">\n\t\t\t\t<span>";
        for (int wfid: getWfids()){
            ret=ret+"\n\t\t\t\t\t<target id=\"w"+wfid+"\"/>";
        }
        //ret="\t\t\t<wf wid=\""+wfid+"\" sent=\"#S#\" para=\"#P#\" offset=\""+start_offset+"\" length=\""+tokenLength+"\"><![CDATA["+theToken+"]]></wf>\n";
        ret= ret+"\n\t\t\t\t</span>\n\t\t\t</term>";
        return ret;
    }
    /*
    <term tid="t29" type="close" lemma="because_of" pos="P">
   <span>
    <target id="w32"/>
    <target id="w33"/>
   </span>
  </term>
    */

}
