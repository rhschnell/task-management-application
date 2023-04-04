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
package client.windows.workspace.boardSpace;

import client.MyFXML;
import client.modules.MainModules;
import client.utils.HelperMethods;
import client.utils.Scenes;
import client.windows.cards.view.ViewCardCtrl;
import client.windows.customize.CustomizeCtrl;
import client.windows.lists.list.ListCtrl;
import client.windows.tags.view.TagOverviewCtrl;
import client.windows.workspace.boardCell.BoardCellCtrl;
import client.windows.workspace.leave.LeaveCtrl;
import client.windows.workspace.rename.RenameCtrl;
import com.google.inject.Inject;
import com.sun.istack.NotNull;
import commons.Board;
import commons.Card;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.google.inject.Guice.createInjector;
import static java.lang.Math.abs;

public class WorkspaceCtrl implements Initializable {
    private final WorkspaceService service;
    private HelperMethods helperMethods;

    @FXML
    private Label boardName;
    @FXML
    private VBox boardList;
    @FXML
    private HBox listContainer;
    @FXML
    private VBox boardControls;
    @FXML
    private HBox titleBar;
    @FXML
    private TextField keyField;
    @FXML
    private Button copyButton;

    @FXML
    private Button personalizeButton;

    private List<String> joinedKeys;
    private int focusedCardIndex;
    private int focusedListIndex;
    private Board shownBoard;
    private double oldMouseXPosition;
    private double oldMouseYPosition;
    private double newMouseXPosition;
    private double newMouseYPosition;
    private double mouseMoveThreshold;

    private List<ListCtrl> listControllers;


    /**
     * Constructor for WorkspaceCtrl
     *
     * @param service       corresponding service
     * @param helperMethods corresponding helper methods
     */
    @Inject
    public WorkspaceCtrl(WorkspaceService service, HelperMethods helperMethods) {
        this.service = service;
        this.helperMethods = helperMethods;
        this.listControllers = new ArrayList<>();
        focusedCardIndex = 1;
        focusedListIndex = 1;
        oldMouseXPosition = -1;
        oldMouseYPosition = -1;
        mouseMoveThreshold = 0.5;
    }


    /**
     * Return's to the main screen
     */
    @FXML
    public void disconnect() {
        helperMethods.setScene(Scenes.USER);
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
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        joinedKeys = new ArrayList<>();
        clearWorkspace(); // No board -> board controls

        Timeline tl = new Timeline();
        tl.setCycleCount(-1);
        KeyFrame kf = new KeyFrame(Duration.millis(300),
                event -> {
                    try {
                        refreshWorkspace();
                    } catch (Exception ignored) {
                    }
                });
        tl.getKeyFrames().add(kf);
        tl.play();
    }

    public void connect() {
        if (keyField.getText().equals("")) {
            return;
        }

        showBoard(keyField.getText());

        if (!joinedKeys.contains(keyField.getText())) {
            joinedKeys.add(keyField.getText());
            var boardCell = new MyFXML(createInjector(new MainModules()))
                    .load(BoardCellCtrl.class, "client", "windows", "workspace", "boardCell", "BoardCell.fxml");
            BoardCellCtrl controller = boardCell.getKey();
            controller.setBoard(shownBoard);
            controller.setWorkspaceCtrl(this);
            boardList.getChildren().add(boardCell.getValue());
            helperMethods.getMemMap().get(helperMethods.getServerIP()).add(keyField.getText());
        }
        keyField.clear();
        refreshWorkspace(true);
    }

    /**
     * Method to clear the workspace
     */
    public void clearWorkspace() {
        //Hide title bar
        boardName.setText("");
        titleBar.getChildren().forEach(c -> c.setVisible(false));
        //Hide right bar
        shownBoard = null;
        listContainer.getChildren().clear();
        listContainer.setVisible(false);
        //Hide right bar
        boardControls.setVisible(false);
        boardControls.setManaged(false);
    }

