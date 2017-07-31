/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.panaceaserviceswrapper.app.resources;

import eu.clarin.weblicht.wlfxb.io.TextCorpusStreamed;
import eu.clarin.weblicht.wlfxb.tc.api.TextCorpus;
import eu.clarin.weblicht.wlfxb.tc.xb.TextCorpusLayerTag;
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
import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
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
@Path("panaceaservice/freeling_it")
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

    /**
     * This method analyzes a plain text to produce a tabbed output document
     *
     * @param format the input language
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
    
    @Path("/wl/runservice")
    @POST
    @Consumes(TEXT_TCF_XML)
    public StreamingOutput analyzeTextFromTcf(@QueryParam("format") String format, final InputStream input) {
        String lang = "ita";
        String message;
        //String str = IlcInputToString.convertInputStreamToString(input);
        //InputStream is = input;
        //System.err.println("TEXT TCF -1 " + str);
        String routine = "analyzeTextFromTcf";
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
        if (format.equals(Format.OUT_TCF)) {
//            System.err.println("TEXT TCF 0 " + IlcInputToString.convertInputStreamToString(input));
//            System.err.println("TEXT TCF 1 " + IlcInputToString.convertInputStreamToString(input));
            return tcfProducerFromTcf(format, input);
            
        }
        return null;
    }
    
    @Produces(TEXT_TCF_XML)
    public StreamingOutput tcfProducerFromTcf(String format, InputStream is) {
        ReaderTcf reader = new ReaderTcf();
        TextCorpusStreamed tcs;
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
            tcs = reader.readTcf(is, tempOutputData);
            //tcs = new TextCorpusStreamed(is, requiredLayers);
            System.err.println("TEXT " + tcs.getTextLayer().getText());
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
        return new OutPutWriter(tempOutputFile);
        
    }
    
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
     */
    private void processTcf(String lang, String format, TextCorpus tc, File out) {
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
    
}
