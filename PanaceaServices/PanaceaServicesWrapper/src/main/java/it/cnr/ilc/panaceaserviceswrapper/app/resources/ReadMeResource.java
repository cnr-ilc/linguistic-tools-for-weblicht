package it.cnr.ilc.panaceaserviceswrapper.app.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.InputStream;

/**
 * Resource that serves up the index page.
 */
@Path("/readme")
public class ReadMeResource {
    @GET
    @Produces("text/html")
    public InputStream index() {
        return getClass().getResourceAsStream("/index.html");
    }

    
}
