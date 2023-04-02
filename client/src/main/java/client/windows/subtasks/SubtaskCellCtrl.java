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
    @Inject
    public SubtaskCellCtrl(HelperMethods helperMethods) {
        this.helperMethods = helperMethods;
    }

    public void setEditCardCtrl(EditCardCtrl editCardCtrl) {
        this.editCardCtrl = editCardCtrl;
    }

    public void setSubtaskContainer(SubtaskContainer subtaskContainer) {
        this.subtaskContainer = subtaskContainer;
    }

    public void updateItem(Task task) {
        this.task = task;
        taskTitle.setText(task.getTitle());
        checkBox.setSelected(task.isCompleted());
    }

    public void onCheck() {
        if (task.isCompleted() != checkBox.isSelected()) {
            task.setCompleted(!task.isCompleted());
        }
    }


    public void onTaskTitleAction(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            editTaskTitle();
        }
    }

    public void editTaskTitle() {
        if (taskTitle.getText() != null && !taskTitle.getText().isEmpty()) {
            task.setTitle(taskTitle.getText());
        }
    }

    public void delete() {
        subtaskContainer.deleteSubtask(task);
    }

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