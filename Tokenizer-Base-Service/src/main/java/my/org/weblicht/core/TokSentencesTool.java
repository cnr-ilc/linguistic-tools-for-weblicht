package my.org.weblicht.core;

import eu.clarin.weblicht.wlfxb.api.TextCorpusProcessor;
import eu.clarin.weblicht.wlfxb.api.TextCorpusProcessorException;
import eu.clarin.weblicht.wlfxb.tc.api.SentencesLayer;
import eu.clarin.weblicht.wlfxb.tc.api.TextCorpus;
import eu.clarin.weblicht.wlfxb.tc.api.Token;
import eu.clarin.weblicht.wlfxb.tc.api.TokensLayer;
import eu.clarin.weblicht.wlfxb.tc.xb.TextCorpusLayerTag;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 *
 * @author Yana Panchenko
 */
public class TokSentencesTool implements TextCorpusProcessor {

    private static final EnumSet<TextCorpusLayerTag> requiredLayers =
            EnumSet.of(TextCorpusLayerTag.TEXT);

    public EnumSet<TextCorpusLayerTag> getRequiredLayers() {
        return requiredLayers;
    }

    public void process(TextCorpus textCorpus) throws TextCorpusProcessorException {
        List<List<String>> sentencesTokenStrings = tokenizeAndDetectSentences(textCorpus.getTextLayer().getText());
        // create tokens layer, it is empty first
        TokensLayer tokensLayer = textCorpus.createTokensLayer();
        // create sentences layer, it is empty first
        SentencesLayer sentencesLayer = textCorpus.createSentencesLayer();
        for (List<String> sentenceTokenStrings : sentencesTokenStrings) {
            // temporary storage for sentence tokens
            List<Token> sentenceTokens = new ArrayList<Token>();
            for (String tokenString : sentenceTokenStrings) {
                // create and add Token objects to the tokens layer
                 Token token = tokensLayer.addToken(tokenString);
                 sentenceTokens.add(token);
            }
            //create and add Sentence to the sentences layer
            sentencesLayer.addSentence(sentenceTokens);
        }
    }

    private List<List<String>> tokenizeAndDetectSentences(String text) {
        List<List<String>> sentencesTokenStrings = new ArrayList<List<String>>();
        StringBuilder tokenBuilder = new StringBuilder();
        List<String> sentenceTokenStrings = new ArrayList<String>();
        for (int i = 0; i < text.length(); i++) {
            if (Character.isSpaceChar(text.charAt(i))) {
                if (tokenBuilder.length() > 0) {
                    sentenceTokenStrings.add(tokenBuilder.toString());
                    tokenBuilder = new StringBuilder();
                }
            } else if (text.charAt(i) == '.' || text.charAt(i) == '?' || text.charAt(i) == '!') {
                sentenceTokenStrings.add(tokenBuilder.toString());
                sentenceTokenStrings.add(text.substring(i, i + 1));
                tokenBuilder = new StringBuilder();
                sentencesTokenStrings.add(sentenceTokenStrings);
                sentenceTokenStrings = new ArrayList<String>();
            } else if (tokenBuilder.length() > 0 && !Character.isLetter(text.charAt(i))) {
                sentenceTokenStrings.add(tokenBuilder.toString());
                sentenceTokenStrings.add(text.substring(i, i + 1));
                tokenBuilder = new StringBuilder();
            } else {
                tokenBuilder.append(text.charAt(i));
            }
        }
        return sentencesTokenStrings;
    }
}