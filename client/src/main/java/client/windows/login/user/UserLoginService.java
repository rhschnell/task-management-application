package client.windows.login.user;

import client.serverUtils.ServerUtils;
import client.utils.HelperMethods;
import client.utils.Scenes;
import com.google.inject.Inject;
import javafx.scene.control.Alert;

public class UserLoginService {
    private final ServerUtils server;
    private final HelperMethods hm;

    @Inject
    public UserLoginService(ServerUtils server, HelperMethods hm) {
        this.server = server;
        this.hm = hm;
    }

    /**
     * Middleware that tries to connect to the user specified server. If successful, redirects
     * the user to the workspace. Otherwise, shows an error message.
     */
    public void connect(String serverAddress) {
        server.setServer(serverAddress);
        if (server.pingServer()){
            hm.setScene(Scenes.WORKSPACE);
        } else {
            showErrorMessage();
        }
    }

    /**
     * Shows a message to the user indicating that the connection to the server could not be made.
     */
    private void showErrorMessage() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Connection error");
        alert.setContentText("The server you entered does not exist or is turned off. Please try a new server");
        alert.showAndWait();
    }

    /**
     * Sets the scene back to the main menu.
     */
    public void back() {
        hm.setScene(Scenes.STARTUP);
    }
}
