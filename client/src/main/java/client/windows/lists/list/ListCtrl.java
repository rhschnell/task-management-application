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
import client.utils.HelperMethods;
import client.windows.cards.addCard.AddCardCtrl;
import client.windows.cards.viewCard.ViewCardCtrl;
import client.MainCtrl;
import client.serverUtils.ServerUtils;
import client.windows.lists.CustomListCellCtrl;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

public class ListCtrl {

    private ServerUtils server;
    private MainCtrl mainCtrl;
    private final MyFXML myFXML;
    private CardList cardList;

    @FXML
    private Label listTitle;

    @FXML
    private ListView<Card> cardListView;

    /**
     * Constructor for ListCtrl
     * @param server a server util
     */
    @Inject
    public ListCtrl(ServerUtils server, MyFXML myFXML, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        cardListView = new ListView<>();
        listTitle = new Label();
        cardList = new CardList();
        this.myFXML = myFXML;
    }

    public MainCtrl getMainCtrl() {
        return mainCtrl;
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
     * Adds a CardList object to the list
     * @param cardList the list of cards to be added
     */
    public void addCards(CardList cardList) {
        cardListView.setCellFactory(param -> {
            ListCell<Card> cell = new CustomListCellCtrl();
            cell.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    viewCard(cell.getItem());
                }
            });
            return cell;
        });
        cardListView.getItems().addAll(cardList.getCards());
    }

    /**
     * Opens the view card pop up for a card in the list
     * @param cell the card to be viewed
     */
    public void viewCard(Card cell) {
        var loader = myFXML.load(ViewCardCtrl.class, "client", "scenes", "CardWindows", "ViewCard.fxml");

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
        var loader = myFXML.load(AddCardCtrl.class, "client", "scenes", "CardWindows", "AddCard.fxml");

        Parent root = loader.getValue();
        Scene scene = new Scene(root);

        String title = "Create a card";
        HelperMethods.popUp(scene, title);
    }

    /**
     * Displays the DeleteList FXML into a new window (Popup).
     */
    public void deleteScreen() {
        var loader = myFXML.load(DeleteListCtrl.class ,"client", "scenes", "ListManagement", "DeleteList.fxml");

        Parent root = loader.getValue();
        Scene scene = new Scene(root);

        String title = "Delete a list";
        mainCtrl.popUp(scene, title);
    }

}