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
package client.scenes.TagManagement;

import client.*;
import client.scenes.CardWindows.AddCardCtrl;
import client.scenes.CardWindows.ViewCardCtrl;
import client.scenes.ListManagement.ListCtrl;
import client.scenes.MainCtrl;
import client.utils.CardListUtils;
import client.utils.CardUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.google.inject.Guice.createInjector;

public class TagListCtrl {

    private CardUtils server;
    private AddCardCtrl addCardCtrl;

    private CardList tagList;

    @FXML
    private Label tagListTitle;

    @FXML
    private Button cancelButton;

    @FXML
    private VBox tagListBox;

    /**
     * Constructor for ListCtrl
     * @param server a server util
     * @param addCardCtrl a main controller
     */
    @Inject
    public TagListCtrl(ServerUtils server, AddCardCtrl addCardCtrl) {
        this.addCardCtrl = addCardCtrl;
        tagListTitle = new Label();
        tagList = new CardList();
        tagListBox=new VBox();
    }

    public void setCtrl(AddCardCtrl addCardCtrl)
    {
        this.addCardCtrl=addCardCtrl;
    }
    public void addTags(List<Tag> tagList)
    {
        System.out.println(this+"TagList");
        for(int i=0; i<tagList.size(); i++) {
            var loader = new MyFXML(createInjector(new ListModules()))
                    .load(CustomTagCellCtrl.class, "client", "scenes", "TagManagement", "CustomTagCell.fxml");
            CustomTagCellCtrl ctrl = loader.getKey();
            ctrl.setTagTitle(tagList.get(i).getName());
            ctrl.setTag(tagList.get(i));
            ctrl.setCtrl(this);
            tagListBox.getChildren().add(loader.getValue());
        }
    }
    public AddCardCtrl getCardCtrl() {
        return addCardCtrl;
    }

    public void setListTitle(String title) {
        tagListTitle.setText(title);
    }

    public void cancel() {
        ((Stage)cancelButton.getScene().getWindow()).close();
    }

}