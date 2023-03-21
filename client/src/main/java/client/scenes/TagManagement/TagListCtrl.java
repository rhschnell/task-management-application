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

import client.CustomListCell;
import client.CustomTagCell;
import client.Main;
import client.scenes.CardWindows.AddCardCtrl;
import client.scenes.CardWindows.ViewCardCtrl;
import client.scenes.MainCtrl;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class TagListCtrl {

    private ServerUtils server;
    private MainCtrl mainCtrl;

    private CardList tagList;

    @FXML
    private Label tagListTitle;

    @FXML
    private Button cancelButton;

    @FXML
    private ListView<Tag> tagListView;

    /**
     * Constructor for ListCtrl
     * @param server a server util
     * @param mainCtrl a main controller
     */
    @Inject
    public TagListCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        tagListView = new ListView<>();
        tagListTitle = new Label();
        tagList = new CardList();
    }

    public MainCtrl getMainCtrl() {
        return mainCtrl;
    }

   /* public void setCardList(CardList cardList) {
        this.tagList = cardList;
    }

    */



  /*  public CardList getCardList() {
        return tagList;
    }*/


    public void setListTitle(String title) {
        tagListTitle.setText(title);
    }

    public void addTags(List<Tag> boardTagLists) {
        tagListView.setCellFactory(param -> {
            ListCell<Tag> tagCell = new CustomTagCell();
            tagCell.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {

                }
            });
            return tagCell;
        });
        tagListView.getItems().addAll(boardTagLists);
    }
    public void cancel() {
        ((Stage)cancelButton.getScene().getWindow()).close();
    }

}