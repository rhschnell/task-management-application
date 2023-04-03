package client.serverUtils;

import commons.CardList;
import commons.Route;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CardListUtilsTest {

    private ServerUtils serverUtils;
    private CardListUtils cardListUtils;
    private HTTPMocker mocker;

    @BeforeEach
    void setup()
    {
        serverUtils = Mockito.mock(ServerUtils.class);
        cardListUtils = new CardListUtils(serverUtils);
        mocker = new HTTPMocker(CardList.class);
        cardListUtils.setClient(mocker.clientMock);
        when(serverUtils.getServer()).thenReturn("http://nonexisting:123/");
    }

    @Test
    void insertCardList() {
        CardList cardList = new CardList();
        cardListUtils.insertCardList(cardList);
        verify(mocker.clientMock).target("http://nonexisting:123/");
        verify(mocker.targetMock).path(Route.CARD_LIST);
        verify(mocker.targetMock).request(APPLICATION_JSON);
        verify(mocker.builderMock).accept(APPLICATION_JSON);
        verify(mocker.builderMock).post(Entity.entity(cardList, APPLICATION_JSON), CardList.class);
        verify(serverUtils).getServer();
    }

    @Test
    void deleteCardList() {
        long id = 1;
        cardListUtils.deleteCardList(1);
        verify(mocker.clientMock).target("http://nonexisting:123/");
        verify(mocker.targetMock).path(Route.CARD_LIST + "/" + id);
        verify(mocker.targetMock).request(APPLICATION_JSON);
        verify(mocker.builderMock).accept(APPLICATION_JSON);
        verify(mocker.builderMock).delete(Response.class);
        verify(serverUtils).getServer();
    }

    @Test
    void getCardList() {
        long id = 1;
        cardListUtils.getCardList(id);
        verify(mocker.clientMock).target("http://nonexisting:123/");
        verify(mocker.targetMock).path(Route.CARD_LIST + "/" + id);
        verify(mocker.targetMock).request(APPLICATION_JSON);
        verify(mocker.builderMock).accept(APPLICATION_JSON);
        verify(mocker.builderMock).get(CardList.class);
        verify(serverUtils).getServer();
    }
}