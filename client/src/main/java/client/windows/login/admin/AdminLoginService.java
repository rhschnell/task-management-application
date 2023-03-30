package client.windows.login.admin;

import client.serverUtils.AdminUtils;
import client.serverUtils.ServerUtils;
import client.utils.HelperMethods;
import com.google.inject.Inject;

public class AdminLoginService {
    private final ServerUtils server;
    private final AdminUtils adminUtils;
    private final HelperMethods hml;

    /**
     * Constructor for AdminLoginService
     * @param server a server util
     * @param hml
     */
    @Inject
    public AdminLoginService(ServerUtils server, HelperMethods hml) {
        this.server = server;
        this.adminUtils = new AdminUtils(server);
        this.hml = hml;
    }

    /**
     * Tries to ping the current server
     * @return true if it pings, false otherwise
     */
    public boolean serverPing(String serverAddress) {
        server.setServer(serverAddress);
        hml.setServerIP(serverAddress);
        return server.pingServer();
    }

    public void sendPassword(String password) {
        adminUtils.sendPassword(password);
    }

}
