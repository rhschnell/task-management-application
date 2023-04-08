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
     * Sends a request to the server to delete a certain card from the database
     * @param id the id of the card to delete
     */
    public void deleteCard(long id) {
        client.target(serverUtils.getServer()).path(Route.CARD_LIST + "/deleteCard/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete(Card.class);
    }
    /**
     * Deletes a card from the list of CardLists
     * @param listID    The ID of the card list to delete this card from
     * @param card The card that needs to be deleted from the list of lists
     */
    public void deleteFromCardList(Long listID, Card card) {
        client
                .target(serverUtils.getServer()).path(Route.CARD_LIST+"/removeFromCardList/"+listID)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(card, APPLICATION_JSON), Card.class);
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

    public void setClient(Client client)
    {
        this.client = client;
    }

    public Card getCardById(long id) {
        return client.target(serverUtils.getServer()).path(Route.CARD + "/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(Card.class);
    }
}
