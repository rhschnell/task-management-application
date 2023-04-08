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
package client.windows.lists.list;

import client.MyFXML;
import client.modules.MainModules;
import client.utils.HelperMethods;
import client.windows.cards.add.AddCardCtrl;
import client.windows.lists.cells.CardCtrl;
import client.windows.lists.cells.QuickAddCardCtrl;
import client.windows.lists.delete.DeleteListCtrl;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import client.windows.workspace.lock.AccessDeniedCtrl;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.ArrayList;
import java.util.List;

import static com.google.inject.Guice.createInjector;

public class ListCtrl {
    private HelperMethods helperMethods;
    private final ListService service;

    private DataFormat cardFormat;

    @FXML
    private VBox completeList;
    private List <StompSession.Subscription> subscriptionsList;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Label listTitle;
    @FXML
    private VBox cardVBox;
    @FXML
    private TextField renameTitle;
    @FXML
    private Button deleteButton;
    @FXML
    private Button createCardButton;
    @FXML
    private Button renameButton;
    private int listId;
    private long focusedCardIndex;
    private Pair<CardCtrl, Parent> cardCell;
    private List<CardCtrl> cardControllers;

    private WorkspaceCtrl workspaceCtrl;
    private Separator separator;

    /**
     * Sets the workspace control
     * @param workspaceCtrl the workspace to set
     */
    public void setWorkspaceCtrl(WorkspaceCtrl workspaceCtrl) {
        separator = new Separator();
        this.workspaceCtrl = workspaceCtrl;
        scrollPane.requestFocus();
        scrollPane.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            setKeyEventListeners(event);
            event.consume();
        });
        scrollPane.setOnMouseEntered(event -> {
            scrollPane.requestFocus();
            scrollPane.setOnKeyPressed(this::setKeyEventListeners);
        });
    }

    /**
     * Sets the keyEvent listeners
     * @param keyEvent The key event that needs to be handled
     */
    public void setKeyEventListeners(KeyEvent keyEvent) {
        handleArrowKeys(keyEvent);

        if (keyEvent.getCode() == KeyCode.ENTER) workspaceCtrl.openFocusedCard();
        if (keyEvent.getCode() == KeyCode.E) workspaceCtrl.handleRenameShortcut();
        if (keyEvent.getCode() == KeyCode.DELETE
            || keyEvent.getCode() == KeyCode.BACK_SPACE) workspaceCtrl.handleDeleteShortCut();
        if (keyEvent.getCode() == KeyCode.T) workspaceCtrl.handleTagShortcut();
    }


    /**
     * Method that checks a keyEvent and handles cases of the arrow keys
     *
     * @param keyEvent The keyEvent fired
     */
    private void handleArrowKeys(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.UP) {
            // If shift is down, reorder cards, otherwise move focus
            if (keyEvent.isShiftDown()) {
                workspaceCtrl.handleReorderingShortcut(true);
            } else {
                workspaceCtrl.moveFocusUp();
            }

        }
        if (keyEvent.getCode() == KeyCode.DOWN) {
            // If shift is down, reorder cards, otherwise move focus
            if (keyEvent.isShiftDown()) {
                workspaceCtrl.handleReorderingShortcut(false);
            } else {
                workspaceCtrl.moveFocusDown();
            }
        }
        if (keyEvent.getCode() == KeyCode.LEFT) {
            workspaceCtrl.moveFocusLeft();
        }
        if (keyEvent.getCode() == KeyCode.RIGHT) {
            workspaceCtrl.moveFocusRight();
        }
    }

    /**
     * Returns whether the currently selected list in the workspace is this list
     *
     * @return whether the currently selected list in the workspace is this list
     */
    public boolean isSelected() {
        // The selected indices must be valid (condition()) and the focused list VBox must be the
        // one associated to this controller
        return workspaceCtrl.focusedIndicesAreValid() && workspaceCtrl.getFocusPosition() == this.cardVBox;
    }

    public VBox getCardVBox() {
        return cardVBox;
    }

    /**
     * Constructor for ListCtrl
     *
     * @param service The ListService for this controller
     */
    @Inject
    public ListCtrl(ListService service) {
        this.service = service;
        cardControllers = new ArrayList<>();
        focusedCardIndex = -1;
    }

    /**
     * Sets the cardList
     * @param cardList to set
     */
    public void setCardList(CardList cardList) {
        service.setCardList(cardList);
    }

    /**
     * Setter for the list title
     *
     * @param title the title of the list
     */
    public void setListTitle(String title) {
        listTitle.setText(title);
    }

    /**
     * Displays the cards onto the list's inner VBox
     */
    public void registerForMessages() {
        workspaceCtrl.listListener.add(service.getBoardUtils().registerForMessages("/topic/lists/"+service.getCardList().getId(),CardList.class, newCardList -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    System.out.println("There is a list update");
                    System.out.println(newCardList);
                    service.setCardList(newCardList);
                    displayCards();
                }
            });
        }));
    }
    public void displayCards() {
        updateListColors();

        cardVBox.getChildren().clear();
        for (Card card : service.getCardList().getCards()) {
            cardCell = new MyFXML(createInjector(new MainModules()))
                    .load(CardCtrl.class, "client", "windows", "lists", "cells", "Card.fxml");
            CardCtrl controller = cardCell.getKey();

            cardControllers.add(controller);

            controller.updateItem(card);
            controller.setDisplayTags(card.getTags());
            makeCardDraggable(cardCell);
            controller.setBoardKey(getBoardKey());
            cardVBox.getChildren().add(cardCell.getValue());
        }

        var quickAddCard =
                new MyFXML(createInjector(new MainModules())).load(QuickAddCardCtrl.class, "client", "windows",
                        "lists", "cells", "QuickAddCardCell.fxml");
        quickAddCard.getKey().setListCtrl(this);
        quickAddCard.getKey().setBoardKey(getBoardKey());
        cardVBox.getChildren().add(quickAddCard.getValue());
        makeQuickCardReceiveDrag(quickAddCard);
        quickAddCard.getValue().setOnDragDetected(event -> {
        });
    }

    /**
     * Sets the cardCell draggable by setting events to the listeners
     *
     * @param cardCell the cardCell that needs to be draggable
     */
    private void makeCardDraggable(Pair<CardCtrl, Parent> cardCell) {
        setDragOver(cardCell);
        setDragDetected(cardCell);
        setDragOver(cardCell);
        setDragExited(cardCell);
        setMouseEvents(cardCell);
        setDragEntered(cardCell);
        setOnDragDropped(cardCell);
    }

    /**
     * Sets mouse events to the card so that focused can be reseted
     *
     * @param destination to set the listener
     */
    private void setMouseEvents(Pair<CardCtrl, Parent> destination) {
        destination.getValue().setOnMouseEntered(event -> {
            focusedCardIndex = destination.getKey().getCard().getPriority();
            workspaceCtrl.setFocusedCard((int) focusedCardIndex, listId + 1);
            event.consume();
        });
        destination.getValue().setOnMouseExited(event -> workspaceCtrl.resetFocus());
    }

    /**
     * Sets the drag exited listener to the destination, a separator being removed when exiting the card
     *
     * @param destination to set the listener
     */
    private void setDragExited(Pair<CardCtrl, Parent> destination) {
        destination.getValue().setOnDragExited(event -> {
            {
                ((VBox) destination.getValue().getParent()).getChildren().remove(separator);
                event.consume();
            }
        });
    }

    /**
     * Sets the drag over listener to the destination, the destination accepting the information if dropped
     *
     * @param destination to set the listener
     */
    private void setDragOver(Pair<CardCtrl, Parent> destination) {
        destination.getValue().setOnDragOver(event -> {
            if (event.getGestureSource() != destination.getValue() &&
                event.getDragboard().hasContent(cardFormat)) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
    }

    /**
     * Sets the listener to the destination so that when drag is detected content is put on the drag board
     *
     * @param destination to set the destination
     */
    private void setDragDetected(Pair<CardCtrl, Parent> destination) {
        destination.getValue().setOnDragDetected(event -> {
            Dragboard db = destination.getValue().startDragAndDrop(TransferMode.MOVE);
            Image dragImage = new Image("client/icons/DragFile.png");
            ImageView dragView = new ImageView(dragImage);
            db.setDragView(dragView.getImage(), -20, -10);
            ClipboardContent content = new ClipboardContent();
            content.put(cardFormat, destination.getKey().getCard());
            db.setContent(content);
            event.consume();
        });
    }

    /**
     * Sets the drag entered listener so that a separator is displayed in order to
     * Display the index where the card will drop
     *
     * @param destination The destination where the dragged card will land
     */
    private void setDragEntered(Pair<CardCtrl, Parent> destination) {
        destination.getValue().setOnDragEntered(event -> {
            if (event.getGestureSource() != destination.getValue() && event.getDragboard().
                    hasContent(cardFormat)) {
                int index = ((VBox) destination.getValue().getParent()).getChildren().
                        indexOf(destination.getValue());
                ((VBox) destination.getValue().getParent()).getChildren().add(index, separator);
            }
            event.consume();
        });
    }

    /**
     * Sets the listener so that when the card is dropped the content is also dropped and added to the server
     * in order to perform the change
     *
     * @param destination to set the listener
     */
    private void setOnDragDropped(Pair<CardCtrl, Parent> destination) {
        destination.getValue().setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasContent(cardFormat)) {
                Node draggedNode = (Node) event.getGestureSource();
                Parent oldParent = draggedNode.getParent();
                if (oldParent instanceof VBox) {
                    Card draggedCard = (Card) db.getContent(cardFormat);
                    int position = (((VBox) destination.getValue().getParent()).getChildren().
                            indexOf(destination.getValue())) - 1;
                    service.dragAndDrop(draggedCard, position);
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    /**
     * Sets the list id in order to know the index of the list displayed in the workspace container
     *
     * @param id The new ID for this list
     */
    public void setListId(int id) {
        listId = id;
    }

    /**
     * Sets the key of the board the list is in
     *
     * @param key The new corresponding board key
     */
    public void setBoardKey(String key) {
        service.setBoardKey(key);
    }

    /**
     * Gets the board key
     *
     * @return the key
     */
    public String getBoardKey() {
        return service.getBoardKey();
    }

    /**
     * Returns the CardList of the Controller
     *
     * @return The card list associated to this controller
     */
    public CardList getCardList() {
        return service.getCardList();
    }

    /**
     * Sets the cardCell listeners in order to receive dragAndDrop and send the information
     * to the server
     *
     * @param cardCell the cardCell we need to put the listeners to
     */
    private void makeQuickCardReceiveDrag(Pair<QuickAddCardCtrl, Parent> cardCell) {
        cardCell.getValue().setCursor(Cursor.HAND);
        quickCardDragOver(cardCell);
        quickCardDragEntered(cardCell);
        quickCardDragExited(cardCell);
        quickCardDragDropped(cardCell);
    }

    /**
     * Sets the Drag Over listener to the destination
     *
     * @param destination to set the listener
     */
    private void quickCardDragOver(Pair<QuickAddCardCtrl, Parent> destination) {
        destination.getValue().setOnDragOver(event -> {
            if (event.getGestureSource() != destination.getValue() &&
                event.getDragboard().hasContent(cardFormat)) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
    }

    /**
     * Sets the dragEntered listener to the destination
     *
     * @param destination to set the listener
     */
    private void quickCardDragEntered(Pair<QuickAddCardCtrl, Parent> destination) {
        destination.getValue().setOnDragEntered(event -> {
            if (event.getGestureSource() != destination.getValue() &&
                event.getDragboard().hasContent(cardFormat)) {
                int index = ((VBox) destination.getValue().getParent()).getChildren().
                        indexOf(destination.getValue());
                ((VBox) destination.getValue().getParent()).getChildren().add(index, separator);
            }
            event.consume();
        });
    }

    /**
     * Sets the DragExited listener to the destination
     *
     * @param destination to set the listener
     */
    private void quickCardDragExited(Pair<QuickAddCardCtrl, Parent> destination) {
        destination.getValue().setOnDragExited(event -> {
            {
                ((VBox) destination.getValue().getParent()).getChildren().remove(separator);
                event.consume();
            }
        });
    }

    /**
     * Sets the dragDropped listener to the destination
     *
     * @param destination to set the listener
     */
    private void quickCardDragDropped(Pair<QuickAddCardCtrl, Parent> destination) {
        destination.getValue().setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasContent(cardFormat)) {
                Node draggedNode = (Node) event.getGestureSource();
                Parent oldParent = draggedNode.getParent();
                if (oldParent instanceof VBox) {
                    Card draggedCard = (Card) db.getContent(cardFormat);
                    int position = (((VBox) destination.getValue().getParent()).getChildren().
                            indexOf(destination.getValue())) - 1;
                    service.dragAndDrop(draggedCard, position);
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    /**
     * Displays the AddCard FXML into a new window (Popup).
     */
    public void addCardScreen() {
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(AddCardCtrl.class, "client", "windows", "cards", "AddCard.fxml");

        Parent root = loader.getValue();
        Scene scene = new Scene(root);
        loader.getKey().setCardList(service.getCardList());
        loader.getKey().setBoardKey(getBoardKey());

        String title = "Create a card";
        helperMethods.popUp(scene, title);
    }

    /**
     * Displays the DeleteList FXML into a new window (Popup).
     */
    public void deleteScreen() {
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(DeleteListCtrl.class, "client", "windows", "lists", "delete", "DeleteList.fxml");

        Parent root = loader.getValue();
        Scene scene = new Scene(root);
        loader.getKey().setDeleteId(service.getCardList().getId());
        loader.getKey().setBoardKey(getBoardKey());
        scene.getRoot().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                loader.getKey().escape();
            }
            if (event.getCode() == KeyCode.ENTER) {
                loader.getKey().delete();
            }
        });
        String title = "Delete a list";
        helperMethods.popUp(scene, title);
    }

    public void rename() {
        renameTitle.setVisible(true);
        renameTitle.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                listTitle.setText(renameTitle.getText());
                service.renameCardList(renameTitle.getText());
                renameTitle.setVisible(false);
            }
        });
    }

    /**
     * Method to update the list colors
     */
    public void updateListColors() {
        String backgroundColor = getCardList().getBackgroundColor();
        String style = "-fx-border-radius: 10; -fx-border-color: transparent; -fx-background-color: #"
                       + backgroundColor + "; -fx-background-radius: 10; -fx-effect: " +
                       "dropshadow(gaussian, grey, 10, 0, 0.0, 3.0);";
        cardVBox.setStyle("-fx-background-color: #" + backgroundColor);
        completeList.setStyle(style);
        listTitle.setTextFill(Color.web(getCardList().getFontColor()));
    }

    /**
     * Sets the helperMethod
     * @param hm The new instance of helper methods
     */
    public void setHelperMethod(HelperMethods hm) {
        this.helperMethods = hm;
        this.cardFormat = this.helperMethods.getCardFormat();
    }

    /**
     * Shifts a card to a new index
     *
     * @param card     The card to shift
     * @param newIndex The destination index of the card
     */
    public void shiftCard(Card card, int newIndex) {
        service.dragAndDrop(card, newIndex);
    }


    public void lock() {
        for(int i = 0; i < cardVBox.getChildren().size() - 1; ++i) {
            CardCtrl ctrl = cardControllers.get(i);

            ctrl.getEditButton().setOnMouseClicked(e -> {
                accessDeniedPopUp();
            });
            ctrl.getDeleteButton().setOnMouseClicked(e -> {
                accessDeniedPopUp();
            });
        }
        renameButton.setOnMouseClicked(e -> {
            accessDeniedPopUp();
        });
        deleteButton.setOnMouseClicked(e -> {
            accessDeniedPopUp();
        });
        createCardButton.setOnMouseClicked(e -> {
            accessDeniedPopUp();
        });
        Node quickAddCard = cardVBox.getChildren().get(cardVBox.getChildren().size() - 1);
        quickAddCard.setVisible(false);
        quickAddCard.managedProperty().bind(quickAddCard.visibleProperty());
    }

    public void unlock() {
        for(int i = 0; i < cardVBox.getChildren().size() - 1; ++i) {
            CardCtrl ctrl = cardControllers.get(i);

            ctrl.getEditButton().setOnMouseClicked(e -> {
                ctrl.edit();
            });
            ctrl.getDeleteButton().setOnMouseClicked(e -> {
                ctrl.delete();
            });
        }
        renameButton.setOnMouseClicked(e -> {
            rename();
        });
        deleteButton.setOnMouseClicked(e -> {
            deleteScreen();
        });
        createCardButton.setOnMouseClicked(e -> {
            addCardScreen();
        });

        Node quickAddCard = cardVBox.getChildren().get(cardVBox.getChildren().size() - 1);
        quickAddCard.setVisible(true);
        quickAddCard.managedProperty().bind(quickAddCard.visibleProperty());
    }

    public void accessDeniedPopUp() {
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(AccessDeniedCtrl.class, "client", "windows", "workspace", "lock", "AccessDenied.fxml");

        Parent root = loader.getValue();
        Scene scene = new Scene(root);
        String title = "Access denied!";
        helperMethods.popUp(scene, title);
    }
}
