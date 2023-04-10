package client.windows.customize.cards.add;

import client.serverUtils.BoardUtils;
import commons.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AddCardPresetServiceTest {
    private AddCardPresetService addCardPresetService;

    @Mock
    private BoardUtils boardUtilsMock;

    @BeforeEach
    public void setup() {
        boardUtilsMock = mock(BoardUtils.class);
        addCardPresetService = new AddCardPresetService(boardUtilsMock);
    }

    @Test
    public void insertBoard() {
        Board board = new Board();
        addCardPresetService.insertBoard(board);
        verify(boardUtilsMock).insertBoard(board);
    }
}
