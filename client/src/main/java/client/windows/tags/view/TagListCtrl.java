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

import client.MyFXML;
import client.modules.MainModules;
import client.windows.cards.add.AddCardCtrl;;
import client.windows.cards.edit.EditCardCtrl;
import com.google.inject.Inject;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import static com.google.inject.Guice.createInjector;


public class TagListCtrl {

    private AddCardCtrl addCardCtrl;
    private EditCardCtrl editCardCtrl;
    @FXML
    private Button cancelButton;

    @FXML
    private VBox appliedTagsBox;
    @FXML
    private VBox availableTagsBox;

    private String type;

    private List<Tag> availableTags;
    private List<Tag> appliedTags;



    @Inject
    public TagListCtrl(AddCardCtrl addCardCtrl, EditCardCtrl editCardCtrl) {
        this.addCardCtrl = addCardCtrl;
        this.editCardCtrl = editCardCtrl;
        availableTagsBox=new VBox();
        availableTags=new ArrayList<>();
        appliedTags=new ArrayList<>();
        appliedTagsBox=new VBox();
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    /**
     * Sets the tagList VBOX contain all the tags that are available in the board
     * @param tagList
     */
    public void setAvailableTags(List<Tag> tagList)
    {
        this.availableTags=new ArrayList<>();
        this.availableTags.addAll(tagList);
        displayAvailableTags();
    }
    public void displayAvailableTags()
    {
        availableTagsBox.getChildren().clear();
        for(int i=0; i<availableTags.size(); i++) {
            var loader = new MyFXML(createInjector(new MainModules()))
                    .load(CustomTagCellCtrl.class, "client", "windows", "tags","CustomTagCell.fxml");
            CustomTagCellCtrl ctrl = loader.getKey();
            ctrl.setTagObject(availableTags.get(i),"addFromTagList");
            ctrl.setTagListCtrl(this);
            availableTagsBox.getChildren().add(loader.getValue());
        }
    }

    /**
     * sets the AppliedTags VBOX contian all the tags that are applied on the card
     * @param tagList
     */
    public void setAppliedTags(List<Tag> tagList)
    {
        this.appliedTags = new ArrayList<>();
        this.appliedTags.addAll(tagList);
        displayAppliedTags();
    }
    public void displayAppliedTags()
    {
        appliedTagsBox.getChildren().clear();
        for(int i=0; i<appliedTags.size(); i++) {
            var loader = new MyFXML(createInjector(new MainModules()))
                    .load(CustomTagCellCtrl.class, "client", "windows", "tags","CustomTagCell.fxml");
            CustomTagCellCtrl ctrl = loader.getKey();
            ctrl.setTagObject(appliedTags.get(i),"removeFromTagList");
            ctrl.setTagListCtrl(this);
            appliedTagsBox.getChildren().add(loader.getValue());
        }
    }
    public void refreshRemove(Tag tag)
    {
        appliedTags.remove(tag);
        availableTags.add(tag);
        displayAppliedTags();
        displayAvailableTags();
    }
    public void refreshAdd(Tag tag)
    {
        availableTags.remove(tag);
        appliedTags.add(tag);
        displayAppliedTags();
        displayAvailableTags();
    }

    public void setAddCardCtrl(AddCardCtrl addCardCtrl){
        this.addCardCtrl = addCardCtrl;
    }

    public void setEditCardCtrl(EditCardCtrl editCardCtrl) {
        this.editCardCtrl = editCardCtrl;
    }


    /**
     * Escapes the pop-up in which the AvailableCard and the AppliedTags are displayed
     */
    public void escapeWindow() {
        if(type.equals("add"))
            addCardCtrl.setAppliedTags(appliedTags);
        if(type.equals("edit"))
            editCardCtrl.setAppliedTags(appliedTags);
        ((Stage)cancelButton.getScene().getWindow()).close();
    }

}