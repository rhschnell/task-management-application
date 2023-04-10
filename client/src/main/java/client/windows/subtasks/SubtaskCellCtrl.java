package client.windows.subtasks;

import client.utils.HelperMethods;
import client.windows.cards.edit.EditCardCtrl;
import com.google.inject.Inject;
import commons.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;


public class SubtaskCellCtrl implements Initializable {
    @FXML
    private CheckBox checkBox;

    @FXML
    private ImageView trashIcon;

    @FXML
    private ImageView dragAndDropIcon;
    @FXML
    private ImageView saveIcon;

    @FXML
    private TextField taskTitle;

    private SubtaskContainer subtaskContainer;

    private HelperMethods helperMethods;

    private Task task;

    private EditCardCtrl editCardCtrl;
    private boolean savedIconIsShown;

    /**
     * Constructor for the SubtaskCellCtrl
     *
     * @param helperMethods a helperMethods instance
     */
    @Inject
    public SubtaskCellCtrl(HelperMethods helperMethods) {
        this.helperMethods = helperMethods;
        this.savedIconIsShown = false;
    }

    /**
     * Setter for the editCardCtrl
     *
     * @param editCardCtrl the editCardCtrl
     */
    public void setEditCardCtrl(EditCardCtrl editCardCtrl) {
        this.editCardCtrl = editCardCtrl;
    }

    /**
     * Setter for the SubtaskContainer
     *
     * @param subtaskContainer the SubtaskContainer
     */
    public void setSubtaskContainer(SubtaskContainer subtaskContainer) {
        this.subtaskContainer = subtaskContainer;
    }

    /**
     * Updates the subtask with new information
     *
     * @param task the task to be used for updating the controller
     */
    public void updateItem(Task task) {
        this.task = task;
        taskTitle.setText(task.getTitle());
        checkBox.setSelected(task.isCompleted());
    }

    /**
     * Updates the completion status of the task based on the state of the checkbox
     */
    public void onCheck() {
        if (task.isCompleted() != checkBox.isSelected()) {
            task.setCompleted(!task.isCompleted());
        }
    }

    /**
     * Edits the task title on ENTER
     *
     * @param event the event passed (only ENTER is considered)
     */
    public void onTaskTitleAction(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            saveChanges();

        } else if (!savedIconIsShown){
            showSaveIcon();
        }
    }

    /**
     * Method to edit the task title based on user input
     */
    public void editTaskTitle() {
        String title = helperMethods.getInputValidator().stripWhitespace(taskTitle.getText());
        if (!helperMethods.validateInputAndShowPopup(title)) return;
        task.setTitle(title);
        // Also display this whitespace stripping to the user
        taskTitle.setText(title);
    }

    /**
     * Deletes the subtask
     */
    public void delete() {
        subtaskContainer.deleteSubtask(task);
    }

    /**
     * Disables UI components that allow editing the task
     */
    public void disableEdit() {
        checkBox.setDisable(true);
        taskTitle.setDisable(true);
        trashIcon.setDisable(true);
        trashIcon.setVisible(false);
        dragAndDropIcon.setVisible(false);
        dragAndDropIcon.setDisable(true);
    }


    /**
     * Hides the save icon bny deleting it from the UI component
     */
    private void hideSaveIcon() {
        HBox hBox = (HBox) checkBox.getParent();
        hBox.getChildren().remove(2);
        savedIconIsShown = false;
    }

    /**
     * Dynamically adds a save icon to the UI component
     */
    private void showSaveIcon(){
        HBox hBox = (HBox) checkBox.getParent();

        ImageView saveIcon = new ImageView("/client/icons/save.png");

        saveIcon.setFitWidth(21);
        saveIcon.setFitHeight(21);
        saveIcon.setCursor(Cursor.HAND);
        saveIcon.setOnMouseClicked(event -> {
            saveChanges();
        });
        hBox.getChildren().add(2, saveIcon);
        savedIconIsShown = true;
    }

    /**
     * Saves the changes made to this task by saving the title and hiding the save button
     */
    private void saveChanges() {
        editTaskTitle();
        hideSaveIcon();
        dragAndDropIcon.requestFocus(); // Move the focus to another object to indicate that the
        // editing has been successful
    }


    /**
     * Gets the task associated to this controller
     *
     * @return The task associated to this controller
     */
    public Task getTask() {
        return task;
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
        trashIcon.setCursor(Cursor.HAND);
    }

}