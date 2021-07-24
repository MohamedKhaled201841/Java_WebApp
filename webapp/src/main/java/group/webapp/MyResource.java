package group.webapp;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent to
     * the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
    }

    @GET
    @Path("summary")
    @Produces(MediaType.TEXT_HTML)
    public String exploreData() throws IOException, URISyntaxException {
        return ExploreData.exploreData();
    }

    @GET
    @Path("mostPopularArea")
    @Produces(MediaType.TEXT_HTML)
    public String mostPopularArea() throws IOException, URISyntaxException {
        // Return String
        // Return BufferedImage
        return MostPopularArea.getHtmlPage();
    }

    @GET
    @Path("mostPopularCompany")
    @Produces(MediaType.TEXT_HTML)
    public String mostPopularCompany() throws IOException, URISyntaxException {
        // Return String
        // Return BufferedImage
        return MostPopularCompany.getHtmlPage();
    }

    @GET
    @Path("mostPopularTitle")
    @Produces(MediaType.TEXT_HTML)
    public String mostPopularTitle() throws IOException, URISyntaxException {
        // Return String
        // Return BufferedImage
        return MostPopularTitle.getHtmlPage();
    }

    @GET
    @Path("mostPopularSkill")
    @Produces(MediaType.TEXT_HTML)
    public String mostPopularSkill() throws IOException, URISyntaxException {
        // Return String
        // Return BufferedImage
        return MostPopularSkill.getHtmlPage();
    }

}
