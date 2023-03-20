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
import client.scenes.ListManagement.ListCtrl;
import client.utils.BoardUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.CardList;
import jakarta.ws.rs.BadRequestException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WorkspaceCtrl implements Initializable {

    private final BoardUtils server;
    private final client.scenes.MainCtrl mainCtrl;

    private Board shownBoard;

    @FXML
    private Label boardName;

    @FXML
    private Button boardNameButton;

    @FXML
    private HBox listContainer;

    @FXML
    private HBox boardControls;

    /**
     * Constructor for WorkspaceCtrl
     * @param server a server util
     * @param mainCtrl a main controller
     */
    @Inject
    public WorkspaceCtrl(BoardUtils server, client.scenes.MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        boardName = new Label();
        boardNameButton = new Button();
        listContainer = new HBox();
    }

    public Board getShownBoard() {
        return shownBoard;
    }

    /**
     * Return's to the main screen
     */
    @FXML
    public void disconnect() {
        mainCtrl.setLogin();
    }

    /**
     * Initialize the board by getting a board object. Afterwords, it calls the displays
     * the board by calling the function displayBoard which adds the lists to the Vbox;
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void loadBoard() {
        String targetKey = mainCtrl.getBoardJoinCtrl().getKeyField().getText();
        try {
            shownBoard = server.getBoard(targetKey);
        } catch (BadRequestException e) {
            shownBoard = new Board(targetKey, targetKey, null);
            server.addBoard(shownBoard);
        }
        mainCtrl.setWorkspace();
        displayBoard();
    }

    public void joinPopUp() {
        mainCtrl.getLoginCtrl().joinPopUp();
    }

    /**
     * Adds children (Lists) to the HBOX resulting in the creation of the board.
     */
    public void displayBoard() {
        listContainer.getChildren().clear();
        boardName.setText(shownBoard.getTitle());
        boardNameButton.setText(shownBoard.getTitle());
        for(int i = 0; i < shownBoard.getCardLists().size(); i++) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/ListManagement/List.fxml"));
            try {
                CardList cardList = shownBoard.getCardLists().get(i);
                VBox list = loader.load();
                ListCtrl ctrl = loader.getController();
                ctrl.setCardList(cardList);
                ctrl.addCards(cardList);
                ctrl.setListTitle(cardList.getListTitle());
                listContainer.getChildren().add(list);
            } catch(IOException ioe) {
                ioe.printStackTrace();
            }
        }
        for(Node child : boardControls.getChildren())
            if(!child.isVisible())
                child.setVisible(true);
        if(!boardName.isVisible())
            boardName.setVisible(true);
        if(!boardNameButton.isVisible())
            boardNameButton.setVisible(true);
        if(!listContainer.isVisible())
            listContainer.setVisible(true);
    }

    /**
     * Method to refresh the workspace
     */
    public void refreshWorkspace() {
        shownBoard = server.getBoard(shownBoard.getKey());
        displayBoard();
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
        for(Node child : boardControls.getChildren())
            child.setVisible(false);
    }

    /**
     * Method to delete the shown board from the database
     */
    public void deleteBoard() {
        server.deleteBoard(shownBoard.getKey());
        clearWorkspace();
    }
}
