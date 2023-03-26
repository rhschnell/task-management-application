package client.windows.login.admin;

import client.serverUtils.ServerUtils;
import com.google.inject.Inject;

public class AdminLoginService {
    private final ServerUtils server;

    @Inject
    public AdminLoginService(ServerUtils server) {
        this.server = server;
    }

    /**
     * Tries to ping the current server
     * @return true if it pings, false otherwise
     */
    public boolean serverPing(String serverAddress) {
        server.setServer(serverAddress);
        return server.pingServer();
    }
}
