package client.windows.login.admin;

import client.serverUtils.AdminUtils;
import client.serverUtils.ServerUtils;
import com.google.inject.Inject;

public class AdminLoginService {
    private final ServerUtils server;
    private final AdminUtils adminUtils;

    /**
     * Constructor for AdminLoginService
     * @param server a server util
     */
    @Inject
    public AdminLoginService(ServerUtils server) {
        this.server = server;
        this.adminUtils = new AdminUtils(server);
    }

    /**
     * Tries to ping the current server
     * @return true if it pings, false otherwise
     */
    public boolean serverPing(String serverAddress) {
        server.setServer(serverAddress);
        return server.pingServer();
    }

    public void sendPassword(String password) {
        adminUtils.sendPassword(password);
    }

}
