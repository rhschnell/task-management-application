package client.windows.customize.cards.add;

import client.windows.customize.CustomizeCtrl;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import commons.CardColorPreset;

import java.util.ArrayList;
import java.util.List;

public class AddCardPresetCtrl {
    private final AddCardPresetService service;
    private WorkspaceCtrl workspaceCtrl;
    private CustomizeCtrl customizeCtrl;

    @FXML
    private TextField presetTitle;

    @FXML
    private ColorPicker backgroundColor;

    @FXML
    private ColorPicker fontColor;

    @FXML
    private Button addPresetButton;

    @FXML
    private Button cancelButton;

    /**
     * Constructor for the AddCardPresetCtrl
     * @param service The corresponding service
     * @param customizeCtrl Instance of CustomizeCtrl
     */
    @Inject
    public AddCardPresetCtrl(AddCardPresetService service, CustomizeCtrl customizeCtrl){
        this.service = service;
        this.customizeCtrl = customizeCtrl;
    }

    /**
     * This method saves the preset that the user made and closes the window
     */
    public void save() {
        ((Stage)addPresetButton.getScene().getWindow()).close();
        CardColorPreset preset = new CardColorPreset(
                presetTitle.getText(),
                backgroundColor.getValue().toString(),
                fontColor.getValue().toString());
        customizeCtrl.addPreset(preset);
        customizeCtrl.updateDisplayedPresets();
    }

    /**
     * This method cancels adding the created preset to the board
     */
    public void cancel() {
        ((Stage)cancelButton.getScene().getWindow()).close();
    }

    /**
     * Sets the customizeCtrl
     * @param customizeCtrl The customizeCtrl to be set
     */
    public void setCustomizeCtrl(CustomizeCtrl customizeCtrl){
        this.customizeCtrl = customizeCtrl;
    }

    /**
     * Sets the workspaceCtrl
     * @param workspaceCtrl The workspaceCtrl to be set
     */
    public void setWorkspaceCtrl(WorkspaceCtrl workspaceCtrl){
        this.workspaceCtrl = workspaceCtrl;
    }
}
