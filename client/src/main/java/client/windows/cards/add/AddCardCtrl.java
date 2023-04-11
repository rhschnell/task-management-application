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
import client.utils.DataFormatManager;
import client.utils.HelperMethods;
import client.windows.customize.cards.view.CustomCardPresetCellViewCtrl;
import client.windows.subtasks.SubtaskCellCtrl;
import client.windows.subtasks.SubtaskContainer;
import client.windows.tags.view.CustomTagCellCtrl;
import client.windows.tags.view.TagListCtrl;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Inject;
import commons.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.google.inject.Guice.createInjector;

public class AddCardCtrl extends SubtaskContainer {

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

    @FXML
    private VBox presets;

    @FXML
    private TextField addSubtaskTitle;

    @FXML
    private Button addSubtaskButton;

    @FXML
    private VBox subtasks;

    private List<Task> taskList;

    private Board shownBoard;
    private CardColorPreset preset;

    private HelperMethods helperMethods;
    private WorkspaceCtrl workspaceCtrl;
    private List<CardColorPreset> presetList;

    /**
     * Constructor for AddCardCtrl
     *
     * @param service           corresponding service
     * @param helperMethods     Instance of HelperMethods
     * @param dataFormatManager Instance of DataFormatManager that gets passed to the super class
     */
    @Inject
    public AddCardCtrl(AddCardService service, HelperMethods helperMethods, DataFormatManager dataFormatManager) {
        super(dataFormatManager, helperMethods);
        this.service = service;
    }

    public void setIP(String ip) {
        service.setIP(ip);
    }

    /**
     * Sets the card list for this card
     * @param cardList The card list to which this card belongs
     */
    public void setCardList(CardList cardList) {
        service.setCardList(cardList);
    }

    /**
     * Sets the boardKey
     * @param boardKey the boardKey to be set
     */
    public void setBoardKey(String boardKey) {
        service.setBoardKey(boardKey);
    }

    /**
     * Gets the boardKey
     * @return the boardKey
     */
    public String getBoardKey() {
        return service.getBoardKey();
    }

    /**
     * This method cancels adding the created card to the list
     */
    public void cancel() {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }

    /**
     * This method adds the created card to the list and closes the pop-up. Moreover, it refreshed the workspace.
     */
    public void save() {
        String title = super.helperMethods.getInputValidator().stripWhitespace(cardTitle.getText());

        if (super.checkAndHandleUserInput(title)) return;

        ((Stage) saveButton.getScene().getWindow()).close();

        Card card = new Card(
                title,
                cardDescription.getText(),
                service.getAppliedTags(),
                taskList,
                new ArrayList<>());

        card.setPreset(getAppliedPreset());
        card.setPriority(service.getCardList().getCards().size() + 1);
        service.setAppliedTags(new ArrayList<>());
        service.addCard(card);
        service.insertCardList();
    }

    /**
     * Gets the applied preset
     * @return The applied preset, or default if no preset is applied
     */
    public CardColorPreset getAppliedPreset(){
        for(CardColorPreset preset : presetList){
            if(preset.isDefault()){
                return preset;
            }
        }
        return shownBoard.getPresetList().get(0);
    }

