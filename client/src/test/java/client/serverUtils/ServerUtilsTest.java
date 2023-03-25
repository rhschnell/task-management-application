package client.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServerUtilsTest {

    private ServerUtils serverUtils;
    @BeforeEach
    public void setup(){
        this.serverUtils = new ServerUtils();
    }

    @Test
    void setServer() {
        serverUtils.setServer("testserver:5050");
        assertEquals("testserver:5050", serverUtils.getServer());
    }

    @Test
    void getServer() {
        assertEquals("http://localhost:8080/", serverUtils.getServer());
    }

    @Test
    void pingServer() {
        serverUtils.setServer("unknownserver:someport");
        assertFalse(serverUtils.pingServer());
    }
}
