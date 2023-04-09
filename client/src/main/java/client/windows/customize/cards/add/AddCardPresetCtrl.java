package client.windows.customize.cards.add;

import client.utils.ErrorDialogEntry;
import client.utils.HelperMethods;
import client.windows.customize.CustomizeCtrl;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Inject;
import commons.CardColorPreset;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddCardPresetCtrl {
    private final AddCardPresetService service;
    private final HelperMethods helperMethods;
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
     * @param helperMethods The correspondng helper methods instance
     */
    @Inject
    public AddCardPresetCtrl(AddCardPresetService service,
                             CustomizeCtrl customizeCtrl,
                             HelperMethods helperMethods){
        this.service = service;
        this.customizeCtrl = customizeCtrl;
        this.helperMethods = helperMethods;
    }

    /**
     * This method saves the preset that the user made and closes the window
     */
    public void save() {
        String title = helperMethods.getInputValidator().stripWhitespace(presetTitle.getText());

        if (!checkAndHandleInput(title)) {
            presetTitle.requestFocus();
            return;
        }

        ((Stage)addPresetButton.getScene().getWindow()).close();
        CardColorPreset preset = new CardColorPreset(
                title,
                backgroundColor.getValue().toString(),
                fontColor.getValue().toString());
        customizeCtrl.addPreset(preset);
        customizeCtrl.updateDisplayedPresets();
    }

    /**
     * Checks the user input and shows error messages accordingly
     * @param title The title to check
     */
    private boolean checkAndHandleInput(String title) {
        if (!helperMethods.getInputValidator().isValidInputNonEmpty(title)) {
            helperMethods.showErrorDialog(new ErrorDialogEntry(
                    "Error!",
                    "Your preset name cannot be empty"));
            return false;
        }
        if (!helperMethods.getInputValidator().isValidInputLength(title)) {
            helperMethods.showErrorDialog(new ErrorDialogEntry(
                    "Error!",
                    "Your preset name cannot be longer than " + HelperMethods.getMaxInputLength()));
            return false;
        }
        return true;
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
