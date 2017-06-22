/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.tokenizer.service.resources;

import eu.clarin.weblicht.wlfxb.api.TextCorpusProcessorException;
import eu.clarin.weblicht.wlfxb.io.TextCorpusStreamed;
import eu.clarin.weblicht.wlfxb.io.WLDObjector;
import eu.clarin.weblicht.wlfxb.io.WLFormatException;
import eu.clarin.weblicht.wlfxb.tc.xb.TextCorpusStored;
import eu.clarin.weblicht.wlfxb.xb.WLData;
import it.cnr.ilc.tokenizer.service.core.TokenizerBaseCore;
import it.cnr.ilc.tokenizer.utils.InputToString;
import it.cnr.ilc.tokenizer.utils.OutPutWriter;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
 * The base resource to register in the environment. Registering this resource,
 * the service will be available at the path wl/tokenizer which is the root of
 * the service(s).
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
@Path("wl/tokenizer")
public class TokenizerBaseResource {

    private static final String TEXT_TCF_XML = "text/tcf+xml";
    private static final String FALL_BACK_MESSAGE = "Data processing failed";
    private static final String TEMP_FILE_PREFIX = "tok-sent-output-temp";
    private static final String TEMP_FILE_SUFFIX = ".xml";
    private String CLASS_NAME = TokenizerBaseResource.class.getName();
    private String context = "services/wl";

    @Path("plain")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(TEXT_TCF_XML)
    /**
     * This service tokenizes from a plain text.
     */
    public StreamingOutput tokenizeTextFromPlain(@QueryParam("lang") String lang, final InputStream input) {
        OutputStream tempOutputData = null;
        String message;
        String routine = "tokenizeTextFromPlain";
        message = String.format("Executing  -%s- in context -%s-", routine, context);
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
        process(lang, input, tempOutputData);

        // if there were no errors reading and writing TCF data, the resulting
        // TCF can be sent as StreamingOutput from the TCF output temporary file
        return new OutPutWriter(tempOutputFile);
    }
    
    /*
    starting part for managing the TCF input files
     */
    @Path("tcf")
    @POST
    @Consumes(TEXT_TCF_XML)
    @Produces(TEXT_TCF_XML)
    public StreamingOutput tokenizeTextFromTcf(@QueryParam("lang") String lang, final InputStream input) {
        OutputStream tempOutputData = null;
        String message;
        String routine = "tokenizeTextFromTcf";
        message = String.format("Executing  -%s- in context -%s-", routine, context);
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
        processTcf(lang, input, tempOutputData);
        return new OutPutWriter(tempOutputFile);
    }

    @Path("lrs")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(TEXT_TCF_XML)
    /**
     * This service tokenizes from a plain text.
     */
    public StreamingOutput tokenizeTextFromPlainFromUrl(@QueryParam("lang") String lang, @QueryParam("url") String theUrl, final InputStream input) {

        String message;
        String submessage;
        String routine = "tokenizeTextFromPlainFromUrl";
        message = String.format("Executing  -%s- in context -%s-", routine, context);
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);
        OutputStream tempOutputData = null;
        submessage = String.format("***** FOR LRS WL -GET INVOCATION:\n\t"
                + "***** FOR LRS WL -GET INVOCATION lang parameter -%s- ******\n\t"
                + "***** FOR LRS WL -GET INVOCATION url parameter -%s- ******\n\t"
                + "***** FOR LRS WL -GET INVOCATION END *****\n\n", lang, theUrl);
        message = String.format("-%s-\n", submessage);
       
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);

