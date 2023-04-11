package client.windows.lists.delete;

import client.serverUtils.CardUtils;
import commons.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class DeleteCardServiceTest {
    private DeleteCardService deleteCardService;

    @Mock
    private CardUtils cardUtilsMock;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        deleteCardService = new DeleteCardService(cardUtilsMock);
    }

    @Test
    public void deleteCard() {
        long cardId = 1L;
        Card card = new Card();
        card.setId(cardId);

        deleteCardService.deleteCard(card);

        verify(cardUtilsMock).deleteCard(cardId);
        verify(cardUtilsMock).deleteCardFromDatabase(cardId);
    }
}
