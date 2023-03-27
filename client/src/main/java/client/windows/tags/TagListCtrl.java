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
package client.windows.tags;

import client.MyFXML;
import client.modules.MainModules;
import client.windows.cards.add.AddCardCtrl;;
import client.serverUtils.ServerUtils;
import com.google.inject.Inject;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;

import static com.google.inject.Guice.createInjector;


public class TagListCtrl {

    private ServerUtils server;
    private AddCardCtrl addCardCtrl;
    @FXML
    private Button cancelButton;

    @FXML
    private VBox appliedTagsBox;
    @FXML
    private VBox availableTagsBox;



    @Inject
    public TagListCtrl(ServerUtils server, AddCardCtrl addCardCtrl) {
        this.addCardCtrl = addCardCtrl;
        this.server = server;
        availableTagsBox=new VBox();
        appliedTagsBox=new VBox();
    }

    /**
     * Sets the tagList VBOX contain all the tags that are available in the board
     * @param tagList
     */
    public void setAvailableTags(List<Tag> tagList)
    {
        for(int i=0; i<tagList.size(); i++) {
            var loader =new MyFXML(createInjector(new ListModules()))
                    .load(CustomTagCellCtrl.class, "client", "windows", "tags","CustomTagCell.fxml");
            CustomTagCellCtrl ctrl = loader.getKey();
            ctrl.setTagObject(tagList.get(i),"addFromTagList");
            ctrl.setCtrl(this);
            availableTagsBox.getChildren().add(loader.getValue());
        }
    }

    /**
     * sets the AppliedTags VBOX contian all the tags that are applied on the card
     * @param tagList
     */
    public void setAppliedTags(List<Tag> tagList)
    {
        for(int i=0; i<tagList.size(); i++) {
            var loader = new MyFXML(createInjector(new ListModules()))
                    .load(CustomTagCellCtrl.class, "client", "windows", "tags","CustomTagCell.fxml");
            CustomTagCellCtrl ctrl = loader.getKey();
            ctrl.setTagObject(tagList.get(i),"removeFromTagList");
            ctrl.setCtrl(this);
            appliedTagsBox.getChildren().add(loader.getValue());
        }
    }
    public AddCardCtrl getCardCtrl() {
        return addCardCtrl;
    }

    /**
     * Escapes the pop-up in which the AvailableCard and the AppliedTags are displayed
     */
    public void escapeWindow() {
        ((Stage)cancelButton.getScene().getWindow()).close();
    }

}