package client.windows.customize.cards;

import client.MyFXML;
import client.modules.MainModules;
import client.windows.cards.add.AddCardCtrl;
import client.windows.cards.edit.EditCardCtrl;
import client.windows.customize.cards.view.CustomCardPresetViewCellCtrl;
import com.google.inject.Inject;
import commons.Board;
import commons.CardColorPreset;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import static com.google.inject.Guice.createInjector;

public class CardPresetListCtrl {
    private AddCardCtrl addCardCtrl;
    private EditCardCtrl editCardCtrl;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private VBox appliedPresetBox;

    @FXML
    private VBox availablePresetBox;

    private List<CardColorPreset> availablePresets;
    private CardColorPreset appliedPreset;
    private String type;
    private Board shownBoard;

    @Inject
    public CardPresetListCtrl(AddCardCtrl addCardCtrl, EditCardCtrl editCardCtrl){
        this.addCardCtrl = addCardCtrl;
        this.editCardCtrl = editCardCtrl;
        availablePresetBox = new VBox();
        appliedPresetBox = new VBox();
        availablePresets = new ArrayList<>();
    }

    public void setAvailablePresets(List<CardColorPreset> presetList){
        this.availablePresets = new ArrayList<>();
        this.availablePresets.addAll(presetList);
        displayAvailablePresets();
    }

    public void displayAvailablePresets(){
        availablePresetBox.getChildren().clear();
        for(int i = 0; i < availablePresets.size(); i++){
            var loader = new MyFXML(createInjector(new MainModules()))
                    .load(CustomCardPresetViewCellCtrl.class,
                            "client", "windows", "customize", "cards", "view", "CustomCardPresetViewCell.fxml");
            CustomCardPresetViewCellCtrl ctrl = loader.getKey();
            ctrl.setPresetObject(availablePresets.get(i), "addFromList");
            ctrl.setCardPresetListCtrl(this);

            availablePresetBox.getChildren().add(loader.getValue());
        }
    }

    public void setAppliedPreset(CardColorPreset preset){
        appliedPreset = preset;
        displayAppliedPreset();
    }

    public void displayAppliedPreset(){
        appliedPresetBox.getChildren().clear();
        if(appliedPreset == null){
            return;
        }

        var loader = new MyFXML(createInjector(new MainModules()))
                .load(CustomCardPresetViewCellCtrl.class,
                        "client", "windows", "customize", "cards", "view", "CustomCardPresetViewCell.fxml");
        CustomCardPresetViewCellCtrl ctrl = loader.getKey();
        ctrl.setPresetObject(appliedPreset, "removeFromList");
        ctrl.setCardPresetListCtrl(this);

        appliedPresetBox.getChildren().add(loader.getValue());
    }

    public void refreshRemove(CardColorPreset preset){
        appliedPreset = null;
        availablePresets.add(preset);
        displayAppliedPreset();
        displayAvailablePresets();
    }

    public void refreshAdd(CardColorPreset preset){
        availablePresets.remove(preset);
        if(appliedPreset != null){
            availablePresets.add(appliedPreset);
        }
        appliedPreset = preset;
        displayAppliedPreset();
        displayAvailablePresets();
    }

    public void save() {
        if(type.equals("add"))
            addCardCtrl.setAppliedPreset(appliedPreset);
        if(type.equals("edit"))
            editCardCtrl.setAppliedPreset(appliedPreset);

        ((Stage)saveButton.getScene().getWindow()).close();
    }

    public void cancel() {
        ((Stage)cancelButton.getScene().getWindow()).close();
    }

    public void setAddCartCtrl(AddCardCtrl addCardCtrl) {
        this.addCardCtrl = addCardCtrl;
    }

    public void setEditCardCtrl(EditCardCtrl editCardCtrl) {
        this.editCardCtrl = editCardCtrl;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType(){
        return type;
    }
}
