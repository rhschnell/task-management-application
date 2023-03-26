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

import client.modules.ListModules;
import client.MyFXML;
import client.windows.lists.list.ListCtrl;
import client.windows.tags.CustomTagCellCtrl;
import client.serverUtils.CardListUtils;
import com.google.inject.Inject;
import client.serverUtils.ServerUtils;
import commons.Card;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.google.inject.Guice.createInjector;

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

    @FXML
    private VBox appliedTagsVbox;

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
        appliedTagsVbox = new VBox();
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
     * This method adds the created card to the list and closes the pop-up. Moreover, it refreshed the workspace.
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
        server.insertCardList(listCtrl.getCardList());
//        listCtrl.getMainCtrl().getWorkspaceCtrl().refreshWorkspace();
    }

    /**
     * Adds the tag to the card, adds the added tag to the VBOX.
     * @param tag The tag that is added and needs to be displayed in the appliedTagsVbox on the AddCard
     */
    public void applyTag(Tag tag)
    {
        appliedTags.add(tag);
        var loader =  new MyFXML(createInjector(new ListModules()))
                .load(CustomTagCellCtrl.class, "client", "scenes", "TagManagement", "CustomTagCell.fxml");
        CustomTagCellCtrl ctrl = loader.getKey();
        ctrl.setCtrl2(this);
        ctrl.setTagObject(tag,"removeFromAddCard");
        appliedTagsVbox.getChildren().add(loader.getValue());
    }

    /**
     * Removes the tag from the list of applied tags that will be later sent to the server, and refreshes
     * the AppliedTagsVbox by clearing it and adding again all the applied tags.
     * @param tag the tag that needs to be removed from the list of the applied tags
     */
    public void removeAppliedTag(Tag tag)
    {
        appliedTags.remove(tag);
        appliedTagsVbox.getChildren().clear();

        for(int i=0;i<appliedTags.size();i++)
        {
            var loader =  new MyFXML(createInjector(new ListModules()))
                    .load(CustomTagCellCtrl.class, "client", "scenes", "TagManagement", "CustomTagCell.fxml");
            CustomTagCellCtrl ctrl = loader.getKey();
            ctrl.setCtrl2(this);
            ctrl.setTagObject(appliedTags.get(i),"removeFromAddCard");
            appliedTagsVbox.getChildren().add(loader.getValue());
        }
    }

    /**
     * Displays the pop-up (TagList) in order to choose and add a tag.
     */
    public void addTagPopup() {
//        List<Tag> availableTags = server.getTags();
//        availableTags.removeAll(appliedTags);
//        var loader =  listCtrl.getMyFXML().
//                load(TagListCtrl.class, "client", "scenes", "TagManagement", "TagList.fxml");
//        TagListCtrl ctrl = loader.getKey();
//        ctrl.setAvailableTags(availableTags);
//        ctrl.setAppliedTags(appliedTags);
//        Parent root = loader.getValue();
//        Scenes scene = new Scenes(root);
//        String title = "Add tag";
//        listCtrl.getMainCtrl().popUp(scene, title);
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