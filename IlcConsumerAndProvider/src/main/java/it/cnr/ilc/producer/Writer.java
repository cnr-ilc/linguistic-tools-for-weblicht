/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.producer;

import it.cnr.ilc.consumer.Result;
import it.cnr.ilc.consumer.Vars;
import it.cnr.ilc.ilcsimpletypes.IlcSimpleLemma;
import it.cnr.ilc.ilcsimpletypes.IlcSimpleSentence;
import it.cnr.ilc.ilcsimpletypes.IlcSimpleToken;
import it.cnr.ilc.ilcutils.Format;
import java.util.List;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class Writer {

    private Result result = new Result();
    private String format;

    public Writer() {
    }

    public Writer(Result result) {
        this.result = result;
    }

    /* start tokaf */
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

    /* end to kaf */
 /* start totab */
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

    /* end totab */
 /* start toTcf */
    public String toTcf() {
        WriterTCF writerTcf = new WriterTCF(result, format);
        writerTcf.createTempTcfFileFromInput();
        String ret = "IMHERE ";

        return ret;

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
}
