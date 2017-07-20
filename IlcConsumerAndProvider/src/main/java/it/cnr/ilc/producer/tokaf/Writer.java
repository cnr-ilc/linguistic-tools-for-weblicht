/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.producer.tokaf;

import it.cnr.ilc.consumer.Result;
import it.cnr.ilc.consumer.Vars;
import it.cnr.ilc.ilcsimpletypes.IlcSimpleSentence;
import java.util.List;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class Writer {

    Result result = new Result();

    public String toKaf(Result result) {
        List<IlcSimpleSentence> sentences = result.getSentences();
        LinguisticProcessor linguisticProcessor = result.getLinguisticProcessor();
        String ret = "";
        ret = ret + kafHeader();
        ret = ret + "" + linguisticProcessorToKaf(linguisticProcessor);
        ret = ret + "\t" + "</kafHeader>\n";
        ret = ret + "\t<text>\n";
        for (IlcSimpleSentence s : sentences) {
            ret = ret + s.toKaf();
        }
        ret = ret + "\t</text>\n";
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

    public String linguisticProcessorToKaf(LinguisticProcessor linguisticprocessor) {
        String ret = "";

        ret = "\t\t<linguisticProcessors layer=\"" + linguisticprocessor.getLayer() + "\">\n";
        for (String lp : linguisticprocessor.getLps()) {
            ret = ret + "\t\t\t" + "<lp name=\"" + lp.split("#")[0] + "\" version=\"" + Vars.version + "\" timestamp=\"" + lp.split("#")[1] + "\"/>\n";
        }
        ret = ret + "\t\t</linguisticProcessors>\n";

        return ret;

    }
}
