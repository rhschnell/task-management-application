package client.serverUtils;

import commons.Route;
import jakarta.ws.rs.client.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.mockito.Mockito.*;

class AdminUtilsTest {

    private ServerUtils serverUtils;
    private AdminUtils adminUtils;
    private HTTPMocker mocker;

    @BeforeEach
    void setup()
    {
        serverUtils = Mockito.mock(ServerUtils.class);
        adminUtils = new AdminUtils(serverUtils);
        mocker = new HTTPMocker(AdminUtils.class);
        adminUtils.setClient(mocker.clientMock);
        when(serverUtils.getServer()).thenReturn("http://nonexisting:123/");
    }

    @Test
    void sendPassword() {
        String text = "1";
        adminUtils.sendPassword(text);
        verify(mocker.clientMock).target("http://nonexisting:123/");
        verify(mocker.targetMock).path(Route.ADMIN);
        verify(mocker.targetMock).request(APPLICATION_JSON);
        verify(mocker.builderMock).accept(APPLICATION_JSON);
        verify(mocker.builderMock).post(Entity.entity("1", APPLICATION_JSON), Void.class);
        verify(serverUtils).getServer();
    }
}