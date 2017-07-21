/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.producer;

import eu.clarin.weblicht.wlfxb.api.TextCorpusProcessor;
import eu.clarin.weblicht.wlfxb.api.TextCorpusProcessorException;
import eu.clarin.weblicht.wlfxb.io.WLDObjector;
import eu.clarin.weblicht.wlfxb.tc.api.LemmasLayer;
import eu.clarin.weblicht.wlfxb.tc.api.PosTagsLayer;
import eu.clarin.weblicht.wlfxb.tc.api.Sentence;
import eu.clarin.weblicht.wlfxb.tc.api.SentencesLayer;
import eu.clarin.weblicht.wlfxb.tc.api.TextCorpus;
import eu.clarin.weblicht.wlfxb.tc.api.Token;
import eu.clarin.weblicht.wlfxb.tc.api.TokensLayer;
import eu.clarin.weblicht.wlfxb.tc.xb.TextCorpusLayerTag;
import eu.clarin.weblicht.wlfxb.tc.xb.TextCorpusStored;
import eu.clarin.weblicht.wlfxb.xb.WLData;
import it.cnr.ilc.consumer.Result;
import it.cnr.ilc.ilcsimpletypes.IlcSimpleSentence;
import it.cnr.ilc.ilcsimpletypes.IlcSimpleToken;
import it.cnr.ilc.ilcutils.Format;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class WriterTCF implements TextCorpusProcessor {

    private static final String TEMP_FILE_PREFIX = "tcf-temp-file";
    private static final String TEMP_FILE_SUFFIX = ".xml";
    private Result result;
    private String format;
    private String CLASS_NAME = WriterTCF.class.getName();
    /**
     * a weblicht textcorpus
     */
    private TextCorpusStored textCorpus;

    public WriterTCF(Result result) {
        this.result = result;

    }

    public WriterTCF(Result result, String format) {
        this.result = result;
        this.format = format;

    }

    /**
     * Required layers of the TCF document to be valid
     */
    private static EnumSet<TextCorpusLayerTag> requiredLayers
            = EnumSet.of(TextCorpusLayerTag.TEXT);

    @Override
    public void process(TextCorpus tc) throws TextCorpusProcessorException {
        String input = tc.getTextLayer().getText();
        System.err.println("A");

        PosTagsLayer posesLayer;
        TokensLayer tokensLayer = tc.createTokensLayer();
        SentencesLayer sentencesLayer = tc.createSentencesLayer();
        LemmasLayer lemmasLayer;
        // loop over sentences and tokens
        for (IlcSimpleSentence ilcSentence : result.getSentences()) {
            System.err.println("sent " + ilcSentence);
            List<Token> sentenceTokens = new ArrayList<Token>();
            // loop over token
            for (IlcSimpleToken ilcToken : ilcSentence.getTokens()) {
                Token token = tokensLayer.addToken(ilcToken.getTheToken());
                sentenceTokens.add(token);
            }
            sentencesLayer.addSentence(sentenceTokens);
        }
        //setTextCorpus(textCorpus);

        if (format.equals(Format.SERVICE_OUT_TAG)) { // tokens, pos, lemma layers
            posesLayer = tc.createPosTagsLayer("eagles");
            for (int i = 0; i < tc.getSentencesLayer().size(); i++) {
                // access each sentence
                Sentence sentence = tc.getSentencesLayer().getSentence(i);
                // access tokens of each sentence
                Token[] tokens = tc.getSentencesLayer().getTokens(sentence);
                for (int j = 0; j < tokens.length; j++) {
                    // add part-of-speech annotation to each token
                    posesLayer.addTag(result.getSentences().get(i).getTokens().get(j).getLemma().getThePos(), tokens[j]);
                }
            }
        }
        setTextCorpus((TextCorpusStored) tc);

    }

    public void createTempTcfFileFromInput() {

        OutputStream tempOutputData = null;
        String message;
        String routine = "createTempTcfFileFromInput";
        message = String.format("Executing  -%s- ", routine);
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);
        File tempOutputFile = null;
        TextCorpusStored textCorpusStored = null;
        try {
            textCorpusStored = new TextCorpusStored(result.getLang());
            textCorpusStored.createTextLayer().addText(result.getInput());
            process(textCorpusStored);
            WLData wlData = new WLData(getTextCorpus());
            WLDObjector.write(wlData, System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @param aRequiredLayers the requiredLayers to set
     */
    public static void setRequiredLayers(EnumSet<TextCorpusLayerTag> aRequiredLayers) {
        requiredLayers = aRequiredLayers;
    }

    @Override
    public EnumSet<TextCorpusLayerTag> getRequiredLayers() {
        return requiredLayers;
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
     * @return the textCorpus
     */
    public TextCorpusStored getTextCorpus() {
        return textCorpus;
    }

    /**
     * @param textCorpus the textCorpus to set
     */
    public void setTextCorpus(TextCorpusStored textCorpus) {
        this.textCorpus = textCorpus;
    }

}
