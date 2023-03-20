/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes.MainScreens;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private TextField serverAddress;

    /**
     * Constructor for LoginCtrl
     * @param server a server util
     * @param mainCtrl a main controller
     */
    @Inject
    public LoginCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     *
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.serverAddress.setText("http://localhost:8080");
    }


    /**
     * Middleware that tries to connect to the user specified server. If successful, redirects
     * the user to the workspace. Otherwise, shows an error message.
     */
    public void connect(){
        server.setServer(serverAddress.getText());
        if (server.pingServer()){
            joinPopUp();
        } else {
            showErrorMessage();
        }
    }

    /**
     * Shows a message to the user indicating that the connection to the server could not be made.
     */
    private void showErrorMessage() {
        System.out.println("Could not connect to the server");
    }

    /**
     * When called, it switches back to the board overview, disconnecting the user from the workspace.
     */
    public void showWorkspace() {
        mainCtrl.setWorkspace();
    }

    /**
     * Displays the Board Join FXML into a new window (Popup).
     *
     */
    public void joinPopUp() {
        String title = "Join/Create a board";
        mainCtrl.popUp(mainCtrl.getBoardJoin(), title);
    }
}
