package client.windows.customize.cards.view;

import client.windows.cards.add.AddCardCtrl;
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

    @Inject
    public CustomCardPresetViewCellCtrl(AddCardCtrl addCardCtrl, CardPresetListCtrl cardPresetListCtrl){
        this.cardPresetListCtrl = cardPresetListCtrl;
        this.addCardCtrl = addCardCtrl;
    }

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
    }

    public void action() {
        if(type.equals("removeFromList")){
            cardPresetListCtrl.refreshRemove(preset);
        }
        if(type.equals("addFromList")){
            cardPresetListCtrl.refreshAdd(preset);
        }
    }

    public void setCardPresetListCtrl(CardPresetListCtrl cardPresetListCtrl) {
        this.cardPresetListCtrl = cardPresetListCtrl;
    }

    public void setAddCardCtrl(AddCardCtrl addCardCtrl){
        this.addCardCtrl = addCardCtrl;
    }
}
