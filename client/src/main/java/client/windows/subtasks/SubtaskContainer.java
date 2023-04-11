package client.windows.subtasks;

import client.utils.DataFormatManager;
import client.utils.ErrorDialogEntry;
import client.utils.HelperMethods;
import com.google.inject.Inject;
import commons.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public abstract class SubtaskContainer implements Initializable {
    private final DataFormatManager dataFormatManager;
    protected HelperMethods helperMethods;
    @FXML
    private Label errorMessage;

    /**
     * Constructor for the SubtaskContainer
     *
     * @param dataFormatManager a DataFormatManager instance
     * @param helperMethods     Injected instance of HelperMethods
     */
    @Inject
    public SubtaskContainer(DataFormatManager dataFormatManager, HelperMethods helperMethods) {
        this.dataFormatManager = dataFormatManager;
        this.helperMethods = helperMethods;
    }

    /**
     * Returns the data format manager used by this container
     *
     * @return This container's data format manager
     */
    public DataFormatManager getDataFormatManager() {
        return dataFormatManager;
    }

    /**
     * Sets the action for when a subtask is being dragged over another subtask
     *
     * @param fxComponent The JavaFX UI component
     */
    private void setOnDragOver(Parent fxComponent) {
        fxComponent.setOnDragOver(event -> {
            if (event.getGestureSource() != fxComponent &&
                    event.getDragboard().hasContent(dataFormatManager.getSubtaskFormat())) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
    }

    /**
     * Deletes the subtask passed to the method
     *
     * @param task the subtask you want to delete
     */
    public abstract void deleteSubtask(Task task);

    /**
     * Displays the tasks in the UI
     */
    public abstract void displayTasks();

    /**
     * This method adds eventListeners related to dragging and dropping subtasks
     *
     * @param subtaskCell The loader containing the SubtaskCellCtrl and the JavaFX ui component
     * @param taskVBox    The VBox holding the subtasks
     */
    public void makeTaskDraggable(Pair<SubtaskCellCtrl, Parent> subtaskCell, VBox taskVBox) {
        SubtaskCellCtrl ctrl = subtaskCell.getKey();
        Parent fxComponent = subtaskCell.getValue();

        Separator separator = new Separator();
        // Indicates to the user that the component is interactable
        fxComponent.setCursor(Cursor.HAND);

        setOnDragDetected(ctrl, fxComponent);
        setOnDragOver(fxComponent);
        setOnDragEntered(fxComponent, taskVBox, separator);
        setOnDragExited(fxComponent, taskVBox, separator);
        setOnDragDropped(fxComponent, taskVBox);
    }

    /**
     * Sets the action for when drag is detected on a subtask
     *
     * @param ctrl        The controller for the subtask
     * @param fxComponent The JavaFX UI component
     */
    private void setOnDragDetected(SubtaskCellCtrl ctrl, Parent fxComponent) {
        fxComponent.setOnDragDetected(event -> {
            Dragboard db = fxComponent.startDragAndDrop(TransferMode.MOVE);
            Image dragImage = new Image("client/icons/DragTask.png");
            ImageView dragView = new ImageView(dragImage);
            db.setDragView(dragView.getImage(), -20, -10);

            /* Put data on a dragboard */
            ClipboardContent content = new ClipboardContent();
            content.put(dataFormatManager.getSubtaskFormat(), ctrl.getTask());
            db.setContent(content);
            event.consume();
        });
    }

    /**
     * Sets the action for when a subtask is being entered while dragged
     *
     * @param fxComponent The JavaFX UI component
     * @param taskVBox    The VBox holding the subtasks
     * @param separator   The UI separator
     */
    private void setOnDragEntered(Parent fxComponent, VBox taskVBox, Separator separator) {
        fxComponent.setOnDragEntered(event -> {
            if (event.getGestureSource() != fxComponent &&
                    event.getDragboard().hasContent(dataFormatManager.getSubtaskFormat())) {
                int index = taskVBox.getChildren().indexOf(fxComponent);
                taskVBox.getChildren().add(index, separator);

            }
            event.consume();
        });

    }

    /**
     * Sets the action for when a dragged gesture exits the component
     *
     * @param fxComponent The JavaFX UI component
     * @param taskVBox    The VBox holding the subtasks
     * @param separator   The UI separator
     */
    private void setOnDragExited(Parent fxComponent, VBox taskVBox, Separator separator) {
        fxComponent.setOnDragExited(event -> {
            {
                taskVBox.getChildren().remove(separator);
                event.consume();
            }
        });
    }

    /**
     * Sets the action for when a dragged subtask is dropped
     *
     * @param fxComponent The JavaFX UI component of a subtask
     * @param taskVBox    The VBox holding the tasks
     */
    public abstract void setOnDragDropped(Parent fxComponent, VBox taskVBox);

    /**
     * Shows the user an error in the dedicated place on the window.
     * <p>
     * This method is moved in this class because the implementing classes AddCardCtrl and
     * EditCardCtrl share the same UI, and therefore share the same functionality of showing errors
     *
     * @param errorDialogEntry The error dialog containing the error title and message
     */
    public void showErrorMessage(ErrorDialogEntry errorDialogEntry) {
        this.errorMessage.requestFocus();
        this.errorMessage.setVisible(true);
        this.errorMessage.setText(errorDialogEntry.getMessage());
    }

    /**
     * Hides the error message label
     */
    public void hideErrorMessageLabel() {
        this.errorMessage.setVisible(false);
    }

    /**
     * Checks the user input and shows error messages accordingly
     *
     * @param title The title to check
     */
    protected boolean checkAndHandleUserInput(String title) {
        if (!helperMethods.getInputValidator().isValidInputNonEmpty(title)) {
            showErrorMessage(new ErrorDialogEntry("Error!", "The card title cannot be empty"));
            return true;
        }
        if (!helperMethods.getInputValidator().isValidInputLength(title)) {
            showErrorMessage(new ErrorDialogEntry("Error!", "The card title cannot be longer " +
                    "than " + HelperMethods.getMaxInputLength()));
            return true;
        }
        return false;
    }


}
