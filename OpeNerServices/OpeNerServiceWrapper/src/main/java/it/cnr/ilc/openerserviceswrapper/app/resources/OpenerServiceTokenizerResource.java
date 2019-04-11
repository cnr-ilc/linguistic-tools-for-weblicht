/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.openerserviceswrapper.app.resources;

import eu.clarin.weblicht.wlfxb.io.TextCorpusStreamed;
import eu.clarin.weblicht.wlfxb.tc.api.TextCorpus;
import eu.clarin.weblicht.wlfxb.tc.xb.TextCorpusLayerTag;
import eu.kyotoproject.kaf.KafSaxParser;
import it.cnr.ilc.consumer.ReaderTcf;
import it.cnr.ilc.ilcioutils.IlcInputToString;
import it.cnr.ilc.ilcutils.Format;
import it.cnr.ilc.openerserviceswrapper.app.core.OpenerServiceTokenizerCore;
import it.cnr.ilc.openerserviceswrapper.app.json.parser.InputDataParser;
import it.cnr.ilc.openerserviceswrapper.app.json.pojo.InputData;
import it.cnr.ilc.openerserviceswrapper.utils.OutPutWriter;
import it.cnr.ilc.provider.Writer;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.EnumSet;
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
@Path("openerservice/tokenizer")
public class OpenerServiceTokenizerResource {

    private static final String TEXT_PLAIN = "text/plain";
    private static final String TEXT_TCF_XML = "text/tcf+xml";
    private static final String FALL_BACK_MESSAGE = "Data processing failed";
    private static final String TEMP_FILE_PREFIX = "panaceaservice-output-";
    private static final String TEMP_FILE_SUFFIX = ".xml";
    private String CLASS_NAME = OpenerServiceTokenizerResource.class.getName();
    private final String context = "openerservice/tokenizer";
    private static EnumSet<TextCorpusLayerTag> requiredLayers
            = EnumSet.of(TextCorpusLayerTag.TEXT);

    /**
     * This method analyzes a plain text to produce a tabbed output document
     *
     * @param format the output format
     * @param jsonData the input stream
     * @return the output file
     */
    @Path("runservice")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    //@Produces(MediaType.TEXT_PLAIN)
    public StreamingOutput analyzeTextFromPlain(@QueryParam("format") String format, String jsonData) {
        String lang = "ita";
        String message;
        String data = "{\"file\":\"/tmp/riccardo.txt\",\"language\":\"ita\",\"iformat\":\"raw\"}";
        jsonData = data;
        System.err.println("TEXT " + data);

        String routine = "analyzeTextFromPlain";
        message = String.format("Executing  -%s- in context -%s-", routine, context);
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);

