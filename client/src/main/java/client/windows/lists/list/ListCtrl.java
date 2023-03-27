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
import client.serverUtils.CardListUtils;
import client.serverUtils.CardUtils;
import client.windows.lists.cells.QuickAddCardCtrl;
import client.utils.HelperMethods;
import client.windows.cards.add.AddCardCtrl;
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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.util.Pair;


import static com.google.inject.Guice.createInjector;

public class ListCtrl {
    private final MyFXML myFXML;
    private final ServerUtils server;
    private CardList cardList;

    @FXML
    private Label listTitle;

    @FXML
    private VBox cardVBox;

    @FXML
    private TextField renameTitle;

    private CardUtils cardUtils;

    private DataFormat cardFormat;
    private CardListUtils cardListUtils;
    /**
     * Constructor for ListCtrl
     *
     * @param server  a server util
     */
    @Inject
    public ListCtrl(CardUtils cardUtils, ServerUtils server, MyFXML myFXML, CardListUtils cardListUtils) {
        this.server = server;
        this.myFXML = myFXML;
        this.cardListUtils = cardListUtils;
        this.cardUtils = cardUtils;
        cardList = new CardList();
        Card c = new Card();
        cardFormat = HelperMethods.getCardFormat();
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
            makeCardDraggable(cardCell);
            cardVBox.getChildren().add(cardCell.getValue());
        }

        var quickAddCard =
                new MyFXML(createInjector(new MainModules())).load(QuickAddCardCtrl.class, "client", "windows",
                        "lists", "cells", "QuickAddCardCell.fxml");
        quickAddCard.getKey().setListCtrl(this);
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

            /* Put data on a dragboard */
            ClipboardContent content = new ClipboardContent();
            content.put(cardFormat,cardCell.getKey().getCard());
            db.setContent(content);
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
    public void dragDropHelper(Pair<CardCtrl,Parent> cardCell ) {
        cardCell.getValue().setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasContent(cardFormat)) {
                //Pair<CardCtrl,Parent> draggedObject = ((Pair<CardCtrl,Parent>) event.getD());
                Node draggedNode = (Node) event.getGestureSource();
                Parent oldParent = draggedNode.getParent();
                // If the old parent is a VBox, remove the dragged node from the old parent
                if (oldParent instanceof VBox) {
                    ((VBox) oldParent).getChildren().remove(draggedNode);
                    Card draggedCard =(Card)db.getContent(cardFormat);
                    this.getCardList().removeCard(draggedCard);
                    cardUtils.deleteCard(draggedCard.getId());
                    this.getCardList().addCard(draggedCard, (((VBox) cardCell.getValue().
                            getParent()).getChildren().indexOf(cardCell.getValue())));
                    System.out.println((((VBox) cardCell.getValue().getParent()).getChildren().
                            indexOf(cardCell.getValue())));
                    cardListUtils.insertCardList(this.getCardList());
                }

                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
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
                //Pair<CardCtrl,Parent> draggedObject = ((Pair<CardCtrl,Parent>) event.getD());
                Node draggedNode = (Node) event.getGestureSource();
                Parent oldParent = draggedNode.getParent();
                // If the old parent is a VBox, remove the dragged node from the old parent
                if (oldParent instanceof VBox) {
                    ((VBox) oldParent).getChildren().remove(draggedNode);
                    Card draggedCard =(Card)db.getContent(cardFormat);
                    cardUtils.deleteCard(draggedCard.getId());
                    this.getCardList().removeCard(draggedCard);
                    this.getCardList().addCard(draggedCard);
                    System.out.println((((VBox) cardCell.getValue().getParent()).getChildren().
                            indexOf(cardCell.getValue())));
                    cardListUtils.insertCardList(this.getCardList());
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

        String title = "Create a card";
        HelperMethods.popUp(scene, title);
    }

    /**
     * Displays the DeleteList FXML into a new window (Popup).
     */
    public void deleteScreen() {
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(DeleteListCtrl.class,"client", "windows", "lists", "delete", "DeleteList.fxml");

        Parent root = loader.getValue();
        Scene scene = new Scene(root);
        loader.getKey().setDeleteId(this.getCardList().getId());

        String title = "Delete a list";
        HelperMethods.popUp(scene, title);
    }

    public void rename()
    {
        renameTitle.setVisible(true);
        renameTitle.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER))
            {
                listTitle.setText(renameTitle.getText());
                CardListUtils utils = new CardListUtils(server);
                cardList.setListTitle(renameTitle.getText());
                utils.insertCardList(cardList);
                renameTitle.setVisible(false);
            }
        });
    }
}