/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.tokenizer.service.core;

import eu.clarin.weblicht.wlfxb.api.TextCorpusProcessor;
import eu.clarin.weblicht.wlfxb.api.TextCorpusProcessorException;
import eu.clarin.weblicht.wlfxb.tc.api.TextCorpus;
import eu.clarin.weblicht.wlfxb.tc.xb.TextCorpusLayerTag;
import it.cnr.ilc.tokenizer.Main;
import it.cnr.ilc.tokenizer.utils.Vars;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class TokenizerBaseCore implements TextCorpusProcessor {

    private String lang = "";
    private String iFile = "";
    private String oFile = "";
    private String format = "";

    public TokenizerBaseCore(String lang) {
        this.lang = lang;
    }

    @Override
    public EnumSet<TextCorpusLayerTag> getRequiredLayers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void process(TextCorpus tc) throws TextCorpusProcessorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public synchronized void process() {
        String[] args = new String[1];
        boolean goahead = true;

        Main m = new Main();

        goahead = checkArgs(args);

        m.init(goahead);

    }

    private boolean checkArgs(String[] args) {
        boolean ret = true;
        int i = 0;
        if ((args.length % 2) != 0) {
            return false;
        }
        for (String arg : args) {
            switch (arg) {
                case "-l":

                    if (checkLanguages(args[i + 1])) {
                        setLang(args[i + 1]);
                        break;
                    } else {
                        return false;
                    }
                case "-i":
                    setiFile(args[i + 1]);
                    break;
                case "-o":
                    setoFile(args[i + 1]);
                    break;
                case "-f":
                    setFormat(args[i + 1]);
                    break;

            }
            //System.err.println("arg at " + i + "-" + arg + "-");
            i++;
        }

        return true;
    }

    private boolean checkLanguages(String lang) {
        List<String> langs = new ArrayList<>();
        return Vars.langs.contains(lang);

    }

    /**
     * @return the lang
     */
    public String getLang() {
        return lang;
    }

    /**
     * @return the iFile
     */
    public String getiFile() {
        return iFile;
    }

    /**
     * @return the oFile
     */
    public String getoFile() {
        return oFile;
    }

    /**
     * @return the format
     */
    public String getFormat() {
        return format;
    }

    /**
     * @param lang the lang to set
     */
    public void setLang(String lang) {
        this.lang = lang;
    }

    /**
     * @param iFile the iFile to set
     */
    public void setiFile(String iFile) {
        this.iFile = iFile;
    }

    /**
     * @param oFile the oFile to set
     */
    public void setoFile(String oFile) {
        this.oFile = oFile;
    }

    /**
     * @param format the format to set
     */
    public void setFormat(String format) {
        this.format = format;
    }

}
