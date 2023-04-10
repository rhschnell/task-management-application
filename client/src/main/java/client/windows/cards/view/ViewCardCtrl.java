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

import client.MainCtrl;
import client.MyFXML;
import client.modules.MainModules;
import client.utils.HelperMethods;
import client.windows.cards.edit.EditCardCtrl;
import client.windows.customize.cards.view.CustomCardPresetCellViewCtrl;
import client.windows.subtasks.SubtaskCellCtrl;
import client.windows.tags.view.CustomTagCellCtrl;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.CardColorPreset;
import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static com.google.inject.Guice.createInjector;

public class ViewCardCtrl {
    private final HelperMethods helperMethods;

    private MainCtrl mainCtrl;
    private MyFXML myFXML;
    private Card card;
    private Board shownBoard;

    @FXML
    private Label cardTitle;

    @FXML
    private Text cardDescription;

    @FXML
    private Button cancelButton;

    @FXML
    private VBox appliedTagsVbox;

    @FXML
    private VBox taskBox;

    @FXML
    private Pane appliedPreset;

    private ViewCardService service;
    private WorkspaceCtrl workspaceCtrl;

    /**
     * Constructor for ViewCardCtrl
     * @param helperMethods hm
     * @param service The service to use in this controller
     * @param mainCtrl The main controller to use in this controller
     * @param myFXML The MyFXML injector to use in this controller
     */
    @Inject
    public ViewCardCtrl(HelperMethods helperMethods, ViewCardService service, MainCtrl mainCtrl, MyFXML myFXML) {
        this.helperMethods = helperMethods;
        this.service=service;
        this.mainCtrl = mainCtrl;
        this.myFXML = myFXML;
        appliedTagsVbox = new VBox();
        cardTitle = new Label();
        cardDescription=new Text();
        taskBox=new VBox();
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


    /**
     * Adds a tag to the card view
     */
    public void applyTag() {
        appliedTagsVbox.getChildren().clear();
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
        ((Stage)cancelButton.getScene().getWindow()).close();
        service.deleteCard(card);
    }

    /**
     * Escapes the window
     */
    public void escape() {
        ((Stage)cancelButton.getScene().getWindow()).close();
    }

    /**
     * Pops up the edit card window
     */
    public void edit() {
        var loader = myFXML.load(EditCardCtrl.class, "client", "windows", "cards", "EditCard.fxml");
        loader.getKey().setBoardKey(getBoardKey());
        loader.getKey().setCard(card);
        loader.getKey().displayTasks();
        loader.getKey().setShownBoard(shownBoard);
        loader.getKey().setWorkspaceCtrl(workspaceCtrl);
        loader.getKey().setPresetList(shownBoard.getPresetList());
        loader.getKey().displayPresetList();

        Scene scene = new Scene(loader.getValue());
        scene.getRoot().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                loader.getKey().escape();
            }
        });
        helperMethods.popUp(scene,"Edit Card");
        displayTasks();
    }

    /**
     * Gets the key of the board associated to this card
     * @return The associated board's key
     */
    public String getBoardKey() {
        return service.getBoardKey();
    }

    /**
     * Sets the key of the board associated to this card
     * @param boardKey New key
     */
    public void setBoardKey(String boardKey)
    {
        service.setBoardKey(boardKey);
    }

    /**
     * Allows us to add subtasks to a list
     */
    public void displayTasks() {
        taskBox.getChildren().clear();
        if(card.getSubTasks()!=null) {
            for (Task task : card.getSubTasks()) {
                var loader = new MyFXML(createInjector()).load(SubtaskCellCtrl.class,
                        "client", "windows", "subtasks", "SubtaskCell.fxml");
                loader.getKey().updateItem(task);
                loader.getKey().disableEdit();
                taskBox.getChildren().add(loader.getValue());
            }
        }
    }

    /**
     * Sets the shown board
     * @param shownBoard The shown board to be set
     */
    public void setShownBoard(Board shownBoard){
        this.shownBoard = shownBoard;
    }

    /**
     * Sets the workspaceCtrl
     * @param workspaceCtrl The WorkspaceCtrl to be set
     */
    public void setWorkspaceCtrl(WorkspaceCtrl workspaceCtrl){
        this.workspaceCtrl = workspaceCtrl;
    }

    /**
     * This method displays the applied preset
     * @param preset The applied preset that needs to be displayed
     */
    public void displayPreset(CardColorPreset preset) {
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(CustomCardPresetCellViewCtrl.class,
                        "client", "windows", "customize", "cards", "view", "CustomCardPresetCellView.fxml");
        CustomCardPresetCellViewCtrl ctrl = loader.getKey();
        ctrl.setViewCardCtrl(this);
        ctrl.setWorkspaceCtrl(workspaceCtrl);
        ctrl.setPresetList(shownBoard.getPresetList());
        ctrl.setPresetObject(preset, "ViewCardCtrl");

        appliedPreset.getChildren().add(loader.getValue());
    }
}
