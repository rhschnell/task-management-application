package client.utils;

import commons.Card;
import commons.Route;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientConfig;

import javax.inject.Inject;
import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class CardUtils {
    private final ServerUtils serverUtils;

    /**
     * Creates a new CardUtils object
     * @param serverUtils The ServerUtils object (injected) to use in requests.
     */
    @Inject
    public CardUtils(ServerUtils serverUtils){
        this.serverUtils = serverUtils;
    }

    /**
     * Sends a post request to the server to add a Card to the database
     * @param card The card to add to the database
     */
    public void insertCard(Card card) {
        ClientBuilder.newClient(new ClientConfig())
                .target(serverUtils.getServer()).path(Route.CARD)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    /**
     * Sends a request to the server to delete a certain card from the database
     * @param id the id of the card to delete
     */
    public void deleteCard(long id) {
        ClientBuilder.newClient(new ClientConfig())
                .target(serverUtils.getServer()).path(Route.CARD + "/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete(Card.class);
    }

    /**
     * Sends a request to the server to delete a certain card from the database
     * @param id the id of the card to delete
     */
    public Card getCard(long id) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(serverUtils.getServer()).path(Route.CARD + "/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(Card.class);
    }

    /**
     * Sends a get request to the server to get all cards from the database
     * @return All cards in the database
     */
    public List<Card> getCards() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(serverUtils.getServer()).path(Route.CARD)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {});
    }
}
