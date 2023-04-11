package client.serverUtils;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

public class WebsocketUtilsTest {
    private ServerUtils serverUtils;

    @BeforeEach
    void setUp() {
        serverUtils = Mockito.mock(ServerUtils.class);
        when(serverUtils.getServer()).thenReturn("http://nonexisting:123/");
    }
}
