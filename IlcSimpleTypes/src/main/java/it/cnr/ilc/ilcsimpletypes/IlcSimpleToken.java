/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.ilcsimpletypes;



/**
 * Basic Token
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class IlcSimpleToken {
    
    private String theToken;
    private int start_offset;
    private int end_offset;
    private int tokenLength;
    private int id;
    private int sid;
    private int wfid;
    private IlcSimpleLemma lemma;

    /**
     * Constructor
     * @param theToken the token 
     * @param start_offset starting offset
     * @param end_offset ending offset
     * @param tokenlength length
     */
    public IlcSimpleToken(String theToken, int start_offset, int end_offset, int tokenlength) {
        this.theToken = theToken;
        this.start_offset = start_offset;
        this.end_offset = end_offset;
        this.tokenLength = tokenlength;
    }

    /**
     * Constructor
     * @param theToken the token 
     */
    public IlcSimpleToken(String theToken) {
        this.theToken = theToken;

    }

    /**
     * Constructor
     */
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
        return "Token{" + "theToken=" + theToken + ", start_offset=" + start_offset + ", end_offset=" + end_offset + ", tokenLength=" + tokenLength + ", id=" + id + ", wfid="+wfid+ ", sid="+sid+'}';
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

    /**
     * @return the sid
     */
    public int getSid() {
        return sid;
    }

    /**
     * @param sid the sid to set
     */
    public void setSid(int sid) {
        this.sid = sid;
    }

    /**
     * @return the lemma
     */
    public IlcSimpleLemma getLemma() {
        return lemma;
    }

    /**
     * @param lemma the lemma to set
     */
    public void setLemma(IlcSimpleLemma lemma) {
        this.lemma = lemma;
    }
    
}
