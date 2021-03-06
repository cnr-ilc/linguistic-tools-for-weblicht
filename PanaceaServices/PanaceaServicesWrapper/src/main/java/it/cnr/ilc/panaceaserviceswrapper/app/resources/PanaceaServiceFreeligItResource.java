/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.panaceaserviceswrapper.app.resources;

import eu.clarin.weblicht.wlfxb.io.TextCorpusStreamed;
import eu.clarin.weblicht.wlfxb.tc.api.TextCorpus;
import eu.clarin.weblicht.wlfxb.tc.xb.TextCorpusLayerTag;
import eu.kyotoproject.kaf.KafSaxParser;
import it.cnr.ilc.consumer.ReaderTcf;
import it.cnr.ilc.ilcioutils.IlcInputToString;
import it.cnr.ilc.ilcutils.Format;
import it.cnr.ilc.panaceaserviceswrapper.app.core.PanaceaServiceFreeligItCore;
import it.cnr.ilc.panaceaserviceswrapper.utils.OutPutWriter;
import it.cnr.ilc.provider.Writer;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.EnumSet;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
//@Path("panaceaservice/freeling_it")
@Path("freeling_it")
public class PanaceaServiceFreeligItResource {

    private static final String TEXT_PLAIN = "text/plain";
    private static final String TEXT_TCF_XML = "text/tcf+xml";
    private static final String FALL_BACK_MESSAGE = "Data processing failed";
    private static final String TEMP_FILE_PREFIX = "panaceaservice-output-";
    private static final String TEMP_FILE_SUFFIX = ".xml";
    private String CLASS_NAME = PanaceaServiceFreeligItResource.class.getName();
    private final String context = "panaceaservice/freeling_it";
    private static EnumSet<TextCorpusLayerTag> requiredLayers
            = EnumSet.of(TextCorpusLayerTag.TEXT);
    private Properties prop;

    /**
     * This method analyzes a plain text to produce a tabbed output document
     *
     * @param format the output format
     * @param input the input stream
     * @return the output file
     */
    @Path("runservice")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    //@Produces(MediaType.TEXT_PLAIN)
    public StreamingOutput analyzeTextFromPlain(@QueryParam("format") String format, final InputStream input) {
        String lang = "ita";
        String message;
        String str = IlcInputToString.convertInputStreamToString(input);
//        System.err.println("TEXT " + str);
        String routine = "analyzeTextFromPlain";
        message = String.format("Executing  -%s- in context -%s-", routine, context);
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);

        message = String.format("Calling  the correct producer  according to the format -%s-", format);
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);

        if (format.equals(Format.OUT_TAB)) {
            return tabProducer(str);
        }
        if (format.equals(Format.OUT_KAF)) {
            return kafProducer(str);
        }
        if (format.equals(Format.OUT_TCF)) {
            return tcfProducer(str);
        }
        return null;
    }

    /**
     * This method analyzes a plain text to produce a tabbed output document
     *
     * @param format the output format
     * @param text the string to analyze
     * @return the output file
     */
    @Path("tcf/runservice")
    @POST
    @Consumes(TEXT_TCF_XML)
    //@Produces(TEXT_TCF_XML)
    //public StreamingOutput tokenizeTextFromTcf(@QueryParam("lang") String lang, final InputStream text) {
    public StreamingOutput analyzeTextFromTcf(@QueryParam("format") String format, final InputStream text) {
        OutputStream tempOutputData = null;
        String message;
        String lang = "ita";
        String routine = "analyzeTextFromTcf";
        message = String.format("Executing  -%s- in context -%s-", routine, context);
        String str = null;
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);

        File tempOutputFile = null;
        try {
            tempOutputFile = File.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX);
            tempOutputData = new BufferedOutputStream(new FileOutputStream(tempOutputFile));
        } catch (IOException ex) {
            if (tempOutputData != null) {
                try {
                    message = String.format("IOException -%s- in -%s- with context -%s-", ex.getMessage(), routine, context);
                    Logger
                            .getLogger(CLASS_NAME).log(Level.SEVERE, message);
                    tempOutputData.close();
                } catch (IOException e) {
                    message = String.format("IOException -%s- in -%s- with context -%s-", Response.Status.INTERNAL_SERVER_ERROR, routine, context);
                    Logger
                            .getLogger(CLASS_NAME).log(Level.SEVERE, message);
                    throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
                }
            }
            if (tempOutputFile != null) {
                //System.err.println("XXX DELETE");
                tempOutputFile.delete();
            }
            message = String.format("IOException -%s- in -%s- with context -%s-", Response.Status.INTERNAL_SERVER_ERROR, routine, context);
            Logger
                    .getLogger(CLASS_NAME).log(Level.SEVERE, message);
            throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
        }

        // process incoming TCF and output resulting TCF with new annotation layer(s) added
        //process(input, tempOutputData, tool);
        str = getTextFromTcf(lang, text, tempOutputData);
        if (str != null) {
            if (format.equals(Format.OUT_TAB)) {
                return tabProducer(str);
            }
            if (format.equals(Format.OUT_KAF)) {
                return kafProducer(str);
            }
            if (format.equals(Format.OUT_TCF)) {
                return tcfProducer(str);
            }
            return null;
        }
        return new OutPutWriter(tempOutputFile);
    }

