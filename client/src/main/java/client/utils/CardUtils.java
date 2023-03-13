package client.utils;

import commons.Card;
import commons.Route;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientConfig;

import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class CardUtils {

    /**
     * Sends a post request to the server to add a Card to the database
     * @param card The card to add to the database
     * @return The added card
     */
    public Card addCard(Card card){
        return ClientBuilder.newClient(new ClientConfig())
                .target(ServerUtils.SERVER).path(Route.CARD)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(card, APPLICATION_JSON), Card.class);
    }


    /**
     * Sends a get request to the server to get all cards from the database
     * @return All cards in the database
     */
    public List<Card> getCards() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(ServerUtils.SERVER).path(Route.CARD) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Card>>() {});
    }
}
