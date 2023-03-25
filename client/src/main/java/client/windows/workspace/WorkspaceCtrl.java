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

import client.MyFXML;
import client.modules.ListModules;
import client.windows.lists.ListCtrl;
import com.google.inject.Inject;
import commons.Board;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

import static com.google.inject.Guice.createInjector;

public class WorkspaceCtrl implements Initializable {

    private WorkspaceService service;

    private Board shownBoard;

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
        // TODO
        // Check with server if update
        // if update -> ask server for ids of update items
        // update those locally


    }

    public void connect() {
        service.loadBoard(keyField.getText());
    }


    /**
     * Adds children (Lists) to the HBOX resulting in the creation of the board.
     */
    public void displayBoard() {
        listContainer.getChildren().clear();
        boardName.setText(shownBoard.getTitle());
        boardNameButton.setText(shownBoard.getTitle());
        for (int i = 0; i < shownBoard.getCardLists().size(); i++) {
            var loader = new MyFXML(createInjector(new ListModules()))
                    .load(ListCtrl.class, "client", "window", "lists", "List.fxml");
            CardList cardList = shownBoard.getCardLists().get(i);
            VBox list = (VBox) loader.getValue();
            ListCtrl ctrl = loader.getKey();
            ctrl.setCardList(cardList);
            ctrl.addCards(cardList);
            ctrl.setListTitle(cardList.getListTitle());
            listContainer.getChildren().add(list);
        }
        for (Node child : boardControls.getChildren())
            if (!child.isVisible())
                child.setVisible(true);
        if (!boardName.isVisible())
            boardName.setVisible(true);
        if (!boardNameButton.isVisible())
            boardNameButton.setVisible(true);
        if (!listContainer.isVisible())
            listContainer.setVisible(true);
    }

    public void addListPopup() {
//        var loader = FXML.
//                load(AddListCtrl.class, "client", "windows", "lists", "addList", "AddList.fxml");
//
//        Parent root = loader.getValue();
//        Scene scene = new Scene(root);
//
//        String title = "Create a list";
//        HelperMethods.popUp(scene, title);
//    }
//
//
//    /**
//     * Method to refresh the workspace
//     */
//    public void refreshWorkspace() {
//        shownBoard = server.getBoard(shownBoard.getKey());
//        displayBoard();
    }

    /**
     * Method to clear the workspace
     */
    public void clearWorkspace() {
        shownBoard = null;
        boardName.setText("");
        boardNameButton.setText("");
        listContainer.getChildren().clear();
        boardName.setVisible(false);
        boardNameButton.setVisible(false);
        listContainer.setVisible(false);
        for (Node child : boardControls.getChildren())
            child.setVisible(false);
    }


    /**
     * Method to delete the shown board from the database
     */
    public void deleteBoard() {
//        server.deleteBoard(shownBoard.getKey());
        clearWorkspace();
    }
}
