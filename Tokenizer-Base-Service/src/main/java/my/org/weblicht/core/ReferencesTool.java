package my.org.weblicht.core;

import eu.clarin.weblicht.wlfxb.api.TextCorpusProcessor;
import eu.clarin.weblicht.wlfxb.api.TextCorpusProcessorException;
import eu.clarin.weblicht.wlfxb.tc.api.NamedEntitiesLayer;
import eu.clarin.weblicht.wlfxb.tc.api.NamedEntity;
import eu.clarin.weblicht.wlfxb.tc.api.PosTag;
import eu.clarin.weblicht.wlfxb.tc.api.PosTagsLayer;
import eu.clarin.weblicht.wlfxb.tc.api.Reference;
import eu.clarin.weblicht.wlfxb.tc.api.ReferencesLayer;
import eu.clarin.weblicht.wlfxb.tc.api.TextCorpus;
import eu.clarin.weblicht.wlfxb.tc.api.Token;
import eu.clarin.weblicht.wlfxb.tc.api.TokensLayer;
import eu.clarin.weblicht.wlfxb.tc.xb.TextCorpusLayerTag;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReferencesTool implements TextCorpusProcessor {

    public static final String MODEL_PATH = "/models/prefs.txt";
    private static final EnumSet<TextCorpusLayerTag> requiredLayers =
            EnumSet.of(
                    TextCorpusLayerTag.TOKENS,
                    TextCorpusLayerTag.POSTAGS,
                    TextCorpusLayerTag.NAMED_ENTITIES);
    private Set<String> model;


    public ReferencesTool() throws TextCorpusProcessorException {

        model = new HashSet<String>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(MODEL_PATH), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0) {
                    model.add(line);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ReferencesTool.class.getName()).log(Level.SEVERE, null, ex);
            throw new TextCorpusProcessorException("Failed reading the model", ex);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(ReferencesTool.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


    public EnumSet<TextCorpusLayerTag> getRequiredLayers() {
        return requiredLayers;
    }

    public void process(TextCorpus textCorpus) throws TextCorpusProcessorException {

        // create references layer, it is empty first
        ReferencesLayer refsLayer = textCorpus.createReferencesLayer(null, null, null);
        // get layers to be processed
        TokensLayer tokensLayer = textCorpus.getTokensLayer();
        PosTagsLayer postagsLayer = textCorpus.getPosTagsLayer();
        NamedEntitiesLayer neLayer = textCorpus.getNamedEntitiesLayer();
        // temporary store last person named entity found
        List<Token> prevPerToken = new ArrayList<Token>();
        for (int i = 0; i < tokensLayer.size(); i++) {
            Token token = tokensLayer.getToken(i);
            PosTag tag = postagsLayer.getTag(token);
            NamedEntity ne = neLayer.getEntity(token);
            if (model.contains(tag.getString()) && !prevPerToken.isEmpty()) {
                List<Token> tagTokens = Arrays.asList(postagsLayer.getTokens(tag));
                // assume they refer to the same person entity, create these references
                List<Reference> referentMentions = new ArrayList<Reference>();
                referentMentions.add(refsLayer.createReference(prevPerToken));
                referentMentions.add(refsLayer.createReference(tagTokens));
                // add this person entity (referent) to the references layer
                refsLayer.addReferent(referentMentions);
                // empty temorary storage valiable
                prevPerToken = new ArrayList<Token>();
            } else if (ne != null && ne.getType().toLowerCase().startsWith("per")) {
                // store it in the temporary variable
                prevPerToken = Arrays.asList(neLayer.getTokens(ne));
            }
        }

    }

}