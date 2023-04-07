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
package client.windows.login.admin;

import client.MyFXML;
import client.modules.MainModules;
import client.utils.HelperMethods;
import client.utils.Scenes;
import client.windows.adminview.boardSpace.AdminCtrl;
import com.google.inject.Inject;
import jakarta.ws.rs.ForbiddenException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static com.google.inject.Guice.createInjector;

public class AdminLoginCtrl implements Initializable {
    private final AdminLoginService service;
    private final HelperMethods helperMethods;
    @FXML
    private Label message;
    @FXML
    private TextField serverAddress;
    @FXML
    private PasswordField passwordField;

    /**
     * Constructor for AdminLoginCtrl
     * @param service corresponding service
     * @param helperMethods corresponding helper methods
     */
    @Inject
    public AdminLoginCtrl(AdminLoginService service, HelperMethods helperMethods) {
        this.service = service;
        this.helperMethods = helperMethods;
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
        this.serverAddress.setOnKeyPressed(this::connectOnEnter);
        this.passwordField.setOnKeyPressed(this::connectOnEnter);
        showWelcome();
    }


    /**
     * Middleware that tries to connect to the user specified server. If successful, redirects
     * the user to the workspace. Otherwise, shows an error message.
     */
    public void connect(){
        if (!service.serverPing(serverAddress.getText())){
            showServerIncorrect();
            return;
        } else if (passwordField.getText().isBlank()) {
            showPasswordBlank();
            return;
        }

        try {
            service.sendPassword(passwordField.getText());

            var loader = new MyFXML(createInjector(new MainModules()))
                    .load(AdminCtrl.class, "client", "windows", "adminview", "AdminView.fxml");


            helperMethods.setServerIP(serverAddress.getText());
            helperMethods.getMemMap().computeIfAbsent(helperMethods.getServerIP(), k -> new ArrayList<>());
            loader.getKey().setHelperMethods(helperMethods);
            loader.getKey().refreshWorkspace(true);
            helperMethods.setScene(new Scene(loader.getValue()));

            showWelcome();
        } catch (ForbiddenException e) {
            showPasswordIncorrect();
            passwordField.clear();
        }
    }


    /**
     * Makes sure the user can join a board by pressing ENTER after typing the key
     * @param event The event that gets handled and checked for the ENTER key
     */
    public void connectOnEnter(KeyEvent event)
    {
        if(event.getCode().equals(KeyCode.ENTER))
        {
            connect();
        }
    }

    /**
     * Shows a welcome message to the user
     */
    private void showWelcome() {
        message.setText("Enter the address and the password of the server.");
    }

    /**
     * Shows a message to the user indicating that the entered password is incorrect
     */
    private void showPasswordIncorrect() {
        message.setText("The password you entered is incorrect. Please try again.");
    }

    private void showPasswordBlank() {
        message.setText("The password field must not be blank.");
    }

    /**
     * Shows a message to the user indicating that the connection to the server could not be made.
     */
    private void showServerIncorrect() {
        message.setText("The server you entered does not exist or is turned off. Please try a new server");
    }

    /**
     * Sets the scene back to the main menu.
     */
    public void back() {
        helperMethods.setScene(Scenes.STARTUP);
    }
}
