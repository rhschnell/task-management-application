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
import client.windows.lists.cells.QuickAddCardCtrl;
import client.utils.HelperMethods;
import client.windows.cards.add.AddCardCtrl;
import client.windows.lists.cells.CardCtrl;
import client.windows.lists.delete.DeleteListCtrl;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import static com.google.inject.Guice.createInjector;

public class ListCtrl {
    private HelperMethods hm;
    private final ListService service;


    private DataFormat cardFormat;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Label listTitle;
    @FXML
    private VBox cardVBox;
    @FXML
    private TextField renameTitle;
    private int listId;
    private long focusedCardIndex;
    private  Pair<CardCtrl, Parent> cardCell;

    private WorkspaceCtrl workspaceCtrl;


    public void setWorkspaceCtrl(WorkspaceCtrl workspaceCtrl) {
        this.workspaceCtrl = workspaceCtrl;
        scrollPane.requestFocus();
        scrollPane.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {

                workspaceCtrl.openFocused();
            }
            if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN
                    || event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
                {
                    {
                        if (event.getCode() == KeyCode.UP) {
                            workspaceCtrl.setFocusUp();
                        }
                        if (event.getCode() == KeyCode.DOWN) {
                            workspaceCtrl.setFocusDown();
                        }
                        if (event.getCode() == KeyCode.LEFT) {
                            workspaceCtrl.setFocusLeft();
                        }
                        if (event.getCode() == KeyCode.RIGHT) {
                            workspaceCtrl.setFocusRight();
                        }
                    }
                }
            }
            event.consume();
        });
        scrollPane.setOnMouseEntered(event -> {
            scrollPane.requestFocus();
            scrollPane.setOnKeyPressed(keyEvent -> {
                {
                    if (keyEvent.getCode() == KeyCode.ENTER) {
                        workspaceCtrl.openFocused();
                    }
                        if (keyEvent.getCode() == KeyCode.UP) {
                        workspaceCtrl.setFocusUp();
                    }
                    if (keyEvent.getCode() == KeyCode.DOWN) {
                        workspaceCtrl.setFocusDown();
                    }
                    if (keyEvent.getCode() == KeyCode.LEFT) {
                        workspaceCtrl.setFocusLeft();
                    }
                    if (keyEvent.getCode() == KeyCode.RIGHT) {
                        workspaceCtrl.setFocusRight();
                    }
                }

            });

        });


    }

    public VBox getCardVBox() {
        return cardVBox;
    }

    /**
     * Constructor for ListCtrl
     *
     */
    @Inject
    public ListCtrl(ListService service) {
        this.service = service;
        focusedCardIndex=-1;
    }

    public void setCardList(CardList cardList) {
        service.setCardList(cardList);
    }



    /**
     * Setter for the list title
     * @param title the title of the list
     */
    public void setListTitle(String title) {
        listTitle.setText(title);
    }

    /**
     * Displays the cards onto the list's inner VBox
     */
    public void displayCards() {
        cardVBox.getChildren().clear();
        for (Card card: service.getCardList().getCards()) {
            cardCell = new MyFXML(createInjector(new MainModules()))
                    .load(CardCtrl.class, "client", "windows", "lists", "cells", "Card.fxml");
            CardCtrl controller = cardCell.getKey();
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
        quickAddCard.getValue().setOnDragDetected(event -> {});
    }

    private void makeCardDraggable(Pair<CardCtrl,Parent> cardCell) {
        Separator separator = new Separator();
        cardCell.getValue().setCursor(Cursor.HAND);
        cardCell.getValue().setOnDragDetected(event -> {
            Dragboard db = cardCell.getValue().startDragAndDrop(TransferMode.MOVE);
            Image dragImage = new Image("client/icons/DragFile.png");
            ImageView dragView = new ImageView(dragImage);
            db.setDragView(dragView.getImage(), -20 ,-10);
            ClipboardContent content = new ClipboardContent();
            content.put(cardFormat,cardCell.getKey().getCard());
            db.setContent(content);
            event.consume();
        });
        cardCell.getValue().setOnMouseEntered(event ->{
                focusedCardIndex=cardCell.getKey().getCard().getPriority();
                workspaceCtrl.setFocused((int)focusedCardIndex, listId+1);

            event.consume();
        });
        cardCell.getValue().setOnDragOver(event -> {
            if (event.getGestureSource() != cardCell.getValue() && event.getDragboard().hasContent(cardFormat)) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
        cardCell.getValue().setOnDragEntered(event -> {
            if (event.getGestureSource() != cardCell.getValue() && event.getDragboard().hasContent(cardFormat)) {
                int index = ((VBox) cardCell.getValue().getParent()).getChildren().indexOf(cardCell.getValue());
                ((VBox) cardCell.getValue().getParent()).getChildren().add(index,separator);
            }
            event.consume();
        });

        cardCell.getValue().setOnDragExited(event -> {
            {
                ((VBox) cardCell.getValue().getParent()).getChildren().remove(separator);
                event.consume();
            }
        });
        dragDropHelper(cardCell);
    }
    public void setListId(int index)
    {
        listId = index;
    }

    public void dragDropHelper(Pair<CardCtrl,Parent> cardCell ) {
        cardCell.getValue().setOnMouseExited(event -> {
            workspaceCtrl.resetFocus();
        });

        cardCell.getValue().setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasContent(cardFormat)) {
                Node draggedNode = (Node) event.getGestureSource();
                Parent oldParent = draggedNode.getParent();
                if (oldParent instanceof VBox) {
                    Card draggedCard =(Card)db.getContent(cardFormat);
                    int position = (((VBox) cardCell.getValue().getParent()).getChildren().
                            indexOf(cardCell.getValue()))-1;
                    service.dragAndDrop(draggedCard,position);

                }

                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    /**
     * Sets the key of the board the list is in
     * @param key
     */
    public void setBoardKey(String key)
    {
        service.setBoardKey(key);
    }

    public String getBoardKey()
    {
        return service.getBoardKey();
    }


    /**
     * Returns the CardList of the Controller
     * @return
     */
    public CardList getCardList()
    {
        return service.getCardList();
    }

    private void makeQuickCardReceiveDrag(Pair<QuickAddCardCtrl,Parent> cardCell) {
        Separator separator = new Separator();
        cardCell.getValue().setCursor(Cursor.HAND);


        cardCell.getValue().setOnDragOver(event -> {
            if (event.getGestureSource() != cardCell.getValue() && event.getDragboard().hasContent(cardFormat)) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        cardCell.getValue().setOnDragEntered(event -> {
            if (event.getGestureSource() != cardCell.getValue() && event.getDragboard().hasContent(cardFormat)) {
                int index = ((VBox) cardCell.getValue().getParent()).getChildren().indexOf(cardCell.getValue());
                ((VBox) cardCell.getValue().getParent()).getChildren().add(index,separator);

            }
            event.consume();
        });

        cardCell.getValue().setOnDragExited(event -> {
            {
                ((VBox) cardCell.getValue().getParent()).getChildren().remove(separator);
                event.consume();
            }
        });
        quickCardDragHelper(cardCell);
    }
    public void quickCardDragHelper(Pair<QuickAddCardCtrl,Parent> cardCell ) {
        cardCell.getValue().setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasContent(cardFormat)) {
                Node draggedNode = (Node) event.getGestureSource();
                Parent oldParent = draggedNode.getParent();
                if (oldParent instanceof VBox) {
                    Card draggedCard =(Card)db.getContent(cardFormat);
                    int position = (((VBox) cardCell.getValue().getParent()).getChildren().
                            indexOf(cardCell.getValue()))-1;
                    service.dragAndDrop(draggedCard,position);
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
        hm.popUp(scene, title);
    }

    /**
     * Displays the DeleteList FXML into a new window (Popup).
     */
    public void deleteScreen() {
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(DeleteListCtrl.class,"client", "windows", "lists", "delete", "DeleteList.fxml");

        Parent root = loader.getValue();
        Scene scene = new Scene(root);
        loader.getKey().setDeleteId(service.getCardList().getId());

        String title = "Delete a list";
        HelperMethods.popUp(scene, title);
    }

    public void rename() {
        renameTitle.setVisible(true);
        renameTitle.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER))
            {
                listTitle.setText(renameTitle.getText());
                service.renameCardList(renameTitle.getText());
                renameTitle.setVisible(false);
            }
        });
    }

    public void setHelperMethod(HelperMethods hm) {
        this.hm = hm;
        this.cardFormat = this.hm.getCardFormat();
    }
}