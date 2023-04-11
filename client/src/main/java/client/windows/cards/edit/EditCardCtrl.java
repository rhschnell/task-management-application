package client.windows.cards.edit;

import client.MyFXML;
import client.modules.MainModules;
import client.serverUtils.TaskUtils;
import client.utils.DataFormatManager;
import client.utils.HelperMethods;
import client.windows.cards.view.ViewCardCtrl;
import client.windows.customize.cards.view.CustomCardPresetCellViewCtrl;
import client.windows.lists.delete.DeleteCardCtrl;
import client.windows.subtasks.SubtaskCellCtrl;
import client.windows.subtasks.SubtaskContainer;
import client.windows.tags.view.CustomTagCellCtrl;
import client.windows.tags.view.TagListCtrl;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Inject;
import commons.*;
import jakarta.ws.rs.NotFoundException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.google.inject.Guice.createInjector;

public class EditCardCtrl extends SubtaskContainer implements Initializable {
    private final EditCardService service;
    private TaskUtils taskUtils;

    @FXML
    private TextField cardTitle;
    @FXML
    private TextArea cardDescription;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    private ViewCardCtrl viewCardCtrl;
    private WorkspaceCtrl workspaceCtrl;

    @FXML
    private VBox subtasks;

    @FXML
    private Button addTaskButton;

    @FXML
    private TextField addTaskField;

    @FXML
    private VBox appliedTagsVbox;

    @FXML
    private VBox presets;

    private Card newCard;
    private Card oldCard;
    private long cardID;
    private List<CardColorPreset> presetList;

    private List<Long> deletedSubtaskIDs;

    private Board shownBoard;

    /**
     * Injects the service , the Helper Methods and the viewCardCtrl
     *
     * @param service           The EditCardService to use
     * @param taskUtils         The TaskUtils to use
     * @param helperMethods     Instance of HelperMethods
     * @param viewCardCtrl      The controller that links back to the ViewCard window
     * @param dataFormatManager The DataFormatManager to use
     */
    @Inject
    public EditCardCtrl(EditCardService service, TaskUtils taskUtils, HelperMethods helperMethods,
                        ViewCardCtrl viewCardCtrl, DataFormatManager dataFormatManager) {
        super(dataFormatManager, helperMethods);
        this.service = service;
        this.taskUtils = taskUtils;
        this.viewCardCtrl = viewCardCtrl;
        appliedTagsVbox = new VBox();
        newCard = new Card();
        deletedSubtaskIDs = new ArrayList<>();
    }

    /**
     * @param card The card to get the data from
     */
    public void setCard(Card card) {
        deletedSubtaskIDs=new ArrayList<>();
        this.oldCard = card;
        this.cardID = card.getId();
        service.setCard(card);
        newCard.setTags(card.getTags());
        setAppliedTags(card.getTags());
        setCardTitle(card.getTitle());
        setCardDescription(card.getDescription());
        newCard.setSubTasks(card.getSubTasks());
    }

    /**
     * Getter for the key of the board
     * @return the key of the board
     */
    public String getBoardKey() {
        return service.getBoardKey();
    }

    /**
     * Setter for the key of the board
     * @param boardKey the key of the board to be set
     */
    public void setBoardKey(String boardKey) {
        service.setBoardKey(boardKey);
    }

    /**
     * Sets he title of the card
     *
     * @param title the title
     */
    public void setCardTitle(String title) {
        cardTitle.setText(title);
    }

    /**
     * Sets the description of the card
     *
     * @param description the description
     */
    public void setCardDescription(String description) {
        cardDescription.setText(description);
    }

    /**
     * This method cancels editing the card and return to the previous window
     */
    public void escape() {
        deletedSubtaskIDs.clear();
        ((Stage) cancelButton.getScene().getWindow()).close();
    }


    /**
     * Saves the changes and closes the pop-up
     */
    public void save() {

        String title = super.helperMethods.getInputValidator().stripWhitespace(cardTitle.getText());

        if (super.checkAndHandleUserInput(title)) return;

        ((Stage) saveButton.getScene().getWindow()).close();

        Card editedCard = service.getCard();
        editedCard.setTitle(title);
        editedCard.setTags(newCard.getTags());
        editedCard.setDescription(cardDescription.getText());

        editedCard.setPresets(new ArrayList<>());
        editedCard.setPreset(getAppliedPreset());
        editedCard.setSubTasks(newCard.getSubTasks());
        // Delete the deleted tasks from the database
        for(int i=0;i<deletedSubtaskIDs.size();i++)
        {
            for(int j=0;j<editedCard.getSubTasks().size();j++)
            {
                if(editedCard.getSubTasks().get(j).getId()==deletedSubtaskIDs.get(i))
                    taskUtils.deleteTask(deletedSubtaskIDs.get(i));
            }
        }
        deletedSubtaskIDs.clear();

        service.insertCard(editedCard);
    }

    /**
     * Method to display the presets in a VBox
     */
    public void displayPresetList(){
        for(CardColorPreset preset : presetList){
            var loader = new MyFXML(createInjector(new MainModules()))
                    .load(CustomCardPresetCellViewCtrl.class,
                            "client", "windows", "customize", "cards", "view", "CustomCardPresetCellView.fxml");
            CustomCardPresetCellViewCtrl ctrl = loader.getKey();
            ctrl.setEditCardCtrl(this);
            ctrl.setWorkspaceCtrl(workspaceCtrl);
            ctrl.setPresetList(shownBoard.getPresetList());
            ctrl.setPresetObject(preset, "EditCardCtrl");
            ctrl.setAppliedPreset(oldCard.getPresets().get(0));

            presets.getChildren().add(loader.getValue());
        }
    }

