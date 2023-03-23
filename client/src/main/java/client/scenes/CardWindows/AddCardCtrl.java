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
import client.scenes.TagManagement.TagListCtrl;
import client.utils.CardListUtils;
import com.google.inject.Inject;
import client.utils.ServerUtils;
import commons.Card;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddCardCtrl implements Initializable {

    private final CardListUtils server;
    private final ListCtrl listCtrl;

    @FXML
    private TextField cardTitle;

    @FXML
    private TextArea cardDescription;

    @FXML
    private Button cancelButton;

    @FXML
    private Button saveButton;

    private List<Tag> appliedTags;

    /**
     * Constructor for AddCardCtrl
     * @param server a server util
     * @param listCtrl a main controller
     */
    @Inject
    public AddCardCtrl(ServerUtils server, ListCtrl listCtrl) {
        this.listCtrl = listCtrl;
        this.server = new CardListUtils(server);
        appliedTags = new ArrayList<>();
    }

    public ListCtrl getListCtrl() {
        return listCtrl;
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
        ((Stage)saveButton.getScene().getWindow()).close();
        Card card = new Card(
                cardTitle.getText(),
                cardDescription.getText(),
                "white",
                appliedTags,
                new ArrayList<>());
        appliedTags = new ArrayList<>();
        listCtrl.getCardList().addCard(card);
        server.addCardList(listCtrl.getCardList());
        listCtrl.getMainCtrl().getWorkspaceCtrl().refreshWorkspace();
    }
    public void applyTag(Tag tag)
    {
        appliedTags.add(tag);
    }
    public void removeAppliedTag(Tag tag)
    {
        appliedTags.remove(tag);
    }
    public void addTagPopup() {
        List<Tag> availableTags = server.getTags();
        availableTags.removeAll(appliedTags);
        var loader =  listCtrl.getMyFXML().
                load(TagListCtrl.class, "client", "scenes", "TagManagement", "TagList.fxml");
        TagListCtrl ctrl = loader.getKey();
        ctrl.setAvailableTags(availableTags);
        ctrl.setAppliedTags(appliedTags);
        Parent root = loader.getValue();
        Scene scene = new Scene(root);
        String title = "Add tag";
        listCtrl.getMainCtrl().popUp(scene, title);
    }
    /**
     *
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}