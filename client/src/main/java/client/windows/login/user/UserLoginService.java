package client.windows.login.user;

import client.serverUtils.ServerUtils;
import client.utils.HelperMethods;
import com.google.inject.Inject;

public class UserLoginService {
    private final ServerUtils server;
    private final HelperMethods hm;

    @Inject
    public UserLoginService(ServerUtils server, HelperMethods hm) {
        this.server = server;
        this.hm = hm;
    }

    /**
     * Tries to ping the current server
     * @return true if it pings, false otherwise
     */
    public boolean serverPing(String serverAddress) {
        server.setServer(serverAddress);
        hm.setServerIP(serverAddress);
        return server.pingServer();
    }
}
