/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.tokenizer.service.resources;

import it.cnr.ilc.tokenizer.service.core.TokenizerTabCore;
import it.cnr.ilc.tokenizer.utils.OutPutWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
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
 * the service will be available at the path kaf/tokenizer which is the root of
 * the service(s).
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
@Path("tab/tokenizer")
public class TokenizerTabResource {

    private static final String TEXT_PLAIN = "text/plain";
    private static final String FALL_BACK_MESSAGE = "Data processing failed";
    private static final String TEMP_FILE_PREFIX = "tok-sent-output-temp";
    private static final String TEMP_FILE_SUFFIX = ".xml";
    private String CLASS_NAME = TokenizerTabResource.class.getName();
    private String context = "services/tab";

    /**
     * This method tokenizes a plain text to produce a valid KAF document
     *
     * @param lang the input language
     * @param input the input stream
     * @return the output file
     */
    @Path("plain")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public StreamingOutput tokenizeTextFromPlain(@QueryParam("lang") String lang, final InputStream input) {
        String message;
        String routine = "tokenizeTextFromPlain";
        message = String.format("Executing  -%s- in context -%s-", routine, context);
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);
        OutputStream tempOutputData = null;
        File tempOutputFile = null;
        try {
            tempOutputFile = File.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX);

        } catch (IOException ex) {
            if (tempOutputData != null) {
                try {
                    tempOutputData.close();
                } catch (IOException e) {

                    message = String.format("IOException -%s- in -%s- with context -%s-", e.getMessage(), routine, context);
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
        process(lang, input, tempOutputFile);

        // if there were no errors reading and writing TCF data, the resulting
        // TCF can be sent as StreamingOutput from the TCF output temporary file
        return new OutPutWriter(tempOutputFile);
    }

    @Path("lrs")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public StreamingOutput tokenizeTextFromPlainFromUrl(@QueryParam("lang") String lang, @QueryParam("url") String theUrl, final InputStream input) {
        String message;
        String submessage;
        String routine = "tokenizeTextFromPlainFromUrl";
        message = String.format("Executing  -%s- in context -%s-\n", routine, context);
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);
        OutputStream tempOutputData = null;
        submessage = String.format("***** FOR LRS TAB -GET INVOCATION:\n\t"
                + "***** FOR LRS TAB -GET INVOCATION lang parameter -%s- ******\n\t"
                + "***** FOR LRS TAB -GET INVOCATION url parameter -%s- ******\n\t"
                + "***** FOR LRS TAB -GET INVOCATION END *****\n\n", lang, theUrl);
        message = String.format("-%s-\n", submessage);
        Logger
                .getLogger(CLASS_NAME).log(Level.INFO, message);
        File tempOutputFile = null;
        try {
            tempOutputFile = File.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX);

        } catch (IOException ex) {
            message = String.format("IOException -%s- in -%s- with context -%s-", ex.getMessage(), routine, context);
            Logger
                    .getLogger(CLASS_NAME).log(Level.SEVERE, message);
            if (tempOutputData != null) {
                try {
                    message = String.format("IOException -%s- in -%s- with context -%s-", Response.Status.INTERNAL_SERVER_ERROR, routine, context);
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
            throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
        }
        processUrl(lang, theUrl, input, tempOutputFile);

        // if there were no errors reading and writing TCF data, the resulting
        // TCF can be sent as StreamingOutput from the TCF output temporary file
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
    private void process(String lang, InputStream input, File out) {
        try {
            TokenizerTabCore tool = new TokenizerTabCore(lang);
            tool.process(input);
            PrintStream ps = new PrintStream(out);
            tool.getResult().toTab(ps);

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
    private void processUrl(String lang, String theUrl, InputStream input, File out) {
        try {
            TokenizerTabCore tool = new TokenizerTabCore(lang);
            input = new URL(theUrl).openStream();
            tool.process(input);
            PrintStream ps = new PrintStream(out);
            tool.getResult().toTab(ps);

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
