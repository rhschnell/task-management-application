package client.windows.cards.edit;

import client.MyFXML;
import client.modules.MainModules;
import client.serverUtils.TaskUtils;
import client.utils.DataFormatManager;
import client.utils.HelperMethods;
import client.windows.cards.view.ViewCardCtrl;
import client.windows.customize.cards.view.CustomCardPresetCellViewCtrl;
import client.windows.subtasks.SubtaskCellCtrl;
import client.windows.subtasks.SubtaskContainer;
import client.windows.tags.view.CustomTagCellCtrl;
import client.windows.tags.view.TagListCtrl;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Inject;
import commons.*;
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
    private HelperMethods helperMethods;
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
    private List<CardColorPreset> presetList;

    private List<Long> deletedSubtaskIDs;

    private Board shownBoard;

    private final CardColorPreset DEFAULT_PRESET = new CardColorPreset(
            "Default", "0xDEEDE7FF", "0x000000FF");

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
        super(dataFormatManager);
        this.service = service;
        this.taskUtils = taskUtils;
        this.helperMethods = helperMethods;
        this.viewCardCtrl = viewCardCtrl;
        appliedTagsVbox = new VBox();
        newCard = new Card();
        deletedSubtaskIDs = new ArrayList<>();
    }

    /**
     * @param card The card to get the data from
     */
    public void setCard(Card card) {
        this.oldCard = card;
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
        ((Stage) cancelButton.getScene().getWindow()).close();
    }


    /**
     * Saves the changes and closes the pop-up
     */
    public void save() {
        ((Stage) saveButton.getScene().getWindow()).close();
        Card editedCard = service.getCard();
        editedCard.setTitle(cardTitle.getText());
        editedCard.setTags(newCard.getTags());
        editedCard.setDescription(cardDescription.getText());

        editedCard.setPresets(new ArrayList<>());
        editedCard.setPreset(getAppliedPreset());

        // Delete the tasks from the database that were deleted
        for (long taskID : deletedSubtaskIDs) {
            taskUtils.deleteTask(taskID);
        }
        deletedSubtaskIDs.clear();

        service.insertCard(editedCard);
        viewCardCtrl.applyTag();
        viewCardCtrl.displayTasks();

    }

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

    public void updateDisplayedPresets(){
        presets.getChildren().clear();
        displayPresetList();
    }

    public CardColorPreset getAppliedPreset(){
        for(CardColorPreset preset : presetList){
            if(preset.isDefault()){
                return preset;
            }
        }
        return DEFAULT_PRESET;
    }

    /**
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Displays the pop-up (TagList) in order to choose and add a tag.
     */
    public void editTagPopup() {
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(TagListCtrl.class, "client", "windows", "tags", "TagList.fxml");
        TagListCtrl ctrl = loader.getKey();
        List<Tag> available = service.getTags();
        available.removeAll(newCard.getTags());
        ctrl.setAvailableTags(available);
        ctrl.setAppliedTags(newCard.getTags());
        ctrl.setEditCardCtrl(this);
        ctrl.setType("edit");
        Parent root = loader.getValue();
        Scene scene = new Scene(root);
        String title = "Add tag";
        helperMethods.popUp(scene, title);
    }

    /**
     * Setter for the applied tag list
     * @param appliedTags a list of tag objects
     */
    public void setAppliedTags(List<Tag> appliedTags) {
        newCard.setTags(appliedTags);
        appliedTagsVbox.getChildren().clear();
        for (int i = 0; i < appliedTags.size(); i++) {
            service.applyTag(appliedTags.get(i));
            var loader = new MyFXML(createInjector(new MainModules()))
                    .load(CustomTagCellCtrl.class, "client", "windows", "tags", "CustomTagCell.fxml");
            CustomTagCellCtrl ctrl = loader.getKey();
            ctrl.setTagObject(appliedTags.get(i), "viewTag");
            appliedTagsVbox.getChildren().add(loader.getValue());
        }

    }

    /**
     * Adds a task to the card and displays it based on user input
     */
    public void addTask() {
        if (!(addTaskField.getText() != null && !addTaskField.getText().isEmpty())) {
            return; //TODO: notify user in some way that you cannot add empty tasks
        }
        Task newTask = new Task();
        newTask.setCompleted(false);
        newTask.setTitle(addTaskField.getText());
        newCard.addSubTask(newTask);
        addTaskField.clear();
        displayTasks();
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
        newCard.getSubTasks().remove(task);
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
                service.dragAndDropDB(draggedTask, newIndex);

                // Update the UI
                taskVBox.getChildren().add(newIndex, draggedNode);
                displayTasks();
            }

            event.setDropCompleted(true);
            event.consume();
        });
    }

    public void setShownBoard(Board shownBoard){
        this.shownBoard = shownBoard;
    }

    public void setWorkspaceCtrl(WorkspaceCtrl workspaceCtrl){
        this.workspaceCtrl = workspaceCtrl;
    }

    public void setPresetList(List<CardColorPreset> presetList){
        this.presetList = presetList;
    }
}
