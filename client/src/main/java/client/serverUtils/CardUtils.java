package client.serverUtils;

import commons.Card;
import commons.Route;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientConfig;

import javax.inject.Inject;
import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class CardUtils {
    private final ServerUtils serverUtils;
    private Client client;

    /**
     * Creates a new CardUtils object
     * @param serverUtils The ServerUtils object (injected) to use in requests.
     */
    @Inject
    public CardUtils(ServerUtils serverUtils){
        this.serverUtils = serverUtils;
        this.client = ClientBuilder.newClient(new ClientConfig());
    }

    public void setIP(String ip) {
        serverUtils.setServer(ip);
    }

    /**
     * Sends a post request to the server to add a Card to the database
     * @param card The card to add to the database
     */
    public void insertCard(Card card) {
        client.target(serverUtils.getServer()).path(Route.CARD)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    /**
     * Sends a request to the server to delete the card from the cardList
     * that contains this card so that the priority will be changed to the
     * other card from the method in the commons, not just from the card repo
     * @param id the card's id
     */
    public void deleteCard(long id) {
        client.target(serverUtils.getServer()).path(Route.CARD_LIST + "/deleteCard/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete(Card.class);
    }

    /**
     * Sends a request to the server to delete the card from the repository of
     * card
     * @param id the card's id
     */
    public void deleteCardFromDatabase(long id) {
        client.target(serverUtils.getServer()).path(Route.CARD + "/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete(Card.class);
    }

    /**
     * Sends a get request to the server to get all cards from the database
     * @return All cards in the database
     */
    public List<Card> getCards() {
        return client.target(serverUtils.getServer()).path(Route.CARD)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {});
    }

    /**
     * Setter for the client
     * @param client the client to be set
     */
    public void setClient(Client client)
    {
        this.client = client;
    }

    /**
     * Getter for the card with a given ID
     * @param id the ID of the card to retrieve
     * @return the card with that ID
     */
    public Card getCardById(long id) {
        return client.target(serverUtils.getServer()).path(Route.CARD + "/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(Card.class);
    }
}
