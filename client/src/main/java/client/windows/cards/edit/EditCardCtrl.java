package client.windows.cards.edit;

import client.MyFXML;
import client.modules.MainModules;
import client.serverUtils.TaskUtils;
import client.utils.DataFormatManager;
import client.utils.HelperMethods;
import client.windows.cards.view.ViewCardCtrl;
import client.windows.customize.cards.CardPresetListCtrl;
import client.windows.customize.cards.view.CustomCardPresetViewCellCtrl;
import client.windows.subtasks.SubtaskCellCtrl;
import client.windows.subtasks.SubtaskContainer;
import client.windows.tags.view.CustomTagCellCtrl;
import client.windows.tags.view.TagListCtrl;
import com.google.inject.Inject;
import commons.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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

    @FXML
    private VBox subtasks;

    @FXML
    private Button addTaskButton;

    @FXML
    private TextField addTaskField;

    @FXML
    private VBox appliedTagsVbox;

    @FXML
    private Pane appliedPreset;

    private Card newCard;
    private Card oldCard;

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
     * @param card
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

    public String getBoardKey() {
        return service.getBoardKey();
    }

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

        Label presetName = (Label) ((HBox) appliedPreset
                .getChildren().get(0)).getChildren().get(0);
        String name = presetName.getText();
        Rectangle backgroundColorRectangle = (Rectangle) ((HBox) appliedPreset
                .getChildren().get(0)).getChildren().get(1);
        Color backgroundColor = (Color) backgroundColorRectangle.getFill();
        Rectangle fontColorRectangle = (Rectangle) ((HBox) appliedPreset
                .getChildren().get(0)).getChildren().get(2);
        Color fontColor = (Color) fontColorRectangle.getFill();

        editedCard.setFontColor(fontColor.toString());
        editedCard.setBackgroundColor(backgroundColor.toString());
        editedCard.setPreset(new CardColorPreset(name, backgroundColor.toString(), fontColor.toString()));

        // Delete the tasks from the database
        for (long taskID : deletedSubtaskIDs) {
            taskUtils.deleteTask(taskID);
        }
        deletedSubtaskIDs.clear();

        service.insertCard(editedCard);
        viewCardCtrl.applyTag();
        viewCardCtrl.displayTasks();

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

    public void openPresetList() {
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(CardPresetListCtrl.class,
                        "client", "windows", "customize", "cards", "view", "CardPresetList.fxml");

        CardPresetListCtrl ctrl = loader.getKey();
        ctrl.setAppliedPreset(oldCard.getPreset());
        ctrl.setAvailablePresets(shownBoard.getPresetList());
        ctrl.setEditCardCtrl(this);
        ctrl.setType("edit");

        Parent root = loader.getValue();
        Scene scene = new Scene(root);

        String title = "Add Preset";
        helperMethods.popUp(scene, title);
    }

    public void setAppliedPreset(){
        if(appliedPreset.getChildren() != null){
            appliedPreset.getChildren().clear();
        }
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(CustomCardPresetViewCellCtrl.class,
                        "client", "windows", "customize", "cards", "view", "CustomCardPresetViewCell.fxml");

        HBox cell = (HBox) loader.getValue();
        Rectangle backgroundRectangle = (Rectangle) cell.lookup("#backgroundColor");
        backgroundRectangle.setFill(Color.web(oldCard.getBackgroundColor()));
        Rectangle fontRectangle = (Rectangle) cell.lookup("#fontColor");
        fontRectangle.setFill(Color.web(oldCard.getFontColor()));
        Button actionButton = (Button) cell.lookup("#actionButton");
        actionButton.setVisible(false);

        appliedPreset.getChildren().add(loader.getValue());
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

    public void setAppliedPreset(CardColorPreset preset){
        if(appliedPreset.getChildren() != null){
            appliedPreset.getChildren().clear();
        }
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(CustomCardPresetViewCellCtrl.class,
                        "client", "windows", "customize", "cards", "view", "CustomCardPresetViewCell.fxml");

        CustomCardPresetViewCellCtrl ctrl = loader.getKey();
        ctrl.setPresetObject(preset, "addFromList");

        HBox cell = (HBox) loader.getValue();
        Button actionButton = (Button) cell.lookup("#actionButton");
        actionButton.setVisible(false);

        appliedPreset.getChildren().add(loader.getValue());
    }

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

    public void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            addTask();
        }
    }

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

}
