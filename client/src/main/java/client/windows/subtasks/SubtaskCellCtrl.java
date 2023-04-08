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
    private TextField taskTitle;

    private SubtaskContainer subtaskContainer;

    private HelperMethods helperMethods;

    private Task task;

    private EditCardCtrl editCardCtrl;

    /**
     * Constructor for the SubtaskCellCtrl
     * @param helperMethods a helperMethods instance
     */
    @Inject
    public SubtaskCellCtrl(HelperMethods helperMethods) {
        this.helperMethods = helperMethods;
    }

    /**
     * Setter for the editCardCtrl
     * @param editCardCtrl the editCardCtrl
     */
    public void setEditCardCtrl(EditCardCtrl editCardCtrl) {
        this.editCardCtrl = editCardCtrl;
    }

    /**
     * Setter for the SubtaskContainer
     * @param subtaskContainer the SubtaskContainer
     */
    public void setSubtaskContainer(SubtaskContainer subtaskContainer) {
        this.subtaskContainer = subtaskContainer;
    }

    /**
     * Updates the subtask with new information
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
     * @param event the event passed (only ENTER is considered)
     */
    public void onTaskTitleAction(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            editTaskTitle();
        }
    }

    /**
     * Method to edit the task title based on user input
     */
    public void editTaskTitle() {
        if (taskTitle.getText() != null && !taskTitle.getText().isEmpty()) {
            task.setTitle(taskTitle.getText());
        }
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
     * Gets the task associated to this controller
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