    /**
     * Method to unhide the workspace; the opposite of clearWorkspace()
     */
    public void unhideWorkspace() {
        //Unhide title bar
        titleBar.getChildren().forEach(c -> c.setVisible(true));
        //Unhide right bar
        listContainer.setVisible(true);
        //Unhide right bar
        boardControls.setVisible(true);
        boardControls.setManaged(true);
    }

    public void refreshWorkspace(boolean... forced) {
        if (forced.length == 0) {
            forced = new boolean[]{false};
        }
        // Refresh the board
        String key = "";
        try {
            key = shownBoard.getKey();
            Board serverBoard = service.getBoard(key);
            if (!shownBoard.equals(serverBoard)) {
                showBoard(key);
            }
        } catch (Exception ignored) {
        }
        // Refresh the board list (joined boards)
        boolean removed = false;
        if (joinedKeys == null) {
            return;
        }

        List<String> tempList = new ArrayList<>(joinedKeys);
        for (String k : tempList) {
            try {
                service.getBoard(k);
            } catch (NotFoundException e) {
                removed = true;
                joinedKeys.remove(k);
                helperMethods.getMemMap().get(helperMethods.getServerIP()).remove(k);
                if (key.equals(k)) {
                    clearWorkspace();
                }
            }
        }
        if (removed || forced[0]) {
            boardList.getChildren().clear();
            for (String k : joinedKeys) {
                var boardCell = new MyFXML(createInjector(new MainModules()))
                        .load(BoardCellCtrl.class, "client", "windows", "workspace", "boardCell",
                                "BoardCell.fxml");
                BoardCellCtrl controller = boardCell.getKey();
                controller.setBoard(service.getBoard(k));
                controller.setWorkspaceCtrl(this);
                boardList.getChildren().add(boardCell.getValue());
            }
            if (shownBoard != null) {
                boardName.setText(shownBoard.getTitle());
            }
        }
        updateBoardColours();
    }

    public void updateBoardColours() {
        if (shownBoard != null) {
            listContainer.setStyle("-fx-background-color: #" + shownBoard.getBackgroundColour());
            boardName.setTextFill(Color.web(shownBoard.getFontColour()));
        }
    }

    public void showBoard(String targetKey) {
        try {
            shownBoard = service.getBoard(targetKey);
        } catch (NotFoundException | BadRequestException e) {
            shownBoard = new Board(targetKey, targetKey, null, null);
            service.insertBoard(shownBoard);
        }
        helperMethods.getMemMap().computeIfAbsent(helperMethods.getServerIP(), k -> new ArrayList<>());
        if (!helperMethods.getMemMap().get(helperMethods.getServerIP()).contains(shownBoard.getKey())) {
            helperMethods.getMemMap().get(helperMethods.getServerIP()).add(shownBoard.getKey());
        }
        displayLists();
        unhideWorkspace();
    }

    /**
     * Displays the lists into the Hbox list container
     */
    public void displayLists() {
        listContainer.getChildren().clear();
        listControllers.clear();
        boardName.setText(shownBoard.getTitle());
        for (int i = 0; i < shownBoard.getCardLists().size(); i++) {
            var loader = new MyFXML(createInjector(new MainModules()))
                    .load(ListCtrl.class, "client", "windows", "lists", "list", "List.fxml");
            CardList cardList = shownBoard.getCardLists().get(i);
            VBox list = (VBox) loader.getValue();
            ListCtrl controller = loader.getKey();
            listControllers.add(controller);
            controller.setWorkspaceCtrl(this);
            controller.setListId(i);
            controller.setBoardKey(shownBoard.getKey());
            controller.setHelperMethod(helperMethods);
            controller.setCardList(cardList);
            controller.displayCards();
            setMoveShortcutListeners(loader.getKey().getCardVBox());
            controller.setListTitle(cardList.getListTitle());
            listContainer.getChildren().add(list);
            listContainer.setOnMouseMoved(event -> {
                newMouseXPosition = event.getSceneX();
                newMouseYPosition = event.getSceneY();
            });
        }
        if(focusedIndicesAreValid())
        {
            VBox vbox = getFocusPosition();
            vbox.getChildren().get(focusedCardIndex - 1).setOpacity(0.4);
        }
    }

