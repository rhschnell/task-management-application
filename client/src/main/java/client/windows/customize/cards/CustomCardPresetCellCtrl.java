package client.windows.customize.cards;

import client.MyFXML;
import client.modules.MainModules;
import client.serverUtils.CardColorPresetUtils;
import client.serverUtils.CardUtils;
import client.utils.HelperMethods;
import client.windows.customize.CustomizeCtrl;
import client.windows.customize.cards.edit.EditCardPresetCtrl;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.CardColorPreset;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import static com.google.inject.Guice.createInjector;

public class CustomCardPresetCellCtrl {
    private CardColorPresetUtils server;
    private CardUtils cardUtils;
    private CustomizeCtrl customizeCtrl;

    @FXML
    private Label presetTitle;

    @FXML
    private ColorPicker cardBackgroundColor;

    @FXML
    private ColorPicker cardFontColor;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private CheckBox defaultBox;

    private CardColorPreset preset;
    private Board shownBoard;

    /**
     * Constructor for the CustomCardPresetCellCtrl
     * @param server The CardColorPresetUtils server
     * @param cardUtils The CardUtils server
     */
    @Inject
    public CustomCardPresetCellCtrl(CardColorPresetUtils server, CardUtils cardUtils){
        this.server = server;
        this.cardUtils = cardUtils;
    }

    /***
     * Method to set the color preset in the overview
     * @param preset The preset to be set
     */
    public void setPresetObject(CardColorPreset preset){
        this.preset = preset;
        presetTitle.setText(preset.getName());

        cardBackgroundColor.setValue(Color.web(preset.getBackgroundColor()));
        cardFontColor.setValue(Color.web(preset.getFontColor()));

        cardBackgroundColor.setOnAction(event -> {
            preset.setBackgroundColor(cardBackgroundColor.getValue().toString());
            server.insertPreset(preset);
            customizeCtrl.updateDisplayedPresets();
        });

        cardFontColor.setOnAction(event -> {
            preset.setFontColor(cardFontColor.getValue().toString());
            server.insertPreset(preset);
            customizeCtrl.updateDisplayedPresets();
        });

        defaultBox.setSelected(preset.isDefault());
        defaultBox.setOnAction(event -> {
            preset.setDefault(defaultBox.isSelected());
            if(preset.isDefault()){
                for(CardColorPreset p : customizeCtrl.getBoard().getPresetList()){
                    if(p != preset){
                        p.setDefault(false);
                        server.insertPreset(p);
                    }
                }

                for(CardList cardList : customizeCtrl.getBoard().getCardLists()){
                    for(Card card : cardList.getCards()){
                        card.setBackgroundColor(preset.getBackgroundColor());
                        card.setFontColor(preset.getFontColor());
                    }
                }

                customizeCtrl.getBoard().setDefaultCardBackgroundColor(preset.getBackgroundColor());
                customizeCtrl.getBoard().setDefaultCardFontColor(preset.getFontColor());
            } else {
                customizeCtrl.getBoard().setDefaultCardFontColor("0x000000FF");
                customizeCtrl.getBoard().setDefaultCardBackgroundColor("0xDEEDE7FF");
            }
            server.insertPreset(preset);
            customizeCtrl.updateDisplayedPresets();

        });
    }

    /**
     * This method deletes the preset from the database
     */
    public void delete() {
        server.deletePreset(preset.getId());

        Board shownBoard = customizeCtrl.getBoard();
        shownBoard.removePreset(preset);

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

        String title = "Edit Preset";
        HelperMethods.popUp(scene, title);
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
}
