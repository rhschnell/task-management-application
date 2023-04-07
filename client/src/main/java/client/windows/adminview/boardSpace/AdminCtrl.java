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
import client.windows.customize.CustomizeCtrl;
import client.windows.lists.list.ListCtrl;
import client.windows.tags.view.TagOverviewCtrl;
import client.windows.workspace.rename.RenameCtrl;
import com.google.inject.Inject;
import commons.Board;
import commons.CardList;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.inject.Guice.createInjector;

public class AdminCtrl implements Initializable {
    private final AdminService service;
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
    private Button copyButton;

    private Set<String> joinedKeys;
    private Board shownBoard;
    private HelperMethods helperMethods;

    /**
     * Constructor for WorkspaceCtrl
     * @param service corresponding service
     * @param hm corresponding helper methods
     */
    @Inject
    public AdminCtrl(AdminService service, HelperMethods hm) {
        this.service = service;
        this.helperMethods = hm;
        joinedKeys = new HashSet<>();
    }

    /**
     * Return's to the main screen
     */
    @FXML
    public void disconnect() {
        helperMethods.setScene(Scenes.ADMIN);
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
//        try {
//            joinedKeys = service.getBoards().stream()
//                    .map(Board::getKey)
//                    .collect(Collectors.toSet());
//
//            clearWorkspace();
//
//            for (Board board : service.getBoards()) {
//                var boardCell = new MyFXML(createInjector(new MainModules()))
//                        .load(BoardCellCtrl.class, "client", "windows", "adminview",
//                                "boardcell", "BoardCell.fxml");
//                BoardCellCtrl controller = boardCell.getKey();
//                controller.setBoard(board);
//                controller.setAdminCtrl(this);
//                boardList.getChildren().add(boardCell.getValue());
//            }
//        } catch (ProcessingException ignored) {}
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

    /**
     * Handles the admin press on the ass button by creating a new board on the server
     */
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

    /**
     * Refreshes the workspace
     * @param forced If true forces the refresh even though no board has been deleted
     */
    public void refreshWorkspace(boolean forced) {
        // Refresh the board
        try {
            String key = shownBoard.getKey();
            Board serverBoard = service.getBoard(key);
            boardName.setText(shownBoard.getTitle());
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
        updateBoardColours();
    }

    /**
     * Updates the board on screen to display the correct background and font color
     */
    public void updateBoardColours()
    {
        if(shownBoard!=null){
            listContainer.setStyle("-fx-background-color: #"+shownBoard.getBackgroundColour());
            boardName.setTextFill(Color.web(shownBoard.getFontColour()));
        }
    }


    /**
     * Shows the board with the specified key. Retrieves it from the server or creates it if it
     * does not exist yet
     * @param targetKey The key of the board to show
     */
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
            ctrl.setBoardKey(shownBoard.getKey());
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
        helperMethods.popUp(scene, "Delete the board");
    }


    /**
     * Adds a new list to the currently shown board
     */
    public void addList() {
        shownBoard.addList(new CardList("New List", new ArrayList<>()));
        service.insertBoard(shownBoard);
        refreshWorkspace(false);
    }



    /**
     * Method to load the tag-overview window in a new popup screen
     */
    public void tagOverview() {
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(TagOverviewCtrl.class, "client", "windows", "tags", "TagOverview.fxml");

        loader.getKey().setBoardKey(shownBoard.getKey());

        loader.getKey().displayTagList();

        Parent root = loader.getValue();
        Scene scene = new Scene(root);

        String title = "Tag Overview";
        helperMethods.popUp(scene, title);
    }

    /**
     * Method to rename boards.
     * Called by Rename button in workspace
     */
    public void renameBoard() {
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(RenameCtrl.class, "client", "windows", "workspace", "rename", "Rename.fxml");

        Scene scene = new Scene(loader.getValue());
        loader.getKey().setRemoteCtrl(this);
        loader.getKey().setAdmin(true);
        helperMethods.popUp(scene, "Rename board: " + shownBoard.getTitle());
        refreshWorkspace(true);
    }

    /**
     * Getter for shown board
     * @return the shown board
     */
    public Board getShownBoard() {
        return shownBoard;
    }

    /**
     * Method to copy the key of currently shown board to the
     * clipboard. This method is called by the copy key button.
     * <p>
     * After copying the key to the clipboard a small notification is displayed.
     */
    public void copyKey() throws InterruptedException {
        // Functionality
        String key = shownBoard.getKey();
        service.copyKey(key);

        // Notification
        copyButton.setText("Copied key!");
        copyButton.getStyleClass().remove("green-button");
        copyButton.getStyleClass().add("blue-button");
        delay(2000, () -> {
            copyButton.setText("Copy key");
            copyButton.getStyleClass().remove("blue-button");
            copyButton.getStyleClass().add("green-button");});
    }

    /**
     * Delay method
     * Source: <a href="https://stackoverflow.com/questions/26454149/make-javafx-wait-and-continue-with-code">...</a>
     * @param millis amount of milliseconds to delay
     * @param continuation empty
     */
    private static void delay(long millis, Runnable continuation) {
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try { Thread.sleep(millis); }
                catch (InterruptedException ignored) { }
                return null;
            }
        };
        sleeper.setOnSucceeded(event -> continuation.run());
        new Thread(sleeper).start();
    }

    /**
     * Sets the instance of HelperMethods
     * @param helperMethods The instance of HelperMethods to set
     */
    public void setHelperMethods(HelperMethods helperMethods) {
        this.helperMethods = helperMethods;
        this.service.setServer(helperMethods.getServerIP());
    }


    /**
     * Method to open the customize-window in a new popup
     */
    @FXML
    public void customizeBoard() {
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(CustomizeCtrl.class, "client", "windows", "customize", "Customize.fxml");

        if(shownBoard == null)
        {
            return;
        }
        loader.getKey().setBoard(shownBoard);
        Parent root = loader.getValue();
        Scene scene = new Scene(root);

        String title = "Customize";
        helperMethods.popUp(scene, title);
    }
}
