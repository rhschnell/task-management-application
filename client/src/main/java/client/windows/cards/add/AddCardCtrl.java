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
package client.windows.cards.add;

import client.MyFXML;
import client.modules.MainModules;
import client.utils.HelperMethods;
import client.windows.tags.view.CustomTagCellCtrl;
import client.windows.tags.view.TagListCtrl;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

import static com.google.inject.Guice.createInjector;

public class AddCardCtrl {

    private final AddCardService service;

    @FXML
    private TextField cardTitle;
    @FXML
    private TextArea cardDescription;
    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;
    @FXML
    private VBox appliedTagsVbox;


    private HelperMethods hm;

    /**
     * Constructor for AddCardCtrl
     *
     * @param service corresponding service
     */
    @Inject
    public AddCardCtrl(AddCardService service, HelperMethods hm) {
        this.service = service;
        this.hm=hm;
    }

    public void setCardList(CardList cardList) {
        service.setCardList(cardList);
    }

    public void setBoardKey(String boardKey)
    {
        service.setBoardKey(boardKey);
    }
    public String getBoardKey()
    {
        return service.getBoardKey();
    }

    /**
     * This method cancels adding the created card to the list
     */
    public void cancel() {
        ((Stage)cancelButton.getScene().getWindow()).close();
    }

    /**
     * This method adds the created card to the list and closes the pop-up. Moreover, it refreshed the workspace.
     */
    public void save() {
        ((Stage)saveButton.getScene().getWindow()).close();
        Card card = new Card(
                cardTitle.getText(),
                cardDescription.getText(),
                "white",
                service.getAppliedTags(),
                new ArrayList<>());
        card.setPriority(service.getCardList().getCards().size()+1);
        service.setAppliedTags(new ArrayList<>());
        service.addCard(card);
        service.insertCardList();
    }
    /**
     * Escapes the window
     */
    public void escape() {
        ((Stage)saveButton.getScene().getWindow()).close();
    }
    public void setAppliedTags(List<Tag> appliedTags)
    {
        service.setAppliedTags(new ArrayList<>());
        appliedTagsVbox.getChildren().clear();
        for(int i=0;i<appliedTags.size();i++) {
            service.applyTag(appliedTags.get(i));
            var loader = new MyFXML(createInjector(new MainModules()))
                    .load(CustomTagCellCtrl.class, "client", "windows", "tags", "CustomTagCell.fxml");
            CustomTagCellCtrl ctrl = loader.getKey();
            ctrl.setAddCardCtrl(this);
            ctrl.setTagObject(appliedTags.get(i), "viewTag");
            appliedTagsVbox.getChildren().add(loader.getValue());
        }
    }


    /**
     * Displays the pop-up (TagList) in order to choose and add a tag.
     */
    public void addTagPopup() {
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(TagListCtrl.class, "client", "windows", "tags","TagList.fxml");
        TagListCtrl ctrl = loader.getKey();
        ctrl.setAvailableTags(service.getAvailableTags());
        ctrl.setAppliedTags(service.getAppliedTags());
        ctrl.setAddCardCtrl(this);
        ctrl.setType("add");
        Parent root = loader.getValue();
        Scene scene = new Scene(root);
        String title = "Add tag";
        hm.popUp(scene, title);
    }

}