//    @Path("/wl/runservice")
//    @POST
//    @Consumes(TEXT_TCF_XML)
//    public StreamingOutput analyzeTextFromTcf1(@QueryParam("format") String format, final InputStream input) {
//        String lang = "ita";
//        String message;
//        //String str = IlcInputToString.convertInputStreamToString(input);
//        //InputStream is = input;
//        //System.err.println("TEXT TCF -1 " + str);
//        String routine = "analyzeTextFromTcf";
//        message = String.format("Executing  -%s- in context -%s-", routine, context);
//        Logger
//                .getLogger(CLASS_NAME).log(Level.INFO, message);
//
//        message = String.format("Calling  the correct producer  according to the format -%s-", format);
//        Logger
//                .getLogger(CLASS_NAME).log(Level.INFO, message);
//
////        if (format.equals(Format.OUT_TAB)) {
////            return tabProducerFromTcf(str);
////        }
////        if (format.equals(Format.OUT_KAF)) {
////            return kafProducerTcf(str);
////        }
//        if (format.equals(Format.OUT_TCF)) {
////            System.err.println("TEXT TCF 0 " + IlcInputToString.convertInputStreamToString(input));
////            System.err.println("TEXT TCF 1 " + IlcInputToString.convertInputStreamToString(input));
//            return tcfProducerFromTcf(format, input);
//
//        }
//        //return formatProducerFromTcf(format, input);
//        return null;
//    }
    /**
     * Read from a TCF file
     *
     * @param format the output format
     * @param input the text to analyze
     * @return the processed output
     */
    @Path("/kaf/runservice")
    @POST
    @Consumes(MediaType.TEXT_XML)
    public StreamingOutput analyzeTextFromKaf(@QueryParam("format") String format, final InputStream input) {
        String lang = "ita";
        String message;
        String str;//=input.toString();
        KafSaxParser parser = new KafSaxParser();
        parser.parseFile(input);
        //parser.getFullText();
        str = parser.getFullText();

        //String str = IlcInputToString.convertInputStreamToString(input);
        //InputStream is = input;
        //System.err.println("TEXT KAF XML " + str);
        String routine = "analyzeTextFromKaf";
        message = String.format("Executing  -%s- in context -%s-", routine, context);
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);

        message = String.format("Calling  the correct producer  according to the format -%s-", format);
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);