        message = String.format("Calling  the correct producer  according to the format -%s-", format);
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);

        if (format.equals(Format.OUT_TAB)) {
            return tabProducer(jsonData);
        }
        if (format.equals(Format.OUT_KAF)) {
            return kafProducer(jsonData);
        }
        if (format.equals(Format.OUT_TCF)) {
            return tcfProducer(jsonData);
        }
        return null;
    }

    public StreamingOutput analyzeTextFromPlainOrig(@QueryParam("format") String format, final InputStream input) {
        String lang = "ita";
        String message;
        String str = IlcInputToString.convertInputStreamToString(input);
        System.err.println("TEXT " + str);
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
        System.err.println("TEXT KAF XML " + str);
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

    @Produces(TEXT_TCF_XML)
    public StreamingOutput tcfProducerFromTcf(String format, InputStream is) {
        ReaderTcf reader = new ReaderTcf();
        TextCorpusStreamed tcs = null;
        System.err.println("TEXT TCF IN PROC" + IlcInputToString.convertInputStreamToString(is));
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

    @Produces(TEXT_TCF_XML)
    public StreamingOutput formatProducerFromTcf(String format, InputStream is) {
        ReaderTcf reader = new ReaderTcf();
        TextCorpusStreamed tcs;
        System.err.println("TEXT TCF IN PROC" + IlcInputToString.convertInputStreamToString(is));
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
            System.err.println("TEXT " + str);
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

    @Produces(MediaType.TEXT_PLAIN)
    public StreamingOutput tabProducer(String jsonData) {
        InputData id;

        String lang, iFormat, fileName;
        String message;
        String routine = "tabProducer";
        OutputStream tempOutputData = null;
        File tempOutputFile = null;
        message = String.format("Executing  -%s- ", routine);
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);
        try {
            id = parseInputData(jsonData);
            lang = id.getLanguage();
            iFormat = id.getIformat();
            fileName = id.getFile();
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
        } catch (NullPointerException npe) {
            message = String.format("NullPointerException -%s- in -%s- with context -%s-", npe.getMessage(), routine, context);
            Logger
                    .getLogger(CLASS_NAME).log(Level.SEVERE, message);

            throw new WebApplicationException(createResponse(npe, Response.Status.INTERNAL_SERVER_ERROR));
        }
        // process(lang, "tab", str, tempOutputFile);
        process(lang, "tab", fileName, iFormat, tempOutputFile);
        return new OutPutWriter(tempOutputFile);

    }

    @Produces(MediaType.TEXT_XML)
    public StreamingOutput kafProducer(String jsonData) {
        InputData id;
        String lang, iFormat, fileName;
        String message;
        String routine = "kafProducer";
        OutputStream tempOutputData = null;
        File tempOutputFile = null;
        message = String.format("Executing  -%s- ", routine);
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);
        try {
            id = parseInputData(jsonData);
            lang = id.getLanguage();
            iFormat = id.getIformat();
            fileName = id.getFile();
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
        } catch (NullPointerException npe) {
            message = String.format("NullPointerException -%s- in -%s- with context -%s-", npe.getMessage(), routine, context);
            Logger
                    .getLogger(CLASS_NAME).log(Level.SEVERE, message);

            throw new WebApplicationException(createResponse(npe, Response.Status.INTERNAL_SERVER_ERROR));
        }
        // process(lang, "tab", fileName, iFormat, tempOutputFile);
        process(lang, "kaf", fileName, iFormat, tempOutputFile);
        return new OutPutWriter(tempOutputFile);

    }

    @Produces(TEXT_TCF_XML)
    public StreamingOutput tcfProducer(String jsonData) {
        InputData id;
        String lang, iFormat, fileName;
        String message;
        String routine = "tcfProducer";
        OutputStream tempOutputData = null;
        File tempOutputFile = null;
        message = String.format("Executing  -%s- ", routine);
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);
        try {
            id = parseInputData(jsonData);
            lang = id.getLanguage();
            iFormat = id.getIformat();
            fileName = id.getFile();
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
        } catch (NullPointerException npe) {
            message = String.format("NullPointerException -%s- in -%s- with context -%s-", npe.getMessage(), routine, context);
            Logger
                    .getLogger(CLASS_NAME).log(Level.SEVERE, message);

            throw new WebApplicationException(createResponse(npe, Response.Status.INTERNAL_SERVER_ERROR));
        }
        // process(lang, "tab", str, tempOutputFile);
        process(lang, "tcf", fileName, iFormat, tempOutputFile);
        return new OutPutWriter(tempOutputFile);

    }

    // for language resource
    @Path("lrs")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    /**
     * This service extracts text from an URL.
     */
    public StreamingOutput analyzeTextFromUrl(@QueryParam("lang") String lang, @QueryParam("format") String format, @QueryParam("url") String theUrl, final InputStream input) {
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
     * This method processes the plain text and creates a tabbed document from
     * the input provided. It calls the corresponding method from the tool.
     *
     * @param lang the language used to load the module
     * @param input the input stream
     * @param out the output file lang, "tab", fileName, iFormat, tempOutputFile
     */
    private void process(String lang, String format, String fileName, String iFormat, File out) {
        String message;
        String routine = "analyzeTextFromPlain-process";
        message = String.format("Executing  -%s- with text -%s-", routine, fileName);
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);
        String[] args = new String[10];
        args[0] = "-l";
        args[1] = lang;
        args[2] = "-s";
        args[3] = "tokenizer";
        args[4] = "-if";
        args[5] = iFormat;
        args[6] = "-i";
        args[7] = fileName;
        args[8] = "-f";
        args[9] = format;
        try {
            OpenerServiceTokenizerCore tool = new OpenerServiceTokenizerCore();
            tool.process(args, out);
            PrintStream ps = new PrintStream(out);

            //Writer writer = new Writer(tool.getResult());
            Writer writer = new Writer(tool.getResult(), format, Format.OPENER_SERVICE_OUT_TAG);
            /*
            Writer writer = new Writer(result);
            writer.setFormat(getFormat());
            writer.setServiceFormat(getServiceOutputFormat());
             */
            //Writer writer = new Writer(tool.getResult(), format, args[5]);
//            writer.setFormat(format);
//            writer.setServiceFormat(args[5]);
            if (format.equals(Format.OUT_TAB)) {
                writer.toTab(ps);
            }
            if (format.equals(Format.OUT_KAF)) {
                System.out.println("it.cnr.ilc.openerserviceswrapper.app.resources.OpenerServiceTokenizerResource.process() XXXX "+tool.getOutPutAsKaf());
                writer.toKaf(tool.getOutPutAsKaf(),ps);
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
            OpenerServiceTokenizerCore tool = new OpenerServiceTokenizerCore();
            tool.process(args, out);
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

    private String getTextFromUrl(String theUrl) {

        String routine = "getTextFromUrl";
        String message = String.format("Executing  -%s-", routine);
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

    private InputData parseInputData(String data) {
        String iFile, lang, iFormat;
        String message;
        String routine = "parseInputData";
        boolean ret = false;
        InputData id;
        try {
            id = InputDataParser.getDataFromInput(data);
        } catch (NullPointerException e) {
            message = String.format("NullPointerException reading inputdata in routine %s from data %s", routine, data);
//            //message = "IOException in closing the stream " + e.getMessage();
//            Logger
//                    .getLogger(CLASS_NAME).log(Level.SEVERE, message);
            return null;
//
        }
        return id;
//        if (id != null) {
//            lang = id.getLanguage();
//            System.out.println("it.cnr.ilc.openerserviceswrapper.app.resources.OpenerServiceTokenizerResource.parseInputData() " + lang);
////            checklanguages(lang);
////            iFile=id.getFile();
////            iFormat=id.getIformat();
////            if (checkInputFormat(iFormat) && checklanguages(lang)){
////                iFile=id.getFile();
////                setiFile(iFile);
////                setInputFormat(iFormat);
////                ret=true;
//        } else {
//            ret = false;
//        }
//
////        } else {
////            message = String.format("IOException reading inputdata in routine %s from data %s", routine, data);
////            //message = "IOException in closing the stream " + e.getMessage();
////            Logger
////                    .getLogger(CLASS_NAME).log(Level.SEVERE, message);
////
////            //theservice.printHelp();
////            System.exit(-1);
////
////        }
//        return id;
//    
    }

}
