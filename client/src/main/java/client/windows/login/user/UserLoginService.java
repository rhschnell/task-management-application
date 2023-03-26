package client.windows.login.user;

import client.serverUtils.ServerUtils;
import client.utils.HelperMethods;
import client.utils.Scenes;
import com.google.inject.Inject;

public class UserLoginService {
    private final ServerUtils server;
    private final HelperMethods hm;

    @Inject
    public UserLoginService(ServerUtils server, HelperMethods hm) {
        this.server = server;
        this.hm = hm;
    }

//    /**
//     * Middleware that tries to connect to the user specified server. If successful, redirects
//     * the user to the workspace. Otherwise, shows an error message.
//     */
//    public void connect(String serverAddress) {
//        server.setServer(serverAddress);
//        if (server.pingServer()){
//            hm.setScene(Scenes.WORKSPACE);
//        } else {
//            showErrorMessage();
//        }
//    }

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
