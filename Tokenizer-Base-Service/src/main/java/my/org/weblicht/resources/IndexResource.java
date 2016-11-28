package my.org.weblicht.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.InputStream;

/**
 * Resource that serves up the index page.
 */
@Path("/")
public class IndexResource {
    @GET
    @Produces("text/html")
    public InputStream index() {
        return getClass().getResourceAsStream("/index.html");
    }

    @GET
    @Path("/input_ner.xml")
    @Produces("text/xml")
    public InputStream inputTestNerData() {
        return getClass().getResourceAsStream("/input_ner.xml");
    }

    @GET
    @Path("/input_ref.xml")
    @Produces("text/xml")
    public InputStream inputTestRefData() {
        return getClass().getResourceAsStream("/input_ref.xml");
    }
    @GET
    @Path("/input_tok.xml")
    @Produces("text/xml")
    public InputStream inputTestTokData() {
        return getClass().getResourceAsStream("/input_tok.xml");
    }
}
