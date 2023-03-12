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
import client.TestingClass;
import client.scenes.ListManagement.ListCtrl;
import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WorkspaceCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private Label boardName;

    @FXML
    private Button boardNameButton;

    @FXML
    private HBox listContainer;
    @Inject
    public WorkspaceCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * TODO Calls the function addCard when the button is pressed and adds a new Card to the First List
     */
    @FXML
    public void addCard() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/CardWindows/AddCard.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        String title = "Create Card";
        mainCtrl.popUp(scene, title);
    }

    /**
     * Return's to the main screen
     */
    @FXML
    public void disconnect() {
        mainCtrl.setLogin();
    }

    /**
     * Initialize the board
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    public void initialize(URL location, ResourceBundle resources)
    {
        TestingClass test = new TestingClass();
        Board systemBoard = test.createBoardObject("My First Board");
        boardName.setText(systemBoard.getTitle());
        boardNameButton.setText(systemBoard.getTitle());
        createBoard(systemBoard,listContainer);
    }

    /**
     * TODO Get the information from the database but for testing reasons created
     * @param board
     */
    public void createBoard(Board board,HBox resultedBoard)
    {
        for(int i = 0; i < board.getCardLists().size(); i++)
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/ListManagement/List.fxml"));
            try {
                VBox list = loader.load();
                ListCtrl ctrl = loader.getController();
                ctrl.addCards(board.getCardLists().get(i));
                ctrl.setListTitle(board.getCardLists().get(i).getListTitle());
                resultedBoard.getChildren().add(list);
            } catch(IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}