//        System.err.println("***** FOR LRS WL -POST INVOCATION:");
//        System.err.println("\t***** FOR LRS WL TCF -POST INVOCATION lang parameter :" + lang + " ******");
//        System.err.println("\t***** FOR LRS WL TCF -POST INVOCATION url parameter :" + theUrl + " ******");
//        System.err.println("***** FOR LRS WL -POST INVOCATION END *****\n\n");
        File tempOutputFile = null;
        try {
            tempOutputFile = File.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX);
            tempOutputData = new BufferedOutputStream(new FileOutputStream(tempOutputFile));
        } catch (IOException ex) {
            if (tempOutputData != null) {
                try {
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
            throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
        }
        processUrl(lang, theUrl, input, tempOutputData);

        // if there were no errors reading and writing TCF data, the resulting
        // TCF can be sent as StreamingOutput from the TCF output temporary file
        return new OutPutWriter(tempOutputFile);
    }

    /**
     * This method processes the plain text and creates a TextCorpusStored from
     * the input provided. It calls the corresponding method from the tool. Uses
     * weblicht apis (WLData and WLDObjector) to write the annotated file
     *
     * @param lang the language used to load the module
     * @param input the input stream
     * @param output the output stream
     */
    private void process(String lang, InputStream input, OutputStream output) {
        //System.err.println("LANG -" + lang + "- ");
        //Logger.getLogger(this.getClass().getName()).log(Level.INFO, "MESSAGE -" + input.toString() + "- ");
        //TextCorpusStreamed textCorpus = null;
        TextCorpusStored textCorpusStored = null;

        try {

            textCorpusStored = new TextCorpusStored(lang);
            textCorpusStored.createTextLayer().addText(InputToString.convertInputStreamToString(input));
            TokenizerBaseCore tool = new TokenizerBaseCore(lang);

// process TextCorpus and create new annotation layer(s) with your tool
            tool.process(textCorpusStored);

            WLData wlData = new WLData(tool.getTextCorpusStored());
            WLDObjector.write(wlData, output);

        } catch (TextCorpusProcessorException ex) {
            throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
        } catch (Exception ex) {

            throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
        } finally {
            try {
                if (textCorpusStored != null) {
                    // it's important to close the TextCorpusStreamed, otherwise
                    // the TCF XML output will not be written to the end
                    // textCorpusStored.;
                }
            } catch (Exception ex) {
                throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
            }
        }
    }

    /**
     * This method processes the plain text and creates a TextCorpusStored from
     * the input provided. It calls the corresponding method from the tool. Uses
     * weblicht apis (WLData and WLDObjector) to write the annotated file
     *
     * @param lang the language used to load the module
     * @param input the input stream
     * @param output the output stream
     */
    private void processUrl(String lang, String theUrl, InputStream input, OutputStream output) {
        //System.err.println("LANG -" + lang + "- ");
        //Logger.getLogger(this.getClass().getName()).log(Level.INFO, "MESSAGE -" + input.toString() + "- ");
        //TextCorpusStreamed textCorpus = null;
        TextCorpusStored textCorpusStored = null;

        try {

            textCorpusStored = new TextCorpusStored(lang);
            textCorpusStored.createTextLayer().addText(InputToString.convertInputStreamFromUrlToString(theUrl));
            TokenizerBaseCore tool = new TokenizerBaseCore(lang);

// process TextCorpus and create new annotation layer(s) with your tool
            tool.process(textCorpusStored);

            WLData wlData = new WLData(tool.getTextCorpusStored());
            WLDObjector.write(wlData, output);

        } catch (TextCorpusProcessorException ex) {
            throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
        } catch (Exception ex) {

            throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
        } finally {
            try {
                if (textCorpusStored != null) {
                    // it's important to close the TextCorpusStreamed, otherwise
                    // the TCF XML output will not be written to the end
                    // textCorpusStored.;
                }
            } catch (Exception ex) {
                throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
            }
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

    

    private void processTcf(String lang, final InputStream input, OutputStream output) {
        TextCorpusStreamed textCorpus = null;
        try {
            TokenizerBaseCore tool = new TokenizerBaseCore(lang);
            // create TextCorpus object from the client request input,
            // only required annotation layers will be read into the object
            textCorpus = new TextCorpusStreamed(input, tool.getRequiredLayers(), output, false);
            // process TextCorpus and create new annotation layer(s) with your tool
            tool.process(textCorpus);
        } catch (TextCorpusProcessorException ex) {
            throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
        } catch (WLFormatException ex) {
            throw new WebApplicationException(createResponse(ex, Response.Status.BAD_REQUEST));
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
    }

}
