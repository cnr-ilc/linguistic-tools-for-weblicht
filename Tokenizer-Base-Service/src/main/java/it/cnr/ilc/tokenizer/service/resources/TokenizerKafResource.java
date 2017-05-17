/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.tokenizer.service.resources;

import it.cnr.ilc.tokenizer.service.core.TokenizerKafCore;
import it.cnr.ilc.tokenizer.utils.OutPutWriter;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
 * The base resource to register in the environment. Registering this resource, the service will be available at the path kaf/tokenizer
 * which is the root of the service(s).
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
@Path("kaf/tokenizer")
public class TokenizerKafResource {
    
    private static final String TEXT_XML = "text/xml";
    private static final String FALL_BACK_MESSAGE = "Data processing failed";
    private static final String TEMP_FILE_PREFIX = "tok-sent-output-temp";
    private static final String TEMP_FILE_SUFFIX = ".xml";

    
    
    /**
     * This method tokenizes a plain text to produce a TAB document
     * @param lang the input language
     * @param input the input stream
     * @return the output file
     */
    @Path("plain")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(TEXT_XML)
    public StreamingOutput tokenizeTextFromPlain(@QueryParam("lang") String lang, final InputStream input) {
        OutputStream tempOutputData = null;
        File tempOutputFile = null;
        try {
            tempOutputFile = File.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX);
            
        } catch (IOException ex) {
            if (tempOutputData != null) {
                try {
                    tempOutputData.close();
                } catch (IOException e) {
                    throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
                }
            }
            if (tempOutputFile != null) {
                tempOutputFile.delete();
            }
            throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
        }
        process(lang, input, tempOutputFile);

        // if there were no errors reading and writing TCF data, the resulting
        // TCF can be sent as StreamingOutput from the TCF output temporary file
        return new OutPutWriter(tempOutputFile);
    }
    @Path("plainget")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(TEXT_XML)
    public StreamingOutput tokenizeTextFromPlainFromUrl(@QueryParam("lang") String lang, @QueryParam("url") String theUrl, final InputStream input) {
        OutputStream tempOutputData = null;
        File tempOutputFile = null;
        try {
            tempOutputFile = File.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX);
            
        } catch (IOException ex) {
            if (tempOutputData != null) {
                try {
                    tempOutputData.close();
                } catch (IOException e) {
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
     * This method processes the plain text and creates a KAF document from the input provided.
     * It calls the corresponding method from the tool.
     * @param lang the language used to load the module
     * @param input the input stream
     * @param out the output file
     */
    private void process(String lang, InputStream input, File out) {
         try {
             TokenizerKafCore tool = new TokenizerKafCore(lang);
             tool.process(input);
             PrintStream ps = new PrintStream(out);
             tool.getResult().toKaf(ps);
             
         } catch (Exception ex) {
            
            throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
        }
        
    }
    
    /**
     * This method processes the plain text and creates a KAF document from the input provided.
     * It calls the corresponding method from the tool.
     * @param lang the language used to load the module
     * @param input the input stream
     * @param out the output file
     */
    private void processUrl(String lang, String theUrl,InputStream input, File out) {
         try {
             TokenizerKafCore tool = new TokenizerKafCore(lang);
             input=new URL(theUrl).openStream();
             tool.process(input);
             PrintStream ps = new PrintStream(out);
             tool.getResult().toKaf(ps);
             
         } catch (Exception ex) {
            
            throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
        }
        
    }
    
    /**
     * Private method to create the response depending on statuses and exceptions
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
