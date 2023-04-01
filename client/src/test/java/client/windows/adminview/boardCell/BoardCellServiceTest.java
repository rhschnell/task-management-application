package client.windows.adminview.boardCell;

import client.serverUtils.BoardUtils;
import commons.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class BoardCellServiceTest {

    private BoardUtils server;
    private BoardCellService boardCellService;

    @BeforeEach
    void setUp() {
        server = Mockito.mock(BoardUtils.class);
        boardCellService = new BoardCellService(server);

    }

    @Test
    void deleteBoard() {
        Board board = new Board();
        board.setKey("the key");
        boardCellService.deleteBoard(board);
        verify(server, times(1)).deleteBoard(board.getKey());
    }
}