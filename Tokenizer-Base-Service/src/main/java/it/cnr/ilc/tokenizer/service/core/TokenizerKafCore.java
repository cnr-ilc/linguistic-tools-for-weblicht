/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.tokenizer.service.core;

import eu.clarin.weblicht.wlfxb.api.TextCorpusProcessorException;
import it.cnr.ilc.tokenizer.TokenizerCli;
import it.cnr.ilc.tokenizer.types.Result;
import it.cnr.ilc.tokenizer.utils.InputToString;
import it.cnr.ilc.tokenizer.utils.Vars;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class TokenizerKafCore {

    /**
     * the language
     */
    private String lang = "";

    /**
     * input file
     */
    private String iFile = "";

    /**
     * output file
     */
    private String oFile = "";

    /**
     * output format
     */
    private String format = "";

    /**
     * This is &quote;injection&quote; of the standing alone tokenizer software
     */
    private TokenizerCli tokenizerCli;
    
    private Result result=new Result();

    

    /**
     * Constructor
     *
     * @param lang the language used to select the model for tokenizer text
     */
    public TokenizerKafCore(String lang) {
        this.lang = lang;
    }

    /**
     * This method is responsible for processing. The steps are the following
     * <ol>
     * <li>Extract the text from @param tc ;</li>
     * <li>Check if the language is one of the managed. This is needed for
     * loading the correct module which depends on the language;</li>
     * <li>Initialize the core tokenizer;</li>
     * <li>Execute the run method of the core tokenizer to tokenize @param
     * input;</li>
     * </ol>
     *
     * @param is the inputstream to analyze
     * @throws TextCorpusProcessorException Any TextCorpusProcessorException
     * thrown
     */
    public synchronized void process(InputStream is) throws Exception {
        String input = InputToString.convertInputStreamToString(is);
        //System.err.println("input "+input);
        boolean goahead = true;

        goahead = checkLanguages(lang);

        if (goahead) {
            
            tokenizerCli = new TokenizerCli();
            result = tokenizerCli.run(lang, input);
            
            setResult(result);
       

        } else {
            throw new Exception("******** Unsopported language " + lang + " *********** \n");
        }
    }

    /**
     * check language
     *
     * @param lang the language
     * @return true if lang is supported
     */
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

   

}
