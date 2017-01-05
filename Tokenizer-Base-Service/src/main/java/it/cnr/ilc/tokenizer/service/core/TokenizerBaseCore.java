/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.tokenizer.service.core;

import eu.clarin.weblicht.wlfxb.api.TextCorpusProcessor;
import eu.clarin.weblicht.wlfxb.api.TextCorpusProcessorException;
import eu.clarin.weblicht.wlfxb.tc.api.SentencesLayer;
import eu.clarin.weblicht.wlfxb.tc.api.TextCorpus;
import eu.clarin.weblicht.wlfxb.tc.api.TokensLayer;
import eu.clarin.weblicht.wlfxb.tc.xb.TextCorpusLayerTag;
import eu.clarin.weblicht.wlfxb.tc.xb.TextCorpusStored;
import it.cnr.ilc.tokenizer.TokenizerCli;
import it.cnr.ilc.tokenizer.types.Result;
import it.cnr.ilc.tokenizer.types.Sentence;
import it.cnr.ilc.tokenizer.types.Token;
import it.cnr.ilc.tokenizer.utils.Vars;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * This class extends eu.clarin.weblicht.wlfxb.api.TextCorpusProcessor. This is
 * needed for a full integration into the weblicht suite
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class TokenizerBaseCore implements TextCorpusProcessor {

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

    /**
     * a weblicht textcorpus
     */
    private TextCorpusStored textCorpus;

    /**
     * Constructor
     *
     * @param lang the language used to select the model for tokenizer text
     */
    public TokenizerBaseCore(String lang) {
        this.lang = lang;
    }

    /**
     * Required layers of the TCF document to be valid
     */
    private static final EnumSet<TextCorpusLayerTag> requiredLayers
            = EnumSet.of(TextCorpusLayerTag.TEXT);

    @Override
    public EnumSet<TextCorpusLayerTag> getRequiredLayers() {
        return requiredLayers;
    }

    /**
     * This method is responsible for processing. The steps are the following
     * <ol>
     * <li>Extract the text from @param tc ;</li>
     * <li>Check if the language is one of the managed. This is needed for
     * loading the correct module which depends on the language;</li>
     * <li>Create a TpkenLayer (for weblicht);</li>
     * <li>Create a SentenceLayer (for weblicht);</li>
     * <li>Initialize the core tokenizer;</li>
     * <li>Execute the run method of the core tokenizer to tokenize @param
     * input;</li>
     * <li>Map tokenizer sentences and tokens to weblicht's. This is necessary
     * to write a valid TCF document;</li>
     * <li>Create TextCorpusStored (for weblicht);</li>
     * </ol>
     *
     * @param tc the textcorpus to analyze
     * @throws TextCorpusProcessorException Any TextCorpusProcessorException
     * thrown
     */
    @Override
    public synchronized void process(TextCorpus tc) throws TextCorpusProcessorException {
        String input = tc.getTextLayer().getText();

        boolean goahead = true;

        goahead = checkLanguages(lang);

        if (goahead) {
            TokensLayer tokensLayer = tc.createTokensLayer();
            SentencesLayer sentencesLayer = tc.createSentencesLayer();
            tokenizerCli = new TokenizerCli();
            Result r = tokenizerCli.run(lang, input);
            for (Sentence s : r.getSentences()) {
                List<eu.clarin.weblicht.wlfxb.tc.api.Token> sentenceTokens = new ArrayList<eu.clarin.weblicht.wlfxb.tc.api.Token>();

                for (Token t : s.getTokens()) {
                    //System.err.println(" token TEXT -" + t.getTheToken() + "- ");
                    eu.clarin.weblicht.wlfxb.tc.api.Token token = tokensLayer.addToken(t.getTheToken());
                    sentenceTokens.add(token);
                }
                sentencesLayer.addSentence(sentenceTokens);
            }
            setTextCorpusStored((TextCorpusStored) tc);

        } else {
            throw new TextCorpusProcessorException("******** Unsopported language " + lang+" *********** \n");
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
     * @return the textCorpus
     */
    public TextCorpusStored getTextCorpusStored() {
        return textCorpus;
    }

    /**
     * @param textCorpus the textCorpus to set
     */
    public void setTextCorpusStored(TextCorpusStored textCorpus) {
        this.textCorpus = textCorpus;
    }

}
