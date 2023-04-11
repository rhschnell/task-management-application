package client.serverUtils;

import commons.CardColorPreset;
import commons.Route;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CardColorPresetUtilsTest {
    private ServerUtils serverUtils;
    private CardColorPresetUtils cardColorPresetUtils;
    private HTTPMocker mocker;

    @BeforeEach
    void setup() {
        serverUtils = Mockito.mock(ServerUtils.class);
        cardColorPresetUtils = new CardColorPresetUtils(serverUtils);
        mocker = new HTTPMocker(CardColorPreset.class);
        cardColorPresetUtils.setClient(mocker.clientMock);
        when(serverUtils.getServer()).thenReturn("http://nonexisting:123/");
    }

    @Test
    void insertPreset() {
        CardColorPreset preset = new CardColorPreset();
        cardColorPresetUtils.insertPreset(preset);
        verify(mocker.clientMock).target("http://nonexisting:123/");
        verify(mocker.targetMock).path(Route.CARD_COLOR_PRESET);
        verify(mocker.targetMock).request(APPLICATION_JSON);
        verify(mocker.builderMock).accept(APPLICATION_JSON);
        verify(mocker.builderMock).post(Entity.entity(preset, APPLICATION_JSON), CardColorPreset.class);
        verify(serverUtils).getServer();
    }

    @Test
    void deletePreset() {
        long id = 1;
        cardColorPresetUtils.deletePreset(1);
        verify(mocker.clientMock).target("http://nonexisting:123/");
        verify(mocker.targetMock).path(Route.CARD_COLOR_PRESET + "/" + id);
        verify(mocker.targetMock).request(APPLICATION_JSON);
        verify(mocker.builderMock).accept(APPLICATION_JSON);
        verify(mocker.builderMock).delete(Response.class);
        verify(serverUtils).getServer();
    }
}
