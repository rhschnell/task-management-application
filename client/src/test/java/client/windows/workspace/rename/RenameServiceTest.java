package client.windows.workspace.rename;

import client.serverUtils.BoardUtils;
import commons.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RenameServiceTest {
    private RenameService renameService;

    @Mock
    private BoardUtils server;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        renameService = new RenameService(server);
    }

    @Test
    public void insertBoard() {
        Board board = new Board();
        renameService.insertBoard(board);
        verify(server, times(1)).insertBoard(board);
    }
}
