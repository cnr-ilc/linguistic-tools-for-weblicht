package it.cnr.ilc.openerserviceswrapper.app.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.InputStream;

/**
 * Resource that serves up the index page.
 */
@Path("/tokenizerhr")
public class TokenizerHelpResource {
    @GET
    @Produces("text/html")
    public InputStream index() {
        return getClass().getResourceAsStream("/tokenizer.html");
    }

    
}
