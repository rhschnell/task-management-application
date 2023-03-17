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
import client.utils.BoardUtils;
import client.utils.ControllerCommunicater;
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
import java.util.List;
import java.util.ResourceBundle;

public class WorkspaceCtrl implements Initializable {

    private final BoardUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private Label boardName;

    @FXML
    private Button boardNameButton;

    @FXML
    private HBox listContainer;

    /**
     *
     * Constructor for WorkspaceCtrl
     * @param server a server util
     * @param mainCtrl a main controller
     */
    @Inject
    public WorkspaceCtrl(BoardUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Displays the AddCard FXML into a new window (Popup).
     * @throws IOException
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
    public void initialize(URL location, ResourceBundle resources)
    {
        TestingClass test = new TestingClass();

        // Load a board from the server
        String targetKey = ControllerCommunicater.getKey();
//        System.out.println(targetKey);
        List<Board> allBoards = server.getBoards();
        Board systemBoard = null;
        for (Board b : allBoards) {
            if (b.getKey().equals(targetKey)) {
                systemBoard = b;
            }
        }
        if (systemBoard == null) {
            // This should later be replaced with a text on the connection screen
            systemBoard = test.createBoardObject("Failure");
        }

        boardName.setText(systemBoard.getTitle());
        boardNameButton.setText(systemBoard.getTitle());
        displayBoard(systemBoard,listContainer);
    }

    /**
     * Adds children (Lists) to the HBOX resulting in the creation of the board.
     * @param board The board that needs to be displayed.
     * @param resultedBoard the Hbox in which the board needs to be displyed.
     */
    public void displayBoard(Board board, HBox resultedBoard)
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
