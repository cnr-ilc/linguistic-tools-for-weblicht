/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.ilcsimpletypes;

import it.cnr.ilc.ilcsimpletypes.utils.Format;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class IlcSimpleToken {
    
    private String theToken;
    private int start_offset;
    private int end_offset;
    private int tokenLength;
    private int id;
    private int wfid;

    public IlcSimpleToken(String theToken, int start_offset, int end_offset, int tokenlength) {
        this.theToken = theToken;
        this.start_offset = start_offset;
        this.end_offset = end_offset;
        this.tokenLength = tokenlength;
    }

    public IlcSimpleToken(String theToken) {
        this.theToken = theToken;

    }

    public IlcSimpleToken() {
    }

    /**
     * @return the theToken
     */
    public String getTheToken() {
        return theToken;
    }

    /**
     * @param theToken the theToken to set
     */
    public void setTheToken(String theToken) {
        this.theToken = theToken;
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
     * @return the tokenlength
     */
    public int getTokenLength() {
        return tokenLength;
    }

    /**
     * @param tokenLength the tokenlength to set
     */
    public void setTokenLength(int tokenLength) {
        this.tokenLength = tokenLength;
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

    @Override
    public String toString() {
        return "Token{" + "theToken=" + theToken + ", start_offset=" + start_offset + ", end_offset=" + end_offset + ", tokenLength=" + tokenLength + ", id=" + id + ", wfid="+wfid+ '}';
    }
    
    public String toKaf() {
        String ret="";
        ret="\t\t\t<wf wid=\""+wfid+"\" sent=\"#S#\"  para=\"1\" offset=\""+start_offset+"\" length=\""+tokenLength+"\"><![CDATA["+theToken+"]]></wf>\n";
        //ret="\t\t\t<wf wid=\""+wfid+"\" sent=\"#S#\" para=\"#P#\" offset=\""+start_offset+"\" length=\""+tokenLength+"\"><![CDATA["+theToken+"]]></wf>\n";
        return ret;
    }
    
    public String toTab() {
        /* in tabbed format
        tokenid \t start_offset \t lenght \t token 
        */
        

        String ret="";
        ret=ret+wfid+Format.TAB+start_offset+Format.TAB+tokenLength+Format.TAB+theToken;//+"\n";
        
        return ret;
    }

    /**
     * @return the wfid
     */
    public int getWfid() {
        return wfid;
    }

    /**
     * @param wfid the wfid to set
     */
    public void setWfid(int wfid) {
        this.wfid = wfid;
    }
    
}
