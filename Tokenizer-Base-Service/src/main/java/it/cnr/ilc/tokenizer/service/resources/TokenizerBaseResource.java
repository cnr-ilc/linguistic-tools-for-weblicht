/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.tokenizer.service.resources;

import eu.clarin.weblicht.wlfxb.api.TextCorpusProcessor;
import eu.clarin.weblicht.wlfxb.api.TextCorpusProcessorException;
import eu.clarin.weblicht.wlfxb.io.TextCorpusStreamed;
import eu.clarin.weblicht.wlfxb.io.WLFormatException;
import eu.clarin.weblicht.wlfxb.tc.xb.TextCorpusLayerTag;
import eu.clarin.weblicht.wlfxb.tc.xb.TextCorpusStored;
import it.cnr.ilc.tokenizer.service.core.TokenizerBaseCore;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import my.org.weblicht.resources.StreamingTempFileOutput;
import it.cnr.ilc.tokenizer.utils.Utilities;
import java.io.StringWriter;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Riccardo Del Gratta &lt;riccardo.delgratta@ilc.cnr.it&gt;
 */
@Path("tokenizer")
public class TokenizerBaseResource {

    private static final String TEXT_TCF_XML = "text/tcf+xml";
    private static final String FALL_BACK_MESSAGE = "Data processing failed";
    private static final String TEMP_FILE_PREFIX = "tok-sent-output-temp";
    private static final String TEMP_FILE_SUFFIX = ".xml";

    @Path("tok")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(TEXT_TCF_XML)
    public StreamingOutput processWithStreaming(@QueryParam("lang") String lang, final InputStream input) {
        OutputStream tempOutputData = null;
        File tempOutputFile = null;
        try {
            tempOutputFile = File.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX);
            tempOutputData = new BufferedOutputStream(new FileOutputStream(tempOutputFile));
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
        process(lang, input, tempOutputData);

        // if there were no errors reading and writing TCF data, the resulting
        // TCF can be sent as StreamingOutput from the TCF output temporary file
        return new StreamingTempFileOutput(tempOutputFile);
    }

    private void process(String lang, InputStream input, OutputStream output) {
        System.err.println("LANG -" + lang + "- ");
        //Logger.getLogger(this.getClass().getName()).log(Level.INFO, "MESSAGE -" + input.toString() + "- ");
        TextCorpusStreamed textCorpus = null;

        try {

            TextCorpusStored textCorpusStored = new TextCorpusStored(lang);
            textCorpusStored.createTextLayer().addText(convertInputStreamToString(input));
            TextCorpusProcessor tool = new TokenizerBaseCore(lang);

            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "MESSAGE -" + textCorpusStored.getTextLayer().getText() + "- ");

// process TextCorpus and create new annotation layer(s) with your tool
            tool.process(textCorpusStored);
        } catch (TextCorpusProcessorException ex) {
            throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
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

    private Response createResponse(Exception ex, Response.Status status) {
        String message = ex.getMessage();
        if (message == null) {
            message = FALL_BACK_MESSAGE;
        }
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, message, ex);
        return Response.status(status).entity(message).type(MediaType.TEXT_PLAIN).build();
    }

    private String convertInputStreamToString(InputStream is) {
        StringWriter writer = new StringWriter();
        String encoding = "UTF-8";
        String message = "";
        String theString = "";
        try {
            IOUtils.copy(is, writer, encoding);
            theString = writer.toString();
        } catch (Exception e) {
            message = "IOException in coverting the stream into a string " + e.getMessage();
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, message);
        }

        System.err.println("DDDD " + theString);
        IOUtils.closeQuietly(is);
        IOUtils.closeQuietly(writer);
        return theString;
    }

}
