package client.serverUtils;

import commons.Board;
import commons.Route;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.List;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BoardUtilsTest {

    private ServerUtils serverUtils;
    private BoardUtils boardUtils;
    private HTTPMocker mocker;

    @BeforeEach
    void setup()
    {
        serverUtils = Mockito.mock(ServerUtils.class);
        boardUtils = new BoardUtils(serverUtils);
        mocker = new HTTPMocker();
        boardUtils.setClient(mocker.clientMock);
        when(serverUtils.getServer()).thenReturn("http://nonexisting:123/");
    }

    @Test
    void deleteBoard() {
        String key = "1";
        boardUtils.deleteBoard(key);
        verify(mocker.clientMock).target("http://nonexisting:123/");
        verify(mocker.targetMock).path(Route.BOARD + "/" + key);
        verify(mocker.targetMock).request(APPLICATION_JSON);
        verify(mocker.builderMock).accept(APPLICATION_JSON);
        verify(mocker.builderMock).delete(Response.class);
        verify(serverUtils).getServer();
    }

    @Test
    void getBoard() {
        String key = "1";
        boardUtils.getBoard(key);
        verify(mocker.clientMock).target("http://nonexisting:123/");
        verify(mocker.targetMock).path(Route.BOARD + "/" + key);
        verify(mocker.targetMock).request(APPLICATION_JSON);
        verify(mocker.builderMock).accept(APPLICATION_JSON);
        verify(mocker.builderMock).get(Board.class);
        verify(serverUtils).getServer();
    }

    @Test
    void getBoards() {
        boardUtils.getBoards();
        verify(mocker.clientMock).target("http://nonexisting:123/");
        verify(mocker.targetMock).path(Route.BOARD);
        verify(mocker.targetMock).request(APPLICATION_JSON);
        verify(mocker.builderMock).accept(APPLICATION_JSON);
        verify(mocker.builderMock).get(new GenericType<List<Board>>(){});
        verify(serverUtils).getServer();
    }

//    @Test
//    void insertBoard() {
//        Board board = new Board();
//        boardUtils.insertBoard(board);
//        verify(mocker.clientMock).target("http://nonexisting:123/");
//        verify(mocker.targetMock).path(Route.BOARD);
//        verify(mocker.targetMock).request(APPLICATION_JSON);
//        verify(mocker.builderMock).accept(APPLICATION_JSON);
//        verify(mocker.builderMock).post(Entity.entity(board, APPLICATION_JSON), Board.class);
//        verify(serverUtils).getServer();
//    }

    @Test
    void setServer() {
    }
}