//        if (format.equals(Format.OUT_TAB)) {
//            return tabProducerFromTcf(str);
//        }
//        if (format.equals(Format.OUT_KAF)) {
//            return kafProducerTcf(str);
//        }
        if (format.equals(Format.OUT_TAB)) {
            return tabProducer(str);
        }
        if (format.equals(Format.OUT_KAF)) {
            return kafProducer(str);
        }
        if (format.equals(Format.OUT_TCF)) {
            return tcfProducer(str);
        }
        return null;

    }

    /**
     * Read a TCF file and produce a new one
     *
     * @param format the output format
     * @param is the input stream
     * @return tcf from tcf
     */
    @Produces(TEXT_TCF_XML)
    public StreamingOutput tcfProducerFromTcf(String format, InputStream is) {
        ReaderTcf reader = new ReaderTcf();
        TextCorpusStreamed tcs = null;
        // System.err.println("TEXT TCF IN PROC" + IlcInputToString.convertInputStreamToString(is));
        String lang = "ita";
        String message;
        String routine = "tcfProducerFromTcf";
        OutputStream tempOutputData = null;
        File tempOutputFile = null;
        message = String.format("Executing  -%s- ", routine);
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);
        try {

            tempOutputFile = File.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX);
            tempOutputData = new BufferedOutputStream(new FileOutputStream(tempOutputFile));
            //tcs = new TextCorpusStreamed(is, requiredLayers, tempOutputData, false);
