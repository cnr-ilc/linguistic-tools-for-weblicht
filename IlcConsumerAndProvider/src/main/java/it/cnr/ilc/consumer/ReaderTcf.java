/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.consumer;

import eu.clarin.weblicht.wlfxb.io.TextCorpusStreamed;
import eu.clarin.weblicht.wlfxb.io.WLFormatException;
import eu.clarin.weblicht.wlfxb.tc.api.TextCorpus;
import eu.clarin.weblicht.wlfxb.tc.xb.TextCorpusLayerTag;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.EnumSet;

/**
 * This class reads a valid tcf and adds new annotations layers. This class is used by the rest services
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
public class ReaderTcf {
    private static final String TEMP_FILE_PREFIX = "tcf-input-file";
    private static final String TEMP_FILE_SUFFIX = ".xml";

    /**
     * @return the requiredLayers
     */
    public static EnumSet<TextCorpusLayerTag> getRequiredLayers() {
        return requiredLayers;
    }

    /**
     * @param aRequiredLayers the requiredLayers to set
     */
    public static void setRequiredLayers(EnumSet<TextCorpusLayerTag> aRequiredLayers) {
        requiredLayers = aRequiredLayers;
    }
    private Result result;
    private String serviceFormat;
    private String format;
    private String CLASS_NAME = ReaderTcf.class.getName();
    /**
     * a weblicht textcorpus
     */
    private TextCorpus textCorpus;
    
    /**
     * Required layers of the TCF document to be valid
     */
    private static EnumSet<TextCorpusLayerTag> requiredLayers
            = EnumSet.of(TextCorpusLayerTag.TEXT);
    
    /**
     * Return a TextCorpusStreamed from input
     * @param is input stream
     * @param os output stream
     * @return the new TextCorpusStreamed
     * @throws WLFormatException  FormatException
     */
    public TextCorpusStreamed readTcf(InputStream is, OutputStream os) throws WLFormatException {
        TextCorpusStreamed tcs = new TextCorpusStreamed(is, getRequiredLayers(), os);
        //System.err.println("STR "+tcs.getTextLayer().getText());
        return tcs;
    }
    
}
