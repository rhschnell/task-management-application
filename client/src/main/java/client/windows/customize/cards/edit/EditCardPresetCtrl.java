package client.windows.customize.cards.edit;

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
     * @param service The corresponding service
     */
    @Inject
    public EditCardPresetCtrl(EditCardPresetService service){
        this.service = service;
    }

    /**
     *  Setter for the preset
     * @param preset The new preset
     */
    public void setPreset(CardColorPreset preset){
        this.preset = preset;
        setPresetTitle(preset.getName());
        setPresetBackgroundColor(Color.web(preset.getBackgroundColor()));
        setPresetFontColor(Color.web(preset.getFontColor()));
    }

    /**
     * Setter for the font color of the preset
     * @param color The new font color of the preset
     */
    public void setPresetFontColor(Color color) {
        fontColor.setId(color.toString());
    }

    /**
     * Setter for the background color of the preset
     * @param color The new background color of the preset
     */
    public void setPresetBackgroundColor(Color color) {
        backgroundColor.setId(color.toString());
    }

    /**
     * Setter for the title of the preset
     * @param title The new title of the preset
     */
    public void setPresetTitle(String title) {
        presetTitle.setText(title);
    }

    /**
     * Method to set the ColorPicker for the font color in the edit preset popup
     */
    public void setFontColorPicker(){
        fontColor.setValue(Color.web(preset.getFontColor()));
    }

    /**
     * Method to set the ColorPicker for the background color in the edit preset popup
     */
    public void setBackgroundColorPicker(){
        backgroundColor.setValue(Color.web(preset.getBackgroundColor()));
    }

    /**
     * Method to save the just edited preset
     */
    public void save() {
        String newBackgroundColor = backgroundColor.getValue().toString();
        String newFontColor = fontColor.getValue().toString();
        String newTitle = presetTitle.getText();

        // TODO Move saving to the server to customize ctrl
        preset.setName(newTitle);
        preset.setBackgroundColor(newBackgroundColor);
        preset.setFontColor(newFontColor);

        customCardPresetCellCtrl.getCustomizeCtrl().updateDisplayedPresets();
        workspaceCtrl.refreshWorkspace(true);
        ((Stage)saveButton.getScene().getWindow()).close();
    }

    /**
     * Method to close the popup window when the cancel button is clicked
     */
    public void cancel() {
        ((Stage)cancelButton.getScene().getWindow()).close();
    }

    /**
     * Method to set the customCardPresetCellCtrl
     * @param customCardPresetCellCtrl The new customCardPresetCelLCtrl
     */
    public void setCustomCardPresetCellCtrl(CustomCardPresetCellCtrl customCardPresetCellCtrl){
        this.customCardPresetCellCtrl = customCardPresetCellCtrl;
    }

    /**
     * Sets the workspaceCtrl
     * @param workspaceCtrl The WorkspaceCtrl to be set
     */
    public void setWorkspaceCtrl(WorkspaceCtrl workspaceCtrl){
        this.workspaceCtrl = workspaceCtrl;
    }
}