    /**
     * Sets the applied tags to the VBOX of the displayed cards
     * @param appliedTags the Array of tags that needs to be displayed
     */
    public void setAppliedTags(List<Tag> appliedTags) {
        service.setAppliedTags(new ArrayList<>());
        appliedTagsVbox.getChildren().clear();
        for (int i = 0; i < appliedTags.size(); i++) {
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
                .load(TagListCtrl.class, "client", "windows", "tags", "TagList.fxml");
        TagListCtrl ctrl = loader.getKey();
        ctrl.setAvailableTags(service.getAvailableTags());
        ctrl.setAppliedTags(service.getAppliedTags());
        ctrl.setAddCardCtrl(this);
        ctrl.setType("add");
        Parent root = loader.getValue();
        Scene scene = new Scene(root);
        String title = "Add tag";
        super.helperMethods.popUp(scene, title);
    }

    /**
     * This method displays the preset list of color presets
     */
    public void displayPresetList(){
        this.presetList = workspaceCtrl.getShownBoard().getPresetList();
        for(CardColorPreset preset : presetList){
            var loader = new MyFXML(createInjector(new MainModules()))
                    .load(CustomCardPresetCellViewCtrl.class,
                            "client", "windows", "customize", "cards", "view", "CustomCardPresetCellView.fxml");
            CustomCardPresetCellViewCtrl ctrl = loader.getKey();
            ctrl.setAddCardCtrl(this);
            ctrl.setWorkspaceCtrl(workspaceCtrl);
            ctrl.setPresetList(shownBoard.getPresetList());
            ctrl.setPresetObject(preset, "AddCardCtrl");

            presets.getChildren().add(loader.getValue());
        }
    }

    /**
     * Method that updates the displayed presets
     */
    public void updateDisplayedPresets(){
        presets.getChildren().clear();
        displayPresetList();
    }

    /**
     * Adds a new subtask to a card
     */
    public void addTask() {
        if (taskList == null) {
            taskList = new ArrayList<>();
        }

        String title = super.helperMethods.getInputValidator().stripWhitespace(addSubtaskTitle.getText());
        if (!super.helperMethods.validateInputAndShowPopup(title)) return;

        Task newTask = new Task();
        newTask.setCompleted(false);
        newTask.setTitle(title);
        newTask.setPriority(taskList.size() + 1);
        taskList.add(newTask);
        addSubtaskTitle.clear();
        displayTasks();
    }

    /**
     * Handles key events to make sure the user can add tasks by pressing ENTER
     * @param event The key event to be handled
     */
    public void handleKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            addTask();
        }
    }

    /**
     * Displays all the tasks of the card to the screen
     */
    @Override
    public void displayTasks() {
        subtasks.getChildren().clear();
        for (Task task : taskList) {

            var loader = new MyFXML(createInjector()).load(SubtaskCellCtrl.class,
                    "client", "windows", "subtasks", "SubtaskCell.fxml");
            loader.getKey().updateItem(task);
            loader.getKey().setSubtaskContainer(this);
            makeTaskDraggable(loader, subtasks);
            subtasks.getChildren().add(loader.getValue());
        }
    }

    /**
     * Deletes the subtask from the card and refreshes the display
     * @param task The subtask to remove
     */
    @Override
    public void deleteSubtask(Task task) {
        taskList.remove(task);
        displayTasks();
    }

    /**
     * Sets the action for when a dragged subtask is dropped
     *
     * @param fxComponent The JavaFX UI component of a subtask
     * @param taskVBox    The VBox holding the subtasks
     */
    @Override
    public void setOnDragDropped(Parent fxComponent, VBox taskVBox) {
        fxComponent.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasContent(getDataFormatManager().getSubtaskFormat())) {
                // Remove the task from the VBox
                Node draggedNode = (Node) event.getGestureSource();
                taskVBox.getChildren().remove(draggedNode);

                // Insert the new task into the data object
                Task draggedTask = (Task) db.getContent(getDataFormatManager().getSubtaskFormat());
                int newIndex = taskVBox.getChildren().indexOf(fxComponent) - 1;
                service.reorderTasks(draggedTask, newIndex, taskList);

                // Insert the new task into the UI
                taskVBox.getChildren().add(newIndex, draggedNode);

                // Refresh the controllers with the correct task objects
                displayTasks();
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });

    }

    /**
     * Sets the shown board
     * @param shownBoard The shown board to be set
     */
    public void setBoard(Board shownBoard) {
        this.shownBoard = shownBoard;
    }

    /**
     * Sets the workspaceCtrl
     * @param workspaceCtrl The WorkspaceCtrl to be set
     */
    public void setWorkspaceCtrl(WorkspaceCtrl workspaceCtrl) {
        this.workspaceCtrl = workspaceCtrl;
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
