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

    /**
     * Constructor for the CardPresetListCtrl
     * @param addCardCtrl Instance of addCardCtrl
     * @param editCardCtrl Instance of editCardCtrl
     */
    @Inject
    public CardPresetListCtrl(AddCardCtrl addCardCtrl, EditCardCtrl editCardCtrl){
        this.addCardCtrl = addCardCtrl;
        this.editCardCtrl = editCardCtrl;
        availablePresetBox = new VBox();
        appliedPresetBox = new VBox();
        availablePresets = new ArrayList<>();
    }

    /**
     * This method sets the available presets
     * @param presetList The presets that are currently available on the board
     */
    public void setAvailablePresets(List<CardColorPreset> presetList){
        this.availablePresets = new ArrayList<>();
        this.availablePresets.addAll(presetList);
        displayAvailablePresets();
    }

    /**
     * This method displays the currently available presets
     */
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

    /**
     * This method sets the applied preset
     * @param preset The preset which is the applied preset
     */
    public void setAppliedPreset(CardColorPreset preset){
        appliedPreset = preset;
        displayAppliedPreset();
    }

    /**
     * This method displays the applied preset
     */
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

    /**
     * This method adds the applied preset to the available presets and sets the applied preset to null
     * @param preset The currently applied preset which needs to be moved
     */
    public void refreshRemove(CardColorPreset preset){
        appliedPreset = null;
        availablePresets.add(preset);
        displayAppliedPreset();
        displayAvailablePresets();
    }

    /**
     * This method sets the applied preset to be the given preset
     * @param preset The new applied preset
     */
    public void refreshAdd(CardColorPreset preset){
        availablePresets.remove(preset);
        if(appliedPreset != null){
            availablePresets.add(appliedPreset);
        }
        appliedPreset = preset;
        displayAppliedPreset();
        displayAvailablePresets();
    }

    /**
     * This method closes the popup window and sets the new applied preset
     */
    public void save() {
        if(type.equals("add"))
            addCardCtrl.setAppliedPreset(appliedPreset);
        if(type.equals("edit"))
            editCardCtrl.setAppliedPreset(appliedPreset);

        ((Stage)saveButton.getScene().getWindow()).close();
    }

    /**
     * This method cancels viewing the presets and closes the window
     */
    public void cancel() {
        ((Stage)cancelButton.getScene().getWindow()).close();
    }

    /**
     * Sets the addCardCtrl
     * @param addCardCtrl The addCardCtrl to be set
     */
    public void setAddCartCtrl(AddCardCtrl addCardCtrl) {
        this.addCardCtrl = addCardCtrl;
    }

    /**
     * Sets the editCardCtrl
     * @param editCardCtrl The editCardCtrl to be set
     */
    public void setEditCardCtrl(EditCardCtrl editCardCtrl) {
        this.editCardCtrl = editCardCtrl;
    }

    /**
     * Sets the type
     * @param type The type to be set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the type
     * @return The type
     */
    public String getType(){
        return type;
    }
}
