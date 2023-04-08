package client.windows.customize.cards;

import client.MyFXML;
import client.modules.MainModules;
import client.serverUtils.CardColorPresetUtils;
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
    private HelperMethods helperMethods;
    private CardColorPresetUtils server;
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
     */
    @Inject
    public CustomCardPresetCellCtrl(CardColorPresetUtils server, HelperMethods helperMethods){
        this.server = server;
        this.helperMethods = helperMethods;
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

        defaultBox.setSelected(preset.isDefault());
        defaultBox.setOnAction(event -> {
            preset.setDefault(defaultBox.isSelected());
            if(preset.isDefault()){
                for(CardColorPreset p : customizeCtrl.getBoard().getPresetList()){
                    if(p != preset){
                        p.setDefault(false);
                    }
                }

                for(CardList cardList : customizeCtrl.getBoard().getCardLists()){
                    for(Card card : cardList.getCards()){
                        card.setPreset(new CardColorPreset(preset.getName(),
                                preset.getBackgroundColor(), preset.getFontColor()));
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
            shownBoard.setDefaultPreset(new CardColorPreset(preset.getName(),
                    preset.getBackgroundColor(), preset.getFontColor()));
            customizeCtrl.updateDisplayedPresets();
        });
    }

    /**
     * This method deletes the preset from the database
     */
    public void delete() {
        server.deletePreset(preset.getId());

        Board shownBoard = customizeCtrl.getBoard();
        if(preset.isDefault()) {
            shownBoard.setDefaultPreset(new CardColorPreset("Default", "0xDEEDE7FF", "0x000000FF"));
            shownBoard.setDefaultCardFontColor("0x000000FF");
            shownBoard.setDefaultCardBackgroundColor("0xDEEDE7FF");
        }
        shownBoard.removePreset(preset);
        customizeCtrl.updateDisplayedPresets();
        for(CardList cardList : customizeCtrl.getBoard().getCardLists()){
            for(Card c : cardList.getCards()){
                if(equalsPreset(c.getPreset(), preset)){
                    c.setBackgroundColor(shownBoard.getDefaultCardBackgroundColor());
                    c.setFontColor(shownBoard.getDefaultCardFontColor());
                    c.setPreset(shownBoard.getDefaultPreset());
                }
            }
        }
    }

    public boolean equalsPreset(CardColorPreset p1, CardColorPreset p2){
        return p1.getName().equals(p2.getName())
                && p1.getFontColor().equals(p2.getFontColor())
                && p1.getBackgroundColor().equals(p2.getBackgroundColor());
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

    public void setBoard(Board shownBoard) {
        this.shownBoard = shownBoard;
    }
}
