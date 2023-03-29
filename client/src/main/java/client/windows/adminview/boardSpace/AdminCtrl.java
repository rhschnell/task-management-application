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
package client.windows.adminview.boardSpace;

import client.MyFXML;
import client.modules.MainModules;
import client.utils.HelperMethods;
import client.utils.Scenes;
import client.windows.adminview.boardCell.BoardCellCtrl;
import client.windows.adminview.deleteBoard.DeleteBoardCtrl;
import client.windows.lists.list.ListCtrl;
import com.google.inject.Inject;
import commons.Board;
import commons.CardList;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.inject.Guice.createInjector;

public class AdminCtrl implements Initializable {
    private final AdminService service;
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

    private Set<String> joinedKeys;
    private Board shownBoard;

    /**
     * Constructor for WorkspaceCtrl
     * @param service corresponding service
     * @param hm corresponding helper methods
     */
    @Inject
    public AdminCtrl(AdminService service, HelperMethods hm) {
        this.service = service;
        this.hm = hm;
    }

    /**
     * Return's to the main screen
     */
    @FXML
    public void disconnect() {
        hm.setScene(Scenes.ADMIN);
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
        joinedKeys = service.getBoards().stream()
                .map(Board::getKey)
                .collect(Collectors.toSet());

        clearWorkspace();

        for(Board board : service.getBoards()) {
            var boardCell = new MyFXML(createInjector(new MainModules()))
                    .load(BoardCellCtrl.class, "client", "windows", "adminview", "boardcell", "BoardCell.fxml");
            BoardCellCtrl controller = boardCell.getKey();
            controller.setBoard(board);
            controller.setAdminCtrl(this);
            boardList.getChildren().add(boardCell.getValue());
        }

        // schedule service.refreshWorkspace();
        Timeline tl = new Timeline();
        tl.setCycleCount(-1);
        KeyFrame kf = new KeyFrame(Duration.millis(100),
                event -> {
                    try {
                        refreshWorkspace(false);
                    } catch (Exception ignored) {}
                });
        tl.getKeyFrames().add(kf);
        tl.play();
    }

    public void add() {
        showBoard(keyField.getText());

        if (keyField.getText().equals("")) {
            return;
        }

        if(!joinedKeys.contains(keyField.getText())) {
            joinedKeys.add(keyField.getText());
            var boardCell = new MyFXML(createInjector(new MainModules()))
                    .load(BoardCellCtrl.class, "client", "windows", "adminview", "boardCell", "BoardCell.fxml");
            BoardCellCtrl controller = boardCell.getKey();
            controller.setBoard(shownBoard);
            controller.setAdminCtrl(this);
            boardList.getChildren().add(boardCell.getValue());
        }

        keyField.clear();
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

    public void refreshWorkspace(boolean forced) {
        // Refresh the board
        try {
            String key = shownBoard.getKey();
            Board serverBoard = service.getBoard(key);
            if (!shownBoard.equals(serverBoard)) {
                showBoard(key);
            }
        } catch (Exception ignored) {}

        // Refresh the board list (joined boards)
        Set<String> currentKeys = new HashSet<>(joinedKeys);
        joinedKeys = service.getBoards().stream()
                .map(Board::getKey)
                .collect(Collectors.toSet());

        if(!currentKeys.equals(joinedKeys) || forced) {
            boardList.getChildren().clear();
            for (String k : joinedKeys) {
                var boardCell = new MyFXML(createInjector(new MainModules()))
                        .load(BoardCellCtrl.class, "client", "windows", "adminview", "boardCell",
                                "BoardCell.fxml");
                BoardCellCtrl controller = boardCell.getKey();
                controller.setBoard(service.getBoard(k));
                controller.setAdminCtrl(this);
                boardList.getChildren().add(boardCell.getValue());
            }
        }

    }

    public void showBoard(String targetKey) {
        try {
            shownBoard = service.getBoard(targetKey);
        } catch (NotFoundException | BadRequestException e) {
            shownBoard = new Board(targetKey, targetKey, null, null);
            service.insertBoard(shownBoard);
        }

        listContainer.getChildren().clear();
        boardName.setText(shownBoard.getTitle());
        for (int i = 0; i < shownBoard.getCardLists().size(); i++) {
            var loader = new MyFXML(createInjector(new MainModules()))
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
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(DeleteBoardCtrl.class,
                        "client", "windows", "adminview", "deleteboard", "DeleteBoard.fxml");

        Parent root = loader.getValue();
        Scene scene = new Scene(root);
        loader.getKey().setAdminCtrl(this);
        loader.getKey().setBoardKey(shownBoard.getKey());
        HelperMethods.popUp(scene, "Delete the board");
    }

    public void addList() {
        shownBoard.addList(new CardList("New List", new ArrayList<>()));
        service.insertBoard(shownBoard);
        refreshWorkspace(false);
    }
}
