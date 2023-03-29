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
package client.windows.cards.view;

import client.modules.MainModules;
import client.MyFXML;
import client.MainCtrl;
import client.windows.cards.edit.EditCardCtrl;
import client.serverUtils.CardListUtils;
import client.serverUtils.CardUtils;
import client.serverUtils.ServerUtils;
import client.windows.tags.view.CustomTagCellCtrl;
import com.google.inject.Inject;
import commons.Card;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static com.google.inject.Guice.createInjector;

public class ViewCardCtrl {

    private CardUtils server;
    private CardListUtils cardListUtils;

    private MainCtrl mainCtrl;
    private MyFXML myFXML;
    private Card card;

    @FXML
    private Label cardTitle;

    @FXML
    private Text cardDescription;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private VBox appliedTagsVbox;

    /**
     * Constructor for ViewCardCtrl
     * @param server a server util
     */
    @Inject
    public ViewCardCtrl(ServerUtils server, MainCtrl mainCtrl, MyFXML myFXML, CardListUtils cardListUtils) {
        this.server = new CardUtils(server);
        this.mainCtrl = mainCtrl;
        this.myFXML = myFXML;
        this.cardListUtils = cardListUtils;
    }

    /**
     * A setter for the card shown in the View Card window
     * @param card the card
     */
    public void setCard(Card card) {
        this.card = card;
        setCardTitle(card.getTitle());
        setCardDescription(card.getDescription());
        applyTag();
    }


    public void applyTag() {
        if(card.getTags()!=null) {
            for (int i = 0; i < card.getTags().size(); i++) {
                var loader = new MyFXML(createInjector(new MainModules()))
                        .load(CustomTagCellCtrl.class, "client", "windows", "tags","CustomTagCell.fxml");
                CustomTagCellCtrl ctrl = loader.getKey();
                ctrl.setTagObject(card.getTags().get(i), "viewTag");
                appliedTagsVbox.getChildren().add(loader.getValue());
            }
        }
    }

    /**
     * A setter for the card title shown in the View Card window
     * @param title the card title
     */
    public void setCardTitle(String title) {
        cardTitle.setText(title);
    }

    /**
     * A setter for the card description shown in the View Card window
     * @param description the card description
     */
    public void setCardDescription(String description) {
        cardDescription.setText(description);
    }

    /**
     * Method to delete the current card
     */
    public void delete() {
        ((Stage)deleteButton.getScene().getWindow()).close();
        cardListUtils.deleteFromCardList(card);
        server.deleteCard(card.getId());
    }

    public void edit() {
        var loader = myFXML.load(EditCardCtrl.class, "client", "windows", "cards", "EditCard.fxml");
        loader.getKey().setCard(card);
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.getValue()));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
}
