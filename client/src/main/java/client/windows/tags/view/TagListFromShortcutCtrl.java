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
package client.windows.tags.view;

import client.windows.cards.add.AddCardCtrl;
import client.windows.cards.edit.EditCardCtrl;
import client.windows.lists.cells.CardService;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.hibernate.cfg.NotYetImplementedException;

public class TagListFromShortcutCtrl extends TagListCtrl {

    @FXML
    private Button saveButton;

    private Card card;
    private CardList cardList;
    private final CardService cardService;

    /**
     * Constructor for the TagListFromShortcutCtrl
     * @param addCardCtrl the addCardCtrl
     * @param editCardCtrl the editCardCtrl
     * @param cardService the cardService
     */
    @Inject
    public TagListFromShortcutCtrl(AddCardCtrl addCardCtrl, EditCardCtrl editCardCtrl,
                                   CardService cardService) {
        super(addCardCtrl, editCardCtrl);
        this.cardService = cardService;
    }

    /**
     * Saves the current stage of the displayed tag management
     */
    public void onSave() {
        if (getType().equals("add"))
            throw new NotYetImplementedException();
        if (getType().equals("edit")){
            // Save the tags in the card
            card.setTags(getAppliedTags());
            cardService.updateCard(card);
        }

        ((Stage) saveButton.getScene().getWindow()).close();
    }

    /**
     * Sets the cardList in which the card needs to be saved
     * @param cardList The cardList in which the card with tags needs to be saved.
     */
    public void setCardList(CardList cardList) {
        this.cardList = cardList;
    }

    /**
     * Sets the card on which the tags can be applied
     * @param card
     */
    public void setCard(Card card) {
        this.card = card;
    }
}