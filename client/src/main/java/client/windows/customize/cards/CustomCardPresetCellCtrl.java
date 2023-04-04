package client.windows.customize.cards;

import client.serverUtils.CardColorPresetUtils;
import client.serverUtils.CardUtils;
import client.windows.customize.CustomizeCtrl;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.CardColorPreset;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

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
    private CheckBox defaultBox;

    private CardColorPreset preset;

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
                        System.out.println(card.getTitle());
                        System.out.println(card.getBackgroundColor());
                        System.out.println(card.getFontColor());
                        cardUtils.insertCard(card);
                    }
                }
            }
            server.insertPreset(preset);
            customizeCtrl.updateDisplayedPresets();
        });
    }

    public void delete() {
        server.deletePreset(preset.getId());

        Board shownBoard = customizeCtrl.getBoard();
        shownBoard.removePreset(preset);

        customizeCtrl.updateDisplayedPresets();
    }

    public void setCustomizeCtrl(CustomizeCtrl customizeCtrl) {
        this.customizeCtrl = customizeCtrl;
    }
}
