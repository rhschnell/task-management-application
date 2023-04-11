package client.windows.customize.cards;

import client.MyFXML;
import client.modules.MainModules;
import client.serverUtils.BoardUtils;
import client.serverUtils.CardColorPresetUtils;
import client.utils.HelperMethods;
import client.windows.cards.add.AddCardCtrl;
import client.windows.customize.CustomizeCtrl;
import client.windows.customize.cards.edit.EditCardPresetCtrl;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.CardColorPreset;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

import static com.google.inject.Guice.createInjector;

public class CustomCardPresetCellCtrl {
    private HelperMethods helperMethods;
    private CardColorPresetUtils server;
    private CustomizeCtrl customizeCtrl;
    private WorkspaceCtrl workspaceCtrl;
    private AddCardCtrl addCardCtrl;

    @FXML
    private Label presetTitle;

    @FXML
    private Rectangle cardBackgroundColor;

    @FXML
    private Rectangle cardFontColor;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private RadioButton defaultBox;

    private CardColorPreset preset;
    private Board shownBoard;
    private List<CardColorPreset> presetList;
    private BoardUtils boardUtils;

    /**
     * Constructor for the CustomCardPresetCellCtrl
     * @param server The CardColorPresetUtils server
     * @param helperMethods Instance of HelperMethods
     */
    @Inject
    public CustomCardPresetCellCtrl(CardColorPresetUtils server, HelperMethods helperMethods,BoardUtils boardUtils){
        this.server = server;
        this.helperMethods = helperMethods;
        this.boardUtils=boardUtils;
    }

    /***
     * Method to set the color preset in the overview
     * @param preset The preset to be set
     */
    public void setPresetObject(CardColorPreset preset){
        this.preset = preset;
        presetTitle.setText(preset.getName());

        cardBackgroundColor.setFill(Color.web(preset.getBackgroundColor()));
        cardFontColor.setFill(Color.web(preset.getFontColor()));

        defaultBox.setSelected(preset.isDefault());
        if (preset.isDefault()) {
            deleteButton.setVisible(false);
        }
        defaultBox.setOnAction(event -> {
            preset.setDefault(defaultBox.isSelected());
            CardColorPreset presetPastDefault = null;
            if(preset.isDefault()){
                for(CardColorPreset p : presetList){
                    if(p != preset){
                        if(p.isDefault())
                            presetPastDefault=p;
                        p.setDefault(false);
                    }
                }
                if(!customizeCtrl.getBoard().getPresetList().contains(preset)) {
                    customizeCtrl.getBoard().addPreset(preset);
                    boardUtils.insertBoard(customizeCtrl.getBoard());
                    customizeCtrl.refreshBoard();
                    preset.setId(customizeCtrl.getBoard().getPresetList().get(customizeCtrl.getBoard().getPresetList().size()-1).getId());
                }
                for(CardList cardList : customizeCtrl.getBoard().getCardLists()){
                    for(Card card : cardList.getCards()){
                        System.out.println(card.getPresets());
                        if(card.getPresets().get(0).getBackgroundColor().equals(presetPastDefault.getBackgroundColor()) && card.getPresets().get(0).getFontColor().equals(presetPastDefault.getFontColor())) {
                            card.setPresets(new ArrayList<>());
                            card.setPreset(preset);
                        }
                    }
                }
            }
            customizeCtrl.updateDisplayedPresets();
        });
    }

    /**
     * Delete the preset from the list of presets
     */
    public void delete() {
        presetList.remove(preset);
        customizeCtrl.updateDisplayedPresets();
    }

    /**
     * This method allows the user to edit the custom-made preset
     */
    public void edit() {
        var loader =  new MyFXML(createInjector(new MainModules()))
                .load(EditCardPresetCtrl.class,
                        "client", "windows", "customize", "cards", "edit", "EditCardPreset.fxml");

        Parent root = loader.getValue();
        Scene scene = new Scene(root);

        EditCardPresetCtrl controller = loader.getKey();
        controller.setPreset(preset);
        controller.setFontColorPicker();
        controller.setBackgroundColorPicker();
        controller.setCustomCardPresetCellCtrl(this);
        controller.setWorkspaceCtrl(workspaceCtrl);

        String title = "Edit Preset";
        helperMethods.popUp(scene, title);
    }

    /**
     * Sets the customizeCtrl
     * @param customizeCtrl the CustomizeCtrl to be set
     */
    public void setCustomizeCtrl(CustomizeCtrl customizeCtrl) {
        this.customizeCtrl = customizeCtrl;
    }

    /**
     * Gets the customizeCtrl
     * @return The customizeCtrl
     */
    public CustomizeCtrl getCustomizeCtrl() {
        return this.customizeCtrl;
    }

    /**
     * Sets the preset list of color presets
     * @param presetList The list of presets to be set
     */
    public void setPresetList(List<CardColorPreset> presetList){
        this.presetList = presetList;
    }
    /**
     * Sets the board
     * @param shownBoard The board to be set
     */
    public void setBoard(Board shownBoard) {
        this.shownBoard = shownBoard;
    }

    /**
     * Sets the workspaceCtrl
     * @param workspaceCtrl the WorkspaceCtrl to be set
     */
    public void setWorkspaceCtrl(WorkspaceCtrl workspaceCtrl){
        this.workspaceCtrl = workspaceCtrl;
    }
}
