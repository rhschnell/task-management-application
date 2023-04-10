package client.windows.customize.cards.edit;

import client.serverUtils.CardColorPresetUtils;
import commons.CardColorPreset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class EditCardPresetServiceTest {
    private EditCardPresetService editCardPresetService;

    @Mock
    private CardColorPresetUtils cardColorPresetUtilsMock;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        editCardPresetService = new EditCardPresetService(cardColorPresetUtilsMock);
    }

    @Test
    public void insertPreset() {
        CardColorPreset preset = new CardColorPreset();
        editCardPresetService.insertPreset(preset);
        verify(cardColorPresetUtilsMock, times(1)).insertPreset(preset);
    }
}
