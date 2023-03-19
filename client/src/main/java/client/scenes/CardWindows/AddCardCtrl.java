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
package client.scenes.CardWindows;

import client.scenes.ListManagement.ListCtrl;
import client.scenes.MainCtrl;
import client.utils.CardUtils;
import com.google.inject.Inject;
import client.utils.ServerUtils;
import commons.Card;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;


public class AddCardCtrl {

    private final CardUtils server;
    private final MainCtrl mainCtrl;
    private ListCtrl listCtrl;

    @FXML
    private TextField cardTitle;

    @FXML
    private TextArea cardDescription;

    @FXML
    private Button cancelButton;

    @FXML
    private Button saveButton;

    /**
     * Constructor with no parameters for AddCardCtrl
     */
    public AddCardCtrl() {
        this.mainCtrl = new MainCtrl();
        this.server = new CardUtils(new ServerUtils());
    }

    /**
     * Constructor for AddCardCtrl
     * @param server a server util
     * @param mainCtrl a main controller
     */
    @Inject
    public AddCardCtrl(CardUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Setter for the list controller
     * @param listCtrl the list controller
     */
    public void setListCtrl(ListCtrl listCtrl) {
        this.listCtrl = listCtrl;
    }


    /**
     * This method cancels adding the created card to the list
     */
    public void cancel() {
        ((Stage)cancelButton.getScene().getWindow()).close();
    }

    /**
     * This method adds the created card to the list
     */
    public void save() {
        Card card = new Card(
                cardTitle.getText(),
                cardDescription.getText(),
                "white",
                new ArrayList<>(),
                new ArrayList<>());
        card.setCardList(listCtrl.getCardList());
        server.addCard(card);
        ((Stage)saveButton.getScene().getWindow()).close();
    }


}