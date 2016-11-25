/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.tokenizer.types;

import it.cnr.ilc.tokenizer.utils.Format;
import it.cnr.ilc.tokenizer.utils.Vars;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class Result {

    private List<Sentence> sentences = new ArrayList<>();
    private LinguisticProcessor linguisticProcessor;
    private String message = "";

    /**
     * @return the sentences
     */
    public List<Sentence> getSentences() {
        return sentences;
    }

    /**
     * @param sentences the sentences to set
     */
    public void setSentences(List<Sentence> sentences) {
        this.sentences = sentences;
    }

    @Override
    public String toString() {
        return "Result{" + "sentences=" + sentences + '}';
    }

    public String toTab() {
        String ret = "SentenceId" + Format.TAB + "TokenId" + Format.TAB + "Start OffSet" + Format.TAB + "Token Lenght" + Format.TAB + "Token\n";
        for (Sentence s : getSentences()) {
            ret = ret + s.toTab();
        }
        return ret;
    }

    public void toTab(PrintStream ps) {
        String ret = "SentenceId" + Format.TAB + "TokenId" + Format.TAB + "Start OffSet" + Format.TAB + "Token Lenght" + Format.TAB + "Token\n";
        for (Sentence s : getSentences()) {
            ret = ret + s.toTab();
        }
        try {
            ps.write(ret.getBytes("UTF-8"));
        } catch (IOException e) {
            message = "IOException in writing the stream " + e.getMessage();
            Logger.getLogger(Result.class.getName()).log(Level.SEVERE, message);
            System.exit(-1);
        }
    }

    public String toKaf() {
        String ret = "";
        ret = ret + kafHeader();
        ret = ret + "" + getLinguisticProcessor().toKaf();
        ret = ret + "\t" + "</kafHeader>\n";
        ret = ret + "\t<text>\n";
        for (Sentence s : getSentences()) {
            ret = ret + s.toKaf();
        }
        ret = ret + "\t</text>\n";
        ret = ret + "</KAF>";
        return ret;

    }
    
     public void toKaf(PrintStream ps) {
        String ret = "";
        ret = ret + kafHeader();
        ret = ret + "" + getLinguisticProcessor().toKaf();
        ret = ret + "\t" + "</kafHeader>\n";
        ret = ret + "\t<text>\n";
        for (Sentence s : getSentences()) {
            ret = ret + s.toKaf();
        }
        ret = ret + "\t</text>\n";
        ret = ret + "</KAF>";
        try {
            ps.write(ret.getBytes("UTF-8"));
        } catch (IOException e) {
            message = "IOException in writing the stream " + e.getMessage();
            Logger.getLogger(Result.class.getName()).log(Level.SEVERE, message);
            System.exit(-1);
        }

    }
    
    

    private String kafHeader() {
        String ret = "";
        ret = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n";
        ret = ret + "<KAF xml:lang=\"it\" version=\"" + Vars.version + "\">\n";
        ret = ret + "\t" + "<kafHeader>\n";
        ret = ret + "\t\t<fileDesc />\n";
        return ret;
    }

    /**
     * @return the linguisticProcessor
     */
    public LinguisticProcessor getLinguisticProcessor() {
        return linguisticProcessor;
    }

    /**
     * @param linguisticProcessor the linguisticProcessor to set
     */
    public void setLinguisticProcessor(LinguisticProcessor linguisticProcessor) {
        this.linguisticProcessor = linguisticProcessor;
    }

}
