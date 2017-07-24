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
import java.io.PrintStream;
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

    /**
     * Constructor
     *
     * @param result the result of the service
     */
    public WriterTCF(Result result) {
        this.result = result;

    }

    /**
     * Constructor
     *
     * @param result the result of the service
     * @param serviceFormat the output format of the service
     */
    public WriterTCF(Result result, String serviceFormat) {
        this.result = result;
        this.format = serviceFormat;

    }

    /**
     * Required layers of the TCF document to be valid
     */
    private static EnumSet<TextCorpusLayerTag> requiredLayers
            = EnumSet.of(TextCorpusLayerTag.TEXT);

    @Override
    public void process(TextCorpus tc) throws TextCorpusProcessorException {
        //String input = tc.getTextLayer().getText();

        PosTagsLayer posesLayer;
        TokensLayer tokensLayer = tc.createTokensLayer();
        SentencesLayer sentencesLayer = tc.createSentencesLayer();
        LemmasLayer lemmasLayer;
        // loop over sentences and tokens
        for (IlcSimpleSentence ilcSentence : result.getSentences()) {
//            System.err.println("sent " + ilcSentence);
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
            lemmasLayer = tc.createLemmasLayer();
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

            for (int i = 0; i < tc.getSentencesLayer().size(); i++) {
                // access each sentence
                Sentence sentence = tc.getSentencesLayer().getSentence(i);
                // access tokens of each sentence
                Token[] tokens = tc.getSentencesLayer().getTokens(sentence);
                for (int j = 0; j < tokens.length; j++) {
                    // add part-of-speech annotation to each token
                    lemmasLayer.addLemma(result.getSentences().get(i).getTokens().get(j).getLemma().getTheLemma(), tokens[j]);
                }

            }

        }
        setTextCorpus((TextCorpusStored) tc);

    }

    /**
     * create the TCF output
     *
     * @param ps the printstream where to write
     */
    public void createTcfOutputFromInput(PrintStream ps) {

        //OutputStream tempOutputData = null;
        String message;
        String routine = "createTempTcfFileFromInput";

        TextCorpusStored textCorpusStored = null;
        try {
            textCorpusStored = new TextCorpusStored(result.getLang());

            textCorpusStored.createTextLayer().addText(result.getInput());
            process(textCorpusStored);
            WLData wlData = new WLData(getTextCorpus());
            WLDObjector.write(wlData, ps);
        } catch (Exception e) {
            message = String.format("Error in routine -%s- with message ", routine, e.getMessage());
            Logger
                    .getLogger(CLASS_NAME).log(Level.INFO, message);

        }

    }

    /**
     * create the TCF output  
     */
    public void createTcfOutputFromInput() {

        //OutputStream tempOutputData = null;
        String message;
        String routine = "createTempTcfFileFromInput";

        TextCorpusStored textCorpusStored = null;
        try {
            textCorpusStored = new TextCorpusStored(result.getLang());

            textCorpusStored.createTextLayer().addText(result.getInput());
            process(textCorpusStored);
            WLData wlData = new WLData(getTextCorpus());
            WLDObjector.write(wlData, System.out);
        } catch (Exception e) {
            message = String.format("Error in routine -%s- with message ", routine, e.getMessage());
            Logger
                    .getLogger(CLASS_NAME).log(Level.INFO, message);

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
