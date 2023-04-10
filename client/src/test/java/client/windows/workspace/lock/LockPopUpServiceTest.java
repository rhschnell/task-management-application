package client.windows.workspace.lock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import client.serverUtils.BoardUtils;
import commons.Board;
import org.mockito.Mock;

public class LockPopUpServiceTest {
    private LockPopUpService lockPopUpService;

    @Mock
    private BoardUtils boardUtilsMock;

    @BeforeEach
    public void setUp() {
        boardUtilsMock = mock(BoardUtils.class);
        lockPopUpService = new LockPopUpService(boardUtilsMock);
    }

    @Test
    public void verifyPassword() {
        Board board = new Board();
        board.setPassword("Custom Password");
        String correctPassword = "Custom Password";
        String incorrectPassword = "Wrong Password";

        boolean result1 = lockPopUpService.verifyPassword(board, correctPassword);
        boolean result2 = lockPopUpService.verifyPassword(board, incorrectPassword);
        assert(result1);
        assert(!result2);
    }

    @Test
    public void setProtected() {
        Board board = new Board();
        board.setProtected(false);
        lockPopUpService.setBoard(board);

        lockPopUpService.setProtected(true);

        verify(boardUtilsMock).insertBoard(board);
        assert(board.isProtected());
    }

    @Test
    public void setPassword() {
        Board board = new Board();
        board.setPassword("New Password");
        lockPopUpService.setBoard(board);

        lockPopUpService.setPassword("Another Password");

        verify(boardUtilsMock).insertBoard(board);
        assertEquals("Another Password", board.getPassword());
    }
}