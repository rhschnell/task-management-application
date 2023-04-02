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
import client.windows.subtasks.SubtaskCellCtrl;
import client.windows.subtasks.SubtaskContainer;
import client.windows.tags.view.CustomTagCellCtrl;
import client.windows.tags.view.TagListCtrl;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;
import commons.Tag;
import commons.Task;
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

import java.util.ArrayList;
import java.util.List;

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
    private TextField addSubtaskTitle;

    @FXML
    private Button addSubtaskButton;

    @FXML
    private VBox subtasks;

    private List<Task> taskList;


    private HelperMethods helperMethods;

    /**
     * Constructor for AddCardCtrl
     *
     * @param service           corresponding service
     * @param helperMethods     Instance of HelperMethods
     * @param dataFormatManager Instance of DataFormatManager that gets passed to the super class
     */
    @Inject
    public AddCardCtrl(AddCardService service, HelperMethods helperMethods, DataFormatManager dataFormatManager) {
        super(dataFormatManager);
        this.service = service;
        this.helperMethods = helperMethods;
    }

    public void setCardList(CardList cardList) {
        service.setCardList(cardList);
    }

    public void setBoardKey(String boardKey) {
        service.setBoardKey(boardKey);
    }

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
        ((Stage) saveButton.getScene().getWindow()).close();
        Card card = new Card(
                cardTitle.getText(),
                cardDescription.getText(),
                "white",
                service.getAppliedTags(),
                taskList);
        card.setPriority(service.getCardList().getCards().size() + 1);
        service.setAppliedTags(new ArrayList<>());
        service.addCard(card);
        service.insertCardList();
    }

    /**
     * Escapes the window
     */
    public void escape() {
        ((Stage) saveButton.getScene().getWindow()).close();
    }

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
        helperMethods.popUp(scene, title);
    }

    /**
     * Adds a new subtask to a card
     */
    public void addTask() {
        if (taskList == null) {
            taskList = new ArrayList<>();
        }
        if (!(addSubtaskTitle.getText() != null && !addSubtaskTitle.getText().isEmpty())) {
            return; //TODO: notify user in some way that you cannot add empty tasks
        }
        Task newTask = new Task();
        newTask.setCompleted(false);
        newTask.setTitle(addSubtaskTitle.getText());
        newTask.setPriority(taskList.size() + 1);
        taskList.add(newTask);
        addSubtaskTitle.clear();
        displayTasks();
    }

    public void handleKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            addTask();
        }
    }

    @Override
    public void displayTasks() {
        subtasks.getChildren().clear();
        for (Task task : taskList) {

            var loader = new MyFXML(createInjector()).load(SubtaskCellCtrl.class,
                    "client", "windows", "subtasks", "SubtaskCell.fxml");
            loader.getKey().updateItem(task);
            loader.getKey().setSubtaskContainer(this);
            makeTaskDraggable(loader);
            subtasks.getChildren().add(loader.getValue());
        }
    }

    @Override
    public void deleteSubtask(Task task) {
        taskList.remove(task);
        displayTasks();
    }

    /**
     * Sets the action for when a dragged subtask is dropped
     *
     * @param fxComponent The JavaFX UI component of a subtask
     */
    @Override
    public void setOnDragDropped(Parent fxComponent) {
        fxComponent.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasContent(getDataFormatManager().getSubtaskFormat())) {
                Node draggedNode = (Node) event.getGestureSource();
                Parent oldParent = draggedNode.getParent();
                if (oldParent instanceof VBox) {
                    ((VBox) oldParent).getChildren().remove(draggedNode);
                    Task draggedTask = (Task) db.getContent(getDataFormatManager().getSubtaskFormat());

                    taskList.remove(draggedTask);

                    int newIndex = ((VBox) oldParent).getChildren().indexOf(fxComponent) - 1;
                    taskList.add(newIndex, draggedTask);
                    ((VBox) oldParent).getChildren().add(newIndex, draggedNode);
                    service.reorderTasks(draggedTask, newIndex, taskList);
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });

    }

}