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
package client.scenes.ListManagement;

import client.CustomListCell;
import client.Main;
import client.MyFXML;
import client.MyModule;
import client.scenes.CardWindows.AddCardCtrl;
import client.scenes.CardWindows.ViewCardCtrl;
import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import static com.google.inject.Guice.createInjector;

public class ListCtrl {

    private ServerUtils server;
    private client.scenes.MainCtrl mainCtrl;
    private MyFXML FXML;
    private CardList cardList;

    @FXML
    private Label listTitle;

    @FXML
    private ListView<Card> cardListView;

    /**
     * Constructor for ListCtrl
     * @param server a server util
     * @param mainCtrl a main controller
     */
    @Inject
    public ListCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        cardListView = new ListView<>();
        listTitle = new Label();
        cardList = new CardList();
        this.FXML = new MyFXML(createInjector(new MyModule()));
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
            ListCell<Card> cell = new CustomListCell();
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
        try {
            String path = "/client/scenes/CardWindows/ViewCard.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));

            Parent root = loader.load();
            Scene scene = new Scene(root);

            ViewCardCtrl controller = loader.getController();
            controller.setCardTitle(cell.getTitle());
            controller.setCardDescription(cell.getDescription());

            String title = "View Card";
            mainCtrl.popUp(scene, title);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Displays the AddCard FXML into a new window (Popup).
     */
    public void addCardScreen() {
        var loader = FXML.load(AddCardCtrl.class, "client", "scenes", "CardWindows", "AddCard.fxml");

        Parent root = loader.getValue();
        Scene scene = new Scene(root);

        String title = "Create a card";
        mainCtrl.popUp(scene, title);
    }
}