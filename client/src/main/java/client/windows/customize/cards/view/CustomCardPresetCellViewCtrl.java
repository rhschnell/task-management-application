package client.windows.customize.cards.view;

import client.windows.cards.add.AddCardCtrl;
import client.windows.cards.edit.EditCardCtrl;
import client.windows.cards.view.ViewCardCtrl;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Inject;
import commons.CardColorPreset;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class CustomCardPresetCellViewCtrl {
    private WorkspaceCtrl workspaceCtrl;
    private AddCardCtrl addCardCtrl;
    private EditCardCtrl editCardCtrl;
    private ViewCardCtrl viewCardCtrl;

    @FXML
    private Label presetTitle;

    @FXML
    private Rectangle fontColor;

    @FXML
    private Rectangle backgroundColor;

    @FXML
    private CheckBox defaultBox;

    private CardColorPreset preset;
    private CardColorPreset appliedPreset;
    private List<CardColorPreset> presetList;

    /**
     * Empty constructor for the CustomCardPresetCellViewCtrl
     */
    @Inject
    public CustomCardPresetCellViewCtrl() {

    }

    /**
     * Sets the applied preset
     *
     * @param appliedPreset The color preset that is the applied one
     */
    public void setAppliedPreset(CardColorPreset appliedPreset) {
        this.appliedPreset = appliedPreset;
    }

    /**
     * Sets the workspaceCtrl
     *
     * @param workspaceCtrl the WorkspaceCtrl to be set
     */
    public void setWorkspaceCtrl(WorkspaceCtrl workspaceCtrl) {
        this.workspaceCtrl = workspaceCtrl;
    }

    /**
     * Sets the addCardCtrl
     *
     * @param addCardCtrl the AddCardCtrl to be set
     */
    public void setAddCardCtrl(AddCardCtrl addCardCtrl) {
        this.addCardCtrl = addCardCtrl;
    }

    /**
     * Sets the editCardCtrl
     *
     * @param editCardCtrl the EditCardCtrl to be set
     */
    public void setEditCardCtrl(EditCardCtrl editCardCtrl) {
        this.editCardCtrl = editCardCtrl;
    }

    /**
     * Sets the preset list of color presets
     *
     * @param presetList The list of presets
     */
    public void setPresetList(List<CardColorPreset> presetList) {
        this.presetList = presetList;
    }

    /**
     * Sets the viewCardCtrl
     *
     * @param viewCardCtrl The ViewCardCtrl to be set
     */
    public void setViewCardCtrl(ViewCardCtrl viewCardCtrl) {
        this.viewCardCtrl = viewCardCtrl;
    }

    /**
     * This method sets the preset object and handles button clicks
     *
     * @param preset The preset that will be shown
     * @param caller The method from where it was called
     */
    public void setPresetObject(CardColorPreset preset, String caller) {
        this.preset = preset;
        presetTitle.setText(preset.getName());
        fontColor.setFill(Color.web(preset.getFontColor()));
        backgroundColor.setFill(Color.web(preset.getBackgroundColor()));

        if (caller.equals("ViewCardCtrl")) {
            this.defaultBox.setVisible(false);
        }

        defaultBox.setSelected(preset.isDefault());
        defaultBox.setOnAction(event -> {
            preset.setDefault(defaultBox.isSelected());
            if (preset.isDefault()) {
                for (CardColorPreset p : presetList) {
                    if (p != preset) {
                        p.setDefault(false);
                    }
                }
            }

            if (caller.equals("AddCardCtrl")) {
                addCardCtrl.updateDisplayedPresets();
            } else if (caller.equals("EditCardCtrl")) {
                editCardCtrl.updateDisplayedPresets();
            }
        });
    }
}
