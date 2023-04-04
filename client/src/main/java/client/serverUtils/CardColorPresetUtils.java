package client.serverUtils;

import com.google.inject.Inject;
import commons.CardColorPreset;
import commons.Route;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class CardColorPresetUtils {
    private ServerUtils serverUtils;

    @Inject
    public CardColorPresetUtils(ServerUtils serverUtils){
        this.serverUtils = serverUtils;
    }

    public void insertPreset(CardColorPreset preset){
        ClientBuilder.newClient(new ClientConfig())
                .target(serverUtils.getServer()).path(Route.CARD_COLOR_PRESET)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(preset, APPLICATION_JSON), CardColorPreset.class);
    }

    public void deletePreset(long id){
        ClientBuilder.newClient(new ClientConfig())
                .target(serverUtils.getServer()).path(Route.CARD_COLOR_PRESET + "/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete(Response.class);
    }
}
