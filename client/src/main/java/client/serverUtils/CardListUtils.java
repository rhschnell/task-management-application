package client.serverUtils;
import commons.CardList;
import commons.Route;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import javax.inject.Inject;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class CardListUtils {
    private final ServerUtils serverUtils;
    private Client client;

    /**
     * Creates a new BoardUtils object
     * @param serverUtils The ServerUtils object (injected) to use in requests.
     */
    @Inject
    public CardListUtils(ServerUtils serverUtils){
        this.serverUtils = serverUtils;
        this.client = ClientBuilder.newClient(new ClientConfig());
    }

    /**
     *
     * @param ip
     */
    public void setIP(String ip) {
        serverUtils.setServer(ip);
    }

    /**
     * Sends a post request to the server to add a card list to the database
     * @param cardList The card list to add
     */
    public void insertCardList(CardList cardList) {
        client.target(serverUtils.getServer()).path(Route.CARD_LIST)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(cardList, APPLICATION_JSON), CardList.class);
    }
    /**
     * Sends a request to the server to delete a certain card list from the database
     * @param id of card list delete
     */
    public void deleteCardList(long id) {
        client.target(serverUtils.getServer()).path(Route.CARD_LIST + "/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete(Response.class);
    }

    /**
     * Sends a request to the server to retrieve a certain card list from the database
     * @param id key of the card list to find
     * @return the desired card list
     */
    public CardList getCardList(long id) {
        return client.target(serverUtils.getServer()).path(Route.CARD_LIST + "/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(CardList.class);
    }

    /**
     * Setter for the client
     * @param client the client to be set
     */
    public void setClient(Client client)
    {
        this.client = client;
    }
}
