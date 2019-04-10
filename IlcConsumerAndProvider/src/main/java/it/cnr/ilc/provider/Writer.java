/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.provider;

import eu.clarin.weblicht.wlfxb.tc.api.TextCorpus;
import it.cnr.ilc.consumer.Result;

import it.cnr.ilc.ilcsimpletypes.IlcSimpleLemma;
import it.cnr.ilc.ilcsimpletypes.IlcSimpleSentence;
import it.cnr.ilc.ilcsimpletypes.IlcSimpleToken;
import it.cnr.ilc.ilcutils.Format;
import it.cnr.ilc.ilcutils.Vars;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class exposes methods to write the result in different formats.
 * <p>
 * Even if methods might be redundant (for instance toTab() and
 * toTab(PrintStream ps) they've been kept separated for the integration in
 * jersey
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class Writer {

    public static final String CLASS_NAME = Writer.class.getName();

    private Result result = new Result();
    private String format;
    private String serviceFormat;

    /**
     * void constructor
     *
     */
    public Writer() {
    }

    public Writer(String format, String serviceFormat) {
        this.format = format;
        this.serviceFormat = serviceFormat;
    }
    
    

    /**
     * Constructor
     *
     * @param result The result of the service
     */
    public Writer(Result result) {
        this.result = result;
    }
    
    public Writer(Result result,String format, String serviceFormat) {
        this.result = result;
        this.format = format;
        this.serviceFormat = serviceFormat;
    }

    /**
     * Write the result of a service in kaf format
     *
     * @return the kaffed version of the input
     */
    public String toKaf() {
        List<IlcSimpleSentence> sentences = result.getSentences();
        //System.err.println("sentences " + sentences);
        LinguisticProcessor linguisticProcessor = result.getLinguisticProcessor();
        String ret = "";
        ret = ret + kafHeader();
        ret = ret + "" + linguisticProcessorToKaf(linguisticProcessor);
        ret = ret + "\t" + "</kafHeader>\n";
        ret = ret + "\t<text>\n";
        for (IlcSimpleSentence s : sentences) {
            ret = ret + sentenceToKaf(s);
        }
        ret = ret + "\t</text>\n";
        if (!result.getLemmas().isEmpty()) {
            ret = ret + "\t<terms>\n";
            for (IlcSimpleLemma lemma : result.getLemmas()) {
                ret = ret + lemmaToKaf(lemma);
            }
            ret = ret + "\t</terms>\n";

        }
        ret = ret + "</KAF>";
        return ret;

    }
    
    public String toKaf(String str) {
        
        return str;

    }

    private String kafHeader() {
        String ret = "";
        ret = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n";
        ret = ret + "<KAF xml:lang=\"it\" version=\"" + Vars.version + "\">\n";
        ret = ret + "\t" + "<kafHeader>\n";
        ret = ret + "\t\t<fileDesc />\n";
        return ret;
    }

    private String linguisticProcessorToKaf(LinguisticProcessor linguisticprocessor) {
        String ret = "";

        ret = "\t\t<linguisticProcessors layer=\"" + linguisticprocessor.getLayer() + "\">\n";
        for (String lp : linguisticprocessor.getLps()) {
            ret = ret + "\t\t\t" + "<lp name=\"" + lp.split("#")[0] + "\" version=\"" + Vars.version + "\" timestamp=\"" + lp.split("#")[1] + "\"/>\n";
        }
        ret = ret + "\t\t</linguisticProcessors>\n";

        return ret;

    }

    private String sentenceToKaf(IlcSimpleSentence sentence) {
        String ret = "";

        for (IlcSimpleToken t : sentence.getTokens()) {
            //System.err.println("t "+t);
            ret = ret + tokenToKaf(t); //.replaceAll("#S#", Integer.toString(sentence.getId()));
        }

        return ret;

    }

    private String tokenToKaf(IlcSimpleToken token) {
        String ret = "";
        ret = "\t\t\t<wf wid=\"w" + token.getWfid() + "\" sent=\"" + token.getSid() + "\" para=\"1\" offset=\"" + token.getStart_offset()
                + "\" length=\"" + token.getTokenLength() + "\"><![CDATA[" + token.getTheToken() + "]]></wf>\n";
        //ret="\t\t\t<wf wid=\""+wfid+"\" sent=\"#S#\" para=\"#P#\" offset=\""+start_offset+"\" length=\""+tokenLength+"\"><![CDATA["+theToken+"]]></wf>\n";
        return ret;
    }

    private String lemmaToKaf(IlcSimpleLemma lemma) {
        String ret = "";
        ret = "\t\t\t<term tid=\"t" + lemma.getId() + "\" type=\"" + lemma.getType() + "\"  lemma=\"" + lemma.getTheLemma()
                + "\" pos=\"" + lemma.getThePos() + "\">\n\t\t\t\t<span>";
        for (int wfid : lemma.getWfids()) {
            ret = ret + "\n\t\t\t\t\t<target id=\"w" + wfid + "\"/>";
        }
        //ret="\t\t\t<wf wid=\""+wfid+"\" sent=\"#S#\" para=\"#P#\" offset=\""+start_offset+"\" length=\""+tokenLength+"\"><![CDATA["+theToken+"]]></wf>\n";
        ret = ret + "\n\t\t\t\t</span>\n\t\t\t</term>\n";
        return ret;
    }

    /**
     * Write the result of a service in kaf format
     *
     * @param ps Printstream where to write
     */
    public void toKaf(PrintStream ps) {
        String message;
        String routine = "toKaf-WithPs";
        List<IlcSimpleSentence> sentences = result.getSentences();
        //System.err.println("sentences " + sentences);
        LinguisticProcessor linguisticProcessor = result.getLinguisticProcessor();
        String ret = toKaf();// 
        try {
            ps.write(ret.getBytes("UTF-8"));
        } catch (IOException e) {
            message = String.format("IOException in routine %s writing the stream ", routine, e.getMessage());
            Logger.getLogger(CLASS_NAME).log(Level.SEVERE, message);
            System.exit(-1);
        }

    }
    
    public void toKaf(String str,PrintStream ps) {
        String message;
        String routine = "toKaf-str-WithPs";
        
        String ret = toKaf(str);// 
        try {
            ps.write(ret.getBytes("UTF-8"));
        } catch (IOException e) {
            message = String.format("IOException in routine %s writing the stream ", routine, e.getMessage());
            Logger.getLogger(CLASS_NAME).log(Level.SEVERE, message);
            System.exit(-1);
        }

    }

    /* end to kaf */
 /* start totab */
    /**
     * Write the result of a service in tab format
     *
     * @return the tabbed version of the result
     */
    public String toTab() {
        List<IlcSimpleSentence> sentences = result.getSentences();
        //System.err.println("sentences " + sentences);

        String ret = "";
        if (!result.getLemmas().isEmpty()) {
            for (IlcSimpleSentence s : sentences) {
                ret = ret + sentenceWithLemmaToTab(s);
            }

        } else {
            for (IlcSimpleSentence s : sentences) {
                ret = ret + sentenceToTab(s);
            }
        }

        return ret;

    }

    private String sentenceWithLemmaToTab(IlcSimpleSentence sentence) {
        String ret = "";
        for (IlcSimpleToken t : sentence.getTokens()) {

            ret = ret + sentence.getId() + Format.SEP + tokenWithLemmaToTab(t) + "\n";
        }
        return ret;
    }

    private String tokenWithLemmaToTab(IlcSimpleToken token) {
        /* in tabbed format
        tokenid \t start_offset \t lenght \t token 
         */

        String ret = "";
        ret = ret + token.getWfid() + Format.SEP + token.getStart_offset() + Format.SEP + token.getTokenLength() + Format.SEP + token.getTheToken()
                + Format.SEP + token.getLemma().getTheLemma();//+"\n";

        return ret;
    }

    private String sentenceToTab(IlcSimpleSentence sentence) {
        String ret = "";
        for (IlcSimpleToken t : sentence.getTokens()) {

            ret = ret + sentence.getId() + Format.SEP + tokenToTab(t) + "\n";
        }
        return ret;
    }

    private String tokenToTab(IlcSimpleToken token) {
        /* in tabbed format
        tokenid \t start_offset \t lenght \t token 
         */

        String ret = "";
        ret = ret + token.getWfid() + Format.SEP + token.getTheToken();

        return ret;
    }

    /**
     * Write the result of a service in tab format
     *
     * @param ps Printstream where to write
     */
    public void toTab(PrintStream ps) {
        List<IlcSimpleSentence> sentences = result.getSentences();
        //System.err.println("sentences " + sentences);
        String message;
        String routine = "toTab-WithPs";

        String ret = toTab();

        try {
            ps.write(ret.getBytes("UTF-8"));
        } catch (IOException e) {
            message = String.format("IOException in routine %s writing the stream ", routine, e.getMessage());
            Logger.getLogger(CLASS_NAME).log(Level.SEVERE, message);

            System.exit(-1);
        }

    }

    /* end totab */
 /* start toTcf */
    /**
     * Write the result of a service in TCF format
     *
     */
    public void toTcf() {
        String message;
        String routine = "toTcf";
        try {
            WriterTCF writerTcf = new WriterTCF(result, serviceFormat);
            writerTcf.setFormat(format);
            writerTcf.createTcfOutputFromInput();
        } catch (Exception e) {
            message = String.format("IOException in routine %s writing the stream ", routine, e.getMessage());
            Logger.getLogger(CLASS_NAME).log(Level.SEVERE, message);

            System.exit(-1);
        }
    }

    /**
     * Write the result of a service in TCF format
     *
     * @param ps Printstream where to write
     */
    public void toTcf(PrintStream ps) {

        String message;
        String routine = "toTcf-WithPs";
        try {
            WriterTCF writerTcf = new WriterTCF(result, serviceFormat);
            writerTcf.setFormat(format);
            writerTcf.createTcfOutputFromInput(ps);
        } catch (Exception e) {
            message = String.format("IOException in routine %s writing the stream ", routine, e.getMessage());
            Logger.getLogger(CLASS_NAME).log(Level.SEVERE, message);
            System.exit(-1);
        }
    }
    
    /**
     * Write the result of a service in TCF format
     *
     * @param ps Printstream where to write
     */
    public void fromTcfToTcf(TextCorpus tc, PrintStream ps) {

        String message;
        String routine = "fromTcfToTcf-WithPs";
        try {
            WriterTCF writerTcf = new WriterTCF(result, serviceFormat);
            writerTcf.setFormat(format);
            writerTcf.createTcfOutputFromTcfInput(tc, ps);
        } catch (Exception e) {
            message = String.format("IOException in routine %s writing the stream ", routine, e.getMessage());
            Logger.getLogger(CLASS_NAME).log(Level.SEVERE, message);
            System.exit(-1);
        }
    }

    /* end to tcf */
    /**
     * @return the result
     */
    public Result getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(Result result) {
        this.result = result;
    }

    /**
     * @return the format
     */
    public String getFormat() {
        return format;
    }

    /**
     * @param format the format to set
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * @return the serviceFormat
     */
    public String getServiceFormat() {
        return serviceFormat;
    }

    /**
     * @param serviceFormat the serviceFormat to set
     */
    public void setServiceFormat(String serviceFormat) {
        this.serviceFormat = serviceFormat;
    }
}