//            tcs = reader.readTcf(is, tempOutputData);

            //System.err.println("TEXT " + tcs.getTextLayer().getText());
        } catch (IOException ex) {
            try {
                tempOutputData.close();
            } catch (IOException e) {
                message = String.format("IOException -%s- in -%s- with context -%s-", e.getMessage(), routine, context);
                Logger
                        .getLogger(CLASS_NAME).log(Level.SEVERE, message);

                throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));

            }

            if (tempOutputFile != null) {
                tempOutputFile.delete();
            }
            message = String.format("IOException -%s- in -%s- with context -%s-", Response.Status.INTERNAL_SERVER_ERROR, routine, context);
            Logger
                    .getLogger(CLASS_NAME).log(Level.SEVERE, message);
            throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
        }

        //processTcf(lang, "tcf", tcs, tempOutputFile);
        getTextFromTcf(lang, is, tempOutputData);
        return new OutPutWriter(tempOutputFile);

    }

    /**
     * Read a TCF file and produce a new one according to format
     *
     * @param format the output format
     * @param is the input stream
     * @return tcf from tcf
     */
    @Produces(TEXT_TCF_XML)
    public StreamingOutput formatProducerFromTcf(String format, InputStream is) {
        ReaderTcf reader = new ReaderTcf();
        TextCorpusStreamed tcs;
        //System.err.println("TEXT TCF IN PROC" + IlcInputToString.convertInputStreamToString(is));
        String lang = "ita";
        String message;
        String routine = "formatProducerFromTcf";
        OutputStream tempOutputData = null;
        File tempOutputFile = null;
        message = String.format("Executing  -%s- ", routine);
        String str = "";
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);
        try {

            tempOutputFile = File.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX);
            tempOutputData = new BufferedOutputStream(new FileOutputStream(tempOutputFile));
            tcs = reader.readTcf(is, tempOutputData);
            //tcs = new TextCorpusStreamed(is, requiredLayers);
            str = tcs.getTextLayer().getText();
            //System.err.println("TEXT " + str);
        } catch (IOException ex) {
            try {
                tempOutputData.close();
            } catch (IOException e) {
                message = String.format("IOException -%s- in -%s- with context -%s-", e.getMessage(), routine, context);
                Logger
                        .getLogger(CLASS_NAME).log(Level.SEVERE, message);

                throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));

            }

            if (tempOutputFile != null) {
                tempOutputFile.delete();
            }
            message = String.format("IOException -%s- in -%s- with context -%s-", Response.Status.INTERNAL_SERVER_ERROR, routine, context);
            Logger
                    .getLogger(CLASS_NAME).log(Level.SEVERE, message);
            throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
        }

        if (format.equals(Format.OUT_TAB)) {
            return tabProducer(str);
        }
        if (format.equals(Format.OUT_KAF)) {
            return kafProducer(str);
        }
        if (format.equals(Format.OUT_TCF)) {
            return tcfProducer(str);
        }
        return null;

    }

    /**
     *
     * @param str the data to process
     * @return the TAB output
     */
    @Produces(MediaType.TEXT_PLAIN)
    public StreamingOutput tabProducer(String str) {
        String lang = "ita";
        String message;
        String routine = "tabProducer";
        OutputStream tempOutputData = null;
        File tempOutputFile = null;
        message = String.format("Executing  -%s- ", routine);
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);
        try {
            tempOutputFile = File.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX);
        } catch (IOException ex) {
            try {
                tempOutputData.close();
            } catch (IOException e) {
                message = String.format("IOException -%s- in -%s- with context -%s-", e.getMessage(), routine, context);
                Logger
                        .getLogger(CLASS_NAME).log(Level.SEVERE, message);

                throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));

            }

            if (tempOutputFile != null) {
                tempOutputFile.delete();
            }
            message = String.format("IOException -%s- in -%s- with context -%s-", Response.Status.INTERNAL_SERVER_ERROR, routine, context);
            Logger
                    .getLogger(CLASS_NAME).log(Level.SEVERE, message);
            throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
        }

        process(lang, "tab", str, tempOutputFile);
        return new OutPutWriter(tempOutputFile);

    }

    /**
     *
     * @param str the data to process
     * @return the KAF output
     */
    @Produces(MediaType.TEXT_XML)
    public StreamingOutput kafProducer(String str) {
        String lang = "ita";
        String message;
        String routine = "kafProducer";
        OutputStream tempOutputData = null;
        File tempOutputFile = null;
        message = String.format("Executing  -%s- ", routine);
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);
        try {
            tempOutputFile = File.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX);
        } catch (IOException ex) {
            try {
                tempOutputData.close();
            } catch (IOException e) {
                message = String.format("IOException -%s- in -%s- with context -%s-", e.getMessage(), routine, context);
                Logger
                        .getLogger(CLASS_NAME).log(Level.SEVERE, message);

                throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));

            }

            if (tempOutputFile != null) {
                tempOutputFile.delete();
            }
            message = String.format("IOException -%s- in -%s- with context -%s-", Response.Status.INTERNAL_SERVER_ERROR, routine, context);
            Logger
                    .getLogger(CLASS_NAME).log(Level.SEVERE, message);
            throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
        }

        process(lang, "kaf", str, tempOutputFile);
        return new OutPutWriter(tempOutputFile);

    }

    /**
     *
     * @param str the data to process
     * @return the TCF output
     */
    @Produces(TEXT_TCF_XML)
    public StreamingOutput tcfProducer(String str) {
        String lang = "ita";
        String message;
        String routine = "tcfProducer";
        OutputStream tempOutputData = null;
        File tempOutputFile = null;
        message = String.format("Executing  -%s- ", routine);
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);
        try {
            tempOutputFile = File.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX);
        } catch (IOException ex) {
            try {
                tempOutputData.close();
            } catch (IOException e) {
                message = String.format("IOException -%s- in -%s- with context -%s-", e.getMessage(), routine, context);
                Logger
                        .getLogger(CLASS_NAME).log(Level.SEVERE, message);

                throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));

            }

            if (tempOutputFile != null) {
                tempOutputFile.delete();
            }
            message = String.format("IOException -%s- in -%s- with context -%s-", Response.Status.INTERNAL_SERVER_ERROR, routine, context);
            Logger
                    .getLogger(CLASS_NAME).log(Level.SEVERE, message);
            throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
        }

        process(lang, "tcf", str, tempOutputFile);
        return new OutPutWriter(tempOutputFile);

    }

    // for language resource
    /**
     *
     * @param lang the input language
     * @param format the final format
     * @param theUrl the url where the text is
     * @return the output of the process
     */
    @Path("lrs")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    /**
     * This service extracts text from an URL.
     */
    public StreamingOutput analyzeTextFromUrl(@QueryParam("lang") String lang, @QueryParam("format") String format, @QueryParam("url") String theUrl) {
        OutputStream tempOutputData = null;
        String message;
        //String lang = "ita";
        String routine = "analyzeTextFromUrl";
        message = String.format("Executing  -%s- in context -%s-", routine, context);
        String str = null;
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);

        File tempOutputFile = null;
        try {
            tempOutputFile = File.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX);
            tempOutputData = new BufferedOutputStream(new FileOutputStream(tempOutputFile));
        } catch (IOException ex) {
            if (tempOutputData != null) {
                try {
                    message = String.format("IOException -%s- in -%s- with context -%s-", ex.getMessage(), routine, context);
                    Logger
                            .getLogger(CLASS_NAME).log(Level.SEVERE, message);
                    tempOutputData.close();
                } catch (IOException e) {
                    message = String.format("IOException -%s- in -%s- with context -%s-", Response.Status.INTERNAL_SERVER_ERROR, routine, context);
                    Logger
                            .getLogger(CLASS_NAME).log(Level.SEVERE, message);
                    throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
                }
            }
            if (tempOutputFile != null) {
                tempOutputFile.delete();
            }
            message = String.format("IOException -%s- in -%s- with context -%s-", Response.Status.INTERNAL_SERVER_ERROR, routine, context);
            Logger
                    .getLogger(CLASS_NAME).log(Level.SEVERE, message);
            throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
        }

        // process incoming TCF and output resulting TCF with new annotation layer(s) added
        //process(input, tempOutputData, tool);
        str = getTextFromUrl(theUrl);
        if (str != null) {
            if (format.equals(Format.OUT_TAB)) {
                return tabProducer(str);
            }
            if (format.equals(Format.OUT_KAF)) {
                return kafProducer(str);
            }
            if (format.equals(Format.OUT_TCF)) {
                return tcfProducer(str);
            }
            return null;
        }
        return new OutPutWriter(tempOutputFile);
    }

    /**
     * Input file is a KAF
     * @param lang the input language
     * @param format the final format
     * @param theUrl the url where the text is
     * @return the output of the process
     */
    @Path("kaf/lrs")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    /**
     * This service extracts text from an URL.
     */
    public StreamingOutput analyzeKafFromUrl(@QueryParam("lang") String lang, @QueryParam("format") String format, @QueryParam("url") String theUrl) {
        OutputStream tempOutputData = null;
        String message;
        KafSaxParser parser = new KafSaxParser();

        //String lang = "ita";
        String routine = "analyzeKafFromUrl";
        message = String.format("Executing  -%s- in context -%s-", routine, context);
        String str = null;
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);

        File tempOutputFile = null;
        try {
            tempOutputFile = File.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX);
            tempOutputData = new BufferedOutputStream(new FileOutputStream(tempOutputFile));
        } catch (IOException ex) {
            if (tempOutputData != null) {
                try {
                    message = String.format("IOException -%s- in -%s- with context -%s-", ex.getMessage(), routine, context);
                    Logger
                            .getLogger(CLASS_NAME).log(Level.SEVERE, message);
                    tempOutputData.close();
                } catch (IOException e) {
                    message = String.format("IOException -%s- in -%s- with context -%s-", Response.Status.INTERNAL_SERVER_ERROR, routine, context);
                    Logger
                            .getLogger(CLASS_NAME).log(Level.SEVERE, message);
                    throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
                }
            }
            if (tempOutputFile != null) {
                tempOutputFile.delete();
            }
            message = String.format("IOException -%s- in -%s- with context -%s-", Response.Status.INTERNAL_SERVER_ERROR, routine, context);
            Logger
                    .getLogger(CLASS_NAME).log(Level.SEVERE, message);
            throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
        }

        // process incoming TCF and output resulting TCF with new annotation layer(s) added
        //process(input, tempOutputData, tool);
        //str = getTextFromUrl(theUrl);
        parser.parseFile(getIputStreamFromUrl(theUrl));
        str = parser.getFullText();
        // str is a kaf document
        //System.out.println("it.cnr.ilc.panaceaserviceswrapper.app.resources.PanaceaServiceFreeligItResource.analyzeKafFromUrl() str " + str);
        if (str != null) {
            if (format.equals(Format.OUT_TAB)) {
                return tabProducer(str);
            }
            if (format.equals(Format.OUT_KAF)) {
                return kafProducer(str);
            }
            if (format.equals(Format.OUT_TCF)) {
                return tcfProducer(str);
            }
            return null;
        }
        return new OutPutWriter(tempOutputFile);
    }
    
    /**
     * Input file is a TCF
     * @param lang the input language
     * @param format the final format
     * @param theUrl the url where the text is
     * @return the output of the process
     */
    @Path("tcf/lrs")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    /**
     * This service extracts text from an URL.
     */
    public StreamingOutput analyzeTcfFromUrl(@QueryParam("lang") String lang, @QueryParam("format") String format, @QueryParam("url") String theUrl) {
        OutputStream tempOutputData = null;
        String message;
        

        //String lang = "ita";
        String routine = "analyzeTcfFromUrl";
        message = String.format("Executing  -%s- in context -%s-", routine, context);
        String str = null;
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);

        File tempOutputFile = null;
        try {
            tempOutputFile = File.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX);
            tempOutputData = new BufferedOutputStream(new FileOutputStream(tempOutputFile));
        } catch (IOException ex) {
            if (tempOutputData != null) {
                try {
                    message = String.format("IOException -%s- in -%s- with context -%s-", ex.getMessage(), routine, context);
                    Logger
                            .getLogger(CLASS_NAME).log(Level.SEVERE, message);
                    tempOutputData.close();
                } catch (IOException e) {
                    message = String.format("IOException -%s- in -%s- with context -%s-", Response.Status.INTERNAL_SERVER_ERROR, routine, context);
                    Logger
                            .getLogger(CLASS_NAME).log(Level.SEVERE, message);
                    throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
                }
            }
            if (tempOutputFile != null) {
                tempOutputFile.delete();
            }
            message = String.format("IOException -%s- in -%s- with context -%s-", Response.Status.INTERNAL_SERVER_ERROR, routine, context);
            Logger
                    .getLogger(CLASS_NAME).log(Level.SEVERE, message);
            throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
        }

        // process incoming TCF and output resulting TCF with new annotation layer(s) added
        //process(input, tempOutputData, tool);
        //str = getTextFromUrl(theUrl);
        str = getTextFromTcf(lang, getIputStreamFromUrl(theUrl), tempOutputData);
        
        // str is a kaf document
        //System.out.println("it.cnr.ilc.panaceaserviceswrapper.app.resources.PanaceaServiceFreeligItResource.analyzeTcfFromUrl() str " + str);
        if (str != null) {
            if (format.equals(Format.OUT_TAB)) {
                return tabProducer(str);
            }
            if (format.equals(Format.OUT_KAF)) {
                return kafProducer(str);
            }
            if (format.equals(Format.OUT_TCF)) {
                return tcfProducer(str);
            }
            return null;
        }
        return new OutPutWriter(tempOutputFile);
    }

    /**
     * This method processes the plain text and creates a tabbed document from
     * the input provided. It calls the corresponding method from the tool.
     *
     * @param lang the language used to load the module
     * @param input the input stream
     * @param out the output file
     */
    private void process(String lang, String format, String str, File out) {
        String message;
        String routine = "analyzeTextFromPlain-process";
        message = String.format("Executing  -%s- with text -%s-", routine, str);
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);
        String[] args = new String[6];
        args[0] = "-l";
        args[1] = lang;
        args[2] = "-s";
        args[3] = "freeling_it";
        args[4] = "-f";
        args[5] = format;
        args[4] = "-sf";
        args[5] = "tagged";
        try {
            PanaceaServiceFreeligItCore tool = new PanaceaServiceFreeligItCore();
            tool.setProp(getProp());
            tool.process(args, str, out);
            PrintStream ps = new PrintStream(out);

            //Writer writer = new Writer(tool.getResult());
            Writer writer = new Writer(tool.getResult(), format, args[5]);
//            writer.setFormat(format);
//            writer.setServiceFormat(args[5]);
            if (format.equals(Format.OUT_TAB)) {
                writer.toTab(ps);
            }
            if (format.equals(Format.OUT_KAF)) {
                writer.toKaf(ps);
            }
            if (format.equals(Format.OUT_TCF)) {

                writer.toTcf(ps);
            }

//           
        } catch (Exception ex) {

            throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
        }

    }

    /**
     * This method processes the plain text and creates a tabbed document from
     * the input provided. It calls the corresponding method from the tool.
     *
     * @param lang the language used to load the module
     * @param input the input stream
     * @param out the output file
     * @deprecated
     */
    private void processTcf1(String lang, String format, TextCorpus tc, File out) {
        String message;
        String routine = "processTcf";
        String str = tc.getTextLayer().getText();
        message = String.format("Executing  -%s- with text -%s-", routine, str);
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);
        String[] args = new String[6];
        args[0] = "-l";
        args[1] = lang;
        args[2] = "-s";
        args[3] = "freeling_it";
        args[4] = "-f";
        args[5] = format;
        args[4] = "-sf";
        args[5] = "tagged";
        try {
            PanaceaServiceFreeligItCore tool = new PanaceaServiceFreeligItCore();
            tool.process(args, str, out);
            PrintStream ps = new PrintStream(out);

            //Writer writer = new Writer(tool.getResult());
            Writer writer = new Writer(tool.getResult(), format, args[5]);
//            writer.setFormat(format);
//            writer.setServiceFormat(args[5]);
//            if (format.equals(Format.OUT_TAB)) {
//                writer.toTab(ps);
//            }
//            if (format.equals(Format.OUT_KAF)) {
//                writer.toKaf(ps);
//            }
            if (format.equals(Format.OUT_TCF)) {

                writer.fromTcfToTcf(tc, ps);
            }

//           
        } catch (Exception ex) {

            throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
        }

    }

    /**
     * Read a TCF corpus and extract the text
     *
     * @param lang language
     * @param input input stream
     * @param output output stream
     * @return the text contained in TCF
     */
    private String getTextFromTcf(String lang, final InputStream input, OutputStream output) {
        TextCorpusStreamed textCorpus = null;
        String str = "";
        String routine = "getTextFromTcf";
        String message = String.format("Executing  -%s-", routine);
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);
        try {

            textCorpus = new TextCorpusStreamed(input, requiredLayers, output, false);
            lang = textCorpus.getLanguage();
            //System.err.println("LANG " + lang);

            str = textCorpus.getTextLayer().getText();
            //System.err.println("TEXT " + str);

        } catch (Exception ex) {
            throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
        } finally {
            try {
                if (textCorpus != null) {
                    // it's important to close the TextCorpusStreamed, otherwise
                    // the TCF XML output will not be written to the end
                    textCorpus.close();
                }
            } catch (Exception ex) {
                throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
            }
        }
        message = message = String.format("Executed  -%s- with extracted text -%s-", routine, str);
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);
        return str;
    }

    /**
     *
     * @param theUrl url
     * @return the string from the url
     */
    private String getTextFromUrl(String theUrl) {

        String routine = "getTextFromUrl";
        String message = String.format("Executing  -%s- from url -%s-", routine, theUrl);
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);
        String str = "";
        str = IlcInputToString.convertInputStreamFromUrlToString(theUrl);
        message = message = String.format("Executed  -%s- with extracted text -%s-", routine, str);
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);
        return str;
    }

    /**
     *
     * @param theUrl url
     * @return the string from the url
     */
    private InputStream getIputStreamFromUrl(String theUrl) {

        InputStream is = null;

        String routine = "getIputStreamFromUrl";
        String message = String.format("Executing  -%s- from url -%s-", routine, theUrl);
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);
        String str = "";
        try {
            is = new URL(theUrl).openStream();

            message = message = String.format("Executed  -%s- from url -%s-", routine, theUrl);
            Logger
                    .getLogger(CLASS_NAME).log(Level.INFO, message);

        } catch (MalformedURLException ex) {
            Logger.getLogger(PanaceaServiceFreeligItResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ioex) {
            Logger.getLogger(PanaceaServiceFreeligItResource.class.getName()).log(Level.SEVERE, null, ioex);
        }
        return is;
    }

    /**
     * Private method to create the response depending on statuses and
     * exceptions
     *
     * @param ex the exception
     * @param status the status
     * @return the response
     */
    private Response createResponse(Exception ex, Response.Status status) {
        String message = ex.getMessage();
        if (message == null) {
            message = FALL_BACK_MESSAGE;
        }
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, message, ex);
        return Response.status(status).entity(message).type(MediaType.TEXT_PLAIN).build();
    }

    /**
     * @return the prop
     */
    public Properties getProp() {
        return prop;
    }

    /**
     * @param prop the prop to set
     */
    public void setProp(Properties prop) {
        this.prop = prop;
    }

}
