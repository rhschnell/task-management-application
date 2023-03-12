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
import client.scenes.MainCtrl;
import client.scenes.CardWindows.ViewCardCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;

public class ListCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private Label listTitle;

    @FXML
    private ListView<Card> cardListView;


    public ListCtrl() {
        this.mainCtrl = new MainCtrl();
        this.server = new ServerUtils();
        listTitle = new Label();
        cardListView = new ListView<>();
    }

    @Inject
    public ListCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        cardListView = new ListView<>();
        listTitle = new Label();

    }

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
    public void setListTitle(String title) {
        listTitle.setText(title);
    }

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

}