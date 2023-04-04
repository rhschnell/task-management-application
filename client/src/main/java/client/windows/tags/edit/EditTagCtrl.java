package client.windows.tags.edit;

import client.windows.tags.view.CustomEditTagCellCtrl;
import com.google.inject.Inject;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class EditTagCtrl {

    private final EditTagService service;

    private CustomEditTagCellCtrl customEditTagCellCtrl;

    private Tag tag;

    @FXML
    private TextField tagTitle;

    @FXML
    private ColorPicker tagColor;
    @FXML
    private ColorPicker fontColor;

    @FXML
    private Button cancelButton;

    @FXML
    private Button saveButton;

    /**
     * Constructor for the EditTagCtrl
     * @param service Corresponding service
     */
    @Inject
    public EditTagCtrl(EditTagService service){
        this.service = service;
    }

    /**
     * Setter for the tag
     * @param tag The new tag
     */
    public void setTag(Tag tag){
        this.tag = tag;
        setTagTitle(tag.getName());
        setTagColor(Color.web(tag.getTagColor()));
        setFontColor(Color.web(tag.getFontColor()));
    }

    /**
     * Setter for the title of the tag
     * @param title The new title of the tag
     */
    public void setTagTitle(String title){
        tagTitle.setText(title);
    }

    /**
     * Setter for the color of tag
     * @param color The new color of the tag
     */
    public void setTagColor(Color color){
        tagColor.setId(color.toString());
    }

    /**
     * Setter for the color of font of the tag
     * @param  color The new color of the font
     */
    public void setFontColor(Color color){
        fontColor.setId(color.toString());
    }
    /**
     * Method to set the colorpicker in the edit tag popup
     */
    public void setColorPicker(){
        tagColor.setValue(Color.web(tag.getTagColor()));
    }
    public void setFontColorPicker(){
        fontColor.setValue(Color.web(tag.getFontColor()));
    }

    /**
     * Method to save the just edited tag
     */
    public void save() {
        String title = tagTitle.getText();
        Color newTagColor = tagColor.getValue();
        Color newFontColor = fontColor.getValue();
        tagTitle.setText(tag.getName());
        tag.setName(title);
        tag.setTagColor(newTagColor.toString());
        tag.setFontColor(newFontColor.toString());
        service.insertTag(tag);
        customEditTagCellCtrl.getTagOverviewCtrl().updateDisplayedTags();

        ((Stage)saveButton.getScene().getWindow()).close();
    }

    /**
     * Method to close the popup window when the cancel button is clicked
     */
    public void cancel() {
        ((Stage)cancelButton.getScene().getWindow()).close();
    }

    /**
     * Method to set the customTagCellCtrl
     * @param customEditTagCellCtrl The new CustomTagCellCtrl
     */
    public void setCustomTagCellCtrl(CustomEditTagCellCtrl customEditTagCellCtrl) {
        this.customEditTagCellCtrl = customEditTagCellCtrl;
    }

}
