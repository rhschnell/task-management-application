package client.windows.login.admin;

import client.serverUtils.ServerUtils;
import client.utils.HelperMethods;
import client.utils.Scenes;
import com.google.inject.Inject;

public class AdminLoginService {
    private final ServerUtils server;
    private final HelperMethods hm;

    @Inject
    public AdminLoginService(ServerUtils server, HelperMethods hm) {
        this.server = server;
        this.hm = hm;
    }

    /**
     * Switches to the workspace scene
     */
    public void showWorkspace() {
        hm.setScene(Scenes.WORKSPACE);
    }

    /**
     * Tries to ping the current server
     * @return true if it pings, false otherwise
     */
    public boolean serverPing(String serverAddress) {
        server.setServer(serverAddress);
        return server.pingServer();
    }

    /**
     * Sets the scene back to the main menu.
     */
    public void back() {
        hm.setScene(Scenes.STARTUP);
    }
}
