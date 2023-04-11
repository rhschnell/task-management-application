package client.serverUtils;

import com.google.inject.Inject;
import commons.CardColorPreset;
import commons.Route;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class CardColorPresetUtils {
    private final ServerUtils serverUtils;
    private Client client;

    /**
     * Creates a new CardColorPresetUtils object
     *
     * @param serverUtils The ServerUtils object (injected) to use in requests
     */
    @Inject
    public CardColorPresetUtils(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
        this.client = ClientBuilder.newClient(new ClientConfig());
    }

    /**
     * Setter to set the client
     *
     * @param client The client to be set
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Sends a post request to the server to add a preset to the database
     *
     * @param preset The preset to add
     */
    public void insertPreset(CardColorPreset preset) {
        client
                .target(serverUtils.getServer()).path(Route.CARD_COLOR_PRESET)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(preset, APPLICATION_JSON), CardColorPreset.class);
    }

    /**
     * Sends a request to the server to delete a certain preset from the database
     *
     * @param id The id of the preset to delete
     */
    public void deletePreset(long id) {
        client
                .target(serverUtils.getServer()).path(Route.CARD_COLOR_PRESET + "/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete(Response.class);
    }
}