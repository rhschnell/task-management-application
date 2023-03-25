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
package client.windows.login.user;

import client.MainCtrl;
import client.serverUtils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class UserLoginCtrl implements Initializable {

    private final UserLoginService service;

    @FXML
    private TextField serverAddress;

    /**
     * Constructor for AdminLoginCtrl
     * @param service corresponding service
     */
    @Inject
    public UserLoginCtrl(UserLoginService service) {
        this.service = service;
    }

    /**
     * Getter for server address
     * @return the server address
     */
    public TextField getServerAddress() {
        return serverAddress;
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
        service.connect(serverAddress.getText());
    }


    /**
     * Return's to the main screen
     */
    @FXML
    public void back() {
        service.back();
    }
}
