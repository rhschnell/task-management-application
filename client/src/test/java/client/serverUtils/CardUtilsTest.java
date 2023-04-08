package client.serverUtils;

import commons.Card;
import commons.CardList;
import commons.Route;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CardUtilsTest {

    private ServerUtils serverUtils;
    private CardUtils cardUtils;
    private HTTPMocker mocker;

    @BeforeEach
    void setup()
    {
        serverUtils = Mockito.mock(ServerUtils.class);
        cardUtils = new CardUtils(serverUtils);
        mocker = new HTTPMocker(Card.class);
        cardUtils.setClient(mocker.clientMock);
        when(serverUtils.getServer()).thenReturn("http://nonexisting:123/");
    }

    @Test
    void insertCard() {
        Card card = new Card();
        cardUtils.insertCard(card);
        verify(mocker.clientMock).target("http://nonexisting:123/");
        verify(mocker.targetMock).path(Route.CARD);
        verify(mocker.targetMock).request(APPLICATION_JSON);
        verify(mocker.builderMock).accept(APPLICATION_JSON);
        verify(mocker.builderMock).post(Entity.entity(card, APPLICATION_JSON), Card.class);
        verify(serverUtils).getServer();
    }

    @Test
    void deleteCard() {
        long id = 1;
        cardUtils.deleteCard(id);
        verify(mocker.clientMock).target("http://nonexisting:123/");
        verify(mocker.targetMock).path(Route.CARD_LIST + "/deleteCard/" + id);
        verify(mocker.targetMock).request(APPLICATION_JSON);
        verify(mocker.builderMock).accept(APPLICATION_JSON);
        verify(mocker.builderMock).delete(Card.class);
        verify(serverUtils).getServer();
    }

    @Test
    void deleteFromCardList() {
        Card card = new Card();
        CardList cardList = new CardList();
        cardList.addCard(card);
        cardUtils.deleteFromCardList(cardList.getId(),card);
        verify(mocker.clientMock).target("http://nonexisting:123/");
        verify(mocker.targetMock).path(Route.CARD_LIST + "/removeFromCardList/0");
        verify(mocker.targetMock).request(APPLICATION_JSON);
        verify(mocker.builderMock).accept(APPLICATION_JSON);
        verify(mocker.builderMock).post(Entity.entity(card, APPLICATION_JSON), Card.class);
        verify(serverUtils).getServer();
    }

    @Test
    void getCards() {
        cardUtils.getCards();
        verify(mocker.clientMock).target("http://nonexisting:123/");
        verify(mocker.targetMock).path(Route.CARD);
        verify(mocker.targetMock).request(APPLICATION_JSON);
        verify(mocker.builderMock).accept(APPLICATION_JSON);
        verify(mocker.builderMock).get(new GenericType<List<Card>>() {
        });
        verify(serverUtils).getServer();
    }
}