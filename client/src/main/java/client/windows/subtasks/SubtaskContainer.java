package client.windows.subtasks;

import client.utils.DataFormatManager;
import com.google.inject.Inject;
import commons.Task;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public abstract class SubtaskContainer {
    private final DataFormatManager dataFormatManager;

    @Inject
    public SubtaskContainer(DataFormatManager dataFormatManager) {
        this.dataFormatManager = dataFormatManager;
    }

    public abstract void deleteSubtask(Task task);
    public abstract void displayTasks();

    /**
     * This method adds eventListeners related to dragging and dropping subtasks
     *
     * @param subtaskCell The loader containing the SubtaskCellCtrl and the JavaFX ui component
     */
    public void makeTaskDraggable(Pair<SubtaskCellCtrl, Parent> subtaskCell) {
        SubtaskCellCtrl ctrl = subtaskCell.getKey();
        Parent fxComponent = subtaskCell.getValue();

        Separator separator = new Separator();
        // Indicates to the user that the component is interactable
        fxComponent.setCursor(Cursor.HAND);

        setOnDragDetected(ctrl, fxComponent);
        setOnDragOver(fxComponent);
        setOnDragEntered(fxComponent, separator);
        setOnDragExited(fxComponent, separator);
        setOnDragDropped(fxComponent);
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
     * Sets the action for when a subtask is being entered while dragged
     *
     * @param fxComponent The JavaFX UI component
     * @param separator   The UI separator
     */
    private void setOnDragEntered(Parent fxComponent, Separator separator) {
        fxComponent.setOnDragEntered(event -> {
            if (event.getGestureSource() != fxComponent &&
                event.getDragboard().hasContent(dataFormatManager.getSubtaskFormat())) {
                int index =
                        ((VBox) fxComponent.getParent())
                                .getChildren().indexOf(fxComponent);
                ((VBox) fxComponent.getParent()).getChildren().add(index, separator);

            }
            event.consume();
        });

    }

    /**
     * Sets the action for when a dragged gesture exits the component
     *
     * @param fxComponent The JavaFX UI component
     * @param separator   The UI separator
     */
    private void setOnDragExited(Parent fxComponent, Separator separator) {
        fxComponent.setOnDragExited(event -> {
            {
                ((VBox) fxComponent.getParent()).getChildren().remove(separator);
                event.consume();
            }
        });


    }

    /**
     * Sets the action for when a dragged subtask is dropped
     * @param fxComponent The JavaFX UI component of a subtask
     */
    public abstract void setOnDragDropped(Parent fxComponent);

    /**
     * Returns the data format manager used by this container
     * @return This container's data format manager
     */
    public DataFormatManager getDataFormatManager() {
        return dataFormatManager;
    }
}
