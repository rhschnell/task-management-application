package client.windows.lists.cells;

import client.serverUtils.CardUtils;
import commons.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class RenameCardServiceTest {
    private RenameCardService renameCardService;

    @Mock
    private CardUtils cardUtilsMock;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        renameCardService = new RenameCardService(cardUtilsMock);
    }

    @Test
    public void insertCardTest() {
        Card card = new Card();
        renameCardService.insertCard(card);
        verify(cardUtilsMock).insertCard(card);
    }
}
