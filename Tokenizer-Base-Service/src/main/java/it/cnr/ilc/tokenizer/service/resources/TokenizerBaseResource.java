/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.tokenizer.service.resources;

import eu.clarin.weblicht.wlfxb.io.TextCorpusStreamed;
import eu.clarin.weblicht.wlfxb.io.WLFormatException;
import eu.clarin.weblicht.wlfxb.tc.xb.TextCorpusLayerTag;
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

    private void process(String lang, InputStream input, OutputStream output) throws WLFormatException{
        System.err.println("LANG -"+lang+ "- ");
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "MESSAGE -"+input.toString()+"- ");
         EnumSet<TextCorpusLayerTag> requiredLayers =
            EnumSet.of(
                    TextCorpusLayerTag.TEXT);
        TextCorpusStreamed textCorpus = new TextCorpusStreamed(input, requiredLayers);
        textCorpus.createTextLayer().addText(lang);
//        try {
//            TextCorpusProcessor tool = new TokenizerBaseCore(lang);
//            textCorpus = new TextCorpusStreamed(input, null, output, false);
//            // process TextCorpus and create new annotation layer(s) with your tool
//            tool.process(textCorpus);
//        } catch (TextCorpusProcessorException ex) {
//            throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
//        } catch (WLFormatException ex) {
//            throw new WebApplicationException(createResponse(ex, Response.Status.BAD_REQUEST));
//        } catch (Exception ex) {
//            throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
//        } finally {
//            try {
//                if (textCorpus != null) {
//                    // it's important to close the TextCorpusStreamed, otherwise
//                    // the TCF XML output will not be written to the end
//                    textCorpus.close();
//                }
//            } catch (Exception ex) {
//                throw new WebApplicationException(createResponse(ex, Response.Status.INTERNAL_SERVER_ERROR));
//            }
//        }
    }

    private Response createResponse(Exception ex, Response.Status status) {
        String message = ex.getMessage();
        if (message == null) {
            message = FALL_BACK_MESSAGE;
        }
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, message, ex);
        return Response.status(status).entity(message).type(MediaType.TEXT_PLAIN).build();
    }

}
