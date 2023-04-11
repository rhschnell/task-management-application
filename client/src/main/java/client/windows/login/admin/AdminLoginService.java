package client.windows.login.admin;

import client.serverUtils.AdminUtils;
import client.serverUtils.ServerUtils;
import client.utils.HelperMethods;
import com.google.inject.Inject;

public class AdminLoginService {
    private final ServerUtils server;
    private final AdminUtils adminUtils;
    private final HelperMethods helperMethods;

    /**
     * Constructor for AdminLoginService
     *
     * @param server        a server util
     * @param helperMethods hm instance
     */
    @Inject
    public AdminLoginService(ServerUtils server, HelperMethods helperMethods) {
        this.server = server;
        this.adminUtils = new AdminUtils(server);
        this.helperMethods = helperMethods;
    }

    /**
     * Sets the server's IP address and tries to ping it
     *
     * @param serverAddress The IP address to try and ping
     * @return true if the ping is successful, false otherwise
     */
    public boolean serverPing(String serverAddress) {
        server.setServer(serverAddress);
        helperMethods.setServerIP(serverAddress);
        return server.pingServer();
    }

    /**
     * Sends a post request to the server for the admin password
     *
     * @param password The password to send
     */
    public void sendPassword(String password) {
        adminUtils.sendPassword(password);
    }

}
