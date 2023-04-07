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

    @Inject
    public AddCardPresetCtrl(AddCardPresetService service, CustomizeCtrl customizeCtrl){
        this.service = service;
        this.customizeCtrl = customizeCtrl;
    }

    public void save() {
        ((Stage)addPresetButton.getScene().getWindow()).close();
        CardColorPreset preset = new CardColorPreset(
                presetTitle.getText(),
                backgroundColor.getValue().toString(),
                fontColor.getValue().toString());

        Board shownBoard = customizeCtrl.getBoard();
        if (shownBoard == null){
            return;
        }
        shownBoard.addPreset(preset);
        service.insertBoard(shownBoard);
        customizeCtrl.updateDisplayedPresets();
    }

    public void cancel() {
        ((Stage)cancelButton.getScene().getWindow()).close();
    }

    public void setCustomizeCtrl(CustomizeCtrl customizeCtrl){
        this.customizeCtrl = customizeCtrl;
    }

    public void setWorkspaceCtrl(WorkspaceCtrl workspaceCtrl){
        this.workspaceCtrl = workspaceCtrl;
    }
}
