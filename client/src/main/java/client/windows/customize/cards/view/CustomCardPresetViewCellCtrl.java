package client.windows.customize.cards.view;

import client.windows.cards.add.AddCardCtrl;
import client.windows.cards.view.ViewCardCtrl;
import client.windows.customize.cards.CardPresetListCtrl;
import com.google.inject.Inject;
import commons.CardColorPreset;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CustomCardPresetViewCellCtrl {
    private CardPresetListCtrl cardPresetListCtrl;
    private AddCardCtrl addCardCtrl;
    private ViewCardCtrl viewCardCtrl;

    @FXML
    private Label presetTitle;

    @FXML
    private Rectangle backgroundColor;

    @FXML
    private Rectangle fontColor;

    @FXML
    private Button actionButton;

    private String type;
    private CardColorPreset preset;

    /**
     * Constructor for the CustomCardPresetViewCellCtrl
     * @param addCardCtrl Instance of addCardCtrl
     * @param cardPresetListCtrl Instance of cardPresetListCtrl
     */
    @Inject
    public CustomCardPresetViewCellCtrl(AddCardCtrl addCardCtrl, CardPresetListCtrl cardPresetListCtrl){
        this.cardPresetListCtrl = cardPresetListCtrl;
        this.addCardCtrl = addCardCtrl;
    }

    /**
     * This method sets the color preset
     * @param preset The preset to be set
     * @param type The type of the preset
     */
    public void setPresetObject(CardColorPreset preset, String type){;
        this.preset = preset;
        this.type = type;
        presetTitle.setText(preset.getName());
        backgroundColor.setFill(Color.web(preset.getBackgroundColor()));
        fontColor.setFill(Color.web(preset.getFontColor()));

        if(type.equals("addFromList")){
            actionButton.setText("Add");
            actionButton.getStyleClass().add("blue-button");
        }
        if(type.equals("removeFromList")){
            actionButton.setText("Remove");
            actionButton.getStyleClass().add("red-button");
        }
        if(type.equals("view")){
            actionButton.setVisible(false);
        }
    }

    /**
     * This method moves the preset from applied to available or vice versa
     */
    public void action() {
        if(type.equals("removeFromList")){
            cardPresetListCtrl.refreshRemove(preset);
        }
        if(type.equals("addFromList")){
            cardPresetListCtrl.refreshAdd(preset);
        }
    }

    /**
     * Sets the cardPresetListCtrl
     * @param cardPresetListCtrl The cardPresetListCtrl to be set
     */
    public void setCardPresetListCtrl(CardPresetListCtrl cardPresetListCtrl) {
        this.cardPresetListCtrl = cardPresetListCtrl;
    }

    /**
     * Sets the addCardCtrl
     * @param addCardCtrl The addCardCtrl to be set
     */
    public void setAddCardCtrl(AddCardCtrl addCardCtrl){
        this.addCardCtrl = addCardCtrl;
    }

    public void setViewCardCtrl(ViewCardCtrl viewCardCtrl) {
        this.viewCardCtrl = viewCardCtrl;
    }
}
