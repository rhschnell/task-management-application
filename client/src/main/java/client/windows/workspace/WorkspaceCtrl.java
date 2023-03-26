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
package client.windows.workspace;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class WorkspaceCtrl implements Initializable {

    private WorkspaceService service;

    @FXML
    private Label boardName;
    @FXML
    private Button boardNameButton;
    @FXML
    private HBox listContainer;
    @FXML
    private HBox boardControls;
    @FXML
    private TextField keyField;
    @FXML
    private Button addListButton;

    /**
     * Constructor for WorkspaceCtrl
     * @param service corresponding service
     */
    @Inject
    public WorkspaceCtrl(WorkspaceService service) {
        this.service = service;
    }

    /**
     * Return's to the main screen
     */
    @FXML
    public void disconnect() {
        service.disconnect();
    }

    /**
     * Initialize the board by getting a board object. Afterwords, it calls the displays
     * the board by calling the function displayBoard which adds the lists to the Vbox;
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    public void initialize(URL location, ResourceBundle resources) {
        // schedule service.refreshWorkspace();




        // SET ALL FXML ELEMENTS IN SERVICE
        service.setBoardControls(boardControls);
        service.setBoardName(boardName);
        service.setBoardNameButton(boardNameButton);
        service.setKeyField(keyField);
        service.setListContainer(listContainer);
        service.setAddListButton(addListButton);

        service.clearWorkspace(); // No board -> board controls
    }

    public void connect() {
        service.showBoard(keyField.getText());
    }

    /**
     * Method to clear the workspace
     */
    public void clearWorkspace() {
        service.clearWorkspace();
    }


    /**
     * Method to delete the shown board from the database
     */
    public void deleteBoard() {
        service.deleteBoard();
        clearWorkspace();
    }

    /**
     * TEMPORARY METHOD FOR CONTINUED TESTING
     */
    public void addListTemp() {
        service.addList();
    }
}
