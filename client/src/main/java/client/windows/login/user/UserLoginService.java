package client.windows.login.user;

import client.serverUtils.ServerUtils;
import client.utils.HelperMethods;
import com.google.inject.Inject;

public class UserLoginService {
    private final ServerUtils server;
    private final HelperMethods helperMethods;

    /**
     * Creates a new UserLoginService
     *
     * @param server        The utility class for the server
     * @param helperMethods Instance of HelperMethods
     */
    @Inject
    public UserLoginService(ServerUtils server, HelperMethods helperMethods) {
        this.server = server;
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
}