    /**
     * This method updates the displayed presets
     */
    public void updateDisplayedPresets(){
        presets.getChildren().clear();
        displayPresetList();
    }

    /**
     * Gets the preset that is applied
     * @return The applied preset
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
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Timeline tl = new Timeline();
        tl.setCycleCount(-1);
        KeyFrame kf = new KeyFrame(Duration.millis(500),
                event -> {
                    try {
                        refresh();
                    } catch (NotFoundException e) {
                        escape();
                    }
                });
        tl.getKeyFrames().add(kf);
        tl.play();
    }

    /**
     * Displays the pop-up (TagList) in order to choose and add a tag.
     */
    public void editTagPopup() {
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(TagListCtrl.class, "client", "windows", "tags", "TagList.fxml");
        TagListCtrl ctrl = loader.getKey();
        List<Tag> available = service.getTags();
        if (newCard.getTags() != null)
            available.removeAll(newCard.getTags());
        ctrl.setAvailableTags(available);
        ctrl.setAppliedTags(newCard.getTags());
        ctrl.setEditCardCtrl(this);
        ctrl.setType("edit");
        Parent root = loader.getValue();
        Scene scene = new Scene(root);
        String title = "Add tag";
        super.helperMethods.popUp(scene, title);
    }

    /**
     * Setter for the applied tag list
     * @param appliedTags a list of tag objects
     */
    public void setAppliedTags(List<Tag> appliedTags) {
        if (appliedTags == null) appliedTags = new ArrayList<>();
        newCard.setTags(appliedTags);
        appliedTagsVbox.getChildren().clear();

        // Display the applied tags on screen
        for (Tag appliedTag : appliedTags) {
            service.applyTag(appliedTag);
            var loader = new MyFXML(createInjector(new MainModules()))
                    .load(CustomTagCellCtrl.class, "client", "windows", "tags", "CustomTagCell.fxml");
            CustomTagCellCtrl ctrl = loader.getKey();
            ctrl.setTagObject(appliedTag, "viewTag");
            appliedTagsVbox.getChildren().add(loader.getValue());
        }

    }

    /**
     * Adds a task to the card and displays it based on user input
     */
    public void addTask() {
        String title = super.helperMethods.getInputValidator().stripWhitespace(addTaskField.getText());
        if (!super.helperMethods.validateInputAndShowPopup(title)) return;

        Task newTask = new Task();
        newTask.setCompleted(false);
        newTask.setTitle(title);
        newCard.addSubTask(newTask);
        addTaskField.clear();
        displayTasks();
    }

    public void deleteCard() {
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(DeleteCardCtrl.class, "client", "windows", "lists", "delete", "DeleteCard.fxml");
        Parent root = loader.getValue();
        Scene scene = new Scene(root);
        loader.getKey().setDeleteCard(oldCard);
        scene.getRoot().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                loader.getKey().escape();
            }
            if (event.getCode() == KeyCode.ENTER) {
                loader.getKey().delete();
            }
        });
        String title = "Delete a card";
        helperMethods.popUp(scene, title);
    }

    /**
     * Allows adding a task by pressing ENTER
     * @param event the event (only ENTER is considered)
     */
    public void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            addTask();
        }
    }

    /**
     * Displays the subtasks to the UI
     */
    @Override
    public void displayTasks() {
        subtasks.getChildren().clear();
        if (newCard.getSubTasks() == null) return;

        // Display all the subtasks on screen
        for (Task task : newCard.getSubTasks()) {
            var loader = new MyFXML(createInjector()).load(SubtaskCellCtrl.class,
                    "client", "windows", "subtasks", "SubtaskCell.fxml");
            loader.getKey().updateItem(task);
            loader.getKey().setSubtaskContainer(this);
            makeTaskDraggable(loader, subtasks);
            subtasks.getChildren().add(loader.getValue());
        }

    }

    /**
     * Deletes a subtask from the card
     * @param task the subtask you want to delete
     */
    @Override
    public void deleteSubtask(Task task) {
        deletedSubtaskIDs.add(task.getId());
        newCard.deleteSubTask(task);
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
            if (db.hasContent(getDataFormatManager().getSubtaskFormat())) {
                Node draggedNode = (Node) event.getGestureSource();

                // Remove the task from the VBox
                taskVBox.getChildren().remove(draggedNode);

                // Insert the new task into the data object
                Task draggedTask = (Task) db.getContent(getDataFormatManager().getSubtaskFormat());
                int newIndex = taskVBox.getChildren().indexOf(fxComponent) - 1;

                // If the card already has subtasks saved in the DB, then we should reflect this
                // also in the DB.
                if (service.getCard().getSubTasks() != null){
                    service.dragAndDropDB(draggedTask, newIndex);

                }
                //Note that this does not have to be the case, when subtasks are
                // created for the first time for a card.
                else {
                    // Shift the tasks around in the current unsaved/changed temporary card
                    // (this is implicitly done by addSubTask)
                    newCard.deleteSubTask(draggedTask);
                    newCard.addSubTask(newIndex, draggedTask);
                }

                // Update the UI
                taskVBox.getChildren().add(newIndex, draggedNode);
                displayTasks();
            }

            event.setDropCompleted(true);
            event.consume();
        });
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
     * Sets the preset list of color presets
     * @param presetList The list of presets to be set
     */
    public void setPresetList(List<CardColorPreset> presetList){
        this.presetList = presetList;
    }

    private void refresh() {
        if(!newCard.equals(service.getCard(cardID))) {
            newCard = service.getCard(cardID);
            service.setCard(newCard);
            setCardTitle(newCard.getTitle());
            setCardDescription(newCard.getDescription());
            setAppliedTags(newCard.getTags());
            displayTasks();
            setPresetList(newCard.getPresets());
            updateDisplayedPresets();
        }
    }
}
