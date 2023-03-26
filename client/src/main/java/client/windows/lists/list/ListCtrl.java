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

import client.*;
import client.modules.MainModules;
import client.windows.lists.cells.QuickAddCardCtrl;
import client.utils.HelperMethods;
import client.windows.cards.add.AddCardCtrl;
import client.windows.cards.view.ViewCardCtrl;
import client.serverUtils.ServerUtils;
import client.windows.lists.delete.DeleteListCtrl;
import client.windows.lists.cells.CardCtrl;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import static com.google.inject.Guice.createInjector;

public class ListCtrl {

    private final ServerUtils server;
    private final MyFXML myFXML;
    private CardList cardList;

    @FXML
    private Label listTitle;

    @FXML
    private VBox cardVBox;

    /**
     * Constructor for ListCtrl
     * @param server a server util
     */
    @Inject
    public ListCtrl(ServerUtils server, MyFXML myFXML) {
        this.server = server;
        cardList = new CardList();
        this.myFXML = myFXML;
    }

    public void setCardList(CardList cardList) {
        this.cardList = cardList;
    }

    /**
     * Getter for the card list
     */
    public CardList getCardList() {
        return cardList;
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

        for (Card card: cardList.getCards()) {
            var cardCell = new MyFXML(createInjector(new MainModules()))
                    .load(CardCtrl.class, "client", "windows", "lists", "cells", "Card.fxml");
            CardCtrl controller = cardCell.getKey();
            controller.updateItem(card);
            makeNodeDraggable(cardCell.getValue());
            cardVBox.getChildren().add(cardCell.getValue());
        }

        var quickAddCard =
                new MyFXML(createInjector(new MainModules())).load(QuickAddCardCtrl.class, "client", "windows",
                        "lists", "cells", "QuickAddCardCell.fxml");
        quickAddCard.getKey().setListCtrl(this);
        cardVBox.getChildren().add(quickAddCard.getValue());
        makeNodeDraggable(quickAddCard.getValue());
        quickAddCard.getValue().setOnDragDetected(event -> {});
    }

    private void makeNodeDraggable(Node box) {
        Separator separator = new Separator();
        box.setCursor(Cursor.HAND);

        box.setOnDragDetected(event -> {
            Dragboard db = box.startDragAndDrop(TransferMode.MOVE);
            Image dragImage = new Image("client/icons/DragFile.png");
            ImageView dragView = new ImageView(dragImage);
            db.setDragView(dragView.getImage(), -20 ,-10);

            /* Put a string on a dragboard */
            ClipboardContent content = new ClipboardContent();
            content.putString("DRAGGING");
            db.setContent(content);
            event.consume();
        });

        box.setOnDragOver(event -> {
            if (event.getGestureSource() != box && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        box.setOnDragEntered(event -> {
            if (event.getGestureSource() != box && event.getDragboard().hasString()) {
                int index = ((VBox) box.getParent()).getChildren().indexOf(box);
                ((VBox) box.getParent()).getChildren().add(index,separator);

            }
            event.consume();
        });

        box.setOnDragExited(event -> {
            {
                ((VBox) box.getParent()).getChildren().remove(separator);
                event.consume();
            }
        });
        makeMoreDraggable(box);
    }
    public void makeMoreDraggable(Node box)
    {
        box.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                Node draggedNode = ((Node) event.getGestureSource());
                Parent oldParent = draggedNode.getParent();

                // If the old parent is a VBox, remove the dragged node from the old parent
                if (oldParent instanceof VBox) {
                    ((VBox) oldParent).getChildren().remove(draggedNode);
                }

                // If the target is a VBox, add the dragged node to the target
                if (box.getParent() instanceof VBox) {
                    ((VBox) box.getParent())
                            .getChildren().add((((VBox) box.getParent()).getChildren().
                                    indexOf(box)),draggedNode);
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
        box.setOnDragDone(event -> {
            if (event.getTransferMode() == TransferMode.MOVE) {
                box.getParent().requestLayout();
            }
            event.consume();
        });
    }

    /**
     * Opens the view card pop up for a card in the list
     * @param cell the card to be viewed
     */
    public void viewCard(Card cell) {
        var loader = myFXML.load(ViewCardCtrl.class, "client", "windows", "cards", "ViewCard.fxml");

        Parent root = loader.getValue();
        Scene scene = new Scene(root);

        ViewCardCtrl controller = loader.getKey();
        controller.setCard(cell);

        String title = "View Card";
        HelperMethods.popUp(scene, title);
    }

    /**
     * Displays the AddCard FXML into a new window (Popup).
     */
    public void addCardScreen() {
        var loader = myFXML.load(AddCardCtrl.class, "client", "windows", "cards", "AddCard.fxml");

        Parent root = loader.getValue();
        Scene scene = new Scene(root);

        String title = "Create a card";
        HelperMethods.popUp(scene, title);
    }

    /**
     * Displays the DeleteList FXML into a new window (Popup).
     */
    public void deleteScreen() {
        var loader = myFXML.load(DeleteListCtrl.class,"client", "windows", "lists", "delete", "DeleteList.fxml");

        Parent root = loader.getValue();
        Scene scene = new Scene(root);

        String title = "Delete a list";
        HelperMethods.popUp(scene, title);
    }
}