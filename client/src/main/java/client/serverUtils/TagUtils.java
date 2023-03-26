package client.serverUtils;

import com.google.inject.Inject;
import commons.Route;
import commons.Tag;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientConfig;

import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class TagUtils {
    private ServerUtils serverUtils;

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

    public List<Tag> getTags() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(serverUtils.getServer()).path(Route.TAG)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Tag>>() {});
    }
}
