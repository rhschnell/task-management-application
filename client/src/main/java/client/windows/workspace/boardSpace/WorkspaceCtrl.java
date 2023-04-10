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
import client.serverUtils.WebsocketUtils;
import client.utils.ErrorDialogEntry;
import client.utils.HelperMethods;
import client.utils.Scenes;
import client.windows.cards.view.ViewCardCtrl;
import client.windows.customize.CustomizeCtrl;
import client.windows.lists.cells.CardService;
import client.windows.lists.cells.RenameCardCtrl;
import client.windows.lists.list.ListCtrl;
import client.windows.lists.list.NewListNameCtrl;
import client.windows.tags.view.TagListFromShortcutCtrl;
import client.windows.tags.view.TagOverviewCtrl;
import client.windows.workspace.boardCell.BoardCellCtrl;
import client.windows.workspace.delete.DeleteBoardCtrl;
import client.windows.workspace.leave.LeaveCtrl;
import client.windows.workspace.lock.AccessDeniedCtrl;
import client.windows.workspace.lock.LockPopUpCtrl;
import client.windows.workspace.rename.RenameCtrl;
import com.google.inject.Inject;
import com.sun.istack.NotNull;
import commons.*;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.springframework.messaging.simp.stomp.StompSession;

import java.net.URL;
import java.util.*;

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
    private TextField titleField;
    @FXML
    private Button copyButton;

    // Locking needs
    @FXML
    private Button renameButton;
    @FXML
    private Button personalizeButton;
    @FXML
    private Button tagsButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button removePasswordButton;
    @FXML
    private Button setPasswordButton;
    @FXML
    private Button addListButton;
    private Button[] lockButtonArray;

    @FXML
    private Button unlockBoardButton;

    private Map<String, String> pwdMap;

    private Set<String> joinedKeys;
    private int focusedCardIndex;
    private int focusedListIndex;
    private Board shownBoard;
    private double oldMouseXPosition;
    private double oldMouseYPosition;
    private double newMouseXPosition;
    private double newMouseYPosition;
    private double mouseMoveThreshold;

    private List<ListCtrl> listControllers;
    private final CardService cardService;

    private List <StompSession.Subscription> boardSubscriber;
    private List <StompSession.Subscription> listSubscribers;
    private boolean admin;
    @FXML private Label screenTitle;
    @FXML private Button leaveButton;
    private WebsocketUtils websocketUtils;


    /**
     * Constructor for WorkspaceCtrl
     *
     * @param service       corresponding service
     * @param cardService   injected cardService instance
     * @param helperMethods corresponding helper methods
     * @param websocketUtils injected websocket instance
     */
    @Inject
    public WorkspaceCtrl(WorkspaceService service,
                         CardService cardService, HelperMethods helperMethods,WebsocketUtils websocketUtils) {
        this.service = service;
        this.cardService = cardService;
        this.helperMethods = helperMethods;
        this.listControllers = new ArrayList<>();
        focusedCardIndex = -1;
        focusedListIndex = -1;
        oldMouseXPosition = -1;
        oldMouseYPosition = -1;
        mouseMoveThreshold = 0.5;
        this.pwdMap = new HashMap<>();
        this.admin = false;
        this.websocketUtils=websocketUtils;
    }
    /**
     * Return's to the main screen
     */
    public void disconnect() {
        for(int i = 0; i< boardSubscriber.size(); i++)
        {
            boardSubscriber.get(i).unsubscribe();
        }
        if (isAdmin()) {
            helperMethods.setScene(Scenes.ADMIN);
        } else {
            helperMethods.setScene(Scenes.USER);
        }
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
        joinedKeys = new HashSet<>();
        boardSubscriber =new ArrayList<>();
        listSubscribers =new ArrayList<>();
        clearWorkspace(); // No board -> board controls

        // Initialize array of buttons that need to be disabled if board is locked
        this.lockButtonArray = new Button[]{
            renameButton,
            personalizeButton,
            tagsButton,
            deleteButton,
            removePasswordButton,
            setPasswordButton,
            addListButton
        };
    }

    /**
     * Handles the action of connecting to a board with the typed invite key
     */
    public void connect() {

        String key = helperMethods.getInputValidator().stripWhitespace(keyField.getText());

        if (!helperMethods.getInputValidator().isValidInputNonEmpty(key)) {
            emptyKeyPopUp();
            return;
        }
        if (!helperMethods.getInputValidator().isValidInputLength(key)) {
            helperMethods.showErrorDialog(new ErrorDialogEntry("Error!",
                    "The title cannot be longer than " + HelperMethods.getMaxInputLength()));
            return;
        }

        List<String> tempList = new ArrayList<>();

        for (Board b : service.getBoards()) {
            tempList.add(b.getKey());
        }
        for(int i = 0; i< boardSubscriber.size(); i++)
        {
            boardSubscriber.get(i).unsubscribe();
        }
        showBoard(key);
        keyField.clear();
    }

    /**
     * Handles the action of creating a new board with the typed title
     */
    public void create() {

        String title = helperMethods.getInputValidator().stripWhitespace(titleField.getText());
        if (!helperMethods.getInputValidator().isValidInputNonEmpty(title)) {
            emptyTitlePopUp();
            return;
        }
        if (!helperMethods.getInputValidator().isValidInputLength(title)) {
            helperMethods.showErrorDialog(new ErrorDialogEntry("Error!",
                    "The title cannot be longer than " + HelperMethods.getMaxInputLength()));
            return;
        }

        shownBoard = new Board(title, null, null, null);
        CardColorPreset defaultPreset = new CardColorPreset("Default", "0xDEEDE7FF", "0x000000FF");
        defaultPreset.setDefault(true);
        shownBoard.addPreset(defaultPreset);
        shownBoard = service.insertBoard(shownBoard);
        String key = shownBoard.getKey();

        List<String> tempList = new ArrayList<>(joinedKeys);
        for(int i = 0; i< boardSubscriber.size(); i++)
        {
            boardSubscriber.get(i).unsubscribe();
        }
        showBoard(key);

        pwdMap.putIfAbsent(key, "");

        // Add this board to the list of joined boards (keys) and show it in the UI
        if (!tempList.contains(key)) {
            joinedKeys.add(key);
        }
        titleField.clear();
        refreshWorkspace(true);
    }

    /**
     * Makes sure the user can join a board by pressing ENTER after typing the key
     *
     * @param event The event that gets handled and checked for the ENTER key
     */
    public void connectOnEnter(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            connect();
        }
    }

    /**
     * Makes sure the user can create a board by pressing ENTER after typing the title
     * @param event The event that gets handled and checked for the ENTER key
     */
    public void createOnEnter(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER))
        {
            create();
        }
    }

    /**
     * Method to clear the workspace
     */
    public void clearWorkspace() {
        for(int i = 0; i< boardSubscriber.size(); i++)
        {
            boardSubscriber.get(i).unsubscribe();
        }
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

    /*
     START OF LOCK / UNLOCK METHODS
     */

    /**
     * Method used for locking the lists when the board gets set to locked
     */
    public void lockLists() {
        for (int i = 0; i < listControllers.size(); ++i) {
            ListCtrl listCtrl = listControllers.get(i);
            listCtrl.lock();
        }
    }

    /**
     * Method used for locking the buttons when the board gets set to locked
     */
    public void lockButtons() {
        for (Button b : lockButtonArray) {
            b.setDisable(true);
        }
        unlockBoardButton.setVisible(true);
    }

    /**
     * Method used for unlocking the lists when the board gets set to unlocked
     */
    public void unlockLists() {
        for (int i = 0; i < listControllers.size(); ++i) {
            ListCtrl listCtrl = listControllers.get(i);
            listCtrl.unlock();
        }
    }

    /**
     * Method used for unlocking the buttons when the board gets set to unlocked
     */
    public void unlockButtons() {
        for (Button b : lockButtonArray) {
            b.setDisable(false);
        }
        unlockBoardButton.setVisible(false);
    }

    /**
     * Method for setting the password on a board.
     * Called by the set password button or the lock icon.
     */
    public void setPassword() {
        lockUnlock(shownBoard, "lock");
    }

    /**
     * Method for removing the password from a board.
     * Called by remove password button.
     */
    public void remPassword() {
        shownBoard.setProtected(false);
        shownBoard.setPassword("");
        service.insertBoard(shownBoard);
    }

    /**
     * Method for unlocking shown board.
     * Called by unlock button
     */
    public void unlock() {
        lockUnlock(shownBoard, "unlock");
    }

    /**
     * Method to either add a password onto current board or unlock it while shown
     *
     * @param board board to be changed
     * @param mode  "lock" or "unlock"
     */
    public void lockUnlock(Board board, String mode) {
        // Load new instance of popup
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(LockPopUpCtrl.class, "client", "windows", "workspace", "lock", "Lock.fxml");

        // Setter Injection
        LockPopUpCtrl ctrl = loader.getKey();
        ctrl.setBoard(board);
        ctrl.setMode(mode);
        ctrl.setWorkspace(this);

        // Set title depending on if board locket
        String title = board.isProtected() ? "Unlock board" : "Set password";

        // Create popup through helper method
        helperMethods.popUp(new Scene(loader.getValue()), title);

        // Fix lock button state in workspace
        if (board.equals(shownBoard) && mode.equals("unlock")) {
            unlockBoardButton.setDisable(true);
            unlockLists();
            unlockButtons();
        }
        refreshWorkspace(true);
    }

    /**
     * Gets the password map containing all the board keys and the corresponding passwords
     *
     * @return The password map
     */
    public Map<String, String> getPwdMap() {
        return pwdMap;
    }

    /*
    END OF LOCK / UNLOCK METHODS
     */

    /**
     * Refreshes the workspace
     * @param forceBoardListRefresh If true forces the refresh even though no board has been deleted
     */
    public void refreshWorkspace(boolean forceBoardListRefresh) {
        // Refresh the board
        String key = "";
        try {
            key = shownBoard.getKey();
            refreshBoard(key);
        } catch (Exception ignored) {}

        // Refresh the board list (joined boards)
        refreshBoardList(key, forceBoardListRefresh);
        updateBoardColours();
        updateListColors();
    }

    /**
     * Refreshes the board by getting it from the server again and displaying it again
     *
     * @param key The board key
     */
    public void refreshBoard(String key) {
        // Get board from server to compare to and decide if updating the display is necessary
        Board serverBoard = service.getBoard(key);

        // If the password was changed, the shown board should be locked, EXCEPT if admin
        if (!serverBoard.verifyPassword(shownBoard.getPassword()) && !serverBoard.verifyPassword("")
            && !isAdmin()) {
            shownBoard.setProtected(true);
        } else if (isAdmin()) {
            shownBoard.setProtected(false); // Should not be needed but for stability purposes
        }

        // If shown board is locked client side, and we remember the password
        if (shownBoard.verifyPassword("") || (                   // If board doesn't have password OR
                pwdMap.containsKey(shownBoard.getKey())                  // (We have a saved password for it AND
                        && shownBoard.verifyPassword(pwdMap.get(shownBoard.getKey()))// saved password is correct
                        && shownBoard.isProtected())                     // AND the board is locked on screen)
                        || isAdmin()) {                                  // OR admin {
            unlockButtons();                                             // unlock the board
            unlockLists();
            shownBoard.setProtected(false);

        } else if (!pwdMap.containsKey(shownBoard.getKey())            // else if we do not know a password for it
                || !shownBoard.verifyPassword(pwdMap.get(shownBoard.getKey()))) {// OR stored incorrect password
            lockLists();                                                          // saved for it {
            lockButtons();                                                        // lock the board on screen
            shownBoard.setProtected(true);
        }
        if (!shownBoard.verifyPassword(pwdMap.get(shownBoard.getKey()))    // if saved password board is incorrect
                && !"".equals(pwdMap.get(shownBoard.getKey()))) {          // and the board does have a password
            pwdMap.put(shownBoard.getKey(), "");                           // reset the saved password
        }
        shownBoard = service.getBoard(shownBoard.getKey());
    }

    /**
     * Refreshes the list of boards that the user has joined
     *
     * @param key    The key of the board
     * @param forced Forces a refresh even though there have been no changes
     */
    public void refreshBoardList(String key, boolean forced) {
        if (isAdmin()) { // if admin
            // add all server boards to joined keys
            service.getBoards().forEach(b -> joinedKeys.add(b.getKey()));
        }

        boolean removed = false;
        // temporary list to prevent concurrent modification exception
        List<String> tempList = new ArrayList<>(joinedKeys);
        for (String k : tempList) { // for each saved key
            try {
                Board b = service.getBoard(k); // try to get the board from the server
                // if the password we saved is no longer correct
                // AND the password is not empty AND we stored a password
                if (!b.verifyPassword(pwdMap.get(k)) && !b.verifyPassword("") && !"".equals(pwdMap.get(k))
                        && !isAdmin()) {// AND not admin
                    forced = true; // then force a total refresh of the displayed list
                    pwdMap.remove(k); // and delete the incorrect, stored password
                }
            } catch (NotFoundException e) {
                removed = true; // if we get here, this means that the board was removed
                joinedKeys.remove(k); // remove the board from our joined keys, as it no longer exists
                // remove from server->board memory
                helperMethods.getMemMap().get(helperMethods.getServerIP()).remove(k);
                if (key.equals(k)) { // if the board that was removed was the board we are currently displaying
                    clearWorkspace(); // stop displaying !
                }
            }
        }
        if (removed || forced) {
            // Empty the board list
            boardList.getChildren().clear();
            // Loop to spawn boardCell fxml s in the board list
            for (String k : joinedKeys) {
                var boardCell = new MyFXML(createInjector(new MainModules()))
                        .load(BoardCellCtrl.class, "client", "windows", "workspace", "boardCell",
                                "BoardCell.fxml");
                BoardCellCtrl controller = boardCell.getKey();
                controller.setBoard(service.getBoard(k));
                controller.setWorkspaceCtrl(this);
                boardList.getChildren().add(boardCell.getValue());
            }
        }
        updateBoardColours();
        updateListColors();
        updateCardColors();
    }

    /**
     * Updates the card colors
     */
    public void updateCardColors() {
        if(shownBoard == null) {
            return;
        }

        for(int i = 0; i < shownBoard.getCardLists().size(); i++){
            Node scrollPane = ((VBox) listContainer.getChildren().get(i)).getChildren().get(1);

            for(int j = 0; j < shownBoard.getCardLists().get(i).getCards().size(); j++){
                Card card = shownBoard.getCardLists().get(i).getCards().get(j);
                String backgroundColor = card.getPresets().get(0).getBackgroundColor();
                String fontColor = card.getPresets().get(0).getFontColor();

                String backgroundStyle = "-fx-background-color: #" + backgroundColor.substring(2, 8) +
                        "; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, grey, 5, 0, 0.0, 1.0);";

                if (scrollPane instanceof ScrollPane){
                    Node cardBox = ((VBox) ((ScrollPane) scrollPane).getContent()).getChildren().get(j);
                    cardBox.setStyle(backgroundStyle);
                    Node cardTitle = ((HBox) ((VBox) ((HBox) ((AnchorPane) cardBox)
                            .getChildren().get(0)).getChildren().get(0))
                            .getChildren().get(0)).getChildren().get(0);
                    if (cardTitle instanceof  Label){
                        ((Label) cardTitle).setTextFill(Color.web(fontColor));
                    }
                }
            }
        }
    }

    /**
     * Updates the lists on screen to display the correct background and font color
     */
    public void updateListColors() {
        if (shownBoard == null) {
            return;
        }

        for (int i = 0; i < listContainer.getChildren().size(); i++) {
            String backgroundColor = shownBoard.getListBackgroundColor();
            String style = "-fx-border-radius: 10; -fx-border-color: transparent; -fx-background-color: #"
                           + backgroundColor + "; -fx-background-radius: 10; -fx-effect: " +
                           "dropshadow(gaussian, grey, 10, 0, 0.0, 3.0);";

            VBox listInUI = (VBox) listContainer.getChildren().get(i);
            VBox boxInList = (VBox) ((ScrollPane) listInUI.getChildren().get(1)).getContent();


            // Set the color of the lists (background)
            listInUI.setStyle(style);
            boxInList.setStyle("-fx-background-color: transparent;");

            // Set the title label of each list (font color)
            // This is inside the Group containing (Label, Line,TextField)
            Group group = (Group) listInUI.getChildren().get(0);
            Label listTitle = (Label) group.getChildren().get(0);
            listTitle.setTextFill(Color.web(shownBoard.getCardLists().get(i).getFontColor()));
        }
    }

    /**
     * Updates the board on screen to display the correct background and font color
     */
    public void updateBoardColours() {
        if (shownBoard != null) {
            listContainer.setStyle("-fx-background-color: #" + shownBoard.getBoardBackgroundColour());
            boardName.setTextFill(Color.web(shownBoard.getBoardFontColour()));
        }
    }

    /**
     * Shows the board with the specified key. Retrieves it from the server or creates it if it
     * does not exist yet
     *
     * @param targetKey The key of the board to show
     */
    public void showBoard(String targetKey) {
        try {
            unsubscribeLists();
            shownBoard = service.getBoard(targetKey);
            for(int i = 0; i< boardSubscriber.size(); i++)
            {
                boardSubscriber.get(i).unsubscribe();
            }
            registerForBoardUpdates(targetKey);

            boardName.setText(shownBoard.getTitle());
            // Theoretically unnecessary, but to be sure
            helperMethods.getMemMap().computeIfAbsent(helperMethods.getServerIP(), k -> new HashSet<>());

            if (!isAdmin()) {
                helperMethods.getMemMap().get(helperMethods.getServerIP()).add(shownBoard.getKey());
            }
            if (!shownBoard.verifyPassword("") && !shownBoard.verifyPassword(pwdMap.get(shownBoard.getKey()))) {
                lockButtons();
                lockLists();
                shownBoard.setProtected(true);
            } else {
                unlockButtons();
                unlockLists();
                shownBoard.setProtected(false);
            }
            pwdMap.putIfAbsent(targetKey, "");
            joinedKeys.add(targetKey);
            boardName.setText(shownBoard.getTitle());
            unhideWorkspace();
            displayLists();
            refreshBoardList(shownBoard.getKey(), true);
        } catch (NotFoundException | BadRequestException e) {
            String message = "There is no board with key " + targetKey +
                    ". Try joining a board with a different key.";

            ErrorDialogEntry nonExistingKey = new ErrorDialogEntry("Error!", message);
            helperMethods.showErrorDialog(nonExistingKey);
        } catch (Exception ignored) {}
    }
    /**
     * Displays the lists into the HBox list container
     */
    public void displayLists() {
        //New subscriber are going to be created, so we need to remove the existing ones
        unsubscribeLists();
        for (int i = 0; i < shownBoard.getCardLists().size(); i++) {
            var loader = new MyFXML(createInjector(new MainModules()))
                    .load(ListCtrl.class, "client", "windows", "lists", "list", "List.fxml");
            CardList cardList = shownBoard.getCardLists().get(i);

            cardList.setBackgroundColor(shownBoard.getListBackgroundColor());
            cardList.setFontColor(shownBoard.getListFontColor());

            VBox list = (VBox) loader.getValue();

            ListCtrl controller = loader.getKey();
            listControllers.add(controller);
            controller.setWorkspaceCtrl(this);
            controller.setListId(i);
            controller.setBoardKey(shownBoard.getKey());
            controller.setHelperMethod(helperMethods);
            controller.setCardList(cardList);
            controller.displayCards();
            controller.registerForListUpdates();
            setMoveShortcutListeners(loader.getKey().getCardVBox());
            controller.setListTitle(cardList.getListTitle());
            listContainer.getChildren().add(list);
            listContainer.setOnMouseMoved(event -> {
                newMouseXPosition = event.getSceneX();
                newMouseYPosition = event.getSceneY();
            });
        }
        if (focusedIndicesAreValid()) {
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
            if (this.isAdmin() || !this.shownBoard.isProtected())
                switch (event.getCode()) {
                    case UP:    moveFocusUp();      break;
                    case DOWN:  moveFocusDown();    break;
                    case LEFT:  moveFocusLeft();    break;
                    case RIGHT: moveFocusRight();   break;
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
        if (focusedCardIndex == (shiftUpWards ? 1 :
                focusedListController.getCardList().getCards().size())) return;


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
     * Handles the shortcut associated to quick-renaming cards, "E"
     */
    public void handleRenameShortcut() {
        if (!focusedIndicesAreValid()) return;
        ListCtrl focusedListController = listControllers.get(focusedListIndex - 1);
        // Get the highlighted card
        Card selectedCard = focusedListController.getCardList().getCard(focusedCardIndex - 1);
        var loader = new MyFXML(createInjector())
                .load(RenameCardCtrl.class, "client", "windows", "lists", "cells", "RenameCard" +
                                                                                   ".fxml");
        loader.getKey().setData(selectedCard);
        loader.getKey().setListCtrl(focusedListController);
        Scene scene = new Scene(loader.getValue());
        helperMethods.popUp(scene, "Rename card");
        // Instantiate a new rename window
    }

    /**
     * Method for handling the deletion of the highlighted card
     */
    public void handleDeleteShortCut() {
        if (!focusedIndicesAreValid()) return;
        ListCtrl focusedListController = listControllers.get(focusedListIndex - 1);

        // Get the highlighted card
        int toDeleteIndex = focusedCardIndex - 1;
        Card toDelete = focusedListController.getCardList().getCard(toDeleteIndex);
        // Delete it from the card database
        cardService.deleteCard(toDelete);
    }

    /**
     * Method for handling the shortcuts that lets users manage tags for the highlighted card
     */
    public void handleTagShortcut() {
        if (!focusedIndicesAreValid()) return;
        ListCtrl focusedListController = listControllers.get(focusedListIndex - 1);
        Card highlightedCard = focusedListController.getCardList().getCard(focusedCardIndex - 1);
        // Get the highlighted card from the database, because we get merge conflicts in
        // Hibernate otherwise <a url="">
        highlightedCard = cardService.getCardByID(highlightedCard.getId());

        // Instantiate the popup that controls the tags for this card
        var loader = new MyFXML(createInjector(new MainModules())).load(
                TagListFromShortcutCtrl.class, "client", "windows", "tags", "TagListFromShortCut.fxml");

        TagListFromShortcutCtrl tagListCtrl = loader.getKey();

        List<Tag> appliedTags = highlightedCard.getTags();
        List<Tag> availableTags = shownBoard.getTagList();
        availableTags.removeAll(appliedTags);

        // Set the appropriate field for the controller
        tagListCtrl.setAvailableTags(availableTags);
        tagListCtrl.setAppliedTags(highlightedCard.getTags());
        tagListCtrl.setCard(highlightedCard);
        tagListCtrl.setCardList(focusedListController.getCardList());

        tagListCtrl.setType("edit");

        Scene scene = new Scene(loader.getValue());
        helperMethods.popUp(scene, "Manage card tags");
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
        return focusedCardIndex > 0
               && focusedListIndex > 0
               && focusedListIndex <= shownBoard.getCardLists().size()
               && focusedCardIndex <= shownBoard.getCardLists().get(focusedListIndex - 1).getCards().size()
               && shownBoard.getCardLists().get(focusedListIndex - 1).getCards().size() > 0;
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
            highlightSelectedCard();
        }
        autoScroll(focusedCardIndex, focusedListIndex);

    }

    /**
     * Highlights the currently selected card
     */
    private void highlightSelectedCard() {
        VBox vbox = getFocusPosition();
        vbox.getChildren().get(focusedCardIndex - 1).setOpacity(0.6);
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
            highlightSelectedCard();
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
            highlightSelectedCard();
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
            highlightSelectedCard();
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
            highlightSelectedCard();
        }
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
            controller.setBoardKey(getBoardKey());
            controller.setCard(shownBoard.getCardLists().get(focusedListIndex - 1).
                    getCards().get(focusedCardIndex - 1));
            String title = "View Card";
            helperMethods.popUp(scene, title);
        }
    }

    /**
     * Method to delete a board from the database
     * @param board the board to be deleted
     */
    public void deleteBoard(Board board) {
        service.deleteBoard(board);
        joinedKeys.remove(board.getKey());
        refreshWorkspace(true);
        clearWorkspace();
    }

    /**
     * Helper method
     */
    public void deleteScreen() {
        deleteScreen(shownBoard);
    }

    /**
     * Handles the action of deleting a board from the workspace by opening a
     * confirmation popup.
     * @param board the board to be deleted
     */
    public void deleteScreen(Board board) {
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(DeleteBoardCtrl.class, "client", "windows", "workspace", "delete", "deleteBoard.fxml");

        Parent root = loader.getValue();
        Scene scene = new Scene(root);
        loader.getKey().setWorkspaceCtrl(this);
        loader.getKey().setBoard(board);
        scene.getRoot().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                loader.getKey().escape();
            }
            if (event.getCode() == KeyCode.ENTER) {
                loader.getKey().delete();
            }
        });
        String title = "Delete a board";
        helperMethods.popUp(scene, title);
    }

    /**
     * Leaves the current board
     *
     * @see #leaveBoard(Board)
     */
    public void leaveBoard() {
        leaveBoard(shownBoard);
    }

    /**
     * Sets the action of leaving a board by opening a new popup that asks the user to confirm
     * their choice
     *
     * @param board The board that the user wants to leave
     */
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

    /**
     * Shows a popup that prompts the user to enter a title for their new list
     * Called when the user clicks on the button to add a new list
     */
    public void onAddListButton() {
        // show popup for list title first
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(NewListNameCtrl.class, "client", "windows", "lists", "list", "NewListTitle" +
                                                                                   ".fxml");

        NewListNameCtrl controller = loader.getKey();
        controller.setWorkspaceCtrl(this);

        Parent root = loader.getValue();
        Scene scene = new Scene(root);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.CANCEL) controller.cancel();
        });

        helperMethods.popUp(scene, "Set new list title");
    }

    /**
     * Adds a new list with the given title to the shown board
     *
     * @param listTitle The title for the new list
     */
    public void addList(String listTitle) {
        CardList newCardList = new CardList(listTitle, new ArrayList<>());
        if (!getShownBoard().getCardLists().isEmpty()) {
            newCardList.setBackgroundColor(getShownBoard().getCardLists().get(0).getBackgroundColor());
            newCardList.setFontColor(getShownBoard().getCardLists().get(0).getFontColor());
        }
        shownBoard.addList(newCardList);
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

    /**
     * Method to get the currently shown board
     *
     * @return The board that is shown
     */
    public Board getShownBoard() {
        return shownBoard;
    }

    /**
     * Method to load the tag-overview window in a new popup screen
     */
    public void tagOverview() {
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(TagOverviewCtrl.class, "client", "windows", "tags", "TagOverview.fxml");

        loader.getKey().setBoardKey(shownBoard.getKey());
        loader.getKey().poll();
        loader.getKey().setWorkspaceCtrl(this);

        Parent root = loader.getValue();
        Scene scene = new Scene(root);

        String title = "Tag Overview";
        helperMethods.popUp(scene, title);
    }

    /**
     * Method to copy the key of currently shown board to the
     * clipboard. This method is called by the copy key button.
     * <p>
     * After copying the key to the clipboard a small notification is displayed.
     */
    public void copyKey() {
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
     * Source: <a href="https://stackoverflow.com/questions/26454149/make-javafx-wait-and-continue-with-code">...</a>
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
        helperMethods.popUp(scene, "Rename board: " + this.getShownBoard().getTitle());
        refreshWorkspace(true);
    }

    /**
     * Method to open the customize-window in a new popup
     */
    public void customizeBoard() {
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(CustomizeCtrl.class, "client", "windows", "customize", "Customize.fxml");

        loader.getKey().setBoard(shownBoard);
        loader.getKey().setWorkspaceCtrl(this);
        loader.getKey().displayPresetList();

        Parent root = loader.getValue();
        Scene scene = new Scene(root);

        String title = "Customize";
        helperMethods.popUp(scene, title);
    }

    /**
     * Sets the list of the keys for the joined board for this workspace
     *
     * @param joinedKeys The keys of the joined boards
     */
    public void setJoinedKeys(Set<String> joinedKeys) {
        this.joinedKeys = joinedKeys;
    }


    /**
     * Sets the instance of HelperMethods
     *
     * @param helperMethods The instance of HelperMethods to set
     */
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

    /**
     * Setter for admin mode in workspace
     * @param admin true/false
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
        if (admin) {
            screenTitle.setText("All Server Boards");
            leaveButton.setVisible(false);
            leaveButton.setManaged(false);
        }
    }

    /**
     * Getter for admin mode of workspace
     * @return true if admin, else false
     */
    public boolean isAdmin() {
        return this.admin;
    }

    /**
     * Shows the pop-up for when the entered title is empty
     */
    public void emptyTitlePopUp() {
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(AccessDeniedCtrl.class, "client", "windows", "workspace", "joinAlerts", "EmptyTitle.fxml");

        Parent root = loader.getValue();
        Scene scene = new Scene(root);
        String title = "Error!";
        helperMethods.popUp(scene, title);
    }

    /**
     * Shows the pop-up for when the entered key is empty
     */
    private void emptyKeyPopUp() {
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(AccessDeniedCtrl.class, "client", "windows", "workspace", "joinAlerts", "EmptyKey.fxml");

        Parent root = loader.getValue();
        Scene scene = new Scene(root);
        String title = "Error!";
        helperMethods.popUp(scene, title);
    }

    ///WEBSOCKETS
    /**
     * Register for messages for the entered key, this way we will receive updates just for the board we are on
     * @param key the board we need to get the updated information
     */
    public void registerForBoardUpdates(String key) {
        boardSubscriber.add(websocketUtils.registerForMessages("/topic/boards/"+key,Board.class, board -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (board.getKey() != null) {
                        //Update the board since the new one has changed
                        showBoard(board.getKey());
                        refreshWorkspace(true);
                    } else {
                        clearWorkspace();
                        refreshBoardList("", true);
                    }
                }
            });
        }));
    }

    /**
     * Ads a list subscriber to the list, so we can unsubscribe it later
     * @param subscriber
     */
    public void addListSubscriber(StompSession.Subscription subscriber)
    {
        listSubscribers.add(subscriber);
    }

    /**
     * Updates the board because it exists a newer version
     */
    public void updateBoard()
    {
        shownBoard=service.getBoard(shownBoard.getKey());
    }


    /**
     * Unsubscribe all the lists because the board needs to be updated
     */
    public void unsubscribeLists()
    {
        listContainer.getChildren().clear();
        listControllers.clear();
        for(int i = 0; i< listControllers.size(); i++)
        {
            listControllers.get(i).unsubscribeCards();
        }
        for(int i = 0; i< listSubscribers.size(); i++)
        {
            listSubscribers.get(i).unsubscribe();
        }
    }

    /**
     * Pops up the help screen
     */
    public void helpScreen() {
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(AccessDeniedCtrl.class, "client", "windows", "workspace", "helpWindow", "HelpWindow.fxml");

        Parent root = loader.getValue();
        Scene scene = new Scene(root);
        String title = "Help screen";
        helperMethods.popUp(scene, title);
    }

}
