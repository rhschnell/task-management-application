package client.windows.customize;

import client.serverUtils.BoardUtils;
import client.serverUtils.CardColorPresetUtils;
import client.serverUtils.CardListUtils;
import commons.Board;
import commons.CardColorPreset;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class CustomizeServiceTest {
    private CustomizeService customizeService;

    @Mock
    private BoardUtils boardUtilsMock;

    @Mock
    private CardColorPresetUtils cardColorPresetUtilsMock;

    @Mock
    private CardListUtils cardListUtilsMock;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        customizeService = new CustomizeService(boardUtilsMock, cardColorPresetUtilsMock, cardListUtilsMock);
    }

    @Test
    public void insertBoard() {
        Board board = new Board();
        customizeService.insertBoard(board);
        verify(boardUtilsMock).insertBoard(board);
    }

    @Test
    public void insertCardList() {
        CardList cardList = new CardList();
        customizeService.insertCardList(cardList);
        verify(cardListUtilsMock).insertCardList(cardList);
    }

    @Test
    public void testInsertPreset() {
        CardColorPreset preset = new CardColorPreset();
        customizeService.insertPreset(preset);
        verify(cardColorPresetUtilsMock).insertPreset(preset);
    }

    @Test
    public void testDeletePreset() {
        CardColorPreset preset = new CardColorPreset();
        customizeService.deletePreset(preset);
        verify(cardColorPresetUtilsMock).deletePreset(preset.getId());
    }

}
