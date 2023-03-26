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

import client.Main;
import client.MyFXML;
import client.modules.ListModules;
import client.modules.MainModules;
import client.utils.HelperMethods;
import client.utils.Scenes;
import client.windows.lists.cells.CardCtrl;
import client.windows.lists.list.ListCtrl;
import client.windows.workspace.boardcell.BoardCellCtrl;
import com.google.inject.Inject;
import commons.Board;
import commons.CardList;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.google.inject.Guice.createInjector;

public class WorkspaceCtrl implements Initializable {
    private final WorkspaceService service;
    private final HelperMethods hm;
    @FXML
    private Label boardName;
    @FXML
    private VBox boardList;
    @FXML
    private HBox listContainer;
    @FXML
    private HBox boardControls;
    @FXML
    private TextField keyField;
    @FXML
    private Button addListButton;

    private List<String> joinedKeys;
    private Board shownBoard;

    /**
     * Constructor for WorkspaceCtrl
     * @param service corresponding service
     * @param hm corresponding helper methods
     */
    @Inject
    public WorkspaceCtrl(WorkspaceService service, HelperMethods hm) {
        this.service = service;
        this.hm = hm;
    }

    /**
     * Return's to the main screen
     */
    @FXML
    public void disconnect() {
        hm.setScene(Scenes.USER);
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
        joinedKeys = new ArrayList<>();
        // schedule service.refreshWorkspace();
        clearWorkspace(); // No board -> board controls
        //        Timeline tl = new Timeline();
//        tl.setCycleCount(-1);
//        KeyFrame kf = new KeyFrame(Duration.millis(800),
//                event -> {
//                    try {
//                        refreshWorkspace();
//                    } catch (Exception ignored) {}
//                });
//        tl.getKeyFrames().add(kf);
//        tl.play();
    }

    public void connect() {
        showBoard(keyField.getText());
        if(!joinedKeys.contains(keyField.getText())) {
            joinedKeys.add(keyField.getText());
            var boardCell = new MyFXML(createInjector(new MainModules()))
                    .load(BoardCellCtrl.class, "client", "windows", "workspace", "boardcell", "BoardCell.fxml");
            BoardCellCtrl controller = boardCell.getKey();
            controller.setBoard(shownBoard);
            boardCell.getValue().setCursor(Cursor.HAND);
            boardList.getChildren().add(boardCell.getValue());
        }
    }

    /**
     * Method to clear the workspace
     */
    public void clearWorkspace() {
        shownBoard = null;
        boardName.setText("");
        listContainer.getChildren().clear();
        boardName.setVisible(false);
        listContainer.setVisible(false);
        boardControls.setVisible(false);
    }

    public void refreshWorkspace() {
        // TODO
        // Check with server if update
        // if update -> ask server for ids of update items
        // update those locally
        String key = shownBoard.getKey();
        Board serverBoard = service.getBoard(key);
        if (!shownBoard.equals(serverBoard)) {
            showBoard(key);
        }
    }

    public void showBoard(String targetKey) {
        try {
            shownBoard = service.getBoard(targetKey);
        } catch (NotFoundException | BadRequestException e) {
            shownBoard = new Board(targetKey, targetKey, null);
            service.insertBoard(shownBoard);
        }

        listContainer.getChildren().clear();
        boardName.setText(shownBoard.getTitle());
        for (int i = 0; i < shownBoard.getCardLists().size(); i++) {
            var loader = new MyFXML(createInjector(new ListModules()))
                    .load(ListCtrl.class, "client", "windows", "lists", "list", "List.fxml");
            CardList cardList = shownBoard.getCardLists().get(i);
            VBox list = (VBox) loader.getValue();
            ListCtrl ctrl = loader.getKey();
            ctrl.setCardList(cardList);
            ctrl.displayCards();
            ctrl.setListTitle(cardList.getListTitle());
            listContainer.getChildren().add(list);
        }
        if (!boardControls.isVisible())
            boardControls.setVisible(true);
        if (!boardName.isVisible())
            boardName.setVisible(true);
        if (!listContainer.isVisible())
            listContainer.setVisible(true);
    }

    /**
     * Method to delete the shown board from the database
     */
    public void deleteBoard() {
        service.deleteBoard(shownBoard);
        clearWorkspace();
    }

    public void addList() {
        shownBoard.addList(new CardList("Temporary", new ArrayList<>()));
        service.insertBoard(shownBoard);
        refreshWorkspace();
    }
}
