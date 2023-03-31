package client.windows.subtasks;

import client.utils.HelperMethods;
import client.windows.subtasks.SubtaskContainer;
import com.google.inject.Inject;
import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class SubtaskCellCtrl {
    @FXML
    private CheckBox checkBox;

    @FXML
    private ImageView trashIcon;

    @FXML
    private TextField taskTitle;

    private SubtaskContainer subtaskContainer;

    private HelperMethods helperMethods;

    private Task task;

    @Inject
    public SubtaskCellCtrl(HelperMethods helperMethods) {
        this.helperMethods = helperMethods;
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
    }
}