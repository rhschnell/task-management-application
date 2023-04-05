package client.serverUtils;

import com.google.inject.Inject;
import commons.Route;
import commons.Tag;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class TagUtils {
    private final ServerUtils serverUtils;
    private Client client;


    /**
     * Creates a new TagUtils object
     *
     * @param serverUtils The ServerUtils object (injected) to use in requests.
     */
    @Inject
    public TagUtils(ServerUtils serverUtils) {

        this.serverUtils = serverUtils;
        this.client = ClientBuilder.newClient(new ClientConfig());
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void addTagToCard(Tag tag, long cardId) {
        client.target(serverUtils.getServer()).path("api/tagToCard")
                .queryParam("cardId", cardId)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(tag, APPLICATION_JSON), Tag.class);
    }

    /**
     * Sends a post request to the server to add a tag to the database
     *
     * @param tag The tag list to add
     */
    public void insertTag(Tag tag) {
        client
                .target(serverUtils.getServer()).path(Route.TAG)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(tag, APPLICATION_JSON), Tag.class);
    }

    /**
     * Sends a request to the server to delete a certain tag from the database
     *
     * @param id of the tag to delete
     */
    public void deleteTag(long id) {
        client
                .target(serverUtils.getServer()).path(Route.TAG + "/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete(Response.class);
    }

    public List<Tag> getBoardTags(String key) {
        return client
                .target(serverUtils.getServer()).path(Route.BOARD + "/getBoardTags/" + key)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Tag>>() {
                });
    }

    private static final ExecutorService EXEC = Executors.newSingleThreadExecutor();
    public void registerForUpdates(String key, Consumer<Tag> consumer) {
        EXEC.submit(() -> {
            while(!Thread.interrupted()) {
                var res = client.target(serverUtils.getServer()).path(Route.BOARD + "/" + key + "/tagUpdates")
                        .request(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .get(Response.class);
                if(res.getStatus() == 204) {
                    continue;
                }
                var t = res. readEntity(Tag.class);
                consumer.accept(t);
            }
        });
    }

    public void stop() {
        EXEC.shutdownNow();
    }
}
