package client.windows.customize.cards.edit;

import client.utils.ErrorDialogEntry;
import client.utils.HelperMethods;
import client.windows.customize.cards.CustomCardPresetCellCtrl;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Inject;
import commons.CardColorPreset;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class EditCardPresetCtrl {
    private final EditCardPresetService service;
    private final HelperMethods helperMethods;
    private CustomCardPresetCellCtrl customCardPresetCellCtrl;
    private CardColorPreset preset;
    private WorkspaceCtrl workspaceCtrl;
    @FXML
    private TextField presetTitle;
    @FXML
    private ColorPicker backgroundColor;
    @FXML
    private ColorPicker fontColor;
    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;

    /**
     * Constructor for the EditCardPresetCtrl
     *
     * @param service       The corresponding service
     * @param helperMethods The correspondng helper methods instance
     */
    @Inject
    public EditCardPresetCtrl(EditCardPresetService service, HelperMethods helperMethods) {
        this.service = service;
        this.helperMethods = helperMethods;
    }

    /**
     * Setter for the preset
     *
     * @param preset The new preset
     */
    public void setPreset(CardColorPreset preset) {
        this.preset = preset;
        setPresetTitle(preset.getName());
        setPresetBackgroundColor(Color.web(preset.getBackgroundColor()));
        setPresetFontColor(Color.web(preset.getFontColor()));
    }

    /**
     * Setter for the font color of the preset
     *
     * @param color The new font color of the preset
     */
    public void setPresetFontColor(Color color) {
        fontColor.setId(color.toString());
    }

    /**
     * Setter for the background color of the preset
     *
     * @param color The new background color of the preset
     */
    public void setPresetBackgroundColor(Color color) {
        backgroundColor.setId(color.toString());
    }

    /**
     * Setter for the title of the preset
     *
     * @param title The new title of the preset
     */
    public void setPresetTitle(String title) {
        presetTitle.setText(title);
    }

    /**
     * Method to set the customCardPresetCellCtrl
     *
     * @param customCardPresetCellCtrl The new customCardPresetCelLCtrl
     */
    public void setCustomCardPresetCellCtrl(CustomCardPresetCellCtrl customCardPresetCellCtrl) {
        this.customCardPresetCellCtrl = customCardPresetCellCtrl;
    }

    /**
     * Sets the workspaceCtrl
     *
     * @param workspaceCtrl The WorkspaceCtrl to be set
     */
    public void setWorkspaceCtrl(WorkspaceCtrl workspaceCtrl) {
        this.workspaceCtrl = workspaceCtrl;
    }

    /**
     * Method to set the ColorPicker for the font color in the edit preset popup
     */
    public void setFontColorPicker() {
        fontColor.setValue(Color.web(preset.getFontColor()));
    }

    /**
     * Method to set the ColorPicker for the background color in the edit preset popup
     */
    public void setBackgroundColorPicker() {
        backgroundColor.setValue(Color.web(preset.getBackgroundColor()));
    }

    /**
     * Method to save the just edited preset
     */
    public void save() {
        String newBackgroundColor = backgroundColor.getValue().toString();
        String newFontColor = fontColor.getValue().toString();
        String newTitle = helperMethods.getInputValidator().stripWhitespace(presetTitle.getText());

        if (!checkAndHandleInput(newTitle)) {
            presetTitle.requestFocus();
            return;
        }

        preset.setName(newTitle);
        preset.setBackgroundColor(newBackgroundColor);
        preset.setFontColor(newFontColor);

        customCardPresetCellCtrl.getCustomizeCtrl().updateDisplayedPresets();
        ((Stage) saveButton.getScene().getWindow()).close();
    }

    /**
     * Checks the user input and shows error messages accordingly
     *
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
     * Method to close the popup window when the cancel button is clicked
     */
    public void cancel() {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }
}