    /**
     * Set listener on the listVbox so that we can know when and where to move the focus
     *
     * @param listVbox A VBOX containing cards
     */
    public void setMoveShortcutListeners(VBox listVbox) {
        listVbox.requestFocus();
        listVbox.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.UP) {
                moveFocusUp();
            }
            if (event.getCode() == KeyCode.DOWN) {
                moveFocusDown();
            }
            if (event.getCode() == KeyCode.LEFT) {
                moveFocusLeft();
            }
            if (event.getCode() == KeyCode.RIGHT) {
                moveFocusRight();
            }
            event.consume();
        });
    }

    /**
     * Method for handling the shortcut Shift + Up/Down
     *
     * @param shiftUpWards If true, the reordering will shift the selected cards upwards.
     *                     If false, it will go downwards
     */
    public void handleReorderingShortcut(boolean shiftUpWards) {
        ListCtrl focusedListController = listControllers.get(focusedListIndex - 1);

        // Can only move up/down if the focused card is not already at the top/bottom
        if (focusedCardIndex == (shiftUpWards ? 0 :
                focusedListController.getCardList().getCards().size() - 1)) return;


        Card cardToMove = focusedListController.getCardList().getCard(focusedCardIndex - 1);
        int destIndex = (focusedCardIndex - 1 + (shiftUpWards ? -1 : 1));

        if (shiftUpWards) {
            moveFocusUp();
        } else {
            moveFocusDown();
        }

        // Shift the card to the new position in the controller
        focusedListController.shiftCard(cardToMove, destIndex);
    }

    /**
     * Returns the ListVbox in which the focusedCard is located
     *
     * @return the ListBox that contains the focused card
     */
    public VBox getFocusPosition() {
        return (VBox) ((ScrollPane) ((VBox) (listContainer.getChildren().get(focusedListIndex - 1))).
                getChildren().get(1)).getContent();
    }

    /**
     * Gets the focused list index
     *
     * @return The focusedListIndex
     */
    public int getFocusedListIndex() {
        return focusedListIndex;
    }

    /**
     * Verifies if the focused indices actually contain cards or are not valid
     * Example: left keypad on the first list won't make the list focused index
     * valid
     *
     * @return If the condition is valid
     */
    public boolean focusedIndicesAreValid() {
        if (focusedCardIndex > 0 && focusedListIndex > 0 && focusedListIndex <= shownBoard.getCardLists().size()
            && focusedCardIndex <= shownBoard.getCardLists().get(focusedListIndex - 1).getCards().size() &&
            shownBoard.getCardLists().get(focusedListIndex - 1).getCards().size() > 0)
            return true;
        return false;
    }

    /**
     * Removes the focus from the card that is now focused
     */
    public void removeFocus() {
        if (focusedIndicesAreValid()) {
            VBox vbox = getFocusPosition();
            vbox.getChildren().get(focusedCardIndex - 1).setOpacity(1);
        }
    }

    /**
     * Resets the focus and sets the indices back to their default value.
     */
    public void resetFocus() {

        if (abs(newMouseXPosition - oldMouseXPosition) < mouseMoveThreshold
            && abs(newMouseYPosition - oldMouseYPosition) < mouseMoveThreshold) return;

        if (focusedIndicesAreValid()) {
            VBox vbox = getFocusPosition();
            vbox.getChildren().get(focusedCardIndex - 1).setOpacity(1);
        }
        System.out.println("resetFocused before " + oldMouseXPosition + " " + oldMouseYPosition);
        System.out.println("reset Focused after " + newMouseXPosition + " " + newMouseYPosition);
        oldMouseXPosition = -1;
        oldMouseYPosition = -1;
        focusedCardIndex = -1;
        focusedListIndex = -1;
    }


    /**
     * Moves the focused one place up
     */
    public void moveFocusUp() {
        removeFocus();
        focusedCardIndex = focusedCardIndex - 1;
        if (focusedCardIndex <= 0)
            focusedCardIndex = 1;
        if (focusedIndicesAreValid()) {
            oldMouseXPosition = newMouseXPosition;
            oldMouseYPosition = newMouseYPosition;
            VBox vbox = getFocusPosition();
            vbox.getChildren().get(focusedCardIndex - 1).setOpacity(0.6);
        }
        autoScroll(focusedCardIndex, focusedListIndex);

    }

    /**
     * Moves the focus one place down
     */
    public void moveFocusDown() {
        removeFocus();
        focusedCardIndex = focusedCardIndex + 1;
        if (focusedCardIndex > 0 && focusedListIndex > 0 && focusedCardIndex >= shownBoard.getCardLists().
                get(focusedListIndex - 1).getCards().size())
            focusedCardIndex = shownBoard.getCardLists().get(focusedListIndex - 1).getCards().size();
        if (focusedIndicesAreValid()) {
            oldMouseXPosition = newMouseXPosition;
            oldMouseYPosition = newMouseYPosition;
            VBox vbox = getFocusPosition();
            vbox.getChildren().get(focusedCardIndex - 1).setOpacity(0.6);
        }
        autoScroll(focusedCardIndex, focusedListIndex);
    }

    /**
     * Moves the focus one place left
     */
    public void moveFocusLeft() {
        removeFocus();
        focusedListIndex = focusedListIndex - 1;
        if (focusedListIndex <= 0)
            focusedListIndex = 1;
        if (focusedCardIndex > 0 && shownBoard.getCardLists()
                    .get(focusedListIndex - 1).getCards().size() <= focusedCardIndex)
            focusedCardIndex = shownBoard.getCardLists().get(focusedListIndex - 1).getCards().size();
        if (focusedIndicesAreValid()) {
            VBox vbox = getFocusPosition();
            vbox.getChildren().get(focusedCardIndex - 1).setOpacity(0.6);
            autoScroll(focusedCardIndex, focusedListIndex);
        }
    }

    /**
     * Moves the focus one place right
     */
    public void moveFocusRight() {
        removeFocus();
        focusedListIndex = focusedListIndex + 1;
        if (focusedListIndex >= shownBoard.getCardLists().size())
            focusedListIndex = shownBoard.getCardLists().size();
        if (focusedCardIndex > 0 && focusedListIndex > 0
            && shownBoard.getCardLists()
                       .get(focusedListIndex - 1).getCards().size() <= focusedCardIndex)
            focusedCardIndex = shownBoard.getCardLists()
                    .get(focusedListIndex - 1).getCards().size();
        if (focusedIndicesAreValid()) {
            VBox vbox = getFocusPosition();
            vbox.getChildren().get(focusedCardIndex - 1).setOpacity(0.6);
            autoScroll(focusedCardIndex, focusedListIndex);
        }
    }

    /**
     * Sets the card that needs to be focused
     *
     * @param cardIndex the Index of the card that needs to be focused
     * @param listIndex the Index of the list that contains the card that needs
     *                  to be focused
     */
    public void setFocusedCard(int cardIndex, int listIndex) {
        if (abs(newMouseXPosition - oldMouseXPosition) < mouseMoveThreshold
            && abs(newMouseYPosition - oldMouseYPosition) < mouseMoveThreshold) return;

        focusedCardIndex = cardIndex;
        focusedListIndex = listIndex;
        if (focusedIndicesAreValid()) {
            VBox vbox = getFocusPosition();
            vbox.getChildren().get(focusedCardIndex - 1).setOpacity(0.6);
        }
        System.out.println("setFocused before " + oldMouseXPosition + " " + oldMouseYPosition);
        System.out.println("set Focused after " + newMouseXPosition + " " + newMouseYPosition);
        oldMouseXPosition = newMouseXPosition;
        oldMouseYPosition = newMouseYPosition;

    }

    /**
     * Auto-Scrolls the list that contains the focused card if the list is scrollable
     * in order to have the focused card visible
     *
     * @param cardIndex the index of the card that is focused
     * @param listIndex the index of the list that contains the focused card
     */
    public void autoScroll(int cardIndex, int listIndex) {

        if (focusedIndicesAreValid()) {
            ScrollPane scrollPane = ((ScrollPane) ((VBox) (listContainer.getChildren().get(listIndex - 1))).
                    getChildren().get(1));
            scrollPane.setVvalue((double) (cardIndex - 1) * 30 / (315 - 30));
        }
    }

    /**
     * Opens a pop-up that displays the information that the focused card contains
     */
    public void openFocusedCard() {
        if (focusedIndicesAreValid()) {
            var loader = new MyFXML(createInjector(new MainModules()))
                    .load(ViewCardCtrl.class, "client", "windows", "cards", "ViewCard.fxml");

            Parent root = loader.getValue();
            Scene scene = new Scene(root);
            ViewCardCtrl controller = loader.getKey();
            scene.getRoot().setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ESCAPE)
                    loader.getKey().escape();
            });
            controller.onlyForViewing();
            controller.setCard(shownBoard.getCardLists().get(focusedListIndex - 1).
                    getCards().get(focusedCardIndex - 1));
            controller.setBoardKey(getBoardKey());
            controller.displayTasks();
            String title = "View Card";
            HelperMethods.popUp(scene, title);
        }
    }

    /**
     * Method to delete the shown board from the database
     */
    public void deleteBoard() {
        service.deleteBoard(shownBoard);
        joinedKeys.remove(shownBoard.getKey());
        refreshWorkspace(true);
        clearWorkspace();
    }

    public void leaveBoard() {
        leaveBoard(shownBoard);
    }

    public void leaveBoard(@NotNull Board board) {
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(LeaveCtrl.class, "client", "windows", "workspace", "leave", "LeaveBoard.fxml");

        LeaveCtrl leaveCtrl = loader.getKey();
        leaveCtrl.setWorkspaceCtrl(this);
        leaveCtrl.setHelperMethods(helperMethods);
        leaveCtrl.setJoinedKeys(joinedKeys);
        leaveCtrl.setLeaveBoard(board);

        helperMethods.popUp(new Scene(loader.getValue()), "Leave Board");
    }

    public void addList() {
        shownBoard.addList(new CardList("New List", new ArrayList<>()));
        service.insertBoard(shownBoard);
    }

    /**
     * Gets the board key
     *
     * @return the board key
     */
    public String getBoardKey() {
        return shownBoard.getKey();
    }

    public Board getShownBoard() {
        return shownBoard;
    }

    public void tagOverview() {
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(TagOverviewCtrl.class, "client", "windows", "tags", "TagOverview.fxml");

        loader.getKey().setBoard(shownBoard);

        loader.getKey().displayTagList();

        Parent root = loader.getValue();
        Scene scene = new Scene(root);

        String title = "Tag Overview";
        HelperMethods.popUp(scene, title);
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
        copyButton.setDisable(true);
        delay(2000, () -> {
            copyButton.setText("Copy key");
            copyButton.getStyleClass().remove("blue-button");
            copyButton.getStyleClass().add("green-button");
            copyButton.setDisable(false);
        });
    }

    /**
     * Delay method
     * Source: https://stackoverflow.com/questions/26454149/make-javafx-wait-and-continue-with-code
     *
     * @param millis       amount of milliseconds to delay
     * @param continuation empty
     */
    private static void delay(long millis, Runnable continuation) {
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(millis);
                } catch (InterruptedException ignored) {
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(event -> continuation.run());
        new Thread(sleeper).start();
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
        loader.getKey().setAdmin(false);
        HelperMethods.popUp(scene, "Rename board: " + this.getShownBoard().getTitle());
        refreshWorkspace(true);
    }

    public void customizeBoard() {
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(CustomizeCtrl.class, "client", "windows", "customize", "Customize.fxml");

        loader.getKey().setBoard(shownBoard);
        Parent root = loader.getValue();
        Scene scene = new Scene(root);

        String title = "Customize";
        HelperMethods.popUp(scene, title);
    }

    public void setJoinedKeys(List<String> joinedKeys) {
        this.joinedKeys = joinedKeys;
    }

    public void setHelperMethods(HelperMethods helperMethods) {
        this.helperMethods = helperMethods;
    }

    /**
     * Gets the index of the currently focused card
     *
     * @return The current focusedCardIndex
     */
    public int getFocusedCardIndex() {
        return focusedCardIndex;
    }

}
