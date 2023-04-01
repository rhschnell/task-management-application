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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.google.inject.Guice.createInjector;

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
    private HBox boardControls;
    @FXML
    private TextField keyField;
    @FXML
    private Button copyButton;

    private List<String> joinedKeys;
    private int focusedCardIndex;
    private int focusedListIndex;
    private ListCtrl focusedListIndexCtrl;
    private Board shownBoard;
    private Card highlightedStartedCard;
    private boolean hasFocus;

    /**
     * Constructor for WorkspaceCtrl
     * @param service corresponding service
     * @param helperMethods corresponding helper methods
     */
    @Inject
    public WorkspaceCtrl(WorkspaceService service, HelperMethods helperMethods) {
        this.service = service;
        this.helperMethods = helperMethods;
        focusedCardIndex =0;
        focusedListIndex =0;
    }

    public Card getHighlightedStartedCard() {
        return highlightedStartedCard;
    }

    public void setHighlightedStartedCard(Card highlightedStartedCard) {
        this.highlightedStartedCard = highlightedStartedCard;
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
    public void initialize(URL location, ResourceBundle resources) {
        joinedKeys = new ArrayList<>();
        clearWorkspace(); // No board -> board controls

        Timeline tl = new Timeline();
        tl.setCycleCount(-1);
        KeyFrame kf = new KeyFrame(Duration.millis(300),
                event -> {
                    try {
                        refreshWorkspace();
                    } catch (Exception ignored) {}
                });
        tl.getKeyFrames().add(kf);
        tl.play();
    }

    public void connect() {
        if (keyField.getText().equals("")) {return;}

        showBoard(keyField.getText());

        if(!joinedKeys.contains(keyField.getText())) {
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
        shownBoard = null;
        boardName.setText("");
        listContainer.getChildren().clear();
        boardName.setVisible(false);
        listContainer.setVisible(false);
        boardControls.setVisible(false);
    }

    public void refreshWorkspace(boolean... forced) {
        if (forced.length == 0) {forced = new boolean[] {false};}
        // Refresh the board
        String key = "";

        try {
            key = shownBoard.getKey();
            Board serverBoard = service.getBoard(key);
            if (!shownBoard.equals(serverBoard)) {
                showBoard(key);
            }
        } catch (Exception ignored) {}

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
        fillBoard();
        if (!boardControls.isVisible())
            boardControls.setVisible(true);
        if (!boardName.isVisible())
            boardName.setVisible(true);
        if (!listContainer.isVisible())
            listContainer.setVisible(true);
    }
    public void fillBoard()
    {
        listContainer.getChildren().clear();
        boardName.setText(shownBoard.getTitle());
        for (int i = 0; i < shownBoard.getCardLists().size(); i++) {
            var loader = new MyFXML(createInjector(new MainModules()))
                    .load(ListCtrl.class, "client", "windows", "lists", "list", "List.fxml");
            CardList cardList = shownBoard.getCardLists().get(i);
            VBox list = (VBox) loader.getValue();
            ListCtrl controller = loader.getKey();
            controller.setWorkspaceCtrl(this);
            controller.setListId(i);
            controller.setBoardKey(shownBoard.getKey());
            controller.setHelperMethod(helperMethods);
            if (focusedListIndex == i) {
                focusedListIndexCtrl = controller;
                if (focusedCardIndex > shownBoard.getCardLists().get(i).getCards().size())
                    controller.setFocus(shownBoard.getCardLists().get(i).getCards().size());
                else {
                    controller.setFocus(focusedCardIndex);
                }
                hasFocus = true;
            }

            controller.setCardList(cardList);
            controller.displayCards();
            setMoveShortcutListeners(loader.getKey().getCardVBox(),controller);
            controller.setListTitle(cardList.getListTitle());
            int finalI = i;
            //list.setOnMouseExited(event -> {if(finalI !=listHoveredBefore){System.out.println("de");}});
            list.setOnMouseMoved(event -> {if(!((VBox) loader.getValue()).isHover()) System.out.println("");});
            listContainer.getChildren().add(list);
        }
    }
    public void verifyListHovered(int listIndex)
    {
        if(listIndex!=focusedListIndex)
        {
            setHighlightedStartedCard(new Card());
            focusedListIndex=-1;
            focusedCardIndex=-1;
            if(hasFocus) {
                hasFocus =false;
                fillBoard();
            }
        }
    }
    public void setMoveShortcutListeners(VBox list,ListCtrl controller)
    {
        list.setOnMouseEntered(event -> {
            list.requestFocus();
            list.setOnKeyPressed(keyEvent -> {
                {
                    if (keyEvent.getCode() == KeyCode.UP) {
                        setFocusUp();
                    }
                    if (keyEvent.getCode() == KeyCode.DOWN) {
                        setFocusDown();
                    }
                    if (keyEvent.getCode() == KeyCode.ENTER) {
                        openFocused(focusedListIndexCtrl);
                    }
                    if (keyEvent.getCode() == KeyCode.LEFT) {
                        setFocusLeft();
                    }
                    if (keyEvent.getCode() == KeyCode.RIGHT) {
                        setFocusRight();
                    }
                }
            });
        });
        list.setOnMouseExited(event -> {
            controller.resetFocus();
        });
    }

    public void resetFocus()
    {
        focusedListIndex=-1;
        focusedCardIndex=-1;
        focusedListIndexCtrl.resetFocus();
        if(focusedListIndexCtrl !=null && focusedListIndexCtrl.getFocusedCard()!=null)
            focusedListIndexCtrl.getFocusedCard().getKey().removeFocus();
    }
    public void setFocusUp()
    {
        focusedCardIndex = focusedCardIndex -1;
        if(focusedCardIndex<=0)
            focusedCardIndex=1;
        fillBoard();
    }
    public void setFocusDown()
    {
        focusedCardIndex = focusedCardIndex +1;
        if(focusedCardIndex>=focusedListIndexCtrl.getCardList().getCards().size())
            focusedCardIndex=focusedListIndexCtrl.getCardList().getCards().size();
        fillBoard();
    }
    public void setFocusLeft()
    {
        focusedListIndex = focusedListIndex -1;
        fillBoard();
    }
    public void setFocusRight()
    {
        focusedListIndex = focusedListIndex +1;
        fillBoard();
    }
    public void setFocused(int cardIndex,int listIndex)
    {
        focusedCardIndex = cardIndex;
        focusedListIndex =listIndex;
    }
    public void openFocused(ListCtrl listCtrl)
    {
        if(focusedCardIndex!=-1)
            listCtrl.openFocusedIndex();
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
    public String getBoardKey()
    {
        return shownBoard.getKey();
    }
    public Board getShownBoard(){
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
     *
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

    public void setJoinedKeys(List<String> joinedKeys) {
        this.joinedKeys = joinedKeys;
    }

    public void setHelperMethods(HelperMethods helperMethods) {
        this.helperMethods = helperMethods;
    }
}
