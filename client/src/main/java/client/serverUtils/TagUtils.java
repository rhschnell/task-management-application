package client.serverUtils;

import com.google.inject.Inject;
import commons.Route;
import commons.Tag;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class TagUtils {
    private ServerUtils serverUtils;


    /**
     * Creates a new TagUtils object
     * @param serverUtils The ServerUtils object (injected) to use in requests.
     */
    @Inject
    public TagUtils(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    public void addTagToCard(Tag tag, long cardId) {
        ClientBuilder.newClient(new ClientConfig())
                .target(serverUtils.getServer()).path("api/tagToCard")
                .queryParam("cardId", cardId)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(tag, APPLICATION_JSON), Tag.class);
    }

    /**
     * Sends a request to the server to get all tags from the database
     * @return List of all tags in the database
     */
    public List<Tag> getTags() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(serverUtils.getServer()).path(Route.TAG)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Tag>>() {});
    }

    /**
     * Sends a post request to the server to add a tag to the database
     * @param tag The tag list to add
     */
    public void insertTag(Tag tag) {
        ClientBuilder.newClient(new ClientConfig())
                .target(serverUtils.getServer()).path(Route.TAG)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(tag, APPLICATION_JSON), Tag.class);
    }

    /**
     * Sends a request to the server to delete a certain tag from the database
     * @param id of the tag to delete
     */
    public void deleteTag(long id) {
        ClientBuilder.newClient(new ClientConfig())
                .target(serverUtils.getServer()).path(Route.TAG + "/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete(Response.class);
    }
}
