package client.serverUtils;

import commons.Route;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import org.glassfish.jersey.client.ClientConfig;

import javax.inject.Inject;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class AdminUtils {
    private final ServerUtils serverUtils;

    /**
     * Creates a new BoardUtils object
     * @param serverUtils The ServerUtils object (injected) to use in requests.
     */
    @Inject
    public AdminUtils(ServerUtils serverUtils){
        this.serverUtils = serverUtils;
    }

    /**
     * Sends a post request to the server for the admin password
     * @param pass The password
     */
    public void sendPassword(String pass) {
        ClientBuilder.newClient(new ClientConfig())
                .target(serverUtils.getServer()).path(Route.ADMIN)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(pass, APPLICATION_JSON), String.class);
    }
}
