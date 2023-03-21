package client.utils;

import commons.CardList;
import commons.Route;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import javax.inject.Inject;
import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class CardListUtils {
    private ServerUtils serverUtils;

    /**
     * Creates a new BoardUtils object
     * @param serverUtils The ServerUtils object (injected) to use in requests.
     */
    @Inject
    public CardListUtils(ServerUtils serverUtils){
        this.serverUtils = serverUtils;
    }

    /**
     * Sends a post request to the server to add a card list to the database
     * @param cardList The card list to add
     * @return The added card list
     */
    public CardList addCardList(CardList cardList)
    {
        return ClientBuilder.newClient(new ClientConfig())
                .target(serverUtils.getServer()).path(Route.CARD_LIST)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(cardList, APPLICATION_JSON), CardList.class);
    }

    /**
     * Sends a request to the server to get all card lists from the database
     * @return List of all card lists in the database
     */
    public List<CardList> getCardLists()
    {
        return ClientBuilder.newClient(new ClientConfig())
                .target(serverUtils.getServer()).path(Route.CARD_LIST)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {});
    }

    /**
     * Sends a request to the server to retrieve a certain card list from the database
     * @param id key of the card list to find
     * @return the desired card list
     */
    public CardList getCardList(int id) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(serverUtils.getServer()).path(Route.CARD_LIST + "/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(CardList.class);
    }

    /**
     * Sends a request to the server to delete a certain card list from the database
     * @param id of card list delete
     */
    public void deleteCardList(int id)
    {
        ClientBuilder.newClient(new ClientConfig())
            .target(serverUtils.getServer()).path(Route.CARD_LIST + "/" + id)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .delete(Response.class);
    